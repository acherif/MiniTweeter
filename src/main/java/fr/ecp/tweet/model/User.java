/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ecp.tweet.model;

import com.wordnik.swagger.annotations.ApiModel;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Hamid
 */
@ApiModel(value = "A user's tweet")
@XmlRootElement(name = "Tweet")
public class User {

    private String handle;
    private String password;
    private String server;
    private String _id = "0";
    
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
    
    
    
}
