package com.example.mymanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class Notes
{
    int id;
    String label_note;
    String note;

    Context context;

    String records = "nRecord";
    String record_key_notes = "nKey_notes";

    String register_notes[] = new String[10];
    String note_key = "Wave";
    String note_identity_label = "Wave_name";
    String occupy = "engage";

    int def=-1;
    int record_stored;

    public Notes(int i,String l)
    {
        this.id = i;
        this.label_note = l;
    }

    public Notes(String l,String n,Context cont)
    {
        this.label_note = l;
        this.note = n;
        this.context = cont;
    }

    public int getId() {
        return id;
    }

    public String getLabel_note() {
        return label_note;
    }

    public String getNote() {
        return note;
    }

    public int storeInfo(int i) throws GeneralSecurityException, IOException {
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

        String masterKeys;
        String masterKeys2;


        try {

            int m;
            masterKeys = MasterKeys.getOrCreate( MasterKeys.AES256_GCM_SPEC );
            for(m=0;m<10;m++)
            {

                SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                        register_notes[m],
                        masterKeys,
                        context,
                        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV ,
                        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
                if(sharedPreferences.getString( occupy,"-1" ).equals( "-1" ) || sharedPreferences.getString( occupy,"-1" ).equals( "false" ))
                {
                    break;
                }
            }

            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                    register_notes[m],
                    masterKeys,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV ,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);

            sharedPreferences.edit()
                    .putString( note_identity_label,label_note )
                    .putString( note_key,note )
                    .putString( occupy,"true" )
                    .apply();

            masterKeys2 = MasterKeys.getOrCreate( MasterKeys.AES256_GCM_SPEC );

            SharedPreferences sharedPreferences1 = EncryptedSharedPreferences.create(
                    records,
                    masterKeys2,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);

            //int records_stored = sharedPreferences1.getInt( record_key_notes,def );
            sharedPreferences1.edit()
                    .putInt(record_key_notes,i+1)
                    .apply();


        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            Log.e( "Exception","GeneralSecurityException or "+e.getMessage()+" occurred!" );
            return -1;
        }

        return record_stored+1;
    }

}
