package org.studyeasy.SpringStarterMVCProject.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.studyeasy.SpringStarterMVCProject.models.Account;

@Controller
public class AdminController {
    
    @GetMapping("/admin")
    public String admin(Model model)
    {
        
        return "admin";
    }
}
