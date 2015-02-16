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
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author Hamid
 */
@Path("api")
@Api(value = "api", description = "Federated mini Tweeter RESTFull API")
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
    @ApiResponse(code = 409, message = "User already exists")
    public void addUser(User user){        
        try {
            db.addUser(user);
        } catch (DbException ex) {
            throw new ServicesException(Response.Status.CONFLICT, ex.getMessage());
        }
    }
    
    @GET
    @Path("/{handle}/tweets")
    @Produces("application/json")
    @ApiOperation(value = "Get tweets of a user", notes = "Gets", response = Tweet.class)
    @ApiResponses(value = {
    @ApiResponse(code = 404, message = "Invalid handle supplied"),
    @ApiResponse(code = 204, message = "No tweets")        
    })
    public List<Tweet> getTweets(@PathParam("handle") String handle){
        List<Tweet> tweets = null;
        try{
            tweets = db.getTweets(handle);
        } catch (DbException ex){
            if(ex.getMessage().contains("tweets")){
                throw new ServicesException(Response.Status.NO_CONTENT, ex.getMessage());
            } else {
                throw new ServicesException(Response.Status.NOT_FOUND, ex.getMessage());
            }
        }
        return tweets;
   }

    @POST
    @Path("/{handle}/tweets")
    @Consumes("text/plain")
    @ApiOperation(value = "Add a tweet for a user", notes = "Add a tweet for the user whos handle is specified")
    @ApiResponse(code = 404, message = "Invalid handle supplied")
    public void addTweet(@PathParam("handle") String handle, String content){
           
        Tweet tweet = new Tweet(content, handle);

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        tweet.setTimeStamp(date);
        
        try {
            db.addTweet(handle, tweet);
        } catch (DbException ex) {
            throw new ServicesException(Response.Status.NOT_FOUND, ex.getMessage());
        }
    }

    @GET
    @Path("/{handle}/followers")
    @ApiOperation(value = "Get followers of a user", notes = "Get the list of followers of the user whos handle is specified")
    @ApiResponses(value = {
    @ApiResponse(code = 404, message = "Invalid handle supplied"),
    @ApiResponse(code = 204, message = "No Followers")
    })
    public List<User> getFollowers(@PathParam("handle") String handle){
        List<User> followers;
        try {
            followers = db.getFollowers(handle);
        } catch (DbException ex) {
            throw new ServicesException(Response.Status.NOT_FOUND, ex.getMessage());
        }
        if(followers == null){
             throw new ServicesException(Response.Status.NO_CONTENT, "No followers for : " + handle);
        }
        
        return followers;
    }

    @GET
    @Path("/{handle}/followings")
    @ApiOperation(value = "Get followings of a user", notes = "Get the list of followings of the user whos handle is specified")
    @ApiResponses(value = {
    @ApiResponse(code = 404, message = "Invalid handle supplied"),
    @ApiResponse(code = 204, message = "No Followings")
    })
    public List<User> getFollowing(@PathParam("handle") String handle) {
        List<User> followings;
        try {
            followings = db.getFollowing(handle);
        } catch (DbException ex) {
            throw new ServicesException(Response.Status.NOT_FOUND, ex.getMessage());
        }
        if(followings == null){
            throw new ServicesException(Response.Status.NO_CONTENT, "No followings for : " + handle);
        }
        
        return followings;
    }

    @POST
    @Path("/{handle}/followings")
    @ApiOperation(value = "Follow a user", notes = "Follow the user whos handle is specified")
    @ApiResponse(code = 404, message = "Invalid handle supplied")
    public void follow(@PathParam("handle") String handle, String toFollowHandler){
        
        try {
            db.follow(handle, toFollowHandler);
        } catch (DbException ex) {
            throw new ServicesException(Response.Status.NOT_FOUND, ex.getMessage());
        }
    }

    @DELETE
    @Path("/{handle}/followings")
    @Consumes("application/json")
    @ApiOperation(value = "Unfollow a user", notes = "Unfollow the user whos handle is specified")
    @ApiResponse(code = 404, message = "Invalid handle supplied")
    public void stopFollowing(@PathParam("handle") String handle, String toUnFollowHandler){
        try {
            db.unFollow(handle, toUnFollowHandler);
        } catch (DbException ex) {
            throw new ServicesException(Response.Status.NOT_FOUND, ex.getMessage());
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
    @ApiOperation(value = "Get the users of the API", notes = "Get the list of all the users of the API")
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
