package org.example.common;

import jakarta.annotation.PreDestroy;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import org.springframework.stereotype.Service;

@Service
public class SseService {
    // 保存厨房的单一连接
    private final Sinks.Many<String> kitchenSink = Sinks.many().multicast().onBackpressureBuffer(100);
    // 保存前台的单一连接
    private final Sinks.Many<String> frontDeskSink = Sinks.many().multicast().onBackpressureBuffer(100);
    // 点单客户端使用 Sinks.Many，支持多客户端
    private final Sinks.Many<String> orderSink = Sinks.many().multicast().onBackpressureBuffer(100);

    public Flux<String> getKitchenSseFlux() {
        return kitchenSink.asFlux();
    }

    public Flux<String> getFrontDeskSseFlux() {
        return frontDeskSink.asFlux();
    }

    public Flux<String> getOrderSseFlux() {
        return orderSink.asFlux();
    }


    public void sendToKitchen(String message) {
        Sinks.EmitResult result = kitchenSink.tryEmitNext(message);
        if (result.isFailure()) {
            System.err.println("Failed to emit message to kitchen: " + result.name());
        }
    }

    public void sendToFrontDesk(String message) {
        Sinks.EmitResult result = frontDeskSink.tryEmitNext(message);
        if (result.isFailure()) {
            System.err.println("Failed to emit message to front desk: " + result.name());
        }
    }

    public void sendToOrderClients(String message) {
        Sinks.EmitResult result = orderSink.tryEmitNext(message);
        if (result.isFailure()) {
            System.err.println("Failed to emit message to order clients: " + result.name());
        }
    }

    @PreDestroy
    public void shutdown() {
        kitchenSink.tryEmitComplete();
        frontDeskSink.tryEmitComplete();
        orderSink.tryEmitComplete();
    }
}

