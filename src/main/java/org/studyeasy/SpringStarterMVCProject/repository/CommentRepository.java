package org.studyeasy.SpringStarterMVCProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.studyeasy.SpringStarterMVCProject.models.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

}
