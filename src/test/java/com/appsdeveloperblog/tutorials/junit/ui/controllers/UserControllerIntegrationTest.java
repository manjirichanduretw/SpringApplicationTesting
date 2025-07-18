package com.appsdeveloperblog.tutorials.junit.ui.controllers;

import com.appsdeveloperblog.tutorials.junit.ui.response.UserRest;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import java.util.Arrays;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//properties = {"server.port = 8081", "hostname=192.168.0.2"})
//@TestPropertySource(locations = "/application-test.properties",
//properties = "server.port=8888")
public class UserControllerIntegrationTest {

    @Autowired
    TestRestTemplate testRestTemplate;

    @Value("${server.port}")
    private int serverPort;

//    Random_port have higher priority
//    server port get 0
    @LocalServerPort
    private int tomcatServerPort;

    @Test
    @DisplayName("Integration test for creating user")
    void testUserCreate_whenValidUserDetailsProvided_thenUserShouldCreated() throws JSONException {
//        Arrange
        JSONObject userDetailsRequestJSON = new JSONObject();
        userDetailsRequestJSON.put("firstName", "Manjiri");
        userDetailsRequestJSON.put("lastName", "Chavan");
        userDetailsRequestJSON.put("email", "manjiri@gmail.com");
        userDetailsRequestJSON.put("password", "12345678");
        userDetailsRequestJSON.put("repeatPassword", "12345678");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<String> request = new HttpEntity<>(userDetailsRequestJSON.toString(), headers);
//        Act

        ResponseEntity<UserRest> responseEntity = testRestTemplate.postForEntity("/users", request, UserRest.class);
        UserRest userRest = responseEntity.getBody();
//        Assert
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), "HttpStatus should be 200 OK");
        Assertions.assertEquals(userDetailsRequestJSON.get("firstName"), userRest.getFirstName(),
                "firstName does not get set");
        Assertions.assertEquals(userDetailsRequestJSON.get("lastName"), userRest.getLastName(),
                "lastName does not get set");
        Assertions.assertEquals(userDetailsRequestJSON.get("email"), userRest.getEmail(),
                "Email doesn't set");
        Assertions.assertNotNull(userRest.getUserId(), "UserId doesn't set");

    }

}
