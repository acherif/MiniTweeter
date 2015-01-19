/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ecp.tweet.db;

import fr.ecp.tweet.exception.DbException;
import fr.ecp.tweet.model.Tweet;
import fr.ecp.tweet.model.User;
import java.util.List;

/**
 *
 * @author Hamid
 */
public interface IDbRepo {
    

    
    public void connect();
    
    public void addUser(User user) throws DbException; 
    
    public boolean authenticate(String handler, String token) throws DbException;
    
    public String generateToken(String handler, String password) throws DbException;
    
    public List<User> getUsers();
    
    public List<Tweet> getTweets(String handler) throws DbException;
    
    public List<User> getFollowers(String handler) throws DbException;
    
    public List<User> getFollowing(String handler) throws DbException;
    
    public void follow(String userHander, String toFollowHandler)  throws DbException;
    
    public void unFollow(String userHander, String toUnFollowHandler) throws DbException;
    
    public void addTweet(String handler, Tweet tweet) throws DbException;
       
}
