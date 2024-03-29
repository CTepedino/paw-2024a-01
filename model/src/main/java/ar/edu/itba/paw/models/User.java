package ar.edu.itba.paw.models;

public class User {

    private long userId;
    private final String username;

    public User(long userId, String username){
        this.userId = userId;
        this.username = username;
    }

    public String getUsername(){
        return username;
    }

    public long getUserId(){
        return userId;
    }

}
