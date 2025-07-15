package org.studyeasy.SpringStarterMVCProject.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.studyeasy.SpringStarterMVCProject.models.Account;
import org.studyeasy.SpringStarterMVCProject.models.Authority;
import org.studyeasy.SpringStarterMVCProject.repository.AccountRepository;
import org.studyeasy.SpringStarterMVCProject.util.constants.Roles;
@Service
public class AccountService implements UserDetailsService{
    
    //@Value("${spring.mvc.static-path-pattern}")
    //private String photo_prefix;
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    public Account save(Account account)
    {
        account.setPassword(passwordEncoder.encode(account.getPassword()));//this encodes the password before saving it
        if(account.getRole()==null)
        {
        account.setRole(Roles.USER.getRole());
        }
        if(account.getPhoto()==null)
        {
        String path="images/profile.png";
        account.setPhoto(path);
        }
        return accountRepository.save(account);
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Account> optionalAccount=accountRepository.findOneByEmailIgnoreCase(email);//this matches email with db, if found returns the account
        if(!optionalAccount.isPresent())
        {
            throw new UsernameNotFoundException("Account Not Found");
        } 
        //if account found
        Account account=optionalAccount.get();
        //we'll return a User
        List<GrantedAuthority> grantedAuthority =new ArrayList<>();
        grantedAuthority.add(new SimpleGrantedAuthority(account.getRole()));
        for(Authority _auth:account.getAuthorities())//adding authority
        {
            grantedAuthority.add(new SimpleGrantedAuthority(_auth.getName()));//with this whatever name we give to authority in seed data will go in database
        }

        return new User(account.getEmail(),account.getPassword(),grantedAuthority);//here we are passing null to authority, we'll add authority later , lets go with null now, if we run now it'll give error because authorities are essential part 
        //so to temporarily fix it we'll create a list and pass that, we'll create authorities properly later
    }
    public Optional<Account> findOneByEmail(String email)
    {
        return accountRepository.findOneByEmailIgnoreCase(email);
    }
}
