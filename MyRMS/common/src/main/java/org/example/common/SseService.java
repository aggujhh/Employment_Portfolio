package org.example.common;

//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Sinks;
//import org.springframework.stereotype.Service;
//
//
//@Service
//public class SseService {
//    // 保存厨房的单一连接
//    private final Sinks.Many<String> kitchenSink = Sinks.many().multicast().onBackpressureBuffer(100);
//    // 保存前台的单一连接
//    private final Sinks.Many<String> frontDeskSink = Sinks.many().multicast().onBackpressureBuffer(100);
//    // 点单客户端使用 Sinks.Many，支持多客户端
//    private final Sinks.Many<String> orderSink = Sinks.many().multicast().onBackpressureBuffer(100);
//
//    public Flux<String> getKitchenSseFlux() {
//        // 每 1 分钟发送心跳消息
//        return kitchenSink.asFlux();
//    }
//
//    public Flux<String> getFrontDeskSseFlux() {
//        return frontDeskSink.asFlux();
//    }
//
//    public Flux<String> getOrderSseFlux() {
//        return orderSink.asFlux();
//    }
//
//
//    public void sendToKitchen(String message) {
//        Sinks.EmitResult result = kitchenSink.tryEmitNext(message);
//        if (result.isFailure()) {
//            System.err.println("Failed to emit message to kitchen: " + result.name());
//        }
//    }
//
//    public void sendToFrontDesk(String message) {
//        Sinks.EmitResult result = frontDeskSink.tryEmitNext(message);
//        if (result.isFailure()) {
//            System.err.println("Failed to emit message to front desk: " + result.name());
//        }
//    }
//
//    public void sendToOrderClients(String message) {
//        Sinks.EmitResult result = orderSink.tryEmitNext(message);
//        if (result.isFailure()) {
//            System.err.println("Failed to emit message to order clients: " + result.name());
//        }
//    }
//}




import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import org.springframework.stereotype.Service;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class SseService {
    // 厨房连接列表
    private final CopyOnWriteArrayList<Sinks.Many<String>> kitchenConnections = new CopyOnWriteArrayList<>();
    // 前台连接列表
    private final CopyOnWriteArrayList<Sinks.Many<String>> frontDeskConnections = new CopyOnWriteArrayList<>();
    // 点单客户端连接列表
    private final CopyOnWriteArrayList<Sinks.Many<String>> orderConnections = new CopyOnWriteArrayList<>();

    public Flux<String> getKitchenSseFlux() {
        Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer(100);
        kitchenConnections.add(sink);
        return sink.asFlux().doFinally(signalType -> kitchenConnections.remove(sink));
    }

    public Flux<String> getFrontDeskSseFlux() {
        Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer(100);
        frontDeskConnections.add(sink);
        return sink.asFlux().doFinally(signalType -> frontDeskConnections.remove(sink));
    }

    public Flux<String> getOrderSseFlux() {
        Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer(100);
        orderConnections.add(sink);
        return sink.asFlux().doFinally(signalType -> orderConnections.remove(sink));
    }

    public void sendToKitchen(String message) {
        broadcastMessage(kitchenConnections, "厨房", message);
    }

    public void sendToFrontDesk(String message) {
        broadcastMessage(frontDeskConnections, "前台", message);
    }

    public void sendToOrderClients(String message) {
        broadcastMessage(orderConnections, "点单客户端", message);
    }

    private void broadcastMessage(CopyOnWriteArrayList<Sinks.Many<String>> connections, String target, String message) {
        connections.forEach(sink -> {
            Sinks.EmitResult result = sink.tryEmitNext(message);
            if (result.isFailure()) {
                System.err.println("消息发送到 " + target + " 失败: " + result.name());
            } else {
                System.out.println("消息已发送到 " + target + ": " + message);
            }
        });
    }
}
