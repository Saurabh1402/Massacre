package com.massacre.massacre;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by saurabh on 26/4/16.
 */
public class Wrapper {
    ArrayList<UserProfile> userProfiles =null;
    ArrayList<HashMap<String,String>> yourAllStatus=null;
    public Wrapper(){

    }
    public Wrapper(ArrayList<UserProfile> value){
        this.userProfiles =value;
    }
    public void setYourAllStatus(ArrayList<HashMap<String,String>> value){
        this.yourAllStatus=value;
    }

}
