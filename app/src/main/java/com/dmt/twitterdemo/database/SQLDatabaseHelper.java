package com.dmt.twitterdemo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by ouwechue on 3/20/17.
 */

public class SQLDatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "SQLDatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "TweetsHistory";

    // Table Names
    public static final String TABLE_HASHTAGS = "hashtags_history";
    public static final String TABLE_TWEETDATA = "tweet_data";

    // Common column names
    public static final String KEY_ID = "id";

    // HashtagHistory Table - column nmaes
    public static final String KEY_HASHTAG = "Hashtag";
    public static final String KEY_DATE_ENTERED = "Date_entered";

    // TweetData Table - column names
    public static final String KEY_USER_REAL_NAME = "User_real_name";
    public static final String KEY_USER_TWITTER_NAME = "User_twitter_name";
    public static final String KEY_TWEET_STRING = "Tweet_string";
    public static final String KEY_USER_PIC_URL = "User_pic_url";
    public static final String KEY_HASHTAG_ID = "Hashtag_id";

    // Table Create Statements
    private static final String CREATE_TABLE_HASHTAGS = "CREATE TABLE IF NOT EXISTS "
            + TABLE_HASHTAGS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_HASHTAG  + " TEXT,"
            + KEY_DATE_ENTERED + " DATETIME DEFAULT CURRENT_TIMESTAMP"
            + ")";
    private static final String CREATE_TABLE_TWEETDATA = "CREATE TABLE IF NOT EXISTS "
            + TABLE_TWEETDATA + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_HASHTAG_ID + " INTEGER,"
            + KEY_USER_REAL_NAME + " TEXT,"
            + KEY_USER_TWITTER_NAME + " TEXT,"
            + KEY_TWEET_STRING + " TEXT,"
            + KEY_USER_PIC_URL + " TEXT,"
            + "FOREIGN KEY("+KEY_HASHTAG_ID+") REFERENCES "+TABLE_HASHTAGS+"("+KEY_ID+")"
            + ")";


    public SQLDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_HASHTAGS);
        db.execSQL(CREATE_TABLE_TWEETDATA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HASHTAGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TWEETDATA);

        // create new tables
        onCreate(db);
    }

}
