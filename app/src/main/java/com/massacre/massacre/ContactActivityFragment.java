package com.massacre.massacre;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.support.v7.widget.LinearLayoutManager;
import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * A placeholder fragment containing a simple view.
 */
public class ContactActivityFragment extends Fragment {
    LoadFriendsProfileAdapter adapter;

    public ContactActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       //Intent intent=new Intent(getContext(),ShowNotification.class);
        //getActivity().startService(intent);


        return inflater.inflate(R.layout.fragment_contact, container, false);

    }

    @Override
    public void onStart() {
        super.onStart();

    }

}
