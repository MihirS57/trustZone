package com.example.mymanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddPassDisplay extends AppCompatActivity {


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
                    Fetch recordObj = new Fetch(context,"records",0);
                    record_stored = recordObj.getNotePassRecords( "password" );

                    if (record_stored != def && pass.equals( confpass )) {

                        index = new Pass( name, email, pass,t, context ).storeInfo(  );

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
            final Fetch passObj = new Fetch(context,"password",in);
            final String l = passObj.getPasswordData( "label" );
            final String e = passObj.getPasswordData( "email" );
            final String p = passObj.getPasswordData( "password" );

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

                    if (!li.isEmpty() && !ei.isEmpty() && !pi.isEmpty() && !cpi.isEmpty() && pi.equals( cpi )) {
                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat sf = new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss a", Locale.getDefault() );
                        String t = sf.format( c.getTime() );
                        passObj.setPasswordData( li, ei, pi, t, t, "true" );
                        if (index != -1) {
                            Toast.makeText( AddPassDisplay.this, "Saved!", Toast.LENGTH_SHORT ).show();
                        } else {
                            Toast.makeText( context, "An Error occurred!", Toast.LENGTH_LONG ).show();
                        }
                    } else if (!pi.equals( cpi )) {
                        conf_input_pass.requestFocus();
                        conf_input_pass.setError( "Passwords dont match!" );
                    } else {
                        Toast.makeText( context, "One of the entry is empty!", Toast.LENGTH_SHORT ).show();
                    }

                }
            });

    }
}
