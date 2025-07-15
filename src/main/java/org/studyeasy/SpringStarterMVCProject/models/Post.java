package org.studyeasy.SpringStarterMVCProject.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)//updation sequentially
    private Long id;

    @NotBlank(message = "Missing Post title")
    private String title;

    @Column(columnDefinition = "TEXT")
    @NotBlank(message = "Missing Form Body")
    private String body;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name= "account_id", referencedColumnName = "id", nullable = true)  //a new column will be added in db which will contain foreign key
    private Account account;
    //name of table acc id
    //col name id
    //nullable marks whether we'll accept a post without user or not
}
