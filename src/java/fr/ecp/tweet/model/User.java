/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ecp.tweet.model;

/**
 *
 * @author Hamid
 */
public class User {

    private String handle;
    private String password;
    private String server;
    private String _id = "0";
    private String picture;
    private String token = null;
    
    public User(){
        
    }
    public User(String handle, String password, String server) {
        this.handle = handle;
        this.password = password;
        this.server = server;
    }

    
    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getUserId() {
        return _id;
    }

    public void setUserId(String userId) {
        this._id = userId;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    
    
    
}
