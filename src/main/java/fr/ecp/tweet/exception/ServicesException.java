/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ecp.tweet.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Hamid
 */
public class ServicesException extends WebApplicationException {
     public ServicesException(Response.Status responseType, String message) {
         super(Response.status(responseType)
             .entity(message).type(MediaType.TEXT_PLAIN).build());
     }
}
