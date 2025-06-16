package nbcc.springresto.controllers;

import nbcc.springresto.services.user.LoginServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class HomeController {
    private final LoginServiceImpl loginService;

    public HomeController(LoginServiceImpl loginService) {
        this.loginService = loginService;
    }


    @ModelAttribute
    public void addLoggedInAttribute(Model model) {
        model.addAttribute("isLoggedIn", loginService.isCurrentLoginValid());
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }
}