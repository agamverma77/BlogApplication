package org.studyeasy.SpringStarterMVCProject.Controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.studyeasy.SpringStarterMVCProject.models.Account;
import org.studyeasy.SpringStarterMVCProject.services.AccountService;
import org.studyeasy.SpringStarterMVCProject.util.AppUtil;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
public class AccountController {
    @Autowired
    private AccountService accountService;
    @GetMapping("/register")
    public String register(Model model)
    {
        Account account=new Account();
        model.addAttribute("account",account);
        return "register";
    }

    @PostMapping("/register")
    public String register_user(@Valid @ModelAttribute Account account, BindingResult result) {

        if(result.hasErrors())
        {
            return "register";  //if there are errors we 'll not redirect to home page doing this all details will be lost, we'll return the registration view
        }
        accountService.save(account);
        return "redirect:/";//we'll redirect to home page after registration
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")//this ensures if user is using this end point(api) means user is definetly loggen in
    public String profile(Model model, Principal principal) {
        String authUser="email";
        if(principal!=null)
        {
            authUser=principal.getName();
        }
        Optional<Account> optionalAccount=accountService.findOneByEmail(authUser);
        if(optionalAccount.isPresent())
        {
            Account account=optionalAccount.get();
            model.addAttribute("account", account);
            model.addAttribute("photo", account.getPhoto());
            return "profile";
        }
        else{
            return "redirect:/?error";
        }
    }

     @PostMapping("/profile")
     @PreAuthorize("isAuthenticated()")
     public String post_profile(@Valid @ModelAttribute Account account, BindingResult result, Principal principal)
     {
        if(result.hasErrors())
        {
             return "profile";
        }
        String authUser="email";
        if(principal!=null)
        {
            authUser=principal.getName();
        }
        Optional<Account> optionalAccount = accountService.findOneByEmail(authUser);
        if(optionalAccount.isPresent())
        {
            Account account_by_id=accountService.findById(account.getId()).get();//we'll get account by id & then update profile for this make hidden id field in profile.html & make findbyid method in accountservice
            //by the above lines we'll get the updated values just need to set them
            account_by_id.setAge(account.getAge());
            account_by_id.setDate_of_birth(account.getDate_of_birth());
            account_by_id.setFirstname(account.getFirstname());
            account_by_id.setGender(account.getGender());
            account_by_id.setLastname(account.getLastname());
            account_by_id.setPassword(account.getPassword());
            accountService.save(account_by_id);//save the info
            SecurityContextHolder.clearContext();//this logs out user
            return "redirect:/"; //once updated redirect to home page
        }
        else{
            return "redirect:/?error";
        }
     }

     @PostMapping("/update-photo")
     @PreAuthorize("isAuthenticated()")
     public String updatePhoto(@RequestParam("file") MultipartFile file,RedirectAttributes attributes,Principal principal) {
            if (file.isEmpty()) {
                attributes.addFlashAttribute("error", "No file uploaded.");
                return "redirect:/profile";
            }
            else{
                String fileName = StringUtils.cleanPath(file.getOriginalFilename());//this gives us filename uploaded by te user
                try{
                    int length=10;
                    boolean useLetters=true;
                    boolean useNumbers=true;
                String generatedString = RandomStringUtils.random(length, useLetters, useNumbers);//this is a random generated text to add with filename
                String finalPhotoName = generatedString + fileName;
                String absolutefileLocation= AppUtil.getUploadPath(finalPhotoName);//get absolute path of file uploaded by user

                Path path=Paths.get(absolutefileLocation);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                attributes.addFlashAttribute("message","You successfully uploaded");
                String authUser="email";
                if(principal!=null)
                {
                    authUser=principal.getName();
                }
                Optional<Account> optionalAccount = accountService.findOneByEmail(authUser);
                if(optionalAccount.isPresent())
                {
                    Account account=optionalAccount.get();
                    Account account_by_id=accountService.findById(account.getId()).get();
                    String relativefilelocation = "/uploads/" + finalPhotoName;
                    account_by_id.setPhoto(relativefilelocation);
                    accountService.save(account_by_id);
                }
                try{
                    TimeUnit.SECONDS.sleep(1);
                }
                catch(InterruptedException ie){
                    Thread.currentThread().interrupt();
                }
                return "redirect:/profile";
                }
                catch(Exception e){

                }
            }
            return "redirect:/profile?error";
  }
    @GetMapping("/test")
    public String test(Model model) {
        return "test";
    }
    
}
