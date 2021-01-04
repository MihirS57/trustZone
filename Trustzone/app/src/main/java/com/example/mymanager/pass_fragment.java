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
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class pass_fragment extends Fragment {

    RecyclerView recyclerView;
    List<Pass> passList;
    Pass_adapter adapter;
    String register[] = new String[10];
    String[] title = new String[10];
    String[] title_un = new String[10];
    String[] date = new String[10];
    String defa = "-1",t="";
    String pass_key = "Signal";
    String identity = "Signal_classifier";
    String identity_label = "Label";
    String e_tst_label = "eStamp";
    String v_tst_label = "vStamp";
    String occupy = "engage";
    Context context;
    ProgressBar loadBar;
    View view;
    int rc;

    private SharedPreferences esharedPreferences = null;
    private String emasterKeys=null;

    public pass_fragment(ProgressBar lb){
        this.loadBar = lb;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.passfragment,null);
        //loadBar = view.findViewById( R.id.loadBar );
        String records = "nRecord";
        String record_key = "nKey";
        String record_index = "iKey";
        int record_stored,def=-1;

        passList = new ArrayList<>();

        recyclerView = view.findViewById(R.id.recycler_2);
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

           record_stored = sharedPreferences1.getInt( record_key,def );
            t = sharedPreferences1.getString( record_index,"-1" );
            if(record_stored == 0)
            {
                Log.e("No Pass","No Passwords");
                Toast.makeText(getActivity(),"No passwords stored",Toast.LENGTH_LONG).show();

            }
            else
            {
                Log.e("Yes Pass","Passwords");
                rc = record_stored;
                listUpdate1.start();
                //listUpdate.start();
                /*
                int arr[] = from record's shared preferences
                for(i=0;rc>0;i++){
                    if(arr[i] != 0){
                        same;
                    }
                }
                 */


            }

        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            Log.e( "Shit","Exception here" );
        }

        return view;
    }

    Thread listUpdate = new Thread(  ){
        @Override
        public void run() {
            super.run();

            try {
                Objects.requireNonNull( getActivity() ).runOnUiThread( new Runnable() {
                    @Override
                    public void run() {
                        for(int i = 0; rc>0 && i<10 ;i++)
                        {
                            if(t.charAt( i ) != '0'){
                                passList.add(new Pass(i+1,getLabel( i,0), getLabel( i,1),getLabel( i,3 )));
                                Log.e("MyPassLoadLoop","Password "+i);
                                rc--;
                            }
                        }
                        if(!passList.isEmpty()) {
                            adapter = new Pass_adapter(getContext(),passList);
                            recyclerView.setAdapter(adapter);
                        }
                        loadBar.setVisibility( View.INVISIBLE );
                    }
                } );
            }catch(NullPointerException e){
                Toast.makeText( context, "Problem in loading data..", Toast.LENGTH_SHORT ).show();
            }
        }
    };

    Thread listUpdate1 = new Thread(  ){
        @Override
        public void run() {
            super.run();

            try {
                Objects.requireNonNull( getActivity() ).runOnUiThread( new Runnable() {
                    @Override
                    public void run() {
                        int k=0;
                        for(int i = 0; rc>0 && i<10 ;i++)
                        {
                            if(t.charAt( i ) != '0'){
                                //passList.add(new Pass(i+1,getLabel( i,0), getLabel( i,1),getLabel( i,3 )));
                                initializeSP( i );
                                if(esharedPreferences!=null){
                                    title[k] = getL( i,0 );
                                    title_un[k] = getL( i,1 );
                                    date[k] = getL( i,3 );
                                }else{
                                    title[k] = "Nai Hua Bro";
                                    title_un[k] = "Nai Hua Bro";
                                    date[k] = "Nai Hua Bro";
                                }
                                k++;
                                Log.e("MyPassLoadLoop","Password "+i);
                                rc--;
                            }
                        }
                            adapter = new Pass_adapter(getContext(),title,title_un,date,k);
                            recyclerView.setAdapter(adapter);

                        loadBar.setVisibility( View.INVISIBLE );
                    }
                } );
            }catch(NullPointerException e){
                Toast.makeText( context, "Problem in loading data..", Toast.LENGTH_SHORT ).show();
            }
        }
    };

    private void initializeSP(int i) {
        register[0] = "Log0";
        register[1] = "Log1";
        register[2] = "Log2";
        register[3] = "Log3";
        register[4] = "Log4";
        register[5] = "Log5";
        register[6] = "Log6";
        register[7] = "Log7";
        register[8] = "Log8";
        register[9] = "Log9";

        try {
            emasterKeys = MasterKeys.getOrCreate( MasterKeys.AES256_GCM_SPEC );
            esharedPreferences = EncryptedSharedPreferences.create(
                    register[i],
                    emasterKeys,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM );


        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getL(int i,int m){
        String label="Nai Hua Bro";
        if(m == 0)
        {
            label = esharedPreferences.getString( identity_label,defa );
        }
        else if(m == 1)
        {
            label = esharedPreferences.getString( identity,defa );
        }
        else if(m == 2)
        {
            label = esharedPreferences.getString(occupy,defa  );
        } else if(m == 3){
            label = esharedPreferences.getString( v_tst_label,defa );
            label = "You last visited at "+label;
        }
        return label;
    }
        private String getLabel(int i, int m)
    {

        /*
        Useless Function, either give the ability to initialize the shared preferences for an index just once or shift this entire block inside the
        for loop inside the if condition

         */
        register[0] = "Log0";
        register[1] = "Log1";
        register[2] = "Log2";
        register[3] = "Log3";
        register[4] = "Log4";
        register[5] = "Log5";
        register[6] = "Log6";
        register[7] = "Log7";
        register[8] = "Log8";
        register[9] = "Log9";

        String label = null;
        try {
            String masterKeys = MasterKeys.getOrCreate( MasterKeys.AES256_GCM_SPEC );
            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                    register[i],
                    masterKeys,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);

            if(m == 0)
            {
                label = sharedPreferences.getString( identity_label,defa );
            }
            else if(m == 1)
            {
                label = sharedPreferences.getString( identity,defa );
            }
            else if(m == 2)
            {
                label = sharedPreferences.getString(occupy,defa  );
            } else if(m == 3){
                label = sharedPreferences.getString( v_tst_label,defa );
                label = "You last visited at "+label;
            }
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
        return label;
    }

    public void onAttach(Context cont) {
        super.onAttach( cont );
        this.context = cont;
    }
}
