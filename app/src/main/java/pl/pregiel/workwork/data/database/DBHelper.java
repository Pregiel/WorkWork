package pl.pregiel.workwork.data.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import pl.pregiel.workwork.data.database.tables.WorkTimes;
import pl.pregiel.workwork.data.database.tables.Works;

public class DBHelper extends SQLiteOpenHelper {
    private final static int DB_VERSION = 2;
    private final static String DB_NAME = "WorkWork.db";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE "
                        + Works.TABLE_NAME + " ( "
                        + Works.Columns.WORK_ID + " INTEGER PRIMARY KEY, "
                        + Works.Columns.WORK_TITLE + " TEXT )"

        );
        db.execSQL(
                "CREATE TABLE "
                        + WorkTimes.TABLE_NAME + " ("
                        + WorkTimes.Columns.WORKTIME_ID + " INTEGER PRIMARY KEY, "
                        + WorkTimes.Columns.WORKTIME_TIME + "INTEGER, "
                        + WorkTimes.Columns.WORKTIME_TIME_FROM + "INTEGER, "
                        + WorkTimes.Columns.WORKTIME_TIME_TO + "INTEGER, "
                        + WorkTimes.Columns.WORKTIME_SALARY + "INTEGER, "
                        + WorkTimes.Columns.WORKTIME_NOTES + "TEXT, "
                        + WorkTimes.Columns.WORKTIME_WORK_ID + "INTEGER )"

        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Works.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + WorkTimes.TABLE_NAME);

        onCreate(db);
    }
}
