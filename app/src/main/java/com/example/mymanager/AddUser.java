package com.example.mymanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class AddUser extends AppCompatActivity {

    String seq[] = {
            "Select a Security Question",
            "What Is your favorite book?",
            "What is the name of the road you grew up on?",
            "What is your mother’s maiden name?",
            "What was the name of your first/current/favorite pet?",
            "What was the first company that you worked for?",
            "Where did you meet your spouse?",
            "Where did you go to high school/college?",
            "What is your favorite food?",
            "What city were you born in?",
            "Where is your favorite place to vacation?"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        Button sac = findViewById(R.id.save);
        sac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText name = findViewById(R.id.editName);
                final String n = name.getText().toString();

                EditText recovery = findViewById(R.id.editRecovery );
                final String rec = recovery.getText().toString();

                EditText pass = findViewById(R.id.editPass);
                final String pas = pass.getText().toString();

                EditText confpass = findViewById(R.id.editConfPass);
                final String cpass = confpass.getText().toString();

                Spinner sec_qtn = findViewById( R.id.spinner_secq );
                final String s_q = sec_qtn.getSelectedItem().toString();

                EditText a_sec_qtn = findViewById( R.id.editsecq_a );
                final String a_s_q = a_sec_qtn.getText().toString();

                if(n.length() > 0 && rec.length() > 0 && pas.length() >= 8 && cpass.length() >= 8 && !s_q.equals( seq[0] ) && !a_s_q.isEmpty())
                {

                    if(pas.equals(cpass))
                    {
                        int alpac = 0,nosc = 0,spc = 0;
                        for(int ind = 0;ind<pas.length();ind++)
                        {
                            if(pas.toLowerCase().charAt( ind )>='a' && pas.toLowerCase().charAt( ind )<='z')
                            {
                                alpac++;
                            }
                            else if(pas.charAt( ind )>='0' && pas.charAt( ind )<='9')
                            {
                                nosc++;
                            }
                            else
                            {
                                spc++;
                            }
                        }
                        if(alpac >=4 && nosc >= 4 && spc!=0)
                        {
                            String records = "nRecord";     //Storage Details
                            String record_key = "nKey";
                            String record_key_notes = "nKey_notes";
                            String record_index = "iKey";
                            String record_index_notes = "iKey_notes";

                            final String trial = "Trials";
                            final String num = "trys";
                            final String time = "timer";

                            String pref = "Bundle";     //Personal Details
                            String name_key="Name";
                            String recovery_key="Recovery";
                            String recovery_qtn = "SecQuestion";
                            String recovery_ans = "ansSecQuestion";
                            String pass_key="Code";
                            //String key = "1";

                            String masterkey = null;

                            try {
                                masterkey = MasterKeys.getOrCreate( MasterKeys.AES256_GCM_SPEC );

                                SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                                        records,
                                        masterkey,
                                        getApplicationContext(),
                                        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                                        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);

                                sharedPreferences.edit()
                                        .putInt(record_key,0)
                                        .putInt(record_key_notes,0)
                                        .putString( record_index,"0000000000" )
                                        .putString( record_index_notes,"0000000000" )
                                        .apply();

                                SharedPreferences pass_try = EncryptedSharedPreferences.create(
                                        trial,
                                        masterkey,
                                        getApplicationContext(),
                                        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                                        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM );


                                pass_try.edit()
                                        .putInt( num,4 )
                                        .putBoolean( time,false )
                                        .apply();

                                SharedPreferences sp_details = EncryptedSharedPreferences.create(
                                        pref,
                                        masterkey,
                                        getApplicationContext(),
                                        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                                        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);

                                sp_details.edit()
                                        .putString(name_key,n)
                                        .putString(recovery_key,rec)
                                        .putString( recovery_qtn,s_q )
                                        .putString( recovery_ans,a_s_q )
                                        .putString(pass_key,pas)
                                        .apply();

                            } catch (GeneralSecurityException | IOException e) {
                                e.printStackTrace();
                            }

                            Toast.makeText(AddUser.this,"User named "+n+" added!",Toast.LENGTH_LONG).show();
                            Intent home = new Intent(AddUser.this,MainActivity.class);
                            startActivity(home);
                            AddUser.this.finish();
                        }
                        else
                        {
                            Toast.makeText( AddUser.this, "A minimum of 4 alphabets and 4 numbers should comprise the password", Toast.LENGTH_LONG ).show();
                        }

                    }
                    else
                    {
                        Toast.makeText(AddUser.this,"Passwords don't match",Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    if(n.isEmpty())
                    {
                        name.setError( "You haven't entered your name." );
                        name.requestFocus();
                    }
                    if(pas.length() < 8)
                    {
                        pass.setError( "Minimum 8 characters are required for password" );
                        pass.requestFocus();
                    }
                    if(cpass.length() < 8)
                    {
                        confpass.setError( "Couldn't confirm password" );
                        confpass.requestFocus();
                    }
                    if(rec.isEmpty())
                    {
                        recovery.setError( "You haven't entered your recovery number" );
                        recovery.requestFocus();
                    }
                    if(s_q.equals( seq[0] )){
                        Toast.makeText(AddUser.this,"Select a security question!",Toast.LENGTH_LONG).show();
                        sec_qtn.requestFocus();
                    }
                    if(a_s_q.isEmpty()){
                        a_sec_qtn.setError( "Enter the answer for the security question" );
                        a_sec_qtn.requestFocus();
                    }
                    else
                    {
                        Toast.makeText(AddUser.this,"You have not given any input.",Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
    }
}
