package com.xmxe;

import net.unicon.cas.client.configuration.EnableCasClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableCasClient
@SpringBootApplication
public class CasClientOneAppication {
    public static void main(String[] args) {
        SpringApplication.run(CasClientOneAppication.class,args);
    }
}
