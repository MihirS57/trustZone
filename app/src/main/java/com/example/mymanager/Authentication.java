package com.example.mymanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.security.GeneralSecurityException;

public class Authentication extends AppCompatActivity {

    EditText pass_enter;
    TextView warn,m1,m2;
    ImageView stats,lock_stats;
    SharedPreferences pass_try = null;
    Button rec;
    int n=0,tn=0;
    boolean t=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );


        final String Preference_Name = "FirstOrNot";
        final String Version_Key = "Version_Code";

        int Version = BuildConfig.VERSION_CODE;
        SharedPreferences sp = getSharedPreferences( Preference_Name, MODE_PRIVATE );
        int savedVersion = sp.getInt( Version_Key, -1 );

        if (savedVersion == Version) {
            setContentView( R.layout.authentication );
            getWindow().setFlags( WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
            pass_enter = findViewById( R.id.enter_pass );
            warn = findViewById( R.id.warning );
            m1 = findViewById( R.id.textView );
            m2 = findViewById( R.id.auth );
            lock_stats = findViewById(R.id.lock_status);
            pass_enter.setVisibility( View.VISIBLE );
            rec = findViewById( R.id.recover );
            rec.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent_r = new Intent( Authentication.this, AccRecovery.class );
                    startActivity( intent_r );
                }
            } );
            final String trial = "Trials";
            final String num = "trys";
            final String time = "timer";
            String masterkey1 = null;

            n=0;
            tn=0;
            t=false;
            try {
                masterkey1 = MasterKeys.getOrCreate( MasterKeys.AES256_GCM_SPEC );
                pass_try = EncryptedSharedPreferences.create(
                        trial,
                        masterkey1,
                        getApplicationContext(),
                        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM );

                n = pass_try.getInt( num, -1 );
                t = pass_try.getBoolean( time, false );
            } catch (GeneralSecurityException | IOException e) {
                e.printStackTrace();
            }
            //Toast.makeText( this, "n:"+n+" t:"+t, Toast.LENGTH_LONG ).show();
                if((n <= 0) && t){
                    Log.d("MyStartupN","NoChancesAtStartup");
                    invisibleIt();
                }
                else{
                    Log.d("MyStartupE","ChancesLeft");
                    pass_enter.setOnKeyListener( new View.OnKeyListener() {

                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                                Log.d("MyChances","ChancesLeft");
                                stats = findViewById(R.id.encrypt_stats );
                                if(event.getAction() == KeyEvent.ACTION_DOWN) {
                                    Log.d( "MyKey", "KeyPressed" );
                                    //lock_stats = findViewById(R.id.lock_status);
                                    String pref = "Bundle";
                                    String pass_key = "Code";

                                    String masterkey = null;

                                    try {
                                        if (keyCode == KeyEvent.KEYCODE_ENTER) {
                                            Log.d( "MyEnterKey", "EnterKeyPressed" );
                                            masterkey = MasterKeys.getOrCreate( MasterKeys.AES256_GCM_SPEC );
                                            SharedPreferences sp_check = EncryptedSharedPreferences.create(
                                                    pref,
                                                    masterkey,
                                                    getApplicationContext(),
                                                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                                                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM );
                                            String def = "-1";
                                            if (sp_check.getString( pass_key, def ).equals( pass_enter.getText().toString() )) {
                                                pass_try.edit().putInt( num,4 ).apply();
                                                warn.setText( "" );
                                                stats.setImageDrawable( getResources().getDrawable( R.color.colorBlack, null ) );
                                                lock_stats.setImageDrawable( getResources().getDrawable( R.drawable.ic_action_unlocked, null ) );
                                                Intent home = new Intent( Authentication.this, MainActivity.class );
                                                startActivity( home );
                                                Authentication.this.finish();

                                            } else {
                                                Log.d( "MyWrongPassword", "ElsePartExecuted" );
                                                n = n - 1;
                                                pass_try.edit().putInt( num, n ).apply();
                                                if (n <= 0) {
                                                    pass_try.edit().putBoolean( time, true ).apply();
                                                    Log.d( "MyExecuteNoChances", "ChancesExhausted" );
                                                    invisibleIt();
                                                }
                                                warn.setText( "Wrong Password (" + n + " chance/s left!" );
                                                stats.setImageDrawable( getResources().getDrawable( R.drawable.ic_action_locked, null ) );
                                            }

                                        }

                                    } catch (GeneralSecurityException | IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                            return false;
                        }
                    } );
                }

        } else {
            sp.edit().putInt( Version_Key, Version ).apply();
            Intent intent_au = new Intent( Authentication.this, AddUser.class );
            startActivity( intent_au );
            this.finish();
        }
        }

    private void invisibleIt() {
        pass_enter.setVisibility( View.INVISIBLE );
        rec.setVisibility( View.INVISIBLE );
        m1.setVisibility( View.INVISIBLE );
        m2.setVisibility( View.INVISIBLE );

        countToZero();
    }


    private void countToZero() {

        new CountDownTimer( 30000,1000 ){
            long timer = 30000;
            @Override
            public void onTick(long l) {
                warn.setText( "Application Locked, Please try again after "+(timer/1000)+"s" );
                if(timer%2 == 0){
                    lock_stats.setImageDrawable( getResources().getDrawable(R.drawable.ic_action_locked ,null ));
                }
                timer=timer-1000;
            }

            @Override
            public void onFinish() {
                final String trial = "Trials";
                final String num = "trys";
                final String time = "timer";
                String masterkey1 = null;
                int n=0;
                long t=0;
                try {
                    masterkey1 = MasterKeys.getOrCreate( MasterKeys.AES256_GCM_SPEC );
                    SharedPreferences pass_try = EncryptedSharedPreferences.create(
                            trial,
                            masterkey1,
                            getApplicationContext(),
                            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM );

                    pass_try.edit().putInt( num,4 )
                            .putBoolean( time,false )
                            .apply();

                    /*lock_stats.setImageDrawable( getResources().getDrawable(R.drawable.ic_action_lockscreenicon ,null ));
                    pass_enter.setVisibility( View.VISIBLE );
                    rec.setVisibility( View.VISIBLE );
                    m1.setVisibility( View.VISIBLE );
                    m2.setVisibility( View.VISIBLE );
                    warn.setText( "" );*/
                    Authentication.this.finish();
                    Authentication.this.startActivity( Authentication.this.getIntent() );
                } catch (GeneralSecurityException | IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public void onBackPressed()
    {
        finishAndRemoveTask();
    }
        /*Thread authentication_success = new Thread() {
            ImageView stats;

            public void run() {
                super.run();
                stats = findViewById( R.id.encrypt_stats );
                runOnUiThread( new Runnable() {
                    @Override
                    public void run() {
                        stats.setBackgroundResource( R.drawable.ic_action_unlocked );
                    }
                } );

            }
        };
        Thread authentication_denied = new Thread() {
            ImageView stats;

            public void run() {
                super.run();
                stats = findViewById( R.id.encrypt_stats );
                runOnUiThread( new Runnable() {
                    @Override
                    public void run() {
                        stats.setBackgroundResource( R.drawable.ic_action_locked );
                    }
                } );

            }
        };*/



        /*else if (savedVersion < Version )
        {
            setContentView( R.layout.authentication );
            sp.edit().putInt( Version_Key, Version ).apply();

            final EditText pass_enter = findViewById( R.id.enter_pass );
            setContentView( R.layout.authentication );

            Button rec = findViewById( R.id.recover );
            rec.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent_r = new Intent( Authentication.this, AccRecovery.class );
                    startActivity( intent_r );
                }
            } );

            pass_enter.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    TextView warn = findViewById(R.id.warning);

                    String p_enter = pass_enter.getText().toString();

                    String pref = "Bundle";
                    String pass_key="Code";
                    SharedPreferences sp_check = getSharedPreferences(pref,MODE_PRIVATE);
                    String def = "-1";
                    String pass = sp_check.getString(pass_key,def);

                    if(keyCode==KeyEvent.KEYCODE_ENTER)
                    {
                        if((pass.equals(p_enter)))
                        {
                            warn.setText("Welcome back");
                            //authentication_success.start();
                            Intent home = new Intent(Authentication.this,MainActivity.class);
                            startActivity(home);
                            Authentication.this.finish();

                            //stats.setBackgroundResource(R.drawable.ic_action_locked);
                        }
                        else
                        {
                            warn.setText( "Incorrect PIN" );
                            //authentication_denied.start();
                        }
                        pass_enter.setText("");
                    }
                    return false;
                }
            });
        }
            else
             {
               Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
         }*/

}


