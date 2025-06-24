package org.studyeasy.SpringStarterMVCProject.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.studyeasy.SpringStarterMVCProject.models.Authority;
import org.studyeasy.SpringStarterMVCProject.repository.AuthorityRepository;

@Service
public class AuthorityService {

    @Autowired
    private AuthorityRepository authorityRepository;
    

    public Authority save(Authority authority)
    {
        return authorityRepository.save(authority);
    }
    
    public Optional<Authority> findbyId(Long id)
    {
        return authorityRepository.findById(id);
    }
}
