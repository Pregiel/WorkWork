package pl.pregiel.workwork.data.database.services;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import pl.pregiel.workwork.data.database.DBHelper;
import pl.pregiel.workwork.data.database.tables.Works;
import pl.pregiel.workwork.data.pojo.Work;

public class WorkService {
    private DBHelper dbHelper;

    public WorkService(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void create(final Work work) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Works.Columns.WORK_TITLE, work.getTitle());
        contentValues.put(Works.Columns.WORK_CREATED, System.currentTimeMillis());
        contentValues.put(Works.Columns.WORK_TIME_MODE, work.getTimeMode());
        contentValues.put(Works.Columns.WORK_TIME_FROM, work.getTimeFrom());
        contentValues.put(Works.Columns.WORK_TIME_TO, work.getTimeTo());
        contentValues.put(Works.Columns.WORK_TIME_TO_NOW, work.getTimeToNow());
        contentValues.put(Works.Columns.WORK_TIME_AMOUNT, work.getTimeAmount());
        contentValues.put(Works.Columns.WORK_SALARY, work.getSalary());
        contentValues.put(Works.Columns.WORK_SALARY_MODE, work.getSalaryMode());
        contentValues.put(Works.Columns.WORK_CURRENCY, work.getCurrency());
        contentValues.put(Works.Columns.WORK_INFO, work.getInfo());


        dbHelper.getWritableDatabase().insert(Works.TABLE_NAME, null, contentValues);
    }

    public Work getById(final int id) {
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
                "SELECT * FROM " + Works.TABLE_NAME + " WHERE " + Works.Columns.WORK_ID + " = " + id,
                null
        );
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            return mapCursorToNote(cursor);
        }
        return null;
    }

    public List<Work> getAll() {
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
                "SELECT * FROM " + Works.TABLE_NAME,
                null
        );

        List<Work> results = new ArrayList<>();

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                results.add(mapCursorToNote(cursor));
            }
        }
        return results;
    }

    public void update(final Work work) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Works.Columns.WORK_TITLE, work.getTitle());
        contentValues.put(Works.Columns.WORK_CREATED, work.getCreated());
        contentValues.put(Works.Columns.WORK_TIME_MODE, work.getTimeMode());
        contentValues.put(Works.Columns.WORK_TIME_FROM, work.getTimeFrom());
        contentValues.put(Works.Columns.WORK_TIME_TO, work.getTimeTo());
        contentValues.put(Works.Columns.WORK_TIME_TO_NOW, work.getTimeToNow());
        contentValues.put(Works.Columns.WORK_TIME_AMOUNT, work.getTimeAmount());
        contentValues.put(Works.Columns.WORK_SALARY, work.getSalary());
        contentValues.put(Works.Columns.WORK_SALARY_MODE, work.getSalaryMode());
        contentValues.put(Works.Columns.WORK_CURRENCY, work.getCurrency());
        contentValues.put(Works.Columns.WORK_INFO, work.getInfo());

        dbHelper.getWritableDatabase().update(Works.TABLE_NAME,
                contentValues,
                " " + Works.Columns.WORK_ID + " = ? ",
                new String[]{work.getId().toString()}
        );
    }

    public void deleteById(final Integer id) {
        dbHelper.getWritableDatabase().delete(Works.TABLE_NAME,
                " " + Works.Columns.WORK_ID + " = ? ",
                new String[]{id.toString()});
    }

    private Work mapCursorToNote(final Cursor cursor) {
        int idColumnId = cursor.getColumnIndex(Works.Columns.WORK_ID);
        int titleColumnId = cursor.getColumnIndex(Works.Columns.WORK_TITLE);
        int createdColumnId = cursor.getColumnIndex(Works.Columns.WORK_CREATED);
        int timeModeColumnId = cursor.getColumnIndex(Works.Columns.WORK_TIME_MODE);
        int timeFromColumnId = cursor.getColumnIndex(Works.Columns.WORK_TIME_FROM);
        int timeToColumnId = cursor.getColumnIndex(Works.Columns.WORK_TIME_TO);
        int timeToNowColumnId = cursor.getColumnIndex(Works.Columns.WORK_TIME_TO_NOW);
        int timeAmountColumnId = cursor.getColumnIndex(Works.Columns.WORK_TIME_AMOUNT);
        int salaryColumnId = cursor.getColumnIndex(Works.Columns.WORK_SALARY);
        int salaryModeColumnId = cursor.getColumnIndex(Works.Columns.WORK_SALARY_MODE);
        int currencyColumnId = cursor.getColumnIndex(Works.Columns.WORK_CURRENCY);
        int infoColumnId = cursor.getColumnIndex(Works.Columns.WORK_INFO);

        Work work = new Work();
        work.setId(cursor.getInt(idColumnId));
        work.setTitle(cursor.getString(titleColumnId));
        work.setCreated(cursor.getString(createdColumnId));
        work.setTimeMode(cursor.getInt(timeModeColumnId));
        work.setTimeFrom(cursor.getInt(timeFromColumnId));
        work.setTimeTo(cursor.getInt(timeToColumnId));
        work.setTimeToNow(cursor.getInt(timeToNowColumnId));
        work.setTimeAmount(cursor.getInt(timeAmountColumnId));
        work.setSalary(cursor.getInt(salaryColumnId));
        work.setSalaryMode(cursor.getInt(salaryModeColumnId));
        work.setCurrency(cursor.getInt(currencyColumnId));
        work.setInfo(cursor.getString(infoColumnId));
        return work;
    }
}
