package com.example.mymanager;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class BlankFragment extends Fragment {

    int mode=0;
    Context context;
    TextView mess;
    public BlankFragment(int m,Context c)
    {
        this.mode = m;
        this.context = c;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.empty_pass_fragment,null);
        mess = view.findViewById( R.id.status_message);
        if(mode == 1)
        {
            mess.setText( "No Notes Stored"+"\n"+"\n"+"Press the three-dots on the upper right corner to add a new Note" );
        }
        else if(mode == 2)
        {
            mess.setText( "No Passwords Stored"+"\n"+"\n"+"Press the three-dots on the upper right corner to add a new Password" );
        }
        return view;
    }
}