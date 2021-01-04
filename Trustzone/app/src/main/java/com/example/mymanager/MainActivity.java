package com.example.mymanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class MainActivity extends AppCompatActivity
{

    String records = "nRecord";
    String record_key = "nKey";
    String record_key_notes = "nKey_notes";
    int record_stored,def=-1;
    String masterKeys = null;
    ProgressBar loadBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        loadBar = findViewById( R.id.loadBar );
        loadBar.setVisibility( View.INVISIBLE);
        try {
            masterKeys = MasterKeys.getOrCreate( MasterKeys.AES256_GCM_SPEC );

            SharedPreferences sharedPreferences1 = EncryptedSharedPreferences.create(
                    records,
                    masterKeys,
                    getApplicationContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM );

            record_stored = sharedPreferences1.getInt( record_key_notes, def );

            if (record_stored == 0) {
                Log.e( "No Pass", "No Passwords" );
                loadFragment(new BlankFragment(1,getApplicationContext()));

            } else{
                loadBar.setVisibility( View.VISIBLE );
                loadFragment( new note_fragment(loadBar));
            }
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BottomNavigationView bottomnav = findViewById(R.id.bottom_nav);
        bottomnav.setOnNavigationItemSelectedListener(navlistener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navlistener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


            Fragment fragment = null;
            switch(menuItem.getItemId())
            {
                case R.id.action_note:

                    loadBar.setVisibility( View.VISIBLE );
                    try {
                        masterKeys = MasterKeys.getOrCreate( MasterKeys.AES256_GCM_SPEC );

                        SharedPreferences sharedPreferences1 = EncryptedSharedPreferences.create(
                                records,
                                masterKeys,
                                getApplicationContext(),
                                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM );

                        record_stored = sharedPreferences1.getInt( record_key_notes, def );

                        if (record_stored == 0) {
                            loadBar.setVisibility( View.INVISIBLE );
                            Log.e( "No Pass", "No Passwords" );
                            fragment = new BlankFragment(1,getApplicationContext());

                        } else {
                            fragment = new note_fragment(loadBar);
                        }
                    } catch (GeneralSecurityException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.action_pass:

                    loadBar.setVisibility( View.VISIBLE );
                    try {
                        masterKeys = MasterKeys.getOrCreate( MasterKeys.AES256_GCM_SPEC );

                        SharedPreferences sharedPreferences1 = EncryptedSharedPreferences.create(
                                records,
                                masterKeys,
                                getApplicationContext(),
                                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM );

                        record_stored = sharedPreferences1.getInt( record_key, def );

                        if (record_stored == 0) {
                            loadBar.setVisibility( View.INVISIBLE );
                            Log.e( "No Pass", "No Passwords" );
                            fragment = new BlankFragment(2,getApplicationContext());

                        } else {
                            fragment = new pass_fragment(loadBar);
                        }
                    } catch (GeneralSecurityException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.action_setting:
                    fragment = new setting_fragment();
                    break;
            }
            loadFragment(fragment);
            return true;
        }
    };

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        Drawable drawable = ContextCompat.getDrawable( getApplicationContext(),R.drawable.ic_action_add );
        Toolbar toolbar = new Toolbar( this );
        toolbar.setOverflowIcon( drawable );
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.action_add_note:
                Intent add_n = new Intent(MainActivity.this,AddNoteDisplay.class);
                add_n.putExtra( "Mode","i" );
                add_n.putExtra( "Index",-1 );
                startActivity(add_n);

                break;

            case R.id.action_add_pass:
                  Intent add_p = new Intent( MainActivity.this, AddPassDisplay.class );
                  add_p.putExtra( "Mode","i" );
                  add_p.putExtra( "Index",-1 );

                  startActivity( add_p );

                break;

            case R.id.action_refresh:
                Fragment visFrag = getSupportFragmentManager().findFragmentByTag( "My_Fragment" );
                //Log.d("Fragment","checking frag");
                if(visFrag!=null && visFrag.isVisible())
                {
                    //Log.d("Fragment2","In");
                    if(visFrag.toString().equals("note_fragment"))
                    {
                        loadFragment(new note_fragment(loadBar));
                    }
                    else
                    {
                        loadFragment(new pass_fragment(loadBar));
                    }
                }
                else
                {
                    //Log.d("Fragment3","Out");

                    Toast.makeText( this, "Unable to refresh!", Toast.LENGTH_SHORT ).show();
                }
                break;
        }
        return true;
    }

    public void loadFragment(Fragment frag)
    {
        //String f = frag.toString();
        //Log.d("Fragment4","Loading "+f);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container,frag,"My_Fragment");
        ft.addToBackStack( null );
        ft.commit();
    }

}
