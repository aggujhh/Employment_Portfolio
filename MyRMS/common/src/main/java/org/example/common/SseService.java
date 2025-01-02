//package org.example.common;
//
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.FluxSink;
//import reactor.core.publisher.UnicastProcessor;
//import org.springframework.stereotype.Service;
//
//import java.util.concurrent.atomic.AtomicReference;
//
//@Service
//public class SseService {
//    // 保存厨房客户端连接
//    private final AtomicReference<FluxSink<String>> kitchenClient = new AtomicReference<>();
//    // 保存前台客户端连接
//    private final AtomicReference<FluxSink<String>> frontDeskClient = new AtomicReference<>();
//    // 点单客户端使用 UnicastProcessor，支持多客户端
//    private final UnicastProcessor<String> orderProcessor = UnicastProcessor.create();
//    private final FluxSink<String> orderSink = orderProcessor.sink();
//
//    // 获取厨房的 SSE Flux
//    public Flux<String> getKitchenSseFlux() {
//        return Flux.create(emitter -> {
//            if (kitchenClient.get() != null) {
//                kitchenClient.get().complete();
//            }
//            kitchenClient.set(emitter);
//            emitter.onDispose(() -> kitchenClient.compareAndSet(emitter, null));
//        });
//    }
//
//    // 获取前台的 SSE Flux
//    public Flux<String> getFrontDeskSseFlux() {
//        return Flux.create(emitter -> {
//            if (frontDeskClient.get() != null) {
//                frontDeskClient.get().complete();
//            }
//            frontDeskClient.set(emitter);
//            emitter.onDispose(() -> frontDeskClient.compareAndSet(emitter, null));
//        });
//    }
//
//    // 向厨房推送消息
//    public void sendToKitchen(String message) {
//        FluxSink<String> client = kitchenClient.get();
//        if (client != null) {
//            try {
//                client.next(message);
//            } catch (Exception e) {
//                System.err.println("Failed to send message to kitchen: " + e.getMessage());
//            }
//        }
//    }
//
//    // 向前台推送消息
//    public void sendToFrontDesk(String message) {
//        FluxSink<String> client = frontDeskClient.get();
//        if (client != null) {
//            try {
//                client.next(message);
//            } catch (Exception e) {
//                System.err.println("Failed to send message to kitchen: " + e.getMessage());
//            }
//        }
//    }
//}

package org.example.common;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import org.springframework.stereotype.Service;

@Service
public class SseService {
    // 保存厨房的单一连接
    private final Sinks.Many<String> kitchenSink = Sinks.many().multicast().onBackpressureBuffer();
    // 保存前台的单一连接
    private final Sinks.Many<String> frontDeskSink = Sinks.many().multicast().onBackpressureBuffer();
    // 点单客户端使用 Sinks.Many，支持多客户端
    private final Sinks.Many<String> orderSink = Sinks.many().multicast().onBackpressureBuffer();

    // 获取厨房的 SSE Flux
    public Flux<String> getKitchenSseFlux() {
        return kitchenSink.asFlux();
    }

    // 获取前台的 SSE Flux
    public Flux<String> getFrontDeskSseFlux() {
        return frontDeskSink.asFlux();
    }

    // 获取点单的 SSE Flux，支持多个客户端
    public Flux<String> getOrderSseFlux() {
        return orderSink.asFlux(); // 直接返回消息内容
    }


    public void sendToKitchen(String message) {
        Sinks.EmitResult result = kitchenSink.tryEmitNext(message);
        if (result.isFailure()) {
            throw new RuntimeException("Failed to emit message to kitchen: " + result.name());
        }
    }

    public void sendToFrontDesk(String message) {
        Sinks.EmitResult result = frontDeskSink.tryEmitNext(message);
        if (result.isFailure()) {
            throw new RuntimeException("Failed to emit message to front desk: " + result.name());
        }
    }

    public void sendToOrderClients(String message) {
        Sinks.EmitResult result = orderSink.tryEmitNext(message);
        if (result.isFailure()) {
            throw new RuntimeException("Failed to emit message to order: " + result.name());
        }
    }
}

