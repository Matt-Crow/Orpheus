package controllers;

/**
 * not implemented yet
 * @author Matt Crow
 */
public class User {
    private String userName;
    
    public User(String name){
        userName = name;
    }
    
    public User(){
        this("name not set");
    }
}
