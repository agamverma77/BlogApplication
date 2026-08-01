package org.studyeasy.SpringStarterMVCProject.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.studyeasy.SpringStarterMVCProject.models.Post;
import org.studyeasy.SpringStarterMVCProject.repository.PostRepository;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository; //obj of PostRepository which provides various functions to interact with the database
    public Optional<Post> getById(Long id) {
        return postRepository.findById(id);
    }
    //now we'll list all posts so we'll return a list, ordered by newest first
    public List<Post> getAll() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    // search posts by keyword in title or body
    public List<Post> search(String keyword) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            return postRepository.findByTitleContainingIgnoreCaseOrBodyContainingIgnoreCaseOrderByCreatedAtDesc(keyword.trim(), keyword.trim());
        }
        return getAll();
    }
    //now we'll delete post
    public void delete(Post post) {
        postRepository.delete(post);
    }
    //now we'll save post, once we save data, we'll return object of post which will contain all info that got saved
    public Post save(Post post) {
        if(post.getId()==null)//it means new post
        {//we are saving a new record so we need to capture when it got saved
            post.setCreatedAt(LocalDateTime.now());
        }
        post.setUpdatedAt(LocalDateTime.now());
        return postRepository.save(post);
    }
}
