package com.appsdeveloperblog.tutorials.junit.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.UUID;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    UsersRepository usersRepository;

    private String email1 = "123@test.com";
    private String email2 = "124@test.com";

    UserEntity userEntity;
    UserEntity userEntity2;

    @BeforeEach
    void init(){
        userEntity = new UserEntity();
        userEntity.setUserId(UUID.randomUUID().toString());
        userEntity.setFirstName("Manjiri");
        userEntity.setLastName("Chandure");
        userEntity.setEmail(email1);
        userEntity.setEncryptedPassword("12345678");

        userEntity2 = new UserEntity();
        userEntity2.setUserId(UUID.randomUUID().toString());
        userEntity2.setFirstName("Meow");
        userEntity2.setLastName("Chandure");
        userEntity2.setEmail(email2);
        userEntity2.setEncryptedPassword("12345678");
    }

    @DisplayName("testing findByEmail method of userRepository")
    @Test
    void testFindByEmailMethod_whenCorrectEmailProvided_thenReturnUserEntity(){
//        Arrange

        testEntityManager.persistAndFlush(userEntity);
//        Act

        UserEntity storedUser = usersRepository.findByEmail(email1);
//        Assert
        Assertions.assertEquals(userEntity.getEmail(), storedUser.getEmail(),
                "Email should be equale");
    }

    @DisplayName("Test findUserByIdMethod")
    @Test
    void testFindByUserIdMethod_whenValidUserIdProvided_thenReturnUserEntitty(){

//        Arrange
        testEntityManager.persistAndFlush(userEntity);
//        Act
        UserEntity user = usersRepository.findByUserId(userEntity.getUserId());
//        Assert
        Assertions.assertNotNull(user, "User should not be null");
        Assertions.assertEquals(userEntity.getUserId(), user.getUserId(),
                "UserId should be equale");
    }

    @DisplayName("Test findUsersWithEmailEndingWith")
    @Test
    void testFindUsersWithEmailEndingWithMethod_whenValidEmailProvided_thenReturnListOfUsers(){
//        Arrange
        UserEntity userEntity3 = new UserEntity();
        userEntity3.setUserId(UUID.randomUUID().toString());
        userEntity3.setFirstName("Manjiri");
        userEntity3.setLastName("Chandure");
        userEntity3.setEmail("chanduremanjiri@gmail.com");
        userEntity3.setEncryptedPassword("12345678");
        testEntityManager.persistAndFlush(userEntity3);
//        Act
        List<UserEntity> users = usersRepository.findUsersWithEmailEndingWith("@gmail.com");
//        Assert
        Assertions.assertEquals(1, users.size(), "Should return of size 1 list");
        Assertions.assertTrue(users.get(0).getEmail().endsWith("@gmail.com"));
    }
}
