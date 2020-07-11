package com.vishnu.quizer;

public class ScoreUpload {

    String qno , score;

    public ScoreUpload(){

    }

    public ScoreUpload(String qno , String score){
        this.qno = qno;
        this.score = score;
    }

    public String getQno() {
        return qno;
    }

    public String getScore() {
        return score;
    }

    public void setQno(String qno) {
        this.qno = qno;
    }

    public void setScore(String score) {
        this.score = score;
    }
}

