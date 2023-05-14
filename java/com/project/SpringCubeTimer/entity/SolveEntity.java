package com.project.SpringCubeTimer.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "solve")
public class SolveEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long solveId;

    @NotEmpty
    @Column(name = "cube_variable")
    private String cube;

    @NotEmpty
    @Column(name = "scramble")
    private String scramble;

    @NotEmpty
    @Column(name = "time")
    private String time;

    @NotEmpty
    @Column(name = "penalty")
    private String penalty;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public SolveEntity() {}

    public SolveEntity(String scramble, String time, String cube, String penalty, UserEntity user) {
        this.scramble = scramble;
        this.time = time;
        this.cube = cube;
        this.penalty = penalty;
        this.user = user;
    }

    public Long getSolveId() {
        return solveId;
    }

    public void setSolveId(Long solveId) {
        this.solveId = solveId;
    }

    public String getCube() {
        return cube;
    }

    public void setCube(String cube) {
        this.cube = cube;
    }

    public String getScramble() {
        return scramble;
    }

    public void setScramble(String scramble) {
        this.scramble = scramble;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPenalty() {
        return penalty;
    }

    public void setPenalty(String penalty) {
        this.penalty = penalty;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "SolveEntity{" +
                "solveId=" + solveId +
                ", cube='" + cube + '\'' +
                ", scramble='" + scramble + '\'' +
                ", time='" + time + '\'' +
                ", user=" + user +
                '}';
    }
}
