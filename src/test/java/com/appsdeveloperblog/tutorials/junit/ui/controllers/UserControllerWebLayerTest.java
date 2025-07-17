package com.appsdeveloperblog.tutorials.junit.ui.controllers;

import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest(controllers = UsersController.class,
excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class UserControllerWebLayerTest {
}
