package com.appsdeveloperblog.tutorials.junit.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.persistence.PersistenceException;
import java.sql.SQLOutput;
import java.util.UUID;

@DataJpaTest
public class UserEntityIntegrationTest {
    @Autowired
    TestEntityManager testEntityManager;

    UserEntity userEntity;

    @BeforeEach
    void init(){
        userEntity = new UserEntity();
        userEntity.setUserId(UUID.randomUUID().toString());
        userEntity.setFirstName("Manjiri");
        userEntity.setLastName("Chandure");
        userEntity.setEmail("chanduremanjiri@gmail.com");
        userEntity.setEncryptedPassword("12345678");
    }

    @DisplayName("test UserEntity")
    @Test
    void testUserEntity_whenCorrectUserDetailsProvided_thenShouldReturnStoredUserDetails(){
//        Arrange
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

    @DisplayName("test too long firstName returns exception")
    @Test
    void testUserEntity_whenTooLongFirstNameIsProvided_thenReturnException(){
//        Arrange
        userEntity.setFirstName("Manjiri123Manjiri123Manjiri123Manjiri123Manjiri123M");
//        Act & assert
        Assertions.assertThrows(PersistenceException.class, ()->{
            testEntityManager.persistAndFlush(userEntity);
        }, "When firstName is tooLong should return persistence exception");
    }

    @DisplayName("UserEntity should return exception when duplicate userId provided")
    @Test
    void testUserEntity_whenDuplicationUserIdProvided_thenReturnException(){
//Arrange
        UserEntity duplicateIdUserEntity = new UserEntity();
        duplicateIdUserEntity.setUserId(userEntity.getUserId());
        duplicateIdUserEntity.setFirstName("Manji");
        duplicateIdUserEntity.setLastName("Chandure");
        duplicateIdUserEntity.setEmail("chanduremanjiri@gmail.com");
        duplicateIdUserEntity.setEncryptedPassword("12345678");

        testEntityManager.persistAndFlush(userEntity);
//Act & Assert
        Assertions.assertThrows(PersistenceException.class, ()->{
            testEntityManager.persistAndFlush(duplicateIdUserEntity);
        }, "should return persistence exception when duplicate userId provided");

    }
}
