package com.example.mymanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddPassDisplay extends AppCompatActivity {

    ImageView eye;
    EditText input_name;
    EditText input_email;
    EditText input_pass;
    EditText conf_input_pass;
    Context context;
    Button save;

    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pass_display);

        Intent getI = getIntent();
        String m = getI.getStringExtra( "Mode" );
        int indexI = getI.getIntExtra( "Index",-1 );

        context = this;

        input_name = findViewById(R.id.editName);
        input_email = findViewById(R.id.editRecovery );
        input_pass = findViewById(R.id.editPass);
        conf_input_pass = findViewById(R.id.editConfirmPass);

        eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(input_pass.getInputType() == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                    input_pass.setInputType( InputType.TYPE_CLASS_TEXT );
                    conf_input_pass.setInputType( InputType.TYPE_CLASS_TEXT );
                }
                else {
                    input_pass.setInputType( InputType.TYPE_TEXT_VARIATION_PASSWORD );
                    conf_input_pass.setInputType( InputType.TYPE_TEXT_VARIATION_PASSWORD );
                }
               /*new AlertDialog.Builder(AddPassDisplay.this)
                       .setTitle("Password:")
                       .setMessage(input_pass.getText().toString())
                       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               dialog.dismiss();
                           }
                       }).create().show();*/
            }
        });



        if(m.equals( "e") && indexI != -1 ){
            editPass( indexI );
        }
        else if(m.equals( "i" )){
            save = findViewById( R.id.savePass );
            save.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int record_stored, def = -1;

                    String name = input_name.getText().toString();
                    String email = input_email.getText().toString();
                    String pass = input_pass.getText().toString();
                    String confpass = conf_input_pass.getText().toString();
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat sf = new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss a", Locale.getDefault());
                    String t = sf.format( c.getTime() );

                    String records = "nRecord";
                    String record_key = "nKey";
                    String masterkey;

                    try {
                        masterkey = MasterKeys.getOrCreate( MasterKeys.AES256_GCM_SPEC );

                        SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                                records,
                                masterkey,
                                getApplicationContext(),
                                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM );

                        record_stored = sharedPreferences.getInt( record_key, def );  // 0

                        if (record_stored != def && pass.equals( confpass )) {

                            index = new Pass( name, email, pass,t, context ).storeInfo( record_stored );

                            if (index != -1) {
                                Toast.makeText( AddPassDisplay.this, "Password Stored!", Toast.LENGTH_SHORT ).show();
                            } else {
                                Toast.makeText( context, "An Error occurred!", Toast.LENGTH_LONG ).show();
                            }

                        } else if (!pass.equals( confpass )) {
                            //Toast.makeText(AddPassDisplay.this,"Passwords don't match",Toast.LENGTH_LONG).show();
                            new AlertDialog.Builder( AddPassDisplay.this )
                                    .setTitle( "Error!" )
                                    .setMessage( "Passwords don't match! Please check" )
                                    .setPositiveButton( "OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    } ).create().show();
                        } else {
                            Toast.makeText( AddPassDisplay.this, "Error in storing the data!", Toast.LENGTH_LONG ).show();
                            sharedPreferences.edit().putInt( record_key, 0 ).apply();
                        }


                    } catch (GeneralSecurityException | IOException e) {
                        e.printStackTrace();
                    }


                }
            } );
        }


    }
    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(AddPassDisplay.this,MainActivity.class);
        startActivity( intent );
    }

    public void editPass(int in){
        String register[] = new String[10];
        final String pass_key = "Signal";
        final String identity = "Signal_classifier";
        final String identity_label = "Label";
        final String e_tst_label = "eStamp";
        final String v_tst_label = "vStamp";
        String occupy = "engage";

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

        String masterKeys = null;
        try {
            masterKeys = MasterKeys.getOrCreate( MasterKeys.AES256_GCM_SPEC );
            final SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                    register[in],
                    masterKeys,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV ,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);

            final String l = sharedPreferences.getString( identity_label,"-1" );
            final String e = sharedPreferences.getString( identity,"-1" );
            final String p = sharedPreferences.getString( pass_key,"-1" );

            input_name.setText( l );
            input_email.setText( e );
            input_pass.setText( p );
            conf_input_pass.setText( p );

            save = findViewById(R.id.savePass );
            save.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String li = input_name.getText().toString();
                    String ei = input_email.getText().toString();
                    String pi = input_pass.getText().toString();
                    String cpi = conf_input_pass.getText().toString();
                    int check=1;

                    if((!li.equals(l)) || (!ei.equals(e)) || (!pi.equals( p )) || (!cpi.equals( p ))){
                        if(!li.equals(l)){
                            if(!li.equals("")) {
                                sharedPreferences.edit().putString( identity_label, li ).apply();
                                check=1;
                            }
                            else{
                                Toast.makeText( context, "Label is empty", Toast.LENGTH_SHORT ).show();
                                check=-1;
                            }
                        }
                        if(!ei.equals(e)){
                            if(!ei.equals( "")) {
                                sharedPreferences.edit().putString( identity,ei ).apply();
                                check=1;
                            }
                            else{
                                Toast.makeText( context, "Email is empty", Toast.LENGTH_SHORT ).show();
                                check=-1;
                            }

                        }
                        if(!pi.equals( p ) && pi.equals( cpi )){
                            if(!pi.equals("")) {
                                sharedPreferences.edit().putString( pass_key,pi ).apply();
                                check=1;
                            }
                            else{
                                Toast.makeText( context, "Password is empty", Toast.LENGTH_SHORT ).show();
                                check=-1;
                            }
                        } else if(!pi.equals( cpi )){
                            Toast.makeText( context, "Passwords don't match", Toast.LENGTH_SHORT ).show();
                            check=-1;
                        }
                        if(check == 1){
                            Calendar c = Calendar.getInstance();
                            SimpleDateFormat sf = new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss a", Locale.getDefault());
                            String t = sf.format( c.getTime() );
                            sharedPreferences.edit().putString( e_tst_label,t )
                                    .putString( v_tst_label,t ).apply();
                            Toast.makeText( context, "Changes Saved!", Toast.LENGTH_SHORT ).show();

                        } else{
                            Toast.makeText( context, "Error! Please check if any entry is not blank and the passwords match!", Toast.LENGTH_LONG ).show();
                        }
                    }
                    else{
                        Toast.makeText( context, "No Changes Found", Toast.LENGTH_SHORT ).show();
                    }
                }
            } );

        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        };



    }
}
