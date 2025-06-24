package org.studyeasy.SpringStarterMVCProject.config;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.studyeasy.SpringStarterMVCProject.models.Account;
import org.studyeasy.SpringStarterMVCProject.models.Authority;
import org.studyeasy.SpringStarterMVCProject.models.Post;
import org.studyeasy.SpringStarterMVCProject.services.AccountService;
import org.studyeasy.SpringStarterMVCProject.services.AuthorityService;
import org.studyeasy.SpringStarterMVCProject.services.PostService;
import org.studyeasy.SpringStarterMVCProject.util.constants.Privilages;
import org.studyeasy.SpringStarterMVCProject.util.constants.Roles;
@Component
public class SeedData implements CommandLineRunner{

    @Autowired
    private PostService postService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthorityService authorityService;
    @Override
    public void run(String... args) throws Exception {// String ... means this method can reveive any number of arguments including 0
        for(Privilages auth:Privilages.values())//this will iterate on enum & keep on giving values of enum
        {
            //we'll add the objects
            Authority authority=new Authority();
            authority.setId(auth.getAuthorityId());//we'll set the values in this Authority
            authority.setName(auth.getAuthorityString());
            //now we'll save
            //to save we'll autowire the authorityservice
            authorityService.save(authority);//we'll save the authority
        }


       Account account01=new Account();
       Account account02=new Account();
       Account account03=new Account();
       Account account04=new Account();

       account01.setEmail("user@user.com");
       account01.setPassword("pass987");
       account01.setFirstname("user");
       account01.setLastname("lastname");

       account02.setEmail("admin@admin.com");
       account02.setPassword("pass987");
       account02.setFirstname("admin");
       account02.setLastname("lastname");
       account02.setRole(Roles.ADMIN.getRole());

       account03.setEmail("editor@editor.com");
       account03.setPassword("pass987");
       account03.setFirstname("editor");
       account03.setLastname("lastname");
       account03.setRole(Roles.EDITOR.getRole());

       account04.setEmail("super_editor@editor.com");
       account04.setPassword("pass987");
       account04.setFirstname("super_editor");
       account04.setLastname("lastname");
       account04.setRole(Roles.EDITOR.getRole());
       Set<Authority> authorities=new HashSet<>();
       authorityService.findbyId(Privilages.RESET_ANY_USER_PASSWORD.getAuthorityId()).ifPresent(authorities::add);//if we get the value we add it on set
       authorityService.findbyId(Privilages.ACCESS_ADMIN_PANEL.getAuthorityId()).ifPresent(authorities::add);
        account04.setAuthorities(authorities);





       accountService.save(account01);
       accountService.save(account02);
       accountService.save(account03);
       accountService.save(account04);
       




       List<Post> posts=postService.getAll();//we'll check whether there are posts in db, if no posts then we'll add, if posts there then we won't do anything, i.e. if posts is empty we'll add else don't do anything
       if(posts.size()==0)
       {
        //add seed data
        //add 1 post to start with
        Post post01=new Post();
        post01.setTitle("Post 01");
        post01.setBody("Post 01 body.......");
        post01.setAccount(account01);
        postService.save(post01);

        Post post02=new Post();
        post02.setTitle("Post 02");
        post02.setBody("Post 02 body.......");
        post02.setAccount(account02);
        postService.save(post02);
        //we are not adding createdAt because in service layer when record is new we are automatically adding current date and time
       }
    }
    
}
