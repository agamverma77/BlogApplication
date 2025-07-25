package org.studyeasy.SpringStarterMVCProject.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.studyeasy.SpringStarterMVCProject.models.Post;
import org.studyeasy.SpringStarterMVCProject.services.PostService;


@Controller
public class HomeController {
    @Autowired
    private PostService postService;
    @GetMapping("/")
    public String home(Model model) 
    {
        List<Post> posts=postService.getAll();
        model.addAttribute("posts",posts);//now the attribute posts will hold all posts
        return "home";
    }  
    
}
