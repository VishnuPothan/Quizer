package com.vishnu.quizer;

public class postUpload {
    String title, content , author , star , date ;


    public postUpload(){
    }
    public postUpload(String title , String content , String author , String date , String star){
        this.title = title;
        this.content = content;
        this.author = author;
        this.date = date;
        this.star = star;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public String getStar() {
        return star;
    }

    public String getDate() {
        return date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

