/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ecp.tweet.model;

import java.util.Date;

/**
 *
 * @author grk
 */
public class Tweet {

    private String content;
    private Date timeStamp;
    private String userHandle;

    public Tweet() {
    }

    
    public Tweet(String content, String userHandle) {
        this.content = content;
        this.userHandle = userHandle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
    
    public String getUserHandle() {
        return userHandle;
    }

    public void setUserHandle(String userHandle) {
        this.userHandle = userHandle;
    }
    
    
    
}
