package com.massacre.massacre;

import android.graphics.Bitmap;

import com.backendless.BackendlessUser;

/**
 * Created by saurabh on 27/4/16.
 */
public class MassacreBackendlessUser extends BackendlessUser {
    private final String CONTACT="contact";

    public String getContact() {
    return this.getProperty(CONTACT).toString();
    }

    public void setContact(String contact) {
        this.setProperty(CONTACT,contact);
    }



}
