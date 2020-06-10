package com.example.first.Model_Hope;

public class User_Hope {
    private String id;
    private  String username;
    private String imageURL;
    private String search;
    private String status;
    private String uploadbook;


    public User_Hope(String id, String username, String imageURL,String search,String status,String uploadbook) {
        this.id = id;
        this.username = username;
        this.imageURL = imageURL;
     this.status=status;
        this.search=search;
        this.uploadbook=uploadbook;
    }
    public User_Hope()
    {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageurl() {
        return imageURL;
    }

    public void setImageurl(String imageurl) {
        this.imageURL = imageURL;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getItem_image() {
        return uploadbook;
    }

    public void setItem_image(String uploadbook) {
        this.uploadbook = uploadbook;
    }
}

