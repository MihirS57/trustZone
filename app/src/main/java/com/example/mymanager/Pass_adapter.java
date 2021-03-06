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

import org.w3c.dom.Text;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public class Pass_adapter extends RecyclerView.Adapter<Pass_adapter.PassViewHolder>
{

    Context context;
    List<Pass> passList;
    Context temp;
    int rc;
    String[] title,title_un,date;


    public Pass_adapter(Context context, List<Pass> passList) {
        this.context = context;
        this.passList = passList;
    }

    public Pass_adapter(Context context, String[] title,String[] title_un, String[] date,int rc){
        this.context = context;
        this.title = title;
        this.title_un = title_un;
        this.date = date;
        this.rc = rc;
    }

    @NonNull
    @Override
    public PassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.pass_list_view,null);
        PassViewHolder holder = new PassViewHolder(view);
        return holder;
    }

    /*@Override
    public void onBindViewHolder(@NonNull PassViewHolder holder, int position) {
        Pass pass = passList.get(position);
        String e = pass.getEmailA();
        String l = pass.getPass_label();
        String ts = pass.getTs();
        int i = pass.getId();
        holder.pass_l.setText(l);
        holder.email_l.setText(e);
        holder.tstamp.setText( ts );
    }*/

    @Override
    public void onBindViewHolder(@NonNull PassViewHolder holder, int position) {
        holder.pass_l.setText(title[position]);
        holder.email_l.setText(title_un[position]);
        holder.tstamp.setText(date[position]);
    }

    /*@Override
    public int getItemCount() {
        return passList.size();
    }*/
    @Override
    public int getItemCount() {
        return rc;
    }

    public class PassViewHolder extends RecyclerView.ViewHolder
    {
        TextView pass_l;
        TextView email_l;
        TextView tstamp;
        String index_key = "INDEX";
        String mode_key = "MODE";

        public PassViewHolder(@NonNull View itemView) {
            super(itemView);

            pass_l = itemView.findViewById(R.id.label_setting );
            email_l = itemView.findViewById(R.id.label_subtitle );
            tstamp = itemView.findViewById( R.id.label_tstamp );
            temp = itemView.getContext();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context,"You pressed: "+(getAdapterPosition()+1),Toast.LENGTH_LONG).show();
                    int rec=-1,i=0,tem=0;
                    String t="";
                    Fetch recordObj = new Fetch( context,"records",0 );
                    rec = recordObj.getNotePassRecords( "password" );
                    t = recordObj.getNotePassIndex( "password" );
                    while( !(getAdapterPosition()+1 == tem) && tem<=rec){
                        if(t.charAt( i ) != '0'){
                            tem++;
                        }
                        i++;
                    }
                    Intent display = new Intent(temp,Displayer.class);
                    display.putExtra(index_key,i-1);
                    display.putExtra(mode_key,1);
                    temp.startActivity(display);

                }
            });

        }

    }
}
