package ch.canaweb.microservices.composite.field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("ch.canaweb")
public class FieldCompositeServiceApplication {
    private static final Logger LOG = LoggerFactory.getLogger(FieldCompositeServiceApplication.class);

    public static void main(String[] args) {

        ConfigurableApplicationContext ctx = SpringApplication.run(FieldCompositeServiceApplication.class, args);

        String canaweb_env = ctx.getEnvironment().getProperty("canaweb_env");
        LOG.info("CanaWeb Env: " + canaweb_env);
    }

}
