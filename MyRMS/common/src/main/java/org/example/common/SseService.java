package org.example.common;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import org.springframework.stereotype.Service;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class SseService {
    // 保存厨房客户端连接
    private final AtomicReference<FluxSink<String>> kitchenClient = new AtomicReference<>();
    // 保存前台客户端连接
    private final AtomicReference<FluxSink<String>> frontDeskClient = new AtomicReference<>();

    // 获取厨房的 SSE Flux
    public Flux<String> getKitchenSseFlux() {
        return Flux.create(emitter -> {
            if (kitchenClient.get() != null) {
                kitchenClient.get().complete();
            }
            kitchenClient.set(emitter);
            emitter.onDispose(() -> kitchenClient.compareAndSet(emitter, null));
        });
    }

    // 获取前台的 SSE Flux
    public Flux<String> getFrontDeskSseFlux() {
        return Flux.create(emitter -> {
            if (frontDeskClient.get() != null) {
                frontDeskClient.get().complete();
            }
            frontDeskClient.set(emitter);
            emitter.onDispose(() -> frontDeskClient.compareAndSet(emitter, null));
        });
    }

    // 向厨房推送消息
    public void sendToKitchen(String message) {
        FluxSink<String> client = kitchenClient.get();
        if (client != null) {
            client.next(message);
        }
    }

    // 向前台推送消息
    public void sendToFrontDesk(String message) {
        FluxSink<String> client = frontDeskClient.get();
        if (client != null) {
            client.next(message);
        }
    }
}
