package ch.canaweb.microservices.composite.field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.reactive.function.client.WebClient;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;

import java.util.Collections;
import java.util.LinkedHashMap;

import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;

@SpringBootApplication
@ComponentScan("ch.canaweb")
public class FieldCompositeServiceApplication {
    @Value("${api.common.version}")           String apiVersion;
    @Value("${api.common.title}")             String apiTitle;
    @Value("${api.common.description}")       String apiDescription;

    private static final Logger LOG = LoggerFactory.getLogger(FieldCompositeServiceApplication.class);

    public static void main(String[] args) {

        ConfigurableApplicationContext ctx = SpringApplication.run(FieldCompositeServiceApplication.class, args);

    }

    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        final WebClient.Builder builder = WebClient.builder();
        return builder;
    }

    @Bean
    public Docket apiDocumentation() {

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(basePackage("ch.canaweb.microservices.composite.field.services"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(new ApiInfo(
                        apiTitle,
                        apiDescription,
                        apiVersion,
                        null,
                        null,
                        null,
                        null,
                        Collections.emptyList()
                ));
    }

}
