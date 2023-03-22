package com.project.SpringCubeTimer.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "solve")
public class SolveEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long solveId;

    @Column(name = "cube_variable")
    private String cube;

    @Column(name = "scramble")
    private String scramble;

    @Column(name = "time")
    private String time;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public SolveEntity() {}

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
