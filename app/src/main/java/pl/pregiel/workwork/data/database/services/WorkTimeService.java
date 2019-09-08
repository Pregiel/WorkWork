package pl.pregiel.workwork.data.database.services;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import pl.pregiel.workwork.data.database.DBHelper;
import pl.pregiel.workwork.data.database.tables.WorkTimes;
import pl.pregiel.workwork.data.pojo.WorkTime;

public class WorkTimeService {
    private DBHelper dbHelper;

    public WorkTimeService(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void create(final WorkTime workTime) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(WorkTimes.Columns.WORKTIME_DAY, workTime.getDay());
        contentValues.put(WorkTimes.Columns.WORKTIME_TIME, workTime.getTime());
        contentValues.put(WorkTimes.Columns.WORKTIME_TIME_FROM, workTime.getTimeFrom());
        contentValues.put(WorkTimes.Columns.WORKTIME_TIME_TO, workTime.getTimeTo());
        contentValues.put(WorkTimes.Columns.WORKTIME_SALARY, workTime.getSalary());
        contentValues.put(WorkTimes.Columns.WORKTIME_SALARY_MODE, workTime.getSalaryMode());
        contentValues.put(WorkTimes.Columns.WORKTIME_NOTES, workTime.getNotes());
        contentValues.put(WorkTimes.Columns.WORKTIME_WORK_ID, workTime.getWorkId());

        dbHelper.getWritableDatabase().insert(WorkTimes.TABLE_NAME, null, contentValues);
    }

    public WorkTime getById(final int id) {
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
                "SELECT * FROM " + WorkTimes.TABLE_NAME
                        + " WHERE " + WorkTimes.Columns.WORKTIME_ID + " = " + id,
                null
        );
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            return mapCursorToNote(cursor);
        }
        return null;
    }

    public List<WorkTime> getByWorkId(final int workId) {
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
                "SELECT * FROM " + WorkTimes.TABLE_NAME
                + " WHERE " + WorkTimes.Columns.WORKTIME_WORK_ID + " = " + workId,
                null
        );

        List<WorkTime> results = new ArrayList<>();

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                results.add(mapCursorToNote(cursor));
            }
        }
        return results;
    }

    public List<WorkTime> getAll() {
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
                "SELECT * FROM " + WorkTimes.TABLE_NAME,
                null
        );

        List<WorkTime> results = new ArrayList<>();

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                results.add(mapCursorToNote(cursor));
            }
        }
        return results;
    }

    public void update(final WorkTime workTime) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(WorkTimes.Columns.WORKTIME_DAY, workTime.getDay());
        contentValues.put(WorkTimes.Columns.WORKTIME_TIME, workTime.getTime());
        contentValues.put(WorkTimes.Columns.WORKTIME_TIME_FROM, workTime.getTimeFrom());
        contentValues.put(WorkTimes.Columns.WORKTIME_TIME_TO, workTime.getTimeTo());
        contentValues.put(WorkTimes.Columns.WORKTIME_SALARY, workTime.getSalary());
        contentValues.put(WorkTimes.Columns.WORKTIME_SALARY_MODE, workTime.getSalaryMode());
        contentValues.put(WorkTimes.Columns.WORKTIME_NOTES, workTime.getNotes());
        contentValues.put(WorkTimes.Columns.WORKTIME_WORK_ID, workTime.getWorkId());

        dbHelper.getWritableDatabase().update(WorkTimes.TABLE_NAME,
                contentValues,
                " " + WorkTimes.Columns.WORKTIME_ID + " = ? ",
                new String[]{workTime.getId().toString()}
        );
    }

    public void deleteById(final Integer id) {
        dbHelper.getWritableDatabase().delete(WorkTimes.TABLE_NAME,
                " " + WorkTimes.Columns.WORKTIME_ID + " = ? ",
                new String[]{id.toString()});
    }

    private WorkTime mapCursorToNote(final Cursor cursor) {
        int idColumnId = cursor.getColumnIndex(WorkTimes.Columns.WORKTIME_ID);
        int dayColumnId = cursor.getColumnIndex(WorkTimes.Columns.WORKTIME_DAY);
        int timeColumnId = cursor.getColumnIndex(WorkTimes.Columns.WORKTIME_TIME);
        int timeFromColumnId = cursor.getColumnIndex(WorkTimes.Columns.WORKTIME_TIME_FROM);
        int timeToColumnId = cursor.getColumnIndex(WorkTimes.Columns.WORKTIME_TIME_TO);
        int salaryColumnId = cursor.getColumnIndex(WorkTimes.Columns.WORKTIME_SALARY);
        int salaryModeColumnId = cursor.getColumnIndex(WorkTimes.Columns.WORKTIME_SALARY_MODE);
        int notesColumnId = cursor.getColumnIndex(WorkTimes.Columns.WORKTIME_NOTES);
        int workIdColumnId = cursor.getColumnIndex(WorkTimes.Columns.WORKTIME_WORK_ID);

        WorkTime workTime = new WorkTime();
        workTime.setId(cursor.getInt(idColumnId));
        workTime.setDay(cursor.getString(dayColumnId));
        workTime.setTime(cursor.getInt(timeColumnId));
        workTime.setTimeFrom(cursor.getInt(timeFromColumnId));
        workTime.setTimeTo(cursor.getInt(timeToColumnId));
        workTime.setSalary(cursor.getInt(salaryColumnId));
        workTime.setSalaryMode(cursor.getInt(salaryModeColumnId));
        workTime.setNotes(cursor.getString(notesColumnId));
        workTime.setWorkId(cursor.getInt(workIdColumnId));
        return workTime;
    }
}
