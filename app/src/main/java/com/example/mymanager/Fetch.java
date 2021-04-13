package com.example.mymanager;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class Fetch {
    String records = "nRecord";
    String record_key = "nKey";
    String record_key_notes = "nKey_notes";
    String record_index = "iKey";
    String record_index_notes = "iKey_notes";

    String occupy = "engage";
    String e_tst_label = "eStamp";
    String v_tst_label = "vStamp";

    String register[] = new String[10];
    String pass_key = "Signal";
    String identity = "Signal_classifier";
    String identity_label = "Label";

    String register_notes[] = new String[10];
    String note_key = "Wave";
    String note_identity_label = "Wave_name";

    String pref = "Bundle";     //Personal Details
    String name_key="Name";
    String recovery_key="Recovery";
    String recovery_qtn = "SecQuestion";
    String recovery_ans = "ansSecQuestion";
    String passw_key="Code";

    Context context;
    SharedPreferences sharedPreferences;

    public Fetch(Context context){
        this.context = context;
    }

    public Fetch(Context context,String mode,int index)  //1:UserInfo 2: Records 3:Notes 4:Password
    {
        this.context = context;
        String masterkey;
        switch (mode){
            case "userinfo":
                try {
                    masterkey = MasterKeys.getOrCreate( MasterKeys.AES256_GCM_SPEC );

                    sharedPreferences = EncryptedSharedPreferences.create(
                            pref,
                            masterkey,
                            context,
                            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM );
                }catch (GeneralSecurityException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "records":
                try {
                    masterkey = MasterKeys.getOrCreate( MasterKeys.AES256_GCM_SPEC );

                    sharedPreferences = EncryptedSharedPreferences.create(
                            records,
                            masterkey,
                            context,
                            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM );
                }catch (GeneralSecurityException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "note":
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
                try {
                    masterkey = MasterKeys.getOrCreate( MasterKeys.AES256_GCM_SPEC );

                    sharedPreferences = EncryptedSharedPreferences.create(
                            register_notes[index],
                            masterkey,
                            context,
                            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM );
                }catch (GeneralSecurityException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "password":
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
                    masterkey = MasterKeys.getOrCreate( MasterKeys.AES256_GCM_SPEC );

                    sharedPreferences = EncryptedSharedPreferences.create(
                            register[index],
                            masterkey,
                            context,
                            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM );
                }catch (GeneralSecurityException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public String getUserInfo(String mode)   //1:name 2:recovery 3:recoveryQtn 4: recoveryAns 5:password
    {
        String bus="";

        switch (mode){
            case "name":
                bus = sharedPreferences.getString( name_key,"-1" );
                break;
            case "recovery":
                bus = sharedPreferences.getString( recovery_key,"-1" );
                break;
            case "recoveryQtn":
                bus = sharedPreferences.getString( recovery_qtn,"-1" );
                break;
            case "recoveryAns":
                bus = sharedPreferences.getString( recovery_ans,"-1" );
                break;
            case "password":
                bus = sharedPreferences.getString( passw_key,"-1" );
                break;
        }

        return bus;
    }

    public int getNotePassRecords(String mode)     //1:note 2:password
    {
        int rec=-1;
        switch (mode){
            case "password":
                rec = sharedPreferences.getInt( record_key,-1 );
                break;
            case "note":
                rec = sharedPreferences.getInt( record_key_notes,-1 );
                break;
        }
        return rec;
    }


    public void setNotePassRecords(String mode,int rec)     //1:note 2:password
    {
        switch (mode){
            case "password":
                sharedPreferences.edit().putInt( record_key,rec ).apply();
                break;
            case "note":
                sharedPreferences.edit().putInt( record_key_notes,rec ).apply();
                break;
        }
    }


    public String getNotePassIndex(String mode)     //1: note 2:password
    {
        String p = "";
        switch (mode){
            case "note":
                p = sharedPreferences.getString( record_index_notes,"-1" );
                break;
            case "password":
                p = sharedPreferences.getString( record_index,"-1" );
                break;
        }
        return p;

    }

    public void setNotePassIndex(int[] arr,String mode)     //1: note 2:password
    {
        String p = new StringToArray().convertToString( arr );
        switch (mode){
            case "note":
                sharedPreferences.edit().putString( record_index_notes,p ).apply();
                break;
            case "password":
                sharedPreferences.edit().putString( record_index,p ).apply();
                break;
        }
    }
    public void setNotePassIndex(String p,String mode)     //1: note 2:password
    {

        switch (mode){
            case "note":
                sharedPreferences.edit().putString( record_index_notes,p ).apply();
                break;
            case "password":
                sharedPreferences.edit().putString( record_index,p ).apply();
                break;
        }
    }

    public String getPasswordData(String mode)    //1:label, 2:email 3:password 4:visited 5:edited
    {

        String bus="";
        switch (mode){
            case "label":
                bus = sharedPreferences.getString( identity_label,"-1" );
                break;
            case "email":
                bus = sharedPreferences.getString( identity,"-1" );
                break;
            case "password":
                bus = sharedPreferences.getString( pass_key,"-1" );
                break;
            case "visited":
                bus = sharedPreferences.getString( v_tst_label,"-1" );
                break;
            case "edited":
                bus = sharedPreferences.getString( e_tst_label,"-1" );
                break;
            case "occupy":
                bus = sharedPreferences.getString( occupy,"-1" );
                break;
        }

        return bus;
    }

    public String getNoteData(String mode)     //1:label 2:notes 3:visited 4:edited
    {
        String bus="";
        switch (mode){
            case "label":
                bus = sharedPreferences.getString( note_identity_label,"-1" );
                break;
            case "notes":
                bus = sharedPreferences.getString( note_key,"-1" );
                break;
            case "visited":
                bus = sharedPreferences.getString( v_tst_label,"-1" );
                break;
            case "edited":
                bus = sharedPreferences.getString( e_tst_label,"-1" );
                break;
            case "occupy":
                bus = sharedPreferences.getString( occupy,"-1" );
                break;
        }
        return bus;
    }
    public boolean setPasswordData(String label,String email,String password, String visited,String edited,String occupied)    //1:label, 2:email 3:password 4:visited 5:edited
    {
        sharedPreferences.edit().putString( identity_label,label )
        .putString( identity,email )
        .putString( pass_key,password )
        .putString( v_tst_label,visited )
        .putString( e_tst_label,edited )
        .putString( occupy,occupied ).apply();
        return true;
    }
    public boolean setColumnDataPass(String mode,String data){
        switch (mode){
            case "label":
                sharedPreferences.edit().putString( note_identity_label,data ).apply();
                break;
            case "notes":
                sharedPreferences.edit().putString( note_key,data ).apply();
                break;
            case "visited":
                sharedPreferences.edit().putString( v_tst_label,data ).apply();
                break;
            case "edited":
                sharedPreferences.edit().putString( e_tst_label,data ).apply();
                break;
            case "occupy":
                sharedPreferences.edit().putString( occupy,data ).apply();
                break;
        }
        return true;
    }
    public boolean setNoteData(String label,String note, String visited,String edited,String occupied)    //1:label, 2:email 3:password 4:visited 5:edited
    {
        sharedPreferences.edit().putString( note_identity_label,label )
        .putString( note_key,note )
        .putString( v_tst_label,visited )
        .putString( e_tst_label,edited )
        .putString( occupy,occupied ).apply();
        return true;
    }



}
