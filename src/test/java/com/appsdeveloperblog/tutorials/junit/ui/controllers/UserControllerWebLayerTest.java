package com.appsdeveloperblog.tutorials.junit.ui.controllers;

import com.appsdeveloperblog.tutorials.junit.service.UsersService;
import com.appsdeveloperblog.tutorials.junit.shared.UserDto;
import com.appsdeveloperblog.tutorials.junit.ui.request.UserDetailsRequestModel;
import com.appsdeveloperblog.tutorials.junit.ui.response.UserRest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = UsersController.class,
excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class UserControllerWebLayerTest {

    UserDetailsRequestModel userDetailsRequestModel;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UsersService usersService;

    @BeforeEach
    void init(){

        userDetailsRequestModel = new UserDetailsRequestModel();
        userDetailsRequestModel.setFirstName("Manjiri");
        userDetailsRequestModel.setLastName("Chandure");
        userDetailsRequestModel.setEmail("email@gmail.com");
        userDetailsRequestModel.setPassword("12345678");
        userDetailsRequestModel.setRepeatPassword("12345678");
    }

    @Test
    @DisplayName("Create User when valid user detail provided!")
    void testCreateUser_whenValidUserDetailsProvided_thenCreateUser() throws Exception {
//        Arrange

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userDetailsRequestModel));

//        this for mock user service return object
//        in response new only this so don't set other thing
//        UserDto userDto = new UserDto();
//        userDto.setUserId(UUID.randomUUID().toString());
//        userDto.setFirstName(userDetailsRequestModel.getFirstName());
//        userDto.setLastName(userDetailsRequestModel.getLastName());
//        userDto.setEmail(userDetailsRequestModel.getEmail());

        UserDto userDto = new ModelMapper().map(userDetailsRequestModel, UserDto.class);
        userDto.setUserId(UUID.randomUUID().toString());


        when(usersService.createUser(any(UserDto.class))).thenReturn(userDto);

//        Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString();

        UserRest createdUser = new ObjectMapper().readValue(responseBody, UserRest.class);

//        assert
        Assertions.assertEquals(userDetailsRequestModel.getFirstName(), createdUser.getFirstName(),
                "FirstName not get set correctly!");
        Assertions.assertEquals(userDetailsRequestModel.getLastName(), createdUser.getLastName(),
                "LastName not get set correctly!");
        Assertions.assertEquals(userDetailsRequestModel.getEmail(), createdUser.getEmail(),
                "Email not get set correctly!");
        Assertions.assertNotNull(createdUser.getUserId(), "UderId is not set!");


    }

    @Test
    @DisplayName("When Empty firstName provided should return 400 Bad-Request")
    void testUserCreate_whenEmptyFirstNameProvided_thenShouldReturn400Badrequest() throws Exception {

        userDetailsRequestModel.setFirstName("");

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userDetailsRequestModel));

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus(),
                "Should return 400 bad-request");
    }

    @Test
    @DisplayName("When firstName is shorter than min limit(2 character)")
    void testUserCreate_whenFirstNameLengthShorterThan2Char_thenReturn400BadRequest() throws Exception {

        userDetailsRequestModel.setFirstName("m");

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userDetailsRequestModel));

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus(),
                "First name should be greater than 2 character!");
    }

}
