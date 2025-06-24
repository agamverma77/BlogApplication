package org.studyeasy.SpringStarterMVCProject.Controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.studyeasy.SpringStarterMVCProject.models.Account;
import org.studyeasy.SpringStarterMVCProject.models.Post;
import org.studyeasy.SpringStarterMVCProject.services.AccountService;
import org.studyeasy.SpringStarterMVCProject.services.PostService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class PostController {
    @Autowired
    private PostService postService;

    @Autowired
    private AccountService accountService;

    @GetMapping("/post/{id}")
    public String getPost(@PathVariable Long id, Model model, Principal principal) {
        Optional<Post> optionalPost = postService.getById(id);
        String authUser = "email";
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            model.addAttribute("post", post);

            /*
             * other way to get username of current logged in user
             * String
             * username=SecurityContextHolder.getContext().getAuthentication().getName();
             */

            if (principal != null) {
                authUser = principal.getName();
            }
            if (authUser.equals(post.getAccount().getEmail())) {
                model.addAttribute("isOwner", true);
            } else {
                model.addAttribute("isOwner", false);
            }
            return "post";
        } else {
            return "404";
        }

    }

    @GetMapping("/posts/add")
    @PreAuthorize("isAuthenticated()")
    public String addPost(Model model, Principal principal) {
        String authUser = "email";
        if (principal != null) {
            authUser = principal.getName();
        }
        Optional<Account> optionalAccount = accountService.findOneByEmail(authUser);
        if (optionalAccount.isPresent()) {
            Post post = new Post();
            post.setAccount(optionalAccount.get());
            model.addAttribute("post", post);
            return "post_add";
        } else {
            return "redirect:/";
        }

    }

    @PostMapping("/posts/add")
    @PreAuthorize("isAuthenticated()") // if user is not authenticated it'll not allow user to come here
    public String addPostHandler(@ModelAttribute Post post, Principal principal) {
        String authUser = "email";
        if (principal != null) {
            authUser = principal.getName();// getting current author email, logged in one
        }
        if (post.getAccount().getEmail().compareToIgnoreCase(authUser) < 0) // if email of post is not equal to the
                                                                            // current author
        {
            return "redirect:/?error";
        }
        // everything good then save post
        postService.save(post);

        return "redirect:/post/" + post.getId();
    }

    @GetMapping("/post/{id}/edit")
    @PreAuthorize("isAuthenticated()")
    public String getPostForEdit(@PathVariable Long id, Model model) {
        Optional<Post> optionalPost = postService.getById(id);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            model.addAttribute("post", post);
            return "post_edit";
        } else {
            return "404";
        }
    }

    @PostMapping("/post/{id}/edit")
    @PreAuthorize("isAuthenticated()")
    public String updatePost(@PathVariable Long id,@ModelAttribute Post post)
    {
     Optional<Post> optionalPost=postService.getById(id);
     if(optionalPost.isPresent())
     {
        Post existingPost=optionalPost.get();
        existingPost.setTitle(post.getTitle());//in the existing post s et the tirle which you got from edited post
        existingPost.setBody(post.getBody());
        postService.save(existingPost);
        return "redirect:/post/"+existingPost.getId();
     }
       return "redirect:/?error";
        
    }

}
