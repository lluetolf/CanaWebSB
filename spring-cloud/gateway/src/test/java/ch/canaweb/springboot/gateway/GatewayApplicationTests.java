package ch.canaweb.springboot.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.health.ReactiveHealthContributorRegistry;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment=RANDOM_PORT, properties = {"spring.data.mongodb.port: 0", "eureka.client.enabled=false", "spring.cloud.config.enabled=false"})
class GatewayApplicationTests {

    @MockBean
    private MicroServicesHealthAggregator microServicesHealthAggregator;

    @MockBean
    private FieldServiceHealth fieldServiceHealth;


    @Test
    void contextLoads() {
    }

}
