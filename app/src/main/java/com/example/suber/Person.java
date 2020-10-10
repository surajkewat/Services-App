package com.example.suber;

public class Person {
    public  String personCode;
    public String name;
    public String contact;
    public String profession;
    public String state ;
    public String city;
    public String personId;
    public String imgUrl;
    public String email;
    public String gender;
    public Person(){}

    public Person(String personCode, String name, String contact, String profession, String state, String city, String personId, String imgUrl,String email,String gender) {
        this.personCode = personCode;
        this.name = name;
        this.contact = contact;
        this.profession = profession;
        this.state = state;
        this.city = city;
        this.personId = personId;
        this.imgUrl = imgUrl;
        this.email = email;
        this.gender = gender;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPersonCode() {
        return personCode;
    }

    public void setPersonCode(String personCode) {
        this.personCode = personCode;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
       this.personId = personId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    private String mName;
    private String mImageUrl;

    public Person(String name, String imageUrl) {
        if (name.trim().equals("")) {
            name = "No Name";
        }

        mName = name;
        mImageUrl = imageUrl;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
