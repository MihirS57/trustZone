package com.example.mymanager;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Settings_adapter extends RecyclerView.Adapter<Settings_adapter.SettingsViewHolder> {
    Context context;
    List<Setting> settList;

    public Settings_adapter(Context cont, List<Setting> sl) {
        this.context = cont;
        this.settList = sl;
    }

    @NonNull
    @Override
    public SettingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from( context );
        View view = inflater.inflate( R.layout.settings_list_view, null );
        Settings_adapter.SettingsViewHolder holder = new Settings_adapter.SettingsViewHolder( view );
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull SettingsViewHolder holder, int position) {
        Setting sett = settList.get( position );
        String l = sett.getL();
        String s = sett.getS();

        holder.sett_label.setText( l );
        holder.subtitle.setText( s );


    }

    @Override
    public int getItemCount() {
        return settList.size();
    }

    public class SettingsViewHolder extends RecyclerView.ViewHolder {
        TextView sett_label, subtitle;

        public SettingsViewHolder(final View itemView) {
            super( itemView );

            sett_label = itemView.findViewById( R.id.item_list );
            subtitle = itemView.findViewById( R.id.item_disc );


            itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getAdapterPosition() == 0){
                        Intent record = new Intent(context,RecordStored.class);
                        context.startActivity( record );
                    } else if(getAdapterPosition() == 1){
                        Intent logs = new Intent(context,SystemLogs.class);
                        context.startActivity( logs );
                    }
                    else if(getAdapterPosition() == 3){
                        Intent new_pass = new Intent(context,PasswordReset.class);
                        context.startActivity( new_pass );
                    }
                }
            } );
        }
    }
}
