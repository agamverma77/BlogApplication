package org.studyeasy.SpringStarterMVCProject.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.studyeasy.SpringStarterMVCProject.models.Account;
import org.studyeasy.SpringStarterMVCProject.services.AccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
public class AccountController {
    @Autowired
    private AccountService accountService;
    @GetMapping("/register")
    public String register(Model model)
    {
        Account account=new Account();
        model.addAttribute("account",account);
        return "register";
    }

    @PostMapping("/register")
    public String register_user(@ModelAttribute Account account) {
        accountService.save(account);
        return "redirect:/";//we'll redirect to home page after registration
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        return "profile";
    }

    @GetMapping("/test")
    public String test(Model model) {
        return "test";
    }
    
}
