/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ecp.tweet.exception;

/**
 *
 * @author Hamid
 */
public class DbException extends Exception {
    
    public DbException(String message){
        super(message);        
    }
}
