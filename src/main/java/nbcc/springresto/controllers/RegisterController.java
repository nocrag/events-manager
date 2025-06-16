package nbcc.springresto.controllers;

import jakarta.validation.Valid;
import nbcc.springresto.entities.user.UserDetails;
import nbcc.springresto.services.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {
    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("userDetail", new UserDetails());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("userDetail") @Valid UserDetails userDetails,
                           BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "register";
        }

        if (userService.usernameExists(userDetails.getUsername().trim())) {
            bindingResult.rejectValue("username", "error.user", "Username already exists.");
            return "register";
        }

        if (userService.emailExists(userDetails.getEmail())) {
            bindingResult.rejectValue("email", "error.user", "Email already exists.");
            return "register";
        }

        try {
            userService.register(userDetails);
            model.addAttribute("successMessage", "Registration successful. You can now log in.");
            return "register";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }
    }
}
