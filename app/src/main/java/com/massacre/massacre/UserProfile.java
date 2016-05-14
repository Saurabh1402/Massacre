package com.massacre.massacre;

import java.util.Date;

/**
 * Created by saurabh on 12/4/16.
 */
public class UserProfile {
    private String device_id;
    private String contact;
    private String objectId;
    private Date created;
    private Date updated;
    private String user_status;
    private Date last_seen;
    private Date profile_picture_last_updated;
    private Boolean profile_picture_available;
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_status() {
        return user_status;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }

    public Date getLast_seen() {
        return last_seen;
    }

    public void setLast_seen(Date last_seen) {
        this.last_seen = last_seen;
    }


    public UserProfile(){

    }

    public UserProfile(String device_id, String contact){
        this.contact=contact;
        this.device_id = device_id;
    }

    public String getDevice_id() {
        return device_id;
    }
    public Date getProfile_picture_last_updated() {
        return profile_picture_last_updated;
    }
    public void setProfile_picture_last_updated(Date profile_picture_last_updated) {
        this.profile_picture_last_updated = profile_picture_last_updated;
    }
    public Boolean getProfile_picture_available() {
        return profile_picture_available;
    }
    public void setProfile_picture_available(Boolean profile_picture_available) {
        this.profile_picture_available = profile_picture_available;
    }
    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }
    public String getContact() {
        return contact;
    }
    public void setContact(String contact) {
        this.contact = contact;
    }
    public String getObjectId()
    {
        return objectId;
    }
    public void setObjectId( String objectId )
    {
        this.objectId = objectId;
    }
    public Date getCreated()
    {
        return created;
    }
    public void setCreated( Date created )
    {
        this.created = created;
    }
    public Date getUpdated()
    {
        return updated;
    }
    public void setUpdated( Date updated )
    {
        this.updated = updated;
    }

}
