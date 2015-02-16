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
import fr.ecp.tweet.model.User;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author grk
 */
public class DbRepository {
    
    private static DbRepository instance = null;
    
    public static DbRepository getInstance() {
        if (null == instance) {
            instance = new DbRepository();
        }
        return instance;
    }
    
    public List<User> getUsers() throws UnknownHostException{
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        
        List<User> users = new ArrayList<>();
        

        DB db = mongoClient.getDB("test");

       
        db.createCollection("users", new BasicDBObject("capped", true).append("size", 1048576));
        
        DBCollection coll = db.getCollection("users");
        BasicDBObject doc = new BasicDBObject("name", "MongoDB").append("handle", "user").append("password", "azerty") .append("server", "server 1");
        coll.insert(doc);
        // make a document and insert it
//        BasicDBObject doc = new BasicDBObject("handle", "bonclay7")
//                .append("password", "dshkqdiue")
//                .append("server", "localhos");
//
//        coll.insert(doc);
        try ( // lets get all the documents in the collection and print them out
                DBCursor cursor = coll.find()) {
            if(cursor != null){
                while (cursor.hasNext()) {
                    DBObject obj = cursor.next();
                    User user = new User();
                    user.setHandle(obj.get("handle").toString());
                    user.setPassword(obj.get("password").toString());
                    user.setServer(obj.get("server").toString());
                    users.add(user);
                }
                
            }
        }
        
        return users;
    }
    
    
}
