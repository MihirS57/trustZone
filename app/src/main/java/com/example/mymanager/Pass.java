package com.example.mymanager;

import android.content.Context;
import android.widget.Toast;


public class Pass
{
    String pass_label,emailA,passwrd,ts;
    Context context;
    int id;

    /*String records = "nRecord";     //sp for records
    String record_key = "nKey";     // no of password records
    String record_index = "iKey";   //index of each password


    String register[] = new String[10];
    String pass_key = "Signal";
    String identity = "Signal_classifier";
    String identity_label = "Label";
    String e_tst_label = "eStamp";
    String v_tst_label = "vStamp";
    String occupy = "engage";

    String defa = "-1";
    int def=-1;
    int record_stored;*/


    public Pass( int ind, String pass_label, String emailA, String tst) {
        this.id = ind;
        this.pass_label = pass_label;
        this.emailA = emailA;
        this.ts = tst;
    }

    public Pass(String pass_label, String emailA, String passwrd,String tst, Context context) {
        this.pass_label = pass_label;
        this.emailA = emailA;
        this.passwrd = passwrd;
        this.ts = tst;
        this.context = context;
    }

    public int storeInfo()
    {
        int m,nor;
        String t;
        Fetch fetch = new Fetch(context,"records",0);
        nor = fetch.getNotePassRecords( "password" );
        t = fetch.getNotePassIndex( "password" );
        if(nor != 10){
                for(m=0;m<10;m++){
                    if(t.charAt( m ) == '0'){
                        break;
                    }
                }
                StringToArray sta = new StringToArray();
                t = sta.convertAndAdd( t,m,1 );
                fetch.setNotePassIndex( t,"password" );
                Fetch getpass = new Fetch( context,"password",m );
                getpass.setPasswordData( pass_label,emailA,passwrd,ts,ts,"true" );
                fetch.setNotePassRecords( "password", nor+1);
                return nor+1;
        } else{
            Toast.makeText( context, "Overflow!", Toast.LENGTH_SHORT ).show();
            return -1;
        }
    }
    public int getId() {
        return id;
    }
    public String getTs(){ return ts; }

    public String getEmailA()
    {
        return emailA;
    }
    public String getPass_label()
    {
        return pass_label;
    }
}
