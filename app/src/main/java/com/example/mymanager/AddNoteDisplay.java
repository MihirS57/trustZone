package com.example.mymanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

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

                    int record_stored, index,def = -1;

                    String topic_note = topic_note_input.getText().toString();
                    String note = note_input.getText().toString();
                    DatabaseAdapter dba = new DatabaseAdapter( AddNoteDisplay.this );
                    Fetch recordObj = new Fetch(AddNoteDisplay.this,"records",0);
                    record_stored = recordObj.getNotePassRecords( "note" );
                    if (record_stored != def) {
                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat sf = new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss a", Locale.getDefault());
                        String t = sf.format( c.getTime() );
                        index = new Notes( topic_note, note,t, context ).storeInfo(  );
                        if (index != -1) {
                            long id = dba.insertData( "New Note Stored",topic_note,"Yes","Yes" );
                            Toast.makeText( AddNoteDisplay.this, "Note Stored!", Toast.LENGTH_SHORT ).show();
                        } else {
                            long id = dba.insertData( "Error Occurred","New Note","Yes","No" );
                            Toast.makeText( context, "An Error occurred!", Toast.LENGTH_LONG ).show();
                        }
                    } else {
                        Toast.makeText( AddNoteDisplay.this, "Error in storing the data!", Toast.LENGTH_LONG ).show();
                    }

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

            final Fetch noteObj = new Fetch(AddNoteDisplay.this,"note",in);
            final String l = noteObj.getNoteData( "label" );
            final String n = noteObj.getNoteData( "notes" );
        final DatabaseAdapter dba = new DatabaseAdapter( AddNoteDisplay.this );
            topic_note_input.setText( l );
            note_input.setText( n );

            save = findViewById(R.id.savePass );
            save.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String li = topic_note_input.getText().toString();
                    String ni = note_input.getText().toString();

                    if (!li.isEmpty() && !ni.isEmpty()) {
                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat sf = new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss a", Locale.getDefault() );
                        String t = sf.format( c.getTime() );
                        noteObj.setNoteData( li, ni, t, t, "true" );
                        long id = dba.insertData( "Note Edit Saved",li,"Yes","Yes" );
                        Toast.makeText( AddNoteDisplay.this, "Saved!", Toast.LENGTH_SHORT ).show();
                    } else {
                        Toast.makeText( context, "One of the entry is empty!", Toast.LENGTH_SHORT ).show();
                    }
                }
            } );
    }

}
