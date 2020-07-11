package com.vishnu.quizer;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;
@IgnoreExtraProperties
public class Post {

    public String fname;
    public String imgUrl;
    public String phone;
    public String uname;
    public String rank;
    public String follower;
    public String following;
    public String post;
    public String quiz;
    public String poll;

    public int starCount = 0;

    public Map<String, Boolean> stars = new HashMap<>();

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Post(String fname, String imgUrl, String phone, String uname , String rank , String following, String follower , String poll ,String post ,String quiz   ) {
        this.fname = fname;
        this.imgUrl = imgUrl;
        this.phone = phone;
        this.uname = uname;
        this.rank = rank;
        this.follower = follower;
        this.following = following;
        this.post = post;
        this.poll = poll;
        this.quiz = quiz;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("fname", fname);
        result.put("imgUrl", imgUrl);
        result.put("phone", phone);
        result.put("uname", uname);

        result.put("rank", rank);
        result.put("follower", follower);
        result.put("following", following);
        result.put("poll", poll);
        result.put("post", post);
        result.put("quiz", quiz);

        result.put("starCount", starCount);
        result.put("stars", stars);

        return result;
    }
}
