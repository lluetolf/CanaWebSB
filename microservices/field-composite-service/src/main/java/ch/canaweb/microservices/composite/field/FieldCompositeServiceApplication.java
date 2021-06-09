package ch.canaweb.microservices.composite.field;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;

import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;


import java.time.LocalDate;

import static springfox.documentation.schema.AlternateTypeRules.newRule;

@SpringBootApplication
@ComponentScan("ch.canaweb")
public class FieldCompositeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FieldCompositeServiceApplication.class, args);
    }

    @Bean
    public Docket canawebApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .pathMapping("/")
                .directModelSubstitute(LocalDate.class, String.class);
    }



}
