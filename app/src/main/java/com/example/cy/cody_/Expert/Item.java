package com.example.cy.cody_.Expert;

public class Item {
    int ID;
    String User_Name;
    String User_Picture;  /*************** 이거 String 바꿔야함 ************/
    String image;
    String title;
    int getID (){
        return this.ID;
    }
    String getManager_Name(){
        return this.User_Name;
    }
    String getManager_Picture(){
        return this.User_Picture;
    }
    String getImage() {
        return this.image;
    }
    String getTitle() {
        return this.title;
    }

    Item(int id,String title, String image, String user_name, String user_picture) {
        this.ID = id;
        this.User_Name = user_name;
        this.User_Picture = user_picture;
        this.image = image;
        this.title = title;
    }
}
