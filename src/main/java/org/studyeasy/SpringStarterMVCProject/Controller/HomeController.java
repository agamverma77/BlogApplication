package org.studyeasy.SpringStarterMVCProject.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.studyeasy.SpringStarterMVCProject.models.Post;
import org.studyeasy.SpringStarterMVCProject.services.PostService;

@Controller
public class HomeController {
    @Autowired
    private PostService postService;

    @GetMapping("/")
    public String home(@RequestParam(name = "search", required = false) String search, Model model) {
        List<Post> posts;
        if (search != null && !search.trim().isEmpty()) {
            posts = postService.search(search);
            model.addAttribute("searchQuery", search.trim());
        } else {
            posts = postService.getAll();
        }
        model.addAttribute("posts", posts);
        return "home";
    }
}
