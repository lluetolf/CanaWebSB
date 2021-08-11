package ch.canaweb.springboot.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class FieldServiceHealth implements ReactiveHealthIndicator {

    private final WebClient webClient;

    @Autowired
    public FieldServiceHealth(final WebClient.Builder webClient) {
        this.webClient = webClient.build();
    }


    @Override
    public Mono<Health> health() {
        String url = String.format("http://%s:8080/actuator/health", "field");
        return webClient.get()
                .uri(url).retrieve().bodyToMono(String.class)
                .map(s -> new Health.Builder().up().build())
                .onErrorResume(ex -> Mono.just(new Health.Builder().down(ex).build()))
                .log();
    }
}