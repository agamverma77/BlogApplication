package org.studyeasy.SpringStarterMVCProject.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.studyeasy.SpringStarterMVCProject.models.Authority;;
@Repository
public interface AuthorityRepository extends JpaRepository<Authority,Long>{
    
}
