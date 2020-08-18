package com.xmxe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class CasSecurityClientOneAppication {
    public static void main(String[] args) {
        SpringApplication.run(CasSecurityClientOneAppication.class,args);
    }
}
