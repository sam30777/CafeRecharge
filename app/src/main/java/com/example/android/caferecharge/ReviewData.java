package com.example.android.caferecharge;

public class ReviewData {
    String user_Name;
    String review ;
    Integer user_image;
    ReviewData(String user_Name, String review, Integer user_image){
        this.user_Name=user_Name;
        this.review=review;
        this.user_image=user_image;
    }
    ReviewData(){}
    public void setUser_Name(String user_Name) {
        this.user_Name = user_Name;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public void setUser_image(Integer user_image) {
        this.user_image = user_image;
    }

    public String getUser_Name() {
        return user_Name;
    }

    public String getReview() {
        return review;
    }

    public Integer getUser_image() {
        return user_image;
    }
}
