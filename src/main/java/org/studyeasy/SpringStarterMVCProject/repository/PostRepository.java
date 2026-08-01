package org.studyeasy.SpringStarterMVCProject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.studyeasy.SpringStarterMVCProject.models.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc();
    List<Post> findByTitleContainingIgnoreCaseOrBodyContainingIgnoreCaseOrderByCreatedAtDesc(String title, String body);
}
