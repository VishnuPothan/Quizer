package com.vishnu.quizer;

import java.util.HashMap;
import java.util.Map;

public class postToCard {

    public String title;
    public String contents;
    public String star;
    public String author;
    public String date;
    public Map<String, Boolean> stars = new HashMap<>();
    public postToCard(){
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public postToCard(String title , String contents  , String star , String author ,String date ){
        this.title = title;
        this.contents = contents;
        this.author = author;
        this.date = date;
        this.star = star;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("title",title);
        result.put("contents",contents);
        result.put("author",author);
        result.put("date",date);
        result.put("star",star);

        return result;
    }
}
