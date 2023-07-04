package org.jazztech.portador;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class CardHolderApplication {

    public static void main(String[] args) {
        SpringApplication.run(CardHolderApplication.class, args);
    }

}
