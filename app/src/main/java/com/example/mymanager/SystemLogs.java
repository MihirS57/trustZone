package com.example.mymanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class SystemLogs extends AppCompatActivity {

    TextView logs_view;
    String logs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_system_logs );
        logs_view = findViewById( R.id.logs_view );
        DatabaseAdapter dba = new DatabaseAdapter( SystemLogs.this );
        logs = dba.getData();
        logs_view.setText( logs );
    }
}