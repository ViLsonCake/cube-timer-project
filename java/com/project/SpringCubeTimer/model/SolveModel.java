package com.project.SpringCubeTimer.model;

public class SolveModel {
    private String time;
    private String scramble;
    private String cube;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getScramble() {
        return scramble;
    }

    public void setScramble(String scramble) {
        this.scramble = scramble;
    }

    public String getCube() {
        return cube;
    }

    public void setCube(String cube) {
        this.cube = cube;
    }

    @Override
    public String toString() {
        return "SolveModel{" +
                "time='" + time + '\'' +
                ", scramble='" + scramble + '\'' +
                ", cube='" + cube + '\'' +
                '}';
    }
}
