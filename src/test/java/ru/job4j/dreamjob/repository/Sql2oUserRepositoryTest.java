package ru.job4j.dreamjob.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.dreamjob.configuration.DatasourceConfiguration;
import ru.job4j.dreamjob.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class Sql2oUserRepositoryTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(Sql2oUserRepository.class);
    private static Sql2oUserRepository sql2oUserRepository;

    @BeforeAll
    public static void initRepositories() throws IOException {
        var properties = new Properties();
        try (var inputStream = Sql2oUserRepository.class
                .getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");
        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        var sql2o = configuration.databaseClient(datasource);
        sql2oUserRepository = new Sql2oUserRepository(sql2o);
    }

    @AfterEach
    public void clearUsers() {
        var users = sql2oUserRepository.findAll();
        for (var user : users) {
            sql2oUserRepository.deleteById(user.getId());
        }
    }

    @Test
    public void whenSaveThenGetSame() {
        var user = sql2oUserRepository
                .save(new User(0, "test@example.com", "Test User", "securePassword")).get();
        var savedUser = sql2oUserRepository.findByEmailAndPassword(user.getEmail(), user.getPassword()).get();
        assertThat(savedUser).isEqualTo(user);
    }

    @Test
    public void whenSaveSameUserThenGetSame() {
        var user1 = sql2oUserRepository.save(new User(0, "1@mail", "Name", "1qwerty")).get();
        var user2 = sql2oUserRepository.save(new User(0, "2@mail", "Name", "2qwerty")).get();
        var savedUser1 = sql2oUserRepository.findByEmailAndPassword("1@mail", "1qwerty").get();
        var savedUser2 = sql2oUserRepository.findByEmailAndPassword("2@mail", "2qwerty").get();
        assertThat(savedUser1).isEqualTo(user1);
        assertThat(savedUser2).isEqualTo(user2);
    }

    @Test
    public void whenSaveAfewWithDifferentEmailsBySameUser() {
        var user1 = sql2oUserRepository.save(new User(0, "test@example.com", "Test User", "securePassword")).get();
        var user2 = sql2oUserRepository.save(new User(0, "test2@example.com", "Test User", "secure2Password")).get();
        var user3 = sql2oUserRepository.save(new User(0, "test3@example.com", "Test User", "secure3Password")).get();
        var result = sql2oUserRepository.findAll();
        assertThat(result).isEqualTo(List.of(user1, user2, user3));
    }

    @Test
    public void whenDontSaveThenNothingFound() {
        assertThat(sql2oUserRepository.findAll()).isEqualTo(emptyList());
        assertThat(sql2oUserRepository.findByEmailAndPassword("ffg@mail.ru", "sdf")).isEmpty();
    }

    @Test
    public void whenDeleteThenGetEmptyOptional() {
        var user = sql2oUserRepository.save(new User(0, "name", "email", "password")).get();
        var isDeleted = sql2oUserRepository.deleteById(user.getId());
        var savedUser = sql2oUserRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());
        assertThat(isDeleted).isTrue();
        assertThat(savedUser).isEqualTo(empty());
    }

    @Test
    public void whenSaveThenGetSame2() {
        var userToSave = new User(0, "test@example.com", "Test User", "securePassword");
        var savedUserOptional = sql2oUserRepository.save(userToSave);
        assertThat(savedUserOptional).isPresent();
        var savedUser = savedUserOptional.get();
        assertThat(savedUser.getId()).isGreaterThan(0);

        var fetchedUserOptional = sql2oUserRepository.findByEmailAndPassword(savedUser.getEmail(), savedUser.getPassword());
        assertThat(fetchedUserOptional).isPresent();
        var fetchedUser = fetchedUserOptional.get();

        assertThat(fetchedUser).usingRecursiveComparison().isEqualTo(savedUser);
    }

    @Test
    public void whenDeleteByInvalidIdThenGetFalse() {
        assertThat(sql2oUserRepository.deleteById(0)).isFalse();
    }

    @Test
    public void whenDuplicateThenException() {
        sql2oUserRepository.save(new User(0, "test@example.com", "Test User", "securePassword"));
        sql2oUserRepository.save(new User(0, "test@example.com", "Test User", "securePassword"));
        assertThat(sql2oUserRepository.findAll().size()).isOne();
    }
}
