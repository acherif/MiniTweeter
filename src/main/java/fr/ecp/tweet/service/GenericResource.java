/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package fr.ecp.tweet.service;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import fr.ecp.tweet.db.DbRepoMongo;
import fr.ecp.tweet.db.IDbRepo;
import fr.ecp.tweet.exception.DbException;
import fr.ecp.tweet.exception.ServicesException;
import fr.ecp.tweet.model.Tweet;
import fr.ecp.tweet.model.User;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * REST Web Service
 *
 * @author Hamid
 */
@Path("generic")
public class GenericResource {
    
    @Context
    private UriInfo context;
    
    private final IDbRepo db;
    
    /**
     * Creates a new instance of GenericResource
     */
    public GenericResource() {
        db = DbRepoMongo.getInstance();
        db.connect();
    }
    
    /**
     * Retrieves representation of an instance of fr.ecp.tweet.service.GenericResource1
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/xml")
    public String getXml() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }
    
//    @POST
//    @Path("/users")
//    public String addUser(@FormParam("handle") String handle, @FormParam("passeword") String pwd, @FormParam("server") String server){
//        return handle + " " + pwd + " " + server;
////db.addUser(new User(handle, pwd, server));
//    }
    
    @POST
    @Path("/users")
    @Consumes("application/json")
    @ApiOperation(value = "Add a user", notes = "Add a user by specifying his handle, server and password")
    public void addUser(User user){        
        try {
            db.addUser(user);
        } catch (DbException ex) {
            throw new ServicesException(ex.getMessage());
        }
    }
    
    @GET
    @Path("/{handle}/tweets")
    @Produces("application/json")
    @ApiOperation(value = "Gets the list of tweets of a user", notes = "Gets", response = Tweet.class)
    @ApiResponses(value = {
    @ApiResponse(code = 400, message = "Invalid handle supplied"),
    @ApiResponse(code = 404, message = "Tweets not found") 
    })
    public List<Tweet> getTweets(@PathParam("handle") String handle){
        List<Tweet> tweets = null;
        try{
            tweets = db.getTweets(handle);
        } catch (DbException ex){
            throw new ServicesException(ex.getMessage());
        }
        return tweets;
   }

    @POST
    @Path("/{handle}/tweets")
    @Consumes("text/plain")
    public void addTweet(@PathParam("handle") String handle, String content){
           
        Tweet tweet = new Tweet(content, handle);

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        tweet.setTimeStamp(date);
        
        try {
            db.addTweet(handle, tweet);
        } catch (DbException ex) {
            throw new ServicesException(ex.getMessage());
        }
    }

    @GET
    @Path("{handle}/followers")
    public List<User> getFollowers(@PathParam("handle") String handle){
        List<User> followers;
        try {
            followers = db.getFollowers(handle);
        } catch (DbException ex) {
            throw new ServicesException(ex.getMessage());
        }
        if(followers == null){
             throw new ServicesException("No followers for : " + handle);
        }
        
        return followers;
    }

    @GET
    @Path("{handle}/followings")
    public List<User> getFollowing(@PathParam("handle") String handle) {
        List<User> followings;
        try {
            followings = db.getFollowing(handle);
        } catch (DbException ex) {
            throw new ServicesException(ex.getMessage());
        }
        if(followings == null){
            throw new ServicesException("No followings for : " + handle);
        }
        
        return followings;
    }

    @POST
    @Path("{handle}/followings")
    public void follow(@PathParam("handle") String handle, String toFollowHandler){
        
        try {
            db.follow(handle, toFollowHandler);
        } catch (DbException ex) {
            throw new ServicesException(ex.getMessage());
        }
    }

    @DELETE
    @Path("{handle}/followings")
    @Consumes("application/json")
    public void stopFollowing(@PathParam("handle") String handle, String toUnFollowHandler){
        try {
            db.unFollow(handle, toUnFollowHandler);
        } catch (DbException ex) {
            throw new ServicesException(ex.getMessage());
        }
    }
//
//    @POST
//    @Path()
//    @Produces("application/json")
//    public void signIn(){
//
//    }
    
//    @Get
//    @Path()
//    @Produces("application/json")
//    public void localFollowingTweets(){
//
//    }
    
    /**
     * Retrieves representation of an instance of fr.ecp.sio.filrouge.rs.Services
     * @return an instance of java.lang.String
     */
    @GET
    @Path("/users")
    @Produces("application/json")
    public List<User> getUsers() {              
        return db.getUsers();
    }
    
   
//    @POST
//    @Path()
//    @Produces("application/json")
//    public void addFollower(){
//
//    }
//
//    @DELETE
//    @Path()
//    @Produces("application/json")
//    public void removeFollower(){
//
//    }
    
    
    
    
    
    
    /**
     * PUT method for updating or creating an instance of GenericResource1
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/xml")
    public void putXml(String content) {
    }
}
