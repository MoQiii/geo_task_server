package org.syj.geotask;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GeoTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(GeoTaskApplication.class, args);
    }

}
