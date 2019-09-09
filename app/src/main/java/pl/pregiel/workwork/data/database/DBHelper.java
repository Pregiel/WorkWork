package pl.pregiel.workwork.data.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import pl.pregiel.workwork.data.database.tables.WorkTimes;
import pl.pregiel.workwork.data.database.tables.Works;

public class DBHelper extends SQLiteOpenHelper {
    private final static int DB_VERSION = 11;
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
                        + Works.Columns.WORK_TITLE + " TEXT, "
                        + Works.Columns.WORK_CREATED + " TEXT, "
                        + Works.Columns.WORK_TIME_MODE + " INTEGER, "
                        + Works.Columns.WORK_TIME_FROM + " INTEGER, "
                        + Works.Columns.WORK_TIME_TO + " INTEGER, "
                        + Works.Columns.WORK_TIME_TO_NOW + " INTEGER, "
                        + Works.Columns.WORK_TIME_AMOUNT + " INTEGER, "
                        + Works.Columns.WORK_SALARY + " INTEGER, "
                        + Works.Columns.WORK_SALARY_MODE + " INTEGER, "
                        + Works.Columns.WORK_CURRENCY + " INTEGER, "
                        + WorkTimes.Columns.WORKTIME_INFO + " TEXT )"

        );
        db.execSQL(
                "CREATE TABLE "
                        + WorkTimes.TABLE_NAME + " ("
                        + WorkTimes.Columns.WORKTIME_ID + " INTEGER PRIMARY KEY, "
                        + WorkTimes.Columns.WORKTIME_DAY + " TEXT, "
                        + WorkTimes.Columns.WORKTIME_TIME + " INTEGER, "
                        + WorkTimes.Columns.WORKTIME_TIME_FROM + " INTEGER, "
                        + WorkTimes.Columns.WORKTIME_TIME_TO + " INTEGER, "
                        + WorkTimes.Columns.WORKTIME_SALARY + " INTEGER, "
                        + WorkTimes.Columns.WORKTIME_SALARY_MODE + " INTEGER, "
                        + WorkTimes.Columns.WORKTIME_CURRENCY + " INTEGER, "
                        + WorkTimes.Columns.WORKTIME_INFO + " TEXT, "
                        + WorkTimes.Columns.WORKTIME_WORK_ID + " INTEGER )"

        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Works.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + WorkTimes.TABLE_NAME);

        onCreate(db);
    }
}
