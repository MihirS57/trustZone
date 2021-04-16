package com.example.mymanager;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.fragment.app.FragmentActivity;


import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Displayer extends AppCompatActivity
{
    int index;

    TextView pass_label_disp;
    TextView email_label_disp;
    TextView pass_disp;
    TextView pass_lv;
    TextView pass_le;
    ProgressBar pass_progress,notes_progress;
    Fetch passObj,noteObj,recordObj;

    TextView note_label_disp;
    TextView note_disp,note_lv,note_le;

    int mode;
    String index_key = "INDEX";
    String mode_key = "MODE";


    String dup_label,dup_email,dup_pass,dup_lv,dup_le;
    String dup_note_label,dup_note,dup_note_lv,dup_note_le;

    BiometricPrompt biometricPrompt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        index = intent.getIntExtra(index_key,-1);
        mode = intent.getIntExtra(mode_key,-1);

        if(index!=-1 && mode!=-1)
        {
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
                            DatabaseAdapter dba = new DatabaseAdapter( Displayer.this );
                            long id = dba.insertData( "Authentication Successful","Password Log IN","-","-" );
                            passObj = new Fetch( Displayer.this,"password",index );
                            displayPass();
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        DatabaseAdapter dba = new DatabaseAdapter( Displayer.this );
                        long id = dba.insertData( "Authentication Failed","Password Log IN","-","-" );
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
                        DatabaseAdapter dba = new DatabaseAdapter( Displayer.this );
                        long id = dba.insertData( "Authentication Successful","Note Log IN","-","-" );
                        noteObj = new Fetch( Displayer.this,"note",index );
                        displayNotes();

                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        DatabaseAdapter dba = new DatabaseAdapter( Displayer.this );
                        long id = dba.insertData( "Authentication Failed","Note Log IN","-","-" );
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

                final int def=-1;
                recordObj = new Fetch( Displayer.this,"records",0 );
                if((notes_thread.isAlive() && mode==0) || (pass_thread.isAlive() && mode==1)) {
                    if(mode == 0 && noteObj != null)
                    {
                         new AlertDialog.Builder(Displayer.this)
                                .setTitle("Are You Sure?")
                                .setMessage("You are about to delete this note")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        notes_thread.interrupt();

                                        String note_label=noteObj.getNoteData( "label" );
                                        noteObj.setNoteData( "","","","","false" );

                                        if(recordObj != null){
                                            int record_stored = recordObj.getNotePassRecords( "note" );
                                            String t = recordObj.getNotePassIndex( "note" );
                                            StringToArray sta = new StringToArray();
                                            t = sta.convertAndAdd( t,index,0 );
                                            recordObj.setNotePassIndex( t,"note" );
                                            if(record_stored != def) {
                                                recordObj.setNotePassRecords( "note",record_stored-1 );
                                            }
                                            DatabaseAdapter dba = new DatabaseAdapter( Displayer.this );
                                            long id = dba.insertData( "Note Deleted",note_label,"Yes","Yes" );

                                        }else{
                                            Log.e("NOTES RECORD","Record sp is null");
                                        }

                                        Displayer.this.finish();
                                        dialog.dismiss();
                                    }
                                }).create().show();
                    }
                    else if(mode==1 && passObj != null){

                        new AlertDialog.Builder(Displayer.this)
                                .setTitle("Are You Sure?")
                                .setMessage("You are about to delete this password")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        pass_thread.interrupt();
                                        String pass_label = passObj.getPasswordData( "label" );
                                        passObj.setPasswordData( "","","","","","false" );

                                        if(recordObj != null){
                                            int record_stored = recordObj.getNotePassRecords( "password" );
                                            String t = recordObj.getNotePassIndex( "password" );
                                            StringToArray sta = new StringToArray();
                                            t = sta.convertAndAdd( t,index,0 );
                                            recordObj.setNotePassIndex( t,"password" );
                                            if(record_stored != def) {
                                                recordObj.setNotePassRecords( "password",record_stored-1 );
                                            }
                                            DatabaseAdapter dba = new DatabaseAdapter( Displayer.this );
                                            long id = dba.insertData( "Password Deleted",pass_label,"Yes","Yes" );

                                        }else{
                                            Log.e("PASS RECORD","Record sp is null");
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
        if(passObj != null){
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
                DatabaseAdapter dba = new DatabaseAdapter( Displayer.this );
                long id = dba.insertData( "Password Display",label,"Yes","-" );
                passObj.setColumnDataPass( "visited",t );
                pass_thread.start();
            }else{

            }
        }

    }

    public void displayNotes()
    {
        String label,note,lastNV,lastNE;
        if(noteObj != null){
            label = noteObj.getNoteData( "label" );
            note = noteObj.getNoteData( "notes" );
            lastNV = noteObj.getNoteData( "visited" );
            lastNE = noteObj.getNoteData( "edited" );
            if(!label.equals( "-1" ) && !note.equals( "-1" )){
                dup_note_label = label;
                dup_note = note;
                dup_note_lv = "You visited last at "+lastNV;
                dup_note_le = "You created/edited this at "+lastNE;
                Calendar c = Calendar.getInstance();
                SimpleDateFormat sf = new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss a", Locale.getDefault());
                String t = sf.format( c.getTime() );
                DatabaseAdapter dba = new DatabaseAdapter( Displayer.this );
                long id = dba.insertData( "Note Display",label,"Yes","-" );
                noteObj.setColumnDataPass( "visited",t );
                notes_thread.start();
            }else{

            }
        }

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
            note_lv = findViewById( R.id.note_last_visit );
            note_le = findViewById( R.id.note_last_edit );
            runOnUiThread( new Runnable() {
                @Override
                public void run() {
                    note_label_disp.setText( dup_note_label );
                    note_disp.setText( dup_note );
                    note_lv.setText( dup_note_lv );
                    note_le.setText( dup_note_le );
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

            return 1;
        }
        else if(type.equals( "n" ))
        {
            notes_progress = findViewById( R.id.count_note );
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

            return 1;
        }
        else
        {
            return 0;
        }

    }


}
