package com.appsdeveloperblog.tutorials.junit.ui.controllers;

import com.appsdeveloperblog.tutorials.junit.service.UsersService;
import com.appsdeveloperblog.tutorials.junit.shared.UserDto;
import com.appsdeveloperblog.tutorials.junit.ui.request.UserDetailsRequestModel;
import com.appsdeveloperblog.tutorials.junit.ui.response.UserRest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.mapping.Any;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UsersService usersService;

    @Test
    @DisplayName("Create User when valid user detail provided!")
    void testCreateUser_whenValidUserDetailsProvided_thenCreateUser() throws Exception {
//        Arrange
        UserDetailsRequestModel userDetailsRequestModel = new UserDetailsRequestModel();
        userDetailsRequestModel.setFirstName("Manjiri");
        userDetailsRequestModel.setLastName("Chandure");
        userDetailsRequestModel.setEmail("email@gmail.com");
        userDetailsRequestModel.setPassword("12345678");
        userDetailsRequestModel.setRepeatPassword("12345678");

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

}
