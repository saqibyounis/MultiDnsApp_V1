package com.kamel.tivi.sqlitedatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kamel.tivi.channels.ChannelsModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "trustmeji";
    private static final String TABLE_FAVOURITE = "channelfavourite";
    private static final String CHANNEL_NAME = "channel_name";
    private static final String CHANNEL_URL = "channel_url";
    private static final String CHANNEL_ICON= "channel_icon";
    private static final String CHANNEL_StreamID="channel_streamid";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instanc;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_FAVOURITE + "("
                + CHANNEL_NAME + " TEXT," + CHANNEL_URL+ " TEXT,"
                + CHANNEL_ICON+ " TEXT," +CHANNEL_StreamID+" TEXT )";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVOURITE);

        // Create tables again
        onCreate(db);
    }

public  void addChannel(ChannelsModel channel) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CHANNEL_NAME, channel.getName());
        values.put(CHANNEL_ICON, channel.getLogo());
        values.put(CHANNEL_URL, channel.getLink());
        values.put(CHANNEL_StreamID,channel.getStreamId());
      //    values.put(CHANNEL_GROUP,channel.getGroup());

        // Inserting Row
        db.insert(TABLE_FAVOURITE, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

  public   ChannelsModel getChannel(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_FAVOURITE, new String[] { CHANNEL_NAME,
                        CHANNEL_ICON,CHANNEL_URL,CHANNEL_StreamID }, CHANNEL_NAME + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
/*
        ChannelsModel m3UItem= new ChannelsModel(cursor.getString(0),
                cursor.getString(1), cursor.getString(2),cursor.getString(3));
        // return contact
      */
      ChannelsModel model=new ChannelsModel();
      model.setName(cursor.getString(0));
      model.setLogo(cursor.getString(1));
      model.setLink(cursor.getString(2));
      model.setStreamId(cursor.getString(3));
        return model;
    }

    public List<ChannelsModel> getAllChannel() {
        List<ChannelsModel> channelList = new ArrayList<ChannelsModel>();
        String selectQuery = "SELECT  * FROM " + TABLE_FAVOURITE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ChannelsModel channel = new ChannelsModel();
               channel.setName(cursor.getString(0));
               channel.setLogo(cursor.getString(1));
              channel.setLogo(cursor.getString(2));
              channel.setStreamId(cursor.getString(3));
              //    channel.setGroup(cursor.getString(3));

                channelList.add(channel);
            } while (cursor.moveToNext());
        }

        return channelList;
    }

    public int updateChannels(ChannelsModel channel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CHANNEL_NAME, channel.getName());
        values.put(CHANNEL_ICON, channel.getLogo());
        values.put(CHANNEL_URL, channel.getLink());
      //  values.put(CHANNEL_GROUP,channel.getGroup());
         values.put(CHANNEL_StreamID,channel.streamId);
        return db.update(TABLE_FAVOURITE, values, CHANNEL_NAME + " = ?",
                new String[] { String.valueOf(channel.getName()) });
    }

    public void deletechannel(ChannelsModel channel) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVOURITE, CHANNEL_NAME + " = ?",
                new String[] { String.valueOf(channel.getName()) });
        db.close();
    }

    public int getChannelCounts() {
        String countQuery = "SELECT  * FROM " + TABLE_FAVOURITE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}
