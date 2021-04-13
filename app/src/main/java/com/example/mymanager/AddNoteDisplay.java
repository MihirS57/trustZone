package com.example.mymanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class AddNoteDisplay extends AppCompatActivity {


    EditText topic_note_input;
    EditText note_input;
    Button save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note_display);

        Intent getI = getIntent();
        String m = getI.getStringExtra( "Mode" );
        int indexI = getI.getIntExtra( "Index",-1 );

        topic_note_input = findViewById(R.id.editNoteTopic);
        note_input = findViewById(R.id.editNote);
        save = findViewById( R.id.savePass );


        final Context context = this;
        if(m.equals( "e" ) && indexI!=-1){
            editNote( indexI );
        }
        else if(m.equals( "i" )) {

            save.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int record_stored, index;
                    Notes obj;
                    String topic_note = topic_note_input.getText().toString();
                    String note = note_input.getText().toString();

                    String records = "nRecord";
                    String record_key_notes = "nKey_notes";
                    int def = -1;

                    String masterkey;
                    try {
                        masterkey = MasterKeys.getOrCreate( MasterKeys.AES256_GCM_SPEC );

                        SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                                records,
                                masterkey,
                                getApplicationContext(),
                                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM );

                        record_stored = sharedPreferences.getInt( record_key_notes, def );
                        if (record_stored != def) {

                            index = new Notes( topic_note, note, context ).storeInfo( record_stored );

                            if (index != -1) {
                                Toast.makeText( AddNoteDisplay.this, "Note Stored!", Toast.LENGTH_SHORT ).show();
                            } else {
                                Toast.makeText( context, "An Error occurred!", Toast.LENGTH_LONG ).show();
                            }
                        } else {
                            Toast.makeText( AddNoteDisplay.this, "Error in storing the data!", Toast.LENGTH_LONG ).show();
                        }
                    } catch (GeneralSecurityException | IOException e) {
                        e.printStackTrace();
                    }


                    // 0


                }
            } );
        }

    }
    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(AddNoteDisplay.this,MainActivity.class);
        startActivity( intent );
    }

    public void editNote(int in){
        final Context context=this;
        final String register_notes[] = new String[10];
        final String note_key = "Wave";
        final String note_identity_label = "Wave_name";
        String occupy = "engage";

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

        String masterKeys = null;
        try {
            masterKeys = MasterKeys.getOrCreate( MasterKeys.AES256_GCM_SPEC );
            final SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                    register_notes[in],
                    masterKeys,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV ,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);

            final String l = sharedPreferences.getString( note_identity_label,"-1" );
            final String n = sharedPreferences.getString( note_key,"-1" );

            topic_note_input.setText( l );
            note_input.setText( n );

            save = findViewById(R.id.savePass );
            save.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String li = topic_note_input.getText().toString();
                    String ni = note_input.getText().toString();
                    int check=1;

                    if((!li.equals(l)) || (!ni.equals(n))){
                        if(!li.equals(l)){
                            if(!li.equals("")) {
                                sharedPreferences.edit().putString( note_identity_label, li ).apply();
                                check=1;
                            }
                            else{
                                Toast.makeText( context, "Label is empty", Toast.LENGTH_SHORT ).show();
                                check=-1;
                            }
                        }
                        if(!ni.equals(n)){
                            if(!ni.equals( "")) {
                                sharedPreferences.edit().putString( note_key,ni ).apply();
                                check=1;
                            }
                            else{
                                Toast.makeText( context, "Note is empty", Toast.LENGTH_SHORT ).show();
                                check=-1;
                            }

                        }
                        if(check == 1){
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
