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

        Work work = new Work();
        work.setId(cursor.getInt(idColumnId));
        work.setTitle(cursor.getString(titleColumnId));
        return work;
    }
}
