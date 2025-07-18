package com.appsdeveloperblog.tutorials.junit.ui.controllers;

import com.appsdeveloperblog.tutorials.junit.security.SecurityConstants;
import com.appsdeveloperblog.tutorials.junit.ui.response.UserRest;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.User;

import java.util.Arrays;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//properties = {"server.port = 8081", "hostname=192.168.0.2"})
//@TestPropertySource(locations = "/application-test.properties",
//properties = "server.port=8888")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerIntegrationTest {

    @Autowired
    TestRestTemplate testRestTemplate;

    @Value("${server.port}")
    private int serverPort;

//    Random_port have higher priority
//    server port get 0
    @LocalServerPort
    private int tomcatServerPort;

    private String authorizationToken;

    @Test
    @DisplayName("Integration test for creating user")
    @Order(1)
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

    @Test
    @DisplayName("Should return 403 when jwt token is missing")
    @Order(2)
    void testGetUsers_whenJWTMissing_thenShouldReturn403(){
//        Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");

        HttpEntity requestEntity = new HttpEntity(null, headers);

//        Act
        ResponseEntity<List<UserRest>> response = testRestTemplate.exchange("/users",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<UserRest>>() {
                });
//        Assert
        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode(), "Should return 403!");
    }

    @Test
    @DisplayName("User Login works")
    @Order(3)
    void testUserLogin_whenCorrectCredentialsProvided_UserShouldGetLoggedIn() throws JSONException {
//          Arrange
        JSONObject loginCredentials = new JSONObject();
        loginCredentials.put("email", "manjiri@gmail.com");
        loginCredentials.put("password", "12345678");

        HttpEntity<String> request = new HttpEntity<>(loginCredentials.toString());

//        Act
        ResponseEntity response = testRestTemplate.postForEntity("/users/login",
                request, null);

        authorizationToken = response.getHeaders().getValuesAsList(SecurityConstants.HEADER_STRING).get(0);
//        Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(),
                "Should return 200 Ok");
        Assertions.assertNotNull(response.getHeaders().getValuesAsList(SecurityConstants.HEADER_STRING).get(0),
                "response should contain Authorization header!");
        Assertions.assertNotNull(response.getHeaders().getValuesAsList("UserId").get(0),
                "response should contain userId in response");
    }

    @Test
    @Order(4)
    @DisplayName("Get /users works")
    void testGetUsers_whenValidJWTProvided_returnUsers(){

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(authorizationToken);

        HttpEntity requestEntity = new HttpEntity(headers);

        ResponseEntity<List<UserRest>> response = testRestTemplate.exchange("/users",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<UserRest>>() {
                });

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(), "Should return OK 200");
        Assertions.assertTrue(response.getBody().size() == 1,
                "Should send List with size 1 here");
    }
}