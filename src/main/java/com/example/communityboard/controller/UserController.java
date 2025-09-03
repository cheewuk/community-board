package com.example.communityboard.controller;

import com.example.communityboard.dto.UserRegistrationDto;
import com.example.communityboard.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userRegistrationDto", new UserRegistrationDto());
        return "users/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("userRegistrationDto") UserRegistrationDto userRegistrationDto,
                               BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "users/register";
        }

        if (!userRegistrationDto.getPassword().equals(userRegistrationDto.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "passwordMismatch", "Password confirmation does not match.");
            return "users/register";
        }

        try {
            userService.registerNewUser(userRegistrationDto.getUsername(), userRegistrationDto.getPassword(), userRegistrationDto.getEmail());
        } catch (IllegalStateException e) {
            bindingResult.rejectValue("username", "registrationError", e.getMessage());
            return "users/register";
        }

        return "redirect:/users/login"; // 회원가입 성공 후 로그인 페이지로 리다이렉트
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "users/login";
    }
}
