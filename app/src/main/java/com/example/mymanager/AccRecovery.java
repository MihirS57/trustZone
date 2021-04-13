package com.example.mymanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class AccRecovery extends AppCompatActivity {

    private int SMS_Rights = -1;
    TextView recoveryQ;
    EditText recoveryA;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acc_recovery);

        final EditText recover = findViewById(R.id.editRNumber);

        recoveryQ = findViewById( R.id.recoverQ );
        recoveryA = findViewById( R.id.editRecoveryA );


        //final SharedPreferences sp_check = getSharedPreferences(sp,MODE_PRIVATE);

        String masterkey = null;

        try {
            masterkey = MasterKeys.getOrCreate( MasterKeys.AES256_GCM_SPEC );

            final String pref = "Bundle";     //Personal Details
            String name_key="Name";
            final String recovery_key="Recovery";
            String recovery_qtn = "SecQuestion";
            final String recovery_ans = "ansSecQuestion";
            final String pass_key="Code";
            final String def = "-1";

            final SharedPreferences sp_check = EncryptedSharedPreferences.create(
                    pref,
                    masterkey,
                    getApplicationContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);


            recoveryQ.setText( sp_check.getString( recovery_qtn,"Error!" ) );
            recover.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    String inp_recovery_ans = recoveryA.getText().toString();
                    String stored_recovery_ans = sp_check.getString( recovery_ans,def );
                    String inp_number = recover.getText().toString();
                    String stored_number = sp_check.getString(recovery_key,def);

                    if(keyCode == KeyEvent.KEYCODE_ENTER)
                    {

                        if(stored_number.equals(inp_number) && stored_recovery_ans.equals( inp_recovery_ans ))
                        {

                            String pass = sp_check.getString(pass_key,def);
                            if(ContextCompat.checkSelfPermission(AccRecovery.this,Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED)
                            {
                                SmsManager.getDefault().sendTextMessage(stored_number,null,"Your Password is "+pass+". \n Please Delete this SMS once you gain access to your account.",null,null);
                                Toast.makeText( AccRecovery.this, "If the recovery number is valid, an OTP will be sent to your recovery device!", Toast.LENGTH_LONG ).show();
                            }
                            else
                            {
                                ActivityCompat.shouldShowRequestPermissionRationale(AccRecovery.this,Manifest.permission.SEND_SMS);
                                new AlertDialog.Builder(AccRecovery.this)
                                        .setTitle("Permission Required!")
                                        .setMessage("Sending the recovery password requires SMS permission")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                ActivityCompat.requestPermissions(AccRecovery.this, new String[]{Manifest.permission.SEND_SMS},SMS_Rights);
                                            }
                                        })
                                        .setNegativeButton("Cancel & Exit", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent_au = new Intent(AccRecovery.this, Authentication.class);
                                                AccRecovery.this.finish();
                                                startActivity(intent_au);
                                            }
                                        }).create().show();

                            }
                        }
                    }
                    return false;
                }
            });


        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }




    }
}
