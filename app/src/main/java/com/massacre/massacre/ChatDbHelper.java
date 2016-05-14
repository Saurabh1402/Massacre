package com.massacre.massacre;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by saurabh on 5/5/16.
 */
public class ChatDbHelper extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "SQLiteChat.db";
    private static final int DATABASE_VERSION = 1;
    public static final String MESSAGE_TABLE_NAME = "chatmessage";
    public static final String MESSAGE_COLUMN_ID = "_id";
    public static final String MESSAGE_COLUMN_RECIPIENT = "RECIPIENT";
    public static final String MESSAGE_COLUMN_SEND_OR_RECEIVED="send_or_received";//pending =>1 send =2 recieved = 3,  read or downloaded=3(in case of audio video and pic)
    public static final String MESSAGE_COLUMN_TYPE = "type"; //1=> text ,,2=> photo 3=>video 4=>audio
    public static final String MESSAGE_COLUMN_DATE = "date";
    public static final String MESSAGE_COLUMN_MESSAGE="message";
    public static final int PENDING_MESSAGE=1;
    public static final int SEND_MESSAGE=2;
    public static final int RECEIVED_MESSAGE=3;
    public static final int READ_MESSAGE=4;
    public static final int TEXT_MESSAGE=1;
    public static final int PHOTO_MESSAGE=2;
    public static final int VIDEO_MESSAGE=3;
    public static final int AUDIO_MESSAGE=4;


    public ChatDbHelper(Context context){
        super(context,DATABASE_NAME,null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // ID, MESSAGE, RECIPIENT, DATE, SEND OR RECEIVED, TYPE
        db.execSQL("CREATE TABLE "+MESSAGE_TABLE_NAME+"("+
                MESSAGE_COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                MESSAGE_COLUMN_MESSAGE+" TEXT, "+
                MESSAGE_COLUMN_RECIPIENT+" TEXT, "+
                MESSAGE_COLUMN_DATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "+
                MESSAGE_COLUMN_SEND_OR_RECEIVED+ " INTEGER, "+
                MESSAGE_COLUMN_TYPE+" INTEGER)"
        );

    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //db.execSQL("DROP TABLE IF EXISTS " + PERSON_TABLE_NAME);
        //onCreate(db);

    }
    private String getDateTime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(date);
    }
    public boolean insertMessage(String message,String recipient,Date date, int send_or_received,int type) {
        // ID, MESSAGE, RECIPIENT, DATE, SEND OR RECEIVED, TYPE
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MESSAGE_COLUMN_MESSAGE, message);
        contentValues.put(MESSAGE_COLUMN_RECIPIENT, recipient);
        contentValues.put(MESSAGE_COLUMN_DATE, getDateTime(date));
        contentValues.put(MESSAGE_COLUMN_SEND_OR_RECEIVED,send_or_received);
        contentValues.put(MESSAGE_COLUMN_TYPE, type);
        db.insert(MESSAGE_TABLE_NAME, null, contentValues);
        return true;
    }
    public boolean updateMessage(int id,String message,String recipient,Date date, int send_or_received,int type){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MESSAGE_COLUMN_MESSAGE, message);
        contentValues.put(MESSAGE_COLUMN_RECIPIENT, recipient);
        contentValues.put(MESSAGE_COLUMN_DATE, getDateTime(date));
        contentValues.put(MESSAGE_COLUMN_SEND_OR_RECEIVED,send_or_received);
        contentValues.put(MESSAGE_COLUMN_TYPE, type);
        db.update(MESSAGE_TABLE_NAME, contentValues, MESSAGE_COLUMN_ID + " = ? ", new String[] { Integer.toString(id) } );
        return true;
    }
    public Cursor getMessageWithId(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + MESSAGE_TABLE_NAME + " WHERE " +
                MESSAGE_COLUMN_ID + "=?", new String[] { Integer.toString(id) } );
        return res;
    }
    public Cursor getPendingMessages(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("SELECT * FROM "+MESSAGE_TABLE_NAME + " WHERE " +
                MESSAGE_COLUMN_SEND_OR_RECEIVED+" = ? ",new String[]{Integer.toString(ChatDbHelper.PENDING_MESSAGE)}
        );
        return res;
    }
    public Cursor getMessageofPhone(String recipient){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("SELECT * FROM " + MESSAGE_TABLE_NAME +" WHERE "+
            MESSAGE_COLUMN_RECIPIENT+" = ? ",new String[]{recipient}
        );
        return res;
    }
    public Cursor getAllMessage() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + MESSAGE_TABLE_NAME, null );
        return res;
    }
    public Integer deleteMessage(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(MESSAGE_TABLE_NAME,
                MESSAGE_COLUMN_ID + " = ? ",
                new String[] { Integer.toString(id) });
    }

    public Integer deleteAllMessages() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(MESSAGE_TABLE_NAME,null,null);
    }
    public Integer deleteMessageofPhone(String phoneNumber){
        SQLiteDatabase db=this.getWritableDatabase();
        return db.delete(MESSAGE_TABLE_NAME,MESSAGE_COLUMN_RECIPIENT+" = ?",new String[]{phoneNumber});
    }
}
