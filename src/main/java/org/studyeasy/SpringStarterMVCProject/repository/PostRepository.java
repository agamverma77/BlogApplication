package org.studyeasy.SpringStarterMVCProject.repository;

//import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.studyeasy.SpringStarterMVCProject.models.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{
    
}
