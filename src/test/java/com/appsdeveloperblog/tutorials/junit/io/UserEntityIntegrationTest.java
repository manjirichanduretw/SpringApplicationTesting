package com.appsdeveloperblog.tutorials.junit.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.UUID;

@DataJpaTest
public class UserEntityIntegrationTest {
    @Autowired
    TestEntityManager testEntityManager;

    @DisplayName("test UserEntity")
    @Test
    void testUserEntity_whenCorrectUserDetailsProvided_thenShouldReturnStoredUserDetails(){
//        Arrange
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(UUID.randomUUID().toString());
        userEntity.setFirstName("Manjiri");
        userEntity.setLastName("Chandure");
        userEntity.setEmail("chanduremanjiri@gmail.com");
        userEntity.setEncryptedPassword("12345678");

//        act
        UserEntity storedUser = testEntityManager.persistAndFlush(userEntity);

//        assert
//        Auto generated so it starts from 1, 2, 3 ... onwords
        Assertions.assertTrue(storedUser.getId() > 0, "Should be greater than 0");
        Assertions.assertEquals(userEntity.getUserId(), storedUser.getUserId());
        Assertions.assertEquals(userEntity.getFirstName(), storedUser.getFirstName());
        Assertions.assertEquals(userEntity.getLastName(), storedUser.getLastName());
        Assertions.assertEquals(userEntity.getEmail(), storedUser.getEmail());
        Assertions.assertEquals(userEntity.getEncryptedPassword(), storedUser.getEncryptedPassword());

    }
}
