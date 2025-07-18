package com.appsdeveloperblog.tutorials.junit.ui.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//properties = {"server.port = 8081", "hostname=192.168.0.2"})
//@TestPropertySource(locations = "/application-test.properties",
//properties = "server.port=8888")
public class UserControllerIntegrationTest {

    @Value("${server.port}")
    private int serverPort;

//    Random_port have higher priority
//    server port get 0
    @LocalServerPort
    private int tomcatServerPort;

    @Test
    void contextLoad(){
        System.out.println("Server Port " + serverPort);
        System.out.println("tomcat Server Port" + tomcatServerPort);
    }


}
