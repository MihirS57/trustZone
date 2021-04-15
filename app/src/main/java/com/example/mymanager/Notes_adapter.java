package com.example.mymanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public class Notes_adapter extends RecyclerView.Adapter<Notes_adapter.NotesViewHolder>
{
    private Context context;
    private List<Notes> notesList;

    public Notes_adapter(Context c, List<Notes> nl) {
        this.context = c;
        this.notesList = nl;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.list_view,null);
        NotesViewHolder holder = new NotesViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Notes_adapter.NotesViewHolder holder, int position) {
        Notes notes = notesList.get(position);
        String label = notes.getLabel_note();
        String timeS = notes.getVisited_ts();
        holder.header.setText(label);
        holder.timeS.setText( timeS );
    }



    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public class NotesViewHolder extends RecyclerView.ViewHolder
    {
        TextView header,timeS;
        String index_key = "INDEX";
        String mode_key = "MODE";
        Context temp;
        public NotesViewHolder(@NonNull final View itemView) {
            super(itemView);

            header = itemView.findViewById(R.id.notes_pass);
            timeS = itemView.findViewById( R.id.label_notes_tstamp );
            temp = itemView.getContext();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Toast.makeText(context,"You pressed: "+(getAdapterPosition()+1),Toast.LENGTH_LONG).show();

                    int rec=-1,i=0,tem=0;
                    String t="";
                    Fetch recordObj = new Fetch( context,"records",0 );
                    rec = recordObj.getNotePassRecords( "note" );
                    t = recordObj.getNotePassIndex( "note" );
                    while( !(getAdapterPosition()+1 == tem) && tem<=rec){
                        if(t.charAt( i ) != '0'){
                            tem++;
                        }
                        i++;
                    }
                    Intent display = new Intent(temp,Displayer.class);
                    display.putExtra(index_key,i-1);
                    display.putExtra(mode_key,0);
                    temp.startActivity(display);
                }
            });

        }
    }

}
