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

    String pref = "Bundle";     //Personal Details
    String name_key="Name";
    String recovery_key="Recovery";

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

        String masterkey = null;

        try {
            masterkey = MasterKeys.getOrCreate( MasterKeys.AES256_GCM_SPEC );

            SharedPreferences sp_details = EncryptedSharedPreferences.create(
                    pref,
                    masterkey,
                    v.getContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM );

            prof_name.setText( sp_details.getString( name_key,"-1" ) );
            rec_disp.setText( "Recovery: "+sp_details.getString( recovery_key,"-1" ) );

        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return v;


    }
}
