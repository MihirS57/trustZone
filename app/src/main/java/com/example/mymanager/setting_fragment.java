package com.example.mymanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class setting_fragment extends Fragment {
    List<Setting> settingList;
    RecyclerView recyclerView;
    Settings_adapter adapter;
    TextView prof_name;
    TextView rec_disp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.settingfragment,null );

        prof_name = v.findViewById( R.id.profile_name );
        rec_disp = v.findViewById( R.id.recovery_display );

        settingList = new ArrayList<>(  );
        recyclerView = v.findViewById( R.id.recycler_3 );
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        settingList.add( new Setting( "Records Stored","Review your data" ) );
        settingList.add( new Setting( "User Registry","Access the system logs" ) );
        settingList.add( new Setting( "Change Recovery Number","Update your recovery number" ));
        settingList.add( new Setting( "Change Password","Set a new password" ) );
        settingList.add( new Setting( "Emergency Security Wipe","Conduct App Reset" ) );
        adapter = new Settings_adapter( getContext(),settingList );
        recyclerView.setAdapter( adapter );

        Fetch userRec = new Fetch( getContext(),"userinfo",0 );
        String userN = userRec.getUserInfo( "name" );
        String userR = userRec.getUserInfo( "recovery" );
        prof_name.setText( userN );
        rec_disp.setText( userR );

        return v;


    }
}
