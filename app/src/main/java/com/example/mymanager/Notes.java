package com.example.mymanager;

import android.content.Context;

import android.widget.Toast;



public class Notes
{
    int id;
    String label_note;
    String note, visited_ts;
    String tst;
    Context context;

    public String getVisited_ts() {
        return visited_ts;
    }

    public Notes(int i, String l)
    {
        this.id = i;
        this.label_note = l;
    }

    public Notes(String l,String n,Context cont)
    {
        this.label_note = l;
        this.note = n;
        this.context = cont;
    }

    public Notes(String label_note, String note, String tst, Context context) {
        this.label_note = label_note;
        this.note = note;
        this.tst = tst;
        this.context = context;
    }

    public Notes(int id,String label_note, String visited_ts) {
        this.id = id;
        this.label_note = label_note;
        this.visited_ts = visited_ts;
    }

    public int getId() {
        return id;
    }

    public String getLabel_note() {
        return label_note;
    }

    public String getNote() {
        return note;
    }

    public int storeInfo(){
        int m,nor;
        String t;
        Fetch fetch = new Fetch(context,"records",0);
        nor = fetch.getNotePassRecords( "note" );
        t = fetch.getNotePassIndex( "note" );
        if(nor != 10){
            for(m=0;m<10;m++){
                if(t.charAt( m ) == '0'){
                    break;
                }
            }
            StringToArray sta = new StringToArray();
            t = sta.convertAndAdd( t,m,1 );
            fetch.setNotePassIndex( t,"note" );
            Fetch getnote = new Fetch( context,"note",m );
            getnote.setNoteData( label_note,note,tst,tst,"true" );
            fetch.setNotePassRecords( "note", nor+1);
            return nor+1;
        } else{
            Toast.makeText( context, "Overflow!", Toast.LENGTH_SHORT ).show();
            return -1;
        }
    }

}
