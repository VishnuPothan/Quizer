package com.vishnu.quizer;

public class UserHelperClass {

    String fname ,uname , phone , imgUrl , rank , follower  ,following , post , quiz , poll;

    public UserHelperClass() {

    }

    public UserHelperClass(String fname , String uname, String phone, String imgUrl , String  rank , String follower , String following , String post , String quiz , String poll) {
        this.fname = fname;
        this.uname = uname;
        this.phone = phone;
        this.imgUrl = imgUrl;
        this.rank = rank;
        this.follower = follower;
        this.following = following;
        this.post = post;
        this.quiz = quiz;
        this.poll =poll;
    }



    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getFollower() {
        return follower;
    }

    public void setFollower(String follower) {
        this.follower = follower;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getQuiz() {
        return quiz;
    }

    public void setQuiz(String quiz) {
        this.quiz = quiz;
    }

    public String getPoll() {
        return poll;
    }

    public void setPoll(String poll) {
        this.poll = poll;
    }
}
