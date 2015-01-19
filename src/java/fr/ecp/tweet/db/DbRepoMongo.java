/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ecp.tweet.db;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import fr.ecp.tweet.exception.DbException;
import fr.ecp.tweet.model.Tweet;
import fr.ecp.tweet.model.User;
import fr.rcp.tweet.security.AccountManager;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hamid
 */
public class DbRepoMongo implements IDbRepo {

    private static DbRepoMongo instance;

    private DB clientDB;

    public static DbRepoMongo getInstance() {
        if (DbRepoMongo.instance == null) {
            DbRepoMongo.instance = new DbRepoMongo();
        }

        return instance;
    }

    @Override
    public void connect() {
        MongoClient mongoClient = null;
        try {
            mongoClient = new MongoClient("localhost", 27017);
        } catch (UnknownHostException ex) {
            Logger.getLogger(DbRepoMongo.class.getName()).log(Level.SEVERE, null, ex);
        }

        clientDB = mongoClient.getDB("MiniTweeter");

    }

    @Override
    public void addUser(User user) throws DbException {

        DBCollection coll = clientDB.getCollection("users");
        if (getUser(user.getHandle()) != null) {
            throw new DbException("Handle already userd : " + user.getHandle());
        }
        System.out.println("password = " + user.getPassword() + " hash = " + AccountManager.generateHash(user.getPassword()));
        DBObject doc = new BasicDBObject("handle", user.getHandle()).append("password", AccountManager.generateHash(user.getPassword())).append("server", user.getPassword());

        coll.insert(doc);
    }

    @Override
    public boolean authenticate(String handler, String token) throws DbException {
        User user = getUser(handler);
        if (user.getToken().equals(token)) {
            return true;
        }
        return false;
    }

    private boolean verifyPassword(String handle, String password) throws DbException {
        verifyUser(handle);
        User user = getUser(handle);
        if (!(user.getHandle().equals(handle) && user.getPassword().equals(AccountManager.generateHash(password)))) {
            return false;
        }

        return true;
    }

    public String generateToken(String handle, String password) throws DbException {
        if (!verifyPassword(handle, password)) {
            return null;
        }
        String hash = AccountManager.hash(System.currentTimeMillis() + "");
        DBCollection coll = clientDB.getCollection("users");
        DBObject searchQuery = new BasicDBObject().append("handle", handle);
        DBObject modifiedObjectUser = new BasicDBObject();
        modifiedObjectUser.put("token", hash);
        coll.update(searchQuery, modifiedObjectUser);

        return hash;

    }

    @Override
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        DBCollection coll = clientDB.getCollection("users");
        try (DBCursor cursor = coll.find();) {
            while (cursor.hasNext()) {
                DBObject obj = cursor.next();
                User user = new User();
                user.setHandle(obj.get("handle").toString());
                user.setPassword(obj.get("password").toString());
                user.setServer(obj.get("server").toString());
                user.setUserId(obj.get("_id").toString());
                users.add(user);
            }

        }
        return users;
    }

    @Override
    public List<Tweet> getTweets(String handler) throws DbException {
        verifyUser(handler);

        List<Tweet> tweets = new ArrayList<>();
        DBCollection coll = clientDB.getCollection("tweets");
        DBObject searchQuery = new BasicDBObject().append("handle", handler);
        try (DBCursor cursor = coll.find(searchQuery)) {
            if (!cursor.hasNext()) {
                throw new DbException("No tweets for : " + handler);
            }
            while (cursor.hasNext()) {
                DBObject obj = cursor.next();
                //On récupère la liste de tweets puis on la cast en tableau
                BasicDBList tweetsObject = (BasicDBList) obj.get("tweets");
                BasicDBObject[] tweetsArray = tweetsObject.toArray(new BasicDBObject[0]);

                //On itère sur la tableau de tweets
                for (DBObject tweetsArray1 : tweetsArray) {
                    //On récupère le contenu du champ tweet, qui est un document avec les champs content et timestamp
                    DBObject tweetObj = (BasicDBObject) tweetsArray1.get("tweet");

                    Tweet tweet = new Tweet();
                    tweet.setUserHandle(handler);
                    tweet.setContent(tweetObj.get("content").toString());
                    tweet.setTimeStamp((Date) tweetObj.get("timestamp"));
                    tweets.add(tweet);
                }

            }
        }
        return tweets;
    }

    @Override
    public List<User> getFollowers(String handler) throws DbException {
        verifyUser(handler);
        DBCollection coll = clientDB.getCollection("users");
        List<User> users = new ArrayList<>();
        DBObject searchQuery = new BasicDBObject().append("handle", handler);
        try (DBCursor cursor = coll.find(searchQuery)) {
            while (cursor.hasNext()) {
                DBObject obj = cursor.next();
                BasicDBList followers = (BasicDBList) obj.get("followers");
                if (followers == null) {
                    return null;
                }
                for (Object follower : followers) {
                    User user = getUser(follower.toString());
                    users.add(user);
                }
            }
        }

        return users;
    }

    @Override
    public List<User> getFollowing(String handler) throws DbException {
        verifyUser(handler);
        DBCollection coll = clientDB.getCollection("users");
        List<User> users = new ArrayList<>();
        DBObject searchQuery = new BasicDBObject().append("handle", handler);
        try (DBCursor cursor = coll.find(searchQuery)) {
            while (cursor.hasNext()) {
                DBObject obj = cursor.next();
                BasicDBList followings = (BasicDBList) obj.get("followings");
                if (followings == null) {
                    return null;
                }
                for (Object following : followings) {
                    verifyUser(following.toString());
                    User user = getUser(following.toString());
                    users.add(user);
                }
            }
        }

        return users;
    }

    @Override
    public void follow(String userHander, String toFollowHandler) throws DbException {
        verifyUser(userHander);
        verifyUser(toFollowHandler);
        DBCollection coll = clientDB.getCollection("users");
        DBObject searchQueryUser = new BasicDBObject().append("handle", userHander);
        DBObject searchQueryFollowing = new BasicDBObject().append("handle", toFollowHandler);
        DBObject modifiedObjectUser = new BasicDBObject();
        DBObject modifiedObjectFollowing = new BasicDBObject();

        modifiedObjectUser.put("$push", new BasicDBObject().append("followings", toFollowHandler));
        coll.update(searchQueryUser, modifiedObjectUser);

        modifiedObjectFollowing.put("$push", new BasicDBObject().append("followers", userHander));
        coll.update(searchQueryFollowing, modifiedObjectFollowing);

    }

    @Override
    public void unFollow(String userHander, String toUnFollowHandler) throws DbException {
        verifyUser(userHander);
        verifyUser(toUnFollowHandler);
        DBCollection coll = clientDB.getCollection("users");
        DBObject searchQueryUser = new BasicDBObject().append("handle", userHander);
        DBObject searchQueryFollowing = new BasicDBObject().append("handle", toUnFollowHandler);
        DBObject modifiedObjectUser = new BasicDBObject();
        DBObject modifiedObjectFollowing = new BasicDBObject();

        modifiedObjectUser.put("$pull", new BasicDBObject().append("followings", toUnFollowHandler));
        coll.update(searchQueryUser, modifiedObjectUser);

        modifiedObjectFollowing.put("$pull", new BasicDBObject().append("followers", userHander));
        coll.update(searchQueryFollowing, modifiedObjectFollowing);
    }

    @Override
    public void addTweet(String handler, Tweet tweet) throws DbException {
        DBCollection coll = clientDB.getCollection("tweets");

        /*
         Schéma du doc de tweets
         {
         "handle":"Le handler",
         "tweets":[
         { "tweet":
         { "timestamp":"le timestamp",
         "content":"le contenu du tweet"}},
         { "tweet":
         { "timestamp":"le timestamp num 2",
         "content":"le contenu du tweet num 2"}}
         ]
         },
         {
         "handle":"Le handler 2",...
         }
         */
        verifyUser(handler);
        DBObject searchQuery = new BasicDBObject().append("handle", handler);
        //On vérifie si l'utilisateur tweet pour la première fois ou pas
        if (coll.findOne(searchQuery) == null) {
            coll.insert(searchQuery);
        }

        //Le champ qui sera ajouter au tableau de tweets
        DBObject newTweetDoc = new BasicDBObject();
        //Le nouveau tweet en soi, qui est un document
        DBObject newTweet = new BasicDBObject();
        //Le tableau de tweets
        DBObject newObject = new BasicDBObject();

        newTweet.put("timestamp", tweet.getTimeStamp());
        newTweet.put("content", tweet.getContent());

        newTweetDoc.put("tweet", newTweet);

        newObject.put("$push", new BasicDBObject().append("tweets", newTweetDoc));

        coll.update(searchQuery, newObject);

    }

    private void verifyUser(String handler) throws DbException {
        if (getUser(handler) == null) {
            throw new DbException("User unkowen : " + handler);
        }
    }

    private User getUser(String handler) {
        DBCollection coll = clientDB.getCollection("users");
        DBObject searchQuery = new BasicDBObject().append("handle", handler);
        User user = new User();
        try (DBCursor cursor = coll.find(searchQuery)) {
            while (cursor.hasNext()) {
                DBObject obj = cursor.next();

                user.setUserId(obj.get("_id").toString());
                user.setHandle(obj.get("handle").toString());
                user.setPassword(obj.get("password").toString());
                user.setServer(obj.get("server").toString());
                return user;
            }
        }
        return null;
    }
}
