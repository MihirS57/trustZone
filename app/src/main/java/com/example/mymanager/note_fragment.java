package com.example.mymanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

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

public class note_fragment extends Fragment {

    RecyclerView recyclerView;
    Notes_adapter adapter;
    List<Notes> notesList;
    boolean resp;
    String register_notes[] = new String[10];
    Context context;
    ProgressBar loadBar;
    public note_fragment(ProgressBar lb){
        this.loadBar = lb;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notefragment,null);
        //loadBar = view.findViewById( R.id.loadBar );
        notesList = new ArrayList<>();

        String records = "nRecord";
        String record_key_notes = "nKey_notes";
        int record_stored,def=-1;

        recyclerView = view.findViewById(R.id.recycler_3 );
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        String masterKeys = null;
        try {
            masterKeys = MasterKeys.getOrCreate( MasterKeys.AES256_GCM_SPEC );

            SharedPreferences sharedPreferences1 = EncryptedSharedPreferences.create(
                    records,
                    masterKeys,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);

            record_stored = sharedPreferences1.getInt( record_key_notes,def );

            if(record_stored == 0)
            {
                Log.e("No Notes","No Notes");
                Toast.makeText(getActivity(),"No Notes stored",Toast.LENGTH_SHORT).show();

            }
            else
            {
                Log.e("Yes Notes","Notes");
                int rc=record_stored;
                for(int i = 0; rc>0 && i<10 ;i++)
                {
                    if(getNotes_label(i,0)!=null) {
                        if(getNotes_label( i,1 ).equals( "true" ))
                        {
                            notesList.add(new Notes(i+1,getNotes_label(i,0),getNotes_label( i,2 )));
                            rc--;
                        }
                    }


                }
                if(!notesList.isEmpty()) {
                    adapter = new Notes_adapter(getContext(),notesList);
                    recyclerView.setAdapter(adapter);
                }
                loadBar.setVisibility( View.INVISIBLE );
            }
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            Log.e( "Shit","Exception here" );
        }
        return view;
    }



    private String getNotes_label(int i,int m) {
        register_notes[0] = "Book0";
        register_notes[1] = "Book1";
        register_notes[2] = "Book2";
        register_notes[3] = "Book3";
        register_notes[4] = "Book4";
        register_notes[5] = "Book5";
        register_notes[6] = "Book6";
        register_notes[7] = "Book7";
        register_notes[8] = "Book8";
        register_notes[9] = "Book9";

        String note_identity_label = "Wave_name";
        String occupy = "engage";
        String note_label = null;
        String v_tst_label = "vStamp";

        String label = null;
        try {
            String masterKeys = MasterKeys.getOrCreate( MasterKeys.AES256_GCM_SPEC );
            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                    register_notes[i],
                    masterKeys,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);

            if(m==0){
                note_label = sharedPreferences.getString( note_identity_label,"-1" );
            }
            else if(m==1){
                note_label = sharedPreferences.getString( occupy,"-1" );
            }else if(m == 2){
                note_label = sharedPreferences.getString( v_tst_label,"-1" );
                note_label = "You last visited at "+note_label;
            }


        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
        return note_label;

    }
    public void onAttach(Context cont) {
        super.onAttach( cont );
        this.context = cont;
    }

}

