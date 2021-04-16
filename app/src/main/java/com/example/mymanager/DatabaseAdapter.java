package com.example.mymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DatabaseAdapter {
    DatabaseHelper dbh;

    public DatabaseAdapter(Context context)
    {
        dbh = new DatabaseHelper(context);
    }

    public long insertData(String type, String label,String visited,String edited)
    {
        SQLiteDatabase dbb = dbh.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sf = new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss a", Locale.getDefault());
        String t = sf.format( c.getTime() );
        contentValues.put(DatabaseHelper.DATE_TIME, t);
        contentValues.put(DatabaseHelper.TYPE, type);
        contentValues.put(DatabaseHelper.LABEL, label);
        contentValues.put(DatabaseHelper.VISITED, visited);
        contentValues.put(DatabaseHelper.EDITED, edited);

        long id = dbb.insert(DatabaseHelper.TABLE_NAME, null , contentValues);
        return id;
    }

    public String getData()
    {
        SQLiteDatabase db = dbh.getWritableDatabase();
        String[] columns = {DatabaseHelper.EID,DatabaseHelper.DATE_TIME,DatabaseHelper.TYPE,DatabaseHelper.LABEL,DatabaseHelper.VISITED,DatabaseHelper.EDITED};
        Cursor cursor =db.query(DatabaseHelper.TABLE_NAME,columns,null,null,null,null,null);
        StringBuffer buffer= new StringBuffer();
        while (cursor.moveToNext())
        {
            int eid =cursor.getInt(cursor.getColumnIndex(DatabaseHelper.EID));
            String date =cursor.getString(cursor.getColumnIndex(DatabaseHelper.DATE_TIME));
            String type =cursor.getString(cursor.getColumnIndex(DatabaseHelper.TYPE));
            String label =cursor.getString(cursor.getColumnIndex(DatabaseHelper.LABEL));
            String visited =cursor.getString(cursor.getColumnIndex(DatabaseHelper.VISITED));
            String edited =cursor.getString(cursor.getColumnIndex(DatabaseHelper.EDITED));
            buffer.append(eid+ "   " + date + "   " + type +"   "+label+"   "+visited+"   "+edited+" \n");
        }
        return buffer.toString();
    }

    public  int delete(String label)
    {
        SQLiteDatabase db = dbh.getWritableDatabase();
        String[] whereArgs ={label};

        int count =db.delete(DatabaseHelper.TABLE_NAME ,DatabaseHelper.LABEL+" = ?",whereArgs);
        
        return  count;
    }


/*
    public int updateName(String oldName , String newName)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.NAME,newName);
        String[] whereArgs= {oldName};
        int count =db.update(myDbHelper.TABLE_NAME,contentValues, myDbHelper.NAME+" = ?",whereArgs );
        return count;
    }

     */


    static class DatabaseHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "TrustZoneDatabase";
        private static final String TABLE_NAME = "TrustZoneTable";
        private static final int DATABASE_Version = 1;
        private static final String EID="id";
        private static final String DATE_TIME = "Date_Time";
        private static final String TYPE= "Type";
        private static final String LABEL = "Label";
        private static final String EDITED= "Edited";
        private static final String VISITED= "Visited";
        private static final String CREATE_TABLE = "CREATE TABLE  IF NOT EXISTS "+TABLE_NAME+
                " ("+EID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+DATE_TIME+" VARCHAR(255) ,"+ TYPE+" VARCHAR(255) ,"+LABEL+" VARCHAR(255) ,"+EDITED+" VARCHAR(255) ,"
            +VISITED+" VARCHAR(255));";
        private static final String DROP_TABLE ="DROP TABLE IF EXISTS "+TABLE_NAME;
        private Context context;

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_Version);
            this.context=context;
        }
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL( CREATE_TABLE );
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL(DROP_TABLE);
            onCreate(sqLiteDatabase);
        }
    }
}
