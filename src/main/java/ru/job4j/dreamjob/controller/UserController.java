package ru.job4j.dreamjob.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public static void checkUser(Model model, HttpSession session) {
        var user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setName("Гость");
        }
        model.addAttribute("user", user);
    }

    @GetMapping("/register")
    public String getRegistrationPage(Model model, HttpSession session) {
        UserController.checkUser(model, session);
        return "users/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, Model model, HttpSession session) {
        UserController.checkUser(model, session);
        var savedUser = userService.save(user);
        if (savedUser.isEmpty()) {
            model.addAttribute("message", "Пользователь с такой почтой уже существует");
            return "errors/404";
        }
        return "redirect:/index";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model, HttpSession session) {
        UserController.checkUser(model, session);
        return "users/login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute User user, Model model,
                            HttpSession session, HttpServletRequest request) {
        UserController.checkUser(model, session);
        var userOptional = userService.findByEmailAndPassword(user.getEmail(), user.getPassword());
        if (userOptional.isEmpty()) {
            model.addAttribute("error", "Почта или пароль введены неверно");
            return "users/login";
        }
        session = request.getSession();
        session.setAttribute("user", userOptional.get());
        return "redirect:/vacancies";
    }

    @GetMapping("/logout")
    public String logout(Model model, HttpSession session) {
        UserController.checkUser(model, session);
        session.invalidate();
        return "redirect:/users/login";
    }
}
