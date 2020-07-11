package com.vishnu.quizer;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties

public class quizPost {
    public String today;
    public int starCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();

    public quizPost() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public quizPost(String today){
        this.today = today;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("today", today);
        return result;
    }

}