package com.example.objectserialization;

import java.util.ArrayList;
import java.io.Serializable;

public class Person implements Serializable{
    private String name = "";
    private String phone = "";
    private String email = "";

    private static final long serialVercionUID = 1L;

    public Person(String name, String phone, String email){
        this.name = name;
        this.phone = phone;
        this.email = email;
    }//Person

    public String get_name(){return name;}//get_name
    public void set_name(String name){this.name = name;}//set_name

    public String get_phone(){return phone;}//get_phone
    public void set_phone(String phone){this.phone = phone;}//set_phone

    public String get_email(){return email;}//get_email
    public void set_email(String email){this.email = email;}//set_email

    public String toString(){
        return name + ":" + phone + ":" + email;
    }
}
