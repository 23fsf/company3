package com.ahmed22.company;

public class Listitems {

    private String name,age,imageUri;
    public Listitems(String name, String age ,String imageUri) {
        this.name = name;
        this.age = age;
        this.imageUri=imageUri;
    }

    public String getImageUri() {return imageUri;}

    public void setImageUri(String image) {this.imageUri = image;}
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}