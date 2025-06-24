package org.studyeasy.SpringStarterMVCProject.models;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)//updation sequentially
    private Long id;

    private String email;

    private String password;

    private String firstname;

    private String lastname;

    private String role;

    //every user can have post or may not have post, but every post will have a user so for that we will have to create a relationship
    //one to many relationship from one account to many posts
    @OneToMany(mappedBy = "account")
    private List<Post> posts;//one to many achieved through List , reverse needs to be implemented in Post

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name="account_authority",
        joinColumns={@JoinColumn(name="account_id",referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name="authority_id",referencedColumnName = "id")})
        private Set<Authority> authorities=new HashSet<>();
}
