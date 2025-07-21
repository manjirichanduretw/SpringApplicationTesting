package com.appsdeveloperblog.tutorials.junit.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.UUID;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    UsersRepository usersRepository;

    @DisplayName("testing findByEmail method of userRepository")
    @Test
    void testFindByEmailMethod_whenCorrectEmailProvided_thenReturnUserEntity(){
//        Arrange
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(UUID.randomUUID().toString());
        userEntity.setFirstName("Manjiri");
        userEntity.setLastName("Chandure");
        userEntity.setEmail("chanduremanjiri@gmail.com");
        userEntity.setEncryptedPassword("12345678");

        testEntityManager.persistAndFlush(userEntity);
//        Act

        UserEntity storedUser = usersRepository.findByEmail("chanduremanjiri@gmail.com");
//        Assert
        Assertions.assertEquals(userEntity.getEmail(), storedUser.getEmail(),
                "Email should be equale");
    }
}
