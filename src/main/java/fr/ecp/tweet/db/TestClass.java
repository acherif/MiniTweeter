/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ecp.tweet.db;

import fr.ecp.tweet.model.Tweet;
import fr.ecp.tweet.model.User;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Hamid
 */
public class TestClass implements IDbRepo{

    
    @Override
    public void addUser(User user) {
        System.out.println("user = " + user.getHandle() + ", " + user.getPassword() + ", " + user.getServer());        
    }

    @Override
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User("handle1", "pasw1", "server1"));
        users.add(new User("handle2", "pasw2", "server2"));
        return users;        
    }

    @Override
    public List<Tweet> getTweets(String handler) {
        List<Tweet> tweets = new ArrayList<>();
        tweets.add(new Tweet("tweets1", "handle1"));
         tweets.add(new Tweet("tweets2", "handle2"));
         return tweets;
    }

    @Override
    public List<User> getFollowers(String handler) {
        List<User> users = new ArrayList<>();
        users.add(new User("follower1", "pasw1", "server1"));
        users.add(new User("follower2", "pasw2", "server2"));
        return users;     
    }

    @Override
    public List<User> getFollowing(String handler) {
        List<User> users = new ArrayList<>();
        users.add(new User("following1", "pasw1", "server1"));
        users.add(new User("following2", "pasw2", "server2"));
        return users;  
    }

    @Override
    public void follow(String userHander, String toFollowHandler) {
        System.out.println("follow" + userHander + ", " + toFollowHandler);
    }

    @Override
    public void unFollow(String userHander, String toUnFollowHandler) {
       System.out.println("follow" + userHander + ", " + toUnFollowHandler);
    }

    @Override
    public void connect() {

    }

    @Override
    public void addTweet(String handler, Tweet tweet) {
        System.out.println("handler = " + handler + "tweet = " + tweet.getContent() + ", " + tweet.getUserHandle());        
    }
    
}
