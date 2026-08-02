package org.studyeasy.SpringStarterMVCProject.Controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.studyeasy.SpringStarterMVCProject.models.Account;
import org.studyeasy.SpringStarterMVCProject.models.Comment;
import org.studyeasy.SpringStarterMVCProject.models.Post;
import org.studyeasy.SpringStarterMVCProject.services.AccountService;
import org.studyeasy.SpringStarterMVCProject.services.AiSummarizationService;
import org.studyeasy.SpringStarterMVCProject.services.PostService;

import jakarta.validation.Valid;

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AiSummarizationService aiSummarizationService;

    @GetMapping("/post/{id}")
    public String getPost(@PathVariable Long id, Model model, Principal principal) {
        Optional<Post> optionalPost = postService.getById(id);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            model.addAttribute("post", post);
            model.addAttribute("comment", new Comment());

            String authUser = principal != null ? principal.getName() : "";
            if (post.getAccount() != null && authUser.equalsIgnoreCase(post.getAccount().getEmail())) {
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
        String authUser = principal != null ? principal.getName() : null;
        if (authUser != null) {
            Optional<Account> optionalAccount = accountService.findOneByEmail(authUser);
            if (optionalAccount.isPresent()) {
                Post post = new Post();
                post.setAccount(optionalAccount.get());
                model.addAttribute("post", post);
                return "post_add";
            }
        }
        return "redirect:/login";
    }

    @PostMapping("/posts/add")
    @PreAuthorize("isAuthenticated()")
    public String addPostHandler(@Valid @ModelAttribute Post post, BindingResult result, Principal principal) {
        if (result.hasErrors()) {
            return "post_add";
        }
        if (principal == null) {
            return "redirect:/login";
        }

        String authUser = principal.getName();
        Optional<Account> optionalAccount = accountService.findOneByEmail(authUser);
        if (optionalAccount.isEmpty()) {
            return "redirect:/login";
        }

        // Attach logged in account to the post
        post.setAccount(optionalAccount.get());

        // Generate AI summary if configured
        try {
            String summary = aiSummarizationService.generateSummary(post.getBody());
            post.setSummary(summary);
        } catch (Exception e) {
            post.setSummary(null);
        }

        Post savedPost = postService.save(post);
        return "redirect:/post/" + savedPost.getId();
    }

    @GetMapping("/post/{id}/edit")
    @PreAuthorize("isAuthenticated()")
    public String getPostForEdit(@PathVariable Long id, Model model, Principal principal) {
        Optional<Post> optionalPost = postService.getById(id);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            String authUser = principal != null ? principal.getName() : "";
            if (post.getAccount() != null && !authUser.equalsIgnoreCase(post.getAccount().getEmail())) {
                return "redirect:/?error";
            }
            model.addAttribute("post", post);
            return "post_edit";
        } else {
            return "404";
        }
    }

    @PostMapping("/post/{id}/edit")
    @PreAuthorize("isAuthenticated()")
    public String updatePost(@Valid @ModelAttribute Post post, BindingResult result, @PathVariable Long id, Principal principal) {
        if (result.hasErrors()) {
            return "post_edit";
        }

        Optional<Post> optionalPost = postService.getById(id);
        if (optionalPost.isPresent()) {
            Post existingPost = optionalPost.get();
            String authUser = principal != null ? principal.getName() : "";
            if (existingPost.getAccount() != null && !authUser.equalsIgnoreCase(existingPost.getAccount().getEmail())) {
                return "redirect:/?error";
            }
            existingPost.setTitle(post.getTitle());
            existingPost.setBody(post.getBody());
            try {
                String summary = aiSummarizationService.generateSummary(existingPost.getBody());
                if (summary != null && !summary.isEmpty()) {
                    existingPost.setSummary(summary);
                }
            } catch (Exception e) {
                // Ignore AI summary errors during update
            }
            postService.save(existingPost);
            return "redirect:/post/" + existingPost.getId();
        }
        return "redirect:/?error";
    }

    @GetMapping("/post/{id}/delete")
    @PreAuthorize("isAuthenticated()")
    public String deletePost(@PathVariable Long id, Principal principal) {
        Optional<Post> optionalPost = postService.getById(id);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            String authUser = principal != null ? principal.getName() : "";
            if (post.getAccount() != null && !authUser.equalsIgnoreCase(post.getAccount().getEmail())) {
                return "redirect:/?error";
            }
            postService.delete(post);
            return "redirect:/";
        } else {
            return "redirect:/?error";
        }
    }
}
