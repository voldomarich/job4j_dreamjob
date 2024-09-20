package ru.job4j.dreamjob.controller;

import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

class UserControllerTest {

    private UserService userService;
    private UserController userController;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    void whenGetRegistrationPage() {
        var model = new ConcurrentModel();
        var view = userController.getRegistrationPage(model);
        assertThat(view).isEqualTo("users/register");
    }

    @Test
    void whenRegisterUserSuccessfully() {
        var user = new User(0, "testUser@example.com", "User", "password");
        var model = new ConcurrentModel();
        when(userService.save(user)).thenReturn(Optional.of(user));
        var view = userController.register(user, model);
        assertThat(view).isEqualTo("redirect:/users/login");
    }

    @Test
    void whenRegisterUserUnsuccessfully() {
        var user = new User(0, "testUser@example.com", "User", "password");
        var model = new ConcurrentModel();
        when(userService.save(user)).thenReturn(Optional.empty());
        var view = userController.register(user, model);
        var message = model.getAttribute("message");
        assertThat(view).isEqualTo("errors/404");
        assertThat(message).isEqualTo("Пользователь с такой почтой уже существует");
    }

    @Test
    void whenGetLoginPage() {
        String view = userController.getLoginPage();
        assertThat(view).isEqualTo("users/login");
    }

    @Test
    void whenUserLoginSuccessfully() {
        var user = new User(0, "testUser@example.com", "User", "password");
        var model = new ConcurrentModel();
        var session = mock(HttpSession.class);
        var request = mock(HttpServletRequest.class);
        when(userService.findByEmailAndPassword(user.getEmail(), user.getPassword())).thenReturn(Optional.of(user));
        when(request.getSession()).thenReturn(session);
        var view = userController.loginUser(user, model, request);
        assertThat(view).isEqualTo("redirect:/vacancies");
    }

    @Test
    void whenUserLoginUnsuccessfully() {
        var user = new User(0, "testUser@example.com", "User", "password");
        var model = new ConcurrentModel();
        var request = mock(HttpServletRequest.class);
        when(userService.findByEmailAndPassword(user.getEmail(), user.getPassword())).thenReturn(Optional.empty());
        var view = userController.loginUser(user, model, request);
        var error = model.getAttribute("error");
        assertThat(view).isEqualTo("users/login");
        assertThat(error).isEqualTo("Почта или пароль введены неверно");
    }

    @Test
    void whenLogOut() {
        var request = mock(HttpServletRequest.class);
        var session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        var view = userController.logout(session);
        verify(session, times(1)).invalidate();
        assertThat(view).isEqualTo("redirect:/users/login");
    }
}
