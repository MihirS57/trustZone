package com.example.mymanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class RecordStored extends AppCompatActivity {

    String records = "nRecord";     //Storage Details
    String record_key = "nKey";
    String record_key_notes = "nKey_notes";
    String record_index = "iKey";
    String record_index_notes = "iKey_notes";

    TextView total;
    TextView total_n;
    TextView total_p;

    int t=-2,t_n=-2,t_p=-2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_record_stored );

        total = findViewById( R.id.total_record );
        total_n = findViewById( R.id.total_note );
        total_p = findViewById( R.id.total_pass );
        String t1="",t2="";
        String masterkey = null;

        try {
            masterkey = MasterKeys.getOrCreate( MasterKeys.AES256_GCM_SPEC );

            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                    records,
                    masterkey,
                    getApplicationContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM );

            t_n =sharedPreferences.getInt( record_key_notes,-1 );
            t_p=sharedPreferences.getInt( record_key,-1 );
            t1 = sharedPreferences.getString( record_index_notes,"-1" );
            t2 = sharedPreferences.getString( record_index,"-1" );
            t=t_n+t_p;

            total.setText(String.valueOf(t));
            //total_n.setText( String.valueOf(t_n) );
            //total_p.setText( String.valueOf( t_p ) );
            total_n.setText( t1 );
            total_p.setText( t2 );
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}