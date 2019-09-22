package pl.pregiel.workwork.data.database.services


import android.content.ContentValues
import android.content.Context
import android.database.Cursor

import java.util.ArrayList

import pl.pregiel.workwork.data.database.DBHelper
import pl.pregiel.workwork.data.database.tables.WorkTimes
import pl.pregiel.workwork.data.database.tables.Works
import pl.pregiel.workwork.data.pojo.Work

class WorkService(context: Context) {
    private val dbHelper: DBHelper = DBHelper(context)

    fun create(work: Work) {
        val contentValues = ContentValues()
        contentValues.put(Works.Columns.WORK_TITLE, work.title)
        contentValues.put(Works.Columns.WORK_CREATED, System.currentTimeMillis())
        contentValues.put(Works.Columns.WORK_TIME_MODE, work.timeMode)
        contentValues.put(Works.Columns.WORK_TIME_FROM, work.timeFrom)
        contentValues.put(Works.Columns.WORK_TIME_TO, work.timeTo)
        contentValues.put(Works.Columns.WORK_TIME_TO_NOW, work.timeToNow)
        contentValues.put(Works.Columns.WORK_TIME_AMOUNT, work.timeAmount)
        contentValues.put(Works.Columns.WORK_SALARY, work.salary)
        contentValues.put(Works.Columns.WORK_SALARY_MODE, work.salaryMode)
        contentValues.put(Works.Columns.WORK_CURRENCY, work.currency)
        contentValues.put(Works.Columns.WORK_INFO, work.info)


        dbHelper.writableDatabase.insert(Works.TABLE_NAME, null, contentValues)
    }

    fun getById(id: Int): Work? {
        val cursor = dbHelper.readableDatabase.rawQuery(
                "SELECT * FROM  ${Works.TABLE_NAME} WHERE ${Works.Columns.WORK_ID} = $id",
                null
        )
        if (cursor.count == 1) {
            cursor.moveToFirst()
            return mapCursorToNote(cursor)
        }
        return null
    }

    fun getAll(): List<Work> {
        val cursor = dbHelper.readableDatabase.rawQuery(
                "SELECT * FROM ${Works.TABLE_NAME}",
                null
        )

        val results = ArrayList<Work>()

        if (cursor.count > 0) {
            while (cursor.moveToNext()) {
                results.add(mapCursorToNote(cursor))
            }
        }
        return results
    }

    fun update(work: Work) {
        val contentValues = ContentValues()
        contentValues.put(Works.Columns.WORK_TITLE, work.title)
        contentValues.put(Works.Columns.WORK_CREATED, work.created)
        contentValues.put(Works.Columns.WORK_TIME_MODE, work.timeMode)
        contentValues.put(Works.Columns.WORK_TIME_FROM, work.timeFrom)
        contentValues.put(Works.Columns.WORK_TIME_TO, work.timeTo)
        contentValues.put(Works.Columns.WORK_TIME_TO_NOW, work.timeToNow)
        contentValues.put(Works.Columns.WORK_TIME_AMOUNT, work.timeAmount)
        contentValues.put(Works.Columns.WORK_SALARY, work.salary)
        contentValues.put(Works.Columns.WORK_SALARY_MODE, work.salaryMode)
        contentValues.put(Works.Columns.WORK_CURRENCY, work.currency)
        contentValues.put(Works.Columns.WORK_INFO, work.info)

        dbHelper.writableDatabase.update(Works.TABLE_NAME,
                contentValues,
                " ${Works.Columns.WORK_ID} = ? ",
                arrayOf(work.id.toString())
        )
    }

    fun deleteById(id: Int) {
        dbHelper.writableDatabase.delete(WorkTimes.TABLE_NAME,
                " ${WorkTimes.Columns.WORKTIME_WORK_ID} = ? ",
                arrayOf(id.toString()))

        dbHelper.writableDatabase.delete(Works.TABLE_NAME,
                " ${Works.Columns.WORK_ID} = ? ",
                arrayOf(id.toString()))
    }

    private fun mapCursorToNote(cursor: Cursor): Work {
        val idColumnId = cursor.getColumnIndex(Works.Columns.WORK_ID)
        val titleColumnId = cursor.getColumnIndex(Works.Columns.WORK_TITLE)
        val createdColumnId = cursor.getColumnIndex(Works.Columns.WORK_CREATED)
        val timeModeColumnId = cursor.getColumnIndex(Works.Columns.WORK_TIME_MODE)
        val timeFromColumnId = cursor.getColumnIndex(Works.Columns.WORK_TIME_FROM)
        val timeToColumnId = cursor.getColumnIndex(Works.Columns.WORK_TIME_TO)
        val timeToNowColumnId = cursor.getColumnIndex(Works.Columns.WORK_TIME_TO_NOW)
        val timeAmountColumnId = cursor.getColumnIndex(Works.Columns.WORK_TIME_AMOUNT)
        val salaryColumnId = cursor.getColumnIndex(Works.Columns.WORK_SALARY)
        val salaryModeColumnId = cursor.getColumnIndex(Works.Columns.WORK_SALARY_MODE)
        val currencyColumnId = cursor.getColumnIndex(Works.Columns.WORK_CURRENCY)
        val infoColumnId = cursor.getColumnIndex(Works.Columns.WORK_INFO)

        return Work(
                id = cursor.getInt(idColumnId),
                title = cursor.getString(titleColumnId),
                created = cursor.getString(createdColumnId),
                timeMode = cursor.getInt(timeModeColumnId),
                timeFrom = cursor.getInt(timeFromColumnId),
                timeTo = cursor.getInt(timeToColumnId),
                timeToNow = cursor.getInt(timeToNowColumnId),
                timeAmount = cursor.getInt(timeAmountColumnId),
                salary = cursor.getInt(salaryColumnId),
                salaryMode = cursor.getInt(salaryModeColumnId),
                currency = cursor.getInt(currencyColumnId),
                info = cursor.getString(infoColumnId)
        )
    }
}
