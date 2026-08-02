package org.studyeasy.SpringStarterMVCProject.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.studyeasy.SpringStarterMVCProject.models.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findAllByEmailIgnoreCase(String email);
    Optional<Account> findFirstByEmailIgnoreCase(String email);
}
