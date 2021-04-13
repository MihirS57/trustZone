package com.example.mymanager;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricFragment;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.fragment.app.FragmentActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Displayer extends AppCompatActivity
{
    int index;
    String[] register = new String[10];
    String[] register_notes = new String[10];
    TextView pass_label_disp;
    TextView email_label_disp;
    TextView pass_disp;
    TextView pass_lv;
    TextView pass_le;
    ProgressBar pass_progress,notes_progress;

    TextView note_label_disp;
    TextView note_disp;

    int mode;
    String index_key = "INDEX";
    String mode_key = "MODE";
    String AUTHENTICATION_KEY;

    String dup_label,dup_email,dup_pass,dup_lv,dup_le;
    String dup_note_label,dup_note;

    BiometricPrompt biometricPrompt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        index = intent.getIntExtra(index_key,-1);
        mode = intent.getIntExtra(mode_key,-1);

        if(index!=-1 && mode!=-1)
        {
            //Toast.makeText( Displayer.this, "Mode is "+mode+" and index is "+index, Toast.LENGTH_LONG ).show();
            if(mode == 1)
            {
                setContentView(R.layout.activity_displayer);
                getWindow().setFlags( WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
                BiometricManager biometricManager = BiometricManager.from( Displayer.this );

                Executor executor = Executors.newSingleThreadExecutor();
                FragmentActivity activity = this;

                biometricPrompt = new BiometricPrompt(activity, executor, new BiometricPrompt.AuthenticationCallback() {

                    @Override
                    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                        super.onAuthenticationError( errorCode, errString );
                    }

                    @Override
                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded( result );
                            displayPass();
                            String masterkeys = null;
                            try {
                                masterkeys = MasterKeys.getOrCreate( MasterKeys.AES256_GCM_SPEC );
                                SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                                        "AT",
                                        masterkeys,
                                        getApplicationContext(),
                                        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                                        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM );

                                sharedPreferences.edit().putString( "AuthToken", masterkeys )
                                        .apply();
                            } catch (GeneralSecurityException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        biometricPrompt.cancelAuthentication();
                        Displayer.this.finish();
                    }

                } );

                BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                        .setTitle("Authentication Required")
                        .setDescription( "Place Your Finger on the Fingerprint Sensor mounted on your device" )
                        .setSubtitle( "Confirm Your Identity" )
                        .setDeviceCredentialAllowed( true )
                        .setConfirmationRequired( true )
                        .build();

                biometricPrompt.authenticate( promptInfo );

            }
            else if(mode == 0)
            {

                setContentView(R.layout.activity_displayer);
                getWindow().setFlags( WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

                Executor executor = Executors.newSingleThreadExecutor();
                FragmentActivity activity = this;

                biometricPrompt = new BiometricPrompt(activity, executor, new BiometricPrompt.AuthenticationCallback() {

                    @Override
                    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                        super.onAuthenticationError( errorCode, errString );
                    }

                    @Override
                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded( result );
                        displayNotes();

                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        biometricPrompt.cancelAuthentication();
                        Displayer.this.finish();
                    }

                } );

                BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                        .setTitle("Authentication Required")
                        .setDescription( "Place Your Finger on the Fingerprint Sensor mounted on your device" )
                        .setSubtitle( "Confirm Your Identity" )
                        .setDeviceCredentialAllowed( true )
                        .setConfirmationRequired( true )
                        .build();

                biometricPrompt.authenticate( promptInfo );


                setContentView(R.layout.activity_displayer_notes);


            }
        }



    }
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.work_menu,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_info:
                if((notes_thread.isAlive() && mode==0) || (pass_thread.isAlive() && mode==1)){
                    if(mode == 0){
                        notes_thread.interrupt();
                        Intent edit = new Intent( Displayer.this,AddNoteDisplay.class );
                        edit.putExtra( "Mode","e" );
                        edit.putExtra( "Index",index );
                        startActivity(edit);
                    }
                    else if(mode == 1){
                        pass_thread.interrupt();
                        Intent edit = new Intent(Displayer.this,AddPassDisplay.class);
                        edit.putExtra( "Mode","e" );
                        edit.putExtra( "Index",index );
                        startActivity(edit);
                    }
                } else{
                    Toast.makeText( this, "Please Authenticate to continue!", Toast.LENGTH_LONG ).show();
                }

                break;

            case R.id.delete_info:
                final String records = "nRecord";
                final String record_key_notes = "nKey_notes";
                final String record_key = "nKey";
                final String record_index = "iKey";
                final int def=-1;
                if((notes_thread.isAlive() && mode==0) || (pass_thread.isAlive() && mode==1)) {
                    if(mode == 0)
                    {
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

                        final String note_key = "Wave";
                        final String note_identity_label = "Wave_name";
                        final String occupy = "engage";

                        int record_stored;

                        new AlertDialog.Builder(Displayer.this)
                                .setTitle("Are You Sure?")
                                .setMessage("You are about to delete this note")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        notes_thread.interrupt();
                                        String masterkeys = null;
                                        try {
                                            masterkeys = MasterKeys.getOrCreate( MasterKeys.AES256_GCM_SPEC );
                                            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                                                    register_notes[index],
                                                    masterkeys,
                                                    getApplicationContext(),
                                                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                                                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);

                                            sharedPreferences.edit().putString( note_identity_label,"" )
                                                    .putString( note_key,"" )
                                                    .putString( occupy,"false" ).apply();

                                            SharedPreferences sharedPreferences1 = EncryptedSharedPreferences.create(
                                                    records,
                                                    masterkeys,
                                                    getApplicationContext(),
                                                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                                                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);

                                            int record_stored = sharedPreferences1.getInt(record_key_notes,def);
                                            if(record_stored != def ) {
                                                record_stored = record_stored - 1;
                                                sharedPreferences1.edit().putInt( record_key_notes,record_stored ).apply();
                                            }

                                        } catch (GeneralSecurityException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        Displayer.this.finish();
                                        dialog.dismiss();
                                    }
                                }).create().show();
                    }
                    else if(mode==1){
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

                        final String pass_key = "Signal";
                        final String identity = "Signal_classifier";
                        final String identity_label = "Label";
                        final String e_tst_label = "eStamp";
                        final String v_tst_label = "vStamp";
                        final String occupy = "engage";

                        new AlertDialog.Builder(Displayer.this)
                                .setTitle("Are You Sure?")
                                .setMessage("You are about to delete this password")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        notes_thread.interrupt();
                                        String masterkeys = null;
                                        try {
                                            masterkeys = MasterKeys.getOrCreate( MasterKeys.AES256_GCM_SPEC );
                                            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                                                    register[index],
                                                    masterkeys,
                                                    getApplicationContext(),
                                                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                                                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);

                                            sharedPreferences.edit().putString( identity_label,"" )
                                                    .putString( identity,"" )
                                                    .putString( pass_key,"" )
                                                    .putString( e_tst_label,"" )
                                                    .putString( v_tst_label,"" )
                                                    .putString( occupy,"false" ).apply();

                                            SharedPreferences sharedPreferences1 = EncryptedSharedPreferences.create(
                                                    records,
                                                    masterkeys,
                                                    getApplicationContext(),
                                                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                                                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);

                                            int record_stored = sharedPreferences1.getInt(record_key,def);
                                            if(record_stored != def ) {
                                                record_stored = record_stored - 1;
                                                sharedPreferences1.edit().putInt( record_key,record_stored ).apply();
                                                String t = sharedPreferences1.getString( record_index,"-1" );
                                                StringToArray sta = new StringToArray();
                                                t = sta.convertAndAdd( t,index,0 );
                                                sharedPreferences1.edit().putString( record_index,t ).apply();
                                            }

                                        } catch (GeneralSecurityException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        Displayer.this.finish();
                                        dialog.dismiss();
                                    }
                                })
                                .setNegativeButton( "CANCEL", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                } )
                                .create().show();
                    }
                } else{
                    Toast.makeText( this, "Please Authenticate to continue!", Toast.LENGTH_LONG ).show();
                }


                break;
        }
        return true;
    }

    public void displayPass()
    {
        Fetch passObj = new Fetch(this,"password",index);
        String label,email,pass,lastV,lastE;
        label = passObj.getPasswordData( "label" );
        email = passObj.getPasswordData( "email" );
        pass = passObj.getPasswordData( "password" );
        lastV = passObj.getPasswordData( "visited" );
        lastE = passObj.getPasswordData( "edited" );

        if(!(label.equals( "-1" ) && email.equals( "-1" ) && pass.equals( "-1" ) && lastV.equals( "-1" ) && lastE.equals( "-1" ))){
            dup_email = email;
            dup_label = label;
            dup_pass = pass;
            dup_lv = "You visited last at "+lastV;
            dup_le = "You created/edited this at "+lastE;
            Calendar c = Calendar.getInstance();
            SimpleDateFormat sf = new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss a", Locale.getDefault());
            String t = sf.format( c.getTime() );
            passObj.setColumnDataPass( "visited",t );
            pass_thread.start();
        }else{

        }

    }

    public void displayNotes()
    {
        String label,note;
        Fetch notesObj = new Fetch(this,"notes",index);
        label = notesObj.getNoteData( "label" );
        note = notesObj.getNoteData( "notes" );

        if(!label.equals( "-1" ) && !note.equals( "-1" )){
            dup_label = label;
            dup_note = note;
            notes_thread.start();
        }else{

        }


        /*

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

        String note_key = "Wave";
        String note_identity_label = "Wave_name";



        try {
            String masterkeys = MasterKeys.getOrCreate( MasterKeys.AES256_GCM_SPEC );

            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                    register_notes[index],
                    masterkeys,
                    getApplicationContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);

            label = sharedPreferences.getString( note_identity_label,"-1" );
            note = sharedPreferences.getString( note_key,"-1" );

            if(!(label.equals( "-1" ) && note.equals("-1"))) {

                dup_note_label = label;
                dup_note = note;
                notes_thread.start();

            }
            else
            {
                Toast.makeText( Displayer.this,"Unexpected Error Occurred",Toast.LENGTH_LONG ).show();
            }

        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }

         */


    }

    Thread pass_thread = new Thread() {

        @Override
        public void run() {
            super.run();
            int finish;
            pass_label_disp = findViewById(R.id.disp_passlabel);
            email_label_disp = findViewById( R.id.disp_emaillabel );
            pass_disp = findViewById( R.id.disp_pass );
            pass_lv = findViewById( R.id.last_visit );
            pass_le = findViewById( R.id.last_edit );

            runOnUiThread( new Runnable() {
                @Override
                public void run() {

                        pass_label_disp.setText( dup_label );
                        email_label_disp.setText( dup_email );
                        pass_disp.setText( dup_pass );
                        pass_lv.setText( dup_lv );
                        pass_le.setText( dup_le );

                }
            } );
            finish = timer("p");
            if(finish == 1)
            {
                Displayer.this.finish();
            }
        }
    };

    Thread notes_thread = new Thread()
    {
        @Override
        public void run() {
            super.run();
            int finish;
            note_label_disp = findViewById( R.id.disp_noteslabel );
            note_disp = findViewById( R.id.disp_notes );

            runOnUiThread( new Runnable() {
                @Override
                public void run() {
                    note_label_disp.setText( dup_note_label );
                    note_disp.setText( dup_note );
                }
            } );
            finish = timer("n");
            if(finish == 1)
            {
                Displayer.this.finish();
            }
        }
    };

    public int timer(String type)
    {
        if(type.equals("p"))
        {
            /*ImageView encrypt_stats = findViewById( R.id.encrypt_stats_pass );
            encrypt_stats.setBackground( null );
            encrypt_stats.setImageDrawable( getResources().getDrawable( R.drawable.ic_action_unlocked , null ) );*/
            pass_progress = findViewById( R.id.countdown );
            int i;
            Drawable progressDrawable = pass_progress.getProgressDrawable().mutate();
            progressDrawable.setColorFilter( Color.RED , PorterDuff.Mode.SRC_IN );
            pass_progress.setProgressDrawable( progressDrawable );

            for(i = 0;i<=300 && !Thread.interrupted() ;i++)
            {
                pass_progress.setProgress( i );

                try {
                    Thread.sleep( 50 );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            /*encrypt_stats.setBackground( null );
            encrypt_stats.setImageDrawable( getResources().getDrawable( R.drawable.ic_action_locked , null ) );*/
            return 1;
        }
        else if(type.equals( "n" ))
        {

            notes_progress = findViewById( R.id.count_note );
            /*ImageView encrypt_stats_notes = findViewById( R.id.encrypt_stats_notes );
            encrypt_stats_notes.setBackground( null );*/
            //encrypt_stats_notes.setImageDrawable( getResources().getDrawable( R.drawable.ic_action_unlocked , null ) );
            int i;
            Drawable progressDrawable = notes_progress.getProgressDrawable().mutate();
            progressDrawable.setColorFilter( Color.RED , PorterDuff.Mode.SRC_IN );
            notes_progress.setProgressDrawable( progressDrawable );

            for(i = 0;i<=300  && !Thread.interrupted() ;i++)
            {
                notes_progress.setProgress( i );

                try {
                    Thread.sleep( 50 );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            /*encrypt_stats_notes.setBackground( null );
            encrypt_stats_notes.setImageDrawable( getResources().getDrawable( R.drawable.ic_action_locked , null ) );*/
            return 1;
        }
        else
        {
            return 0;
        }

    }


}
