package com.kamel.tivi.channels.favouritechannels;

import android.content.Context;

import com.kamel.tivi.channels.ChannelsModel;
import com.kamel.tivi.sqlitedatabase.DatabaseHelper;
import com.kamel.tivi.sqlitedatabase.DatabaseHelperHours;
import com.kamel.tivi.sqlitedatabase.DatabaseHelperSway;

import java.util.List;

public class FavouriteChannels {

    public static List<ChannelsModel> favList;
    public static DatabaseHelper databaseHelper=null;
    public static DatabaseHelperSway databaseHelperSway=null;
    public static DatabaseHelperHours databaseHelperHours=null;
     public static void initializeDatabase(Context context){

         databaseHelper=new DatabaseHelper(context);

         favList=databaseHelper.getAllChannel();


     }

    public static void initializeDatabaseSway(Context context){
        databaseHelperSway=new DatabaseHelperSway(context);
        favList=databaseHelperSway.getAllChannel();


    }

    public static void initializeDatabaseHours(Context context){
        databaseHelperHours=new DatabaseHelperHours(context);
        favList=databaseHelperHours.getAllChannel();


    }



}
