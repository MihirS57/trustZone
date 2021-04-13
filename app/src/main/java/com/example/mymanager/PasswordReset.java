package com.example.mymanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class PasswordReset extends AppCompatActivity {

    EditText currPass, newPass, confNewPass;
    Button save;
    String p,np,cnp,sp="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_password_reset );
        currPass = findViewById( R.id.editCurrent );
        newPass = findViewById( R.id.editNew );
        confNewPass = findViewById( R.id.editConfNew );
        save = findViewById( R.id.saveNewPassBtn );

        final String pref = "Bundle";     //Personal Details
        final String pass_key = "Code";

        save.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                p = currPass.getText().toString();
                np = newPass.getText().toString();
                cnp = confNewPass.getText().toString();

                if (p.isEmpty()) {
                    currPass.setError( "Enter current password" );
                    currPass.requestFocus();
                }

                String masterkey = null;

                try {
                    masterkey = MasterKeys.getOrCreate( MasterKeys.AES256_GCM_SPEC );

                    SharedPreferences sp_details = EncryptedSharedPreferences.create(
                            pref,
                            masterkey,
                            getApplicationContext(),
                            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM );

                    sp = sp_details.getString( pass_key, "-1" );

                } catch (GeneralSecurityException | IOException e) {
                    e.printStackTrace();
                }

                if (!p.equals( sp )) {
                    currPass.setError( "Wrong password" );
                    currPass.requestFocus();
                }
                if (np.length() >= 8 && cnp.length() >= 8) {
                    if (np.equals( cnp )) {
                        int alpac = 0, nosc = 0, spc = 0;
                        for (int ind = 0; ind < np.length(); ind++) {
                            if (np.toLowerCase().charAt( ind ) >= 'a' && np.toLowerCase().charAt( ind ) <= 'z') {
                                alpac++;
                            } else if (np.charAt( ind ) >= '0' && np.charAt( ind ) <= '9') {
                                nosc++;
                            } else {
                                spc++;
                            }
                        }
                        if (alpac > 0 && nosc > 0 && spc > 0) {
                            try {
                                masterkey = MasterKeys.getOrCreate( MasterKeys.AES256_GCM_SPEC );
                                SharedPreferences sp_details = EncryptedSharedPreferences.create(
                                        pref,
                                        masterkey,
                                        getApplicationContext(),
                                        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                                        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM );

                                sp_details.edit()
                                        .putString( pass_key, np )
                                        .apply();

                                Toast.makeText( PasswordReset.this, "Password Reset Successful", Toast.LENGTH_SHORT ).show();

                            } catch (GeneralSecurityException | IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            newPass.setError( "Wrong format! Check instructions given above" );
                            newPass.requestFocus();
                        }
                    } else {
                        confNewPass.setError( "Passwords don't match" );
                        newPass.setError( "Passwords don't match" );
                        newPass.requestFocus();
                    }
                } else{
                    newPass.setError( "Wrong format! Check instructions given above" );
                    newPass.requestFocus();
                }
            }
        } );
    }
}