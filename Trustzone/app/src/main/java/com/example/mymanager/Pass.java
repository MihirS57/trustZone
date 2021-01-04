package com.example.mymanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static java.lang.reflect.Array.getInt;

public class Pass
{
    String pass_label,emailA,passwrd,ts;
    Context context;

    String records = "nRecord";
    String record_key = "nKey";
    String record_index = "iKey";


    String register[] = new String[10];
    String pass_key = "Signal";
    String identity = "Signal_classifier";
    String identity_label = "Label";
    String e_tst_label = "eStamp";
    String v_tst_label = "vStamp";
    String occupy = "engage";

    String defa = "-1";
    int def=-1;
    int record_stored;
    int id,m;



    public Pass( int ind, String pass_label, String emailA, String tst) {
        this.id = ind;
        this.pass_label = pass_label;
        this.emailA = emailA;
        this.ts = tst;
    }

    public Pass(String pass_label, String emailA, String passwrd,String tst, Context context) {
        this.pass_label = pass_label;
        this.emailA = emailA;
        this.passwrd = passwrd;
        this.ts = tst;
        this.context = context;
    }

    public int storeInfo(int i)
    {
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

        int m;
        int arr[] = new int[10];
        String masterKeys;
        String masterKeys2;
        String t;
        try {

            masterKeys = MasterKeys.getOrCreate( MasterKeys.AES256_GCM_SPEC );
            SharedPreferences sharedPreferencesI = EncryptedSharedPreferences.create(
                    records,
                    masterKeys,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
            t = sharedPreferencesI.getString( record_index,"-1" );
            for(m=0;m<10;m++){
                if(t.charAt( m ) == '0'){
                    break;
                }
            }
            StringToArray sta = new StringToArray();
            t = sta.convertAndAdd( t,m,1 );
            sharedPreferencesI.edit().putString( record_index,t ).apply();
            /*for(m=0;m<10;m++)
            {
                SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                        register[m],
                        masterKeys,
                        context,
                        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV ,
                        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
                if(sharedPreferences.getString( occupy,"-1" ).equals( "-1" ) || sharedPreferences.getString( occupy,"-1" ).equals( "false" ))
                {
                    break;
                }
            }*/


            /*
            initialize records shared preferences
            int arr[];
            for(i=0;i<records;i++){
                if(arr[i] == 0){
                    break;
                 }
            }
            arr[i] = i+1;
             */



            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                    register[m],
                    masterKeys,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV ,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);

            sharedPreferences.edit()
                    .putString( identity_label,pass_label )
                    .putString( identity,emailA )
                    .putString( pass_key,passwrd )
                    .putString( e_tst_label,ts )
                    .putString( v_tst_label,ts )
                    .putString( occupy,"true" )
                    .apply();

            masterKeys2 = MasterKeys.getOrCreate( MasterKeys.AES256_GCM_SPEC );

            SharedPreferences sharedPreferences1 = EncryptedSharedPreferences.create(
                    records,
                    masterKeys2,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);

            int records_stored = sharedPreferences1.getInt( record_key,def );
            sharedPreferences1.edit()
                    .putInt(record_key,records_stored+1)
                    .apply();

            return records_stored+1;

        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            Log.e( "Exception","GeneralSecurityException or "+e.getMessage()+" occurred!" );
            return -1;
        }


        /*SharedPreferences sharedPreferences1 = context.getSharedPreferences(records,Context.MODE_PRIVATE);
        record_stored = sharedPreferences1.getInt(record_key,def);
        sharedPreferences1.edit().putInt(record_key,record_stored+1).apply();

        SharedPreferences sharedPreferences = context.getSharedPreferences(register[i],Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(identity_label,pass_label).apply();
        sharedPreferences.edit().putString(identity,emailA).apply();
        sharedPreferences.edit().putString(pass_key,passwrd).apply();

        return record_stored+1;*/
    }

    public int getId() {
        return id;
    }
    public String getTs(){ return ts; }

    public String getEmailA()
    {
        return emailA;
    }
    public String getPass_label()
    {
        return pass_label;
    }
}
