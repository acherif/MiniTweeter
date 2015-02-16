/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ecp.tweet.model;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.util.Date;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author grk
 */
@ApiModel(value = "A user's tweet")
@XmlRootElement(name = "Tweet")
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

    @XmlElement(name = "content")
    @ApiModelProperty(value = "Tweet's content", required=true, allowableValues = "placed,approved,delivered")
    public void setContent(String content) {
        this.content = content;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    @XmlElement(name = "timestamp")
    @ApiModelProperty(value = "Tweet's timestamp", required=true, allowableValues = "placed,approved,delivered")
    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
    
    public String getUserHandle() {
        return userHandle;
    }

    @XmlElement(name = "handle")
    @ApiModelProperty(value = "The handle of the user who tweeted the tweet", required=true, allowableValues = "placed,approved,delivered")
    public void setUserHandle(String userHandle) {
        this.userHandle = userHandle;
    }
    
    
    
}
