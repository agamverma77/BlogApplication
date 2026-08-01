package org.studyeasy.SpringStarterMVCProject.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.studyeasy.SpringStarterMVCProject.models.Account;
import org.studyeasy.SpringStarterMVCProject.models.Comment;
import org.studyeasy.SpringStarterMVCProject.models.Post;
import org.studyeasy.SpringStarterMVCProject.services.AccountService;
import org.studyeasy.SpringStarterMVCProject.services.CommentService;
import org.studyeasy.SpringStarterMVCProject.services.PostService;

@Controller
public class CommentController {

    @Autowired
    private PostService postService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CommentService commentService;

    @PostMapping("/post/{postId}/comments")
    @PreAuthorize("isAuthenticated()")
    public String addComment(@PathVariable Long postId, @ModelAttribute Comment comment) {
        Optional<Post> optionalPost = postService.getById(postId);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            String authUser = SecurityContextHolder.getContext().getAuthentication().getName();
            Optional<Account> optionalAccount = accountService.findOneByEmail(authUser);
            if (optionalAccount.isPresent()) {
                Account account = optionalAccount.get();
                comment.setId(null);
                comment.setPost(post);
                comment.setAccount(account);
                commentService.save(comment);
                return "redirect:/post/" + post.getId();
            }
        }
        return "redirect:/";
    }
}
