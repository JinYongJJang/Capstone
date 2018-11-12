package com.example.cy.cody_.How_Cloth;

public class Item {
    String User_Name;
    String User_Picture;  /*************** 이거 String 바꿔야함 ************/
    String image;
    String title;

    String getUser_Name(){
        return this.User_Name;
    }
    String getUser_Picture(){
        return this.User_Picture;
    }
    String getImage() {
        return this.image;
    }
    String getTitle() {
        return this.title;
    }

    Item(String title, String image, String user_name, String user_picture) {
        this.User_Name = user_name;
        this.User_Picture = user_picture;
        this.image = image;
        this.title = title;
    }
}
