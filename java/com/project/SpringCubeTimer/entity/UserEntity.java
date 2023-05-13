package com.project.SpringCubeTimer.entity;

import com.project.SpringCubeTimer.entity.consts.ValidationConst;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name = "user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotEmpty(message = ValidationConst.USERNAME_EMPTY_MESSAGE)
    @Size(min = 2, max = 20)
    private String username;

    @NotEmpty(message = ValidationConst.PASSWORD_EMPTY_MESSAGE)
    private String password;

    @NotEmpty(message = ValidationConst.EMAIL_EMPTY_MESSAGE)
    @Email(message = ValidationConst.EMAIL_NOT_VALID_MESSAGE)
    private String email;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<SolveEntity> solves;

    public UserEntity() {}

    public UserEntity(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<SolveEntity> getSolves() {
        return solves;
    }

    public void setSolves(List<SolveEntity> solves) {
        this.solves = solves;
    }
}
