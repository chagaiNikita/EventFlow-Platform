package petproject.apigateway.infrastructure.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        long startTime = System.currentTimeMillis();

        log.info("→ {} {}",
                exchange.getRequest().getMethod(),
                exchange.getRequest().getURI().getPath()
        );

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            long duration = System.currentTimeMillis() - startTime;
            log.info("← {} {} {}ms",
                    exchange.getResponse().getStatusCode(),
                    exchange.getRequest().getURI().getPath(),
                    duration
            );
        }));
    }

    @Override
    public int getOrder() {
        return -2;
    }
}
