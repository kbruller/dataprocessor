package com.example.dataprocessor;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DataprocessorApplicationTests {

    @Test
    void contextLoads() {
    }

}



/*import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DataprocessorApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void helloEndpointShouldReturnData() {
        String response = this.restTemplate.getForObject(
                "http://localhost:" + port + "/hello",
                String.class
        );

        assertThat(response).contains("test");
    }
}*/
