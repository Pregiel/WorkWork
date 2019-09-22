package pl.pregiel.workwork.data.database.services


import android.content.ContentValues
import android.content.Context
import android.database.Cursor

import java.util.ArrayList

import pl.pregiel.workwork.data.database.DBHelper
import pl.pregiel.workwork.data.database.tables.WorkTimes
import pl.pregiel.workwork.data.pojo.WorkTime

class WorkTimeService(context: Context) {
    private val dbHelper: DBHelper = DBHelper(context)

    fun getAll(): List<WorkTime> {
        val cursor = dbHelper.readableDatabase.rawQuery(
                "SELECT * FROM ${WorkTimes.TABLE_NAME}",
                null
        )

        val results = ArrayList<WorkTime>()

        if (cursor.count > 0) {
            while (cursor.moveToNext()) {
                results.add(mapCursorToNote(cursor))
            }
        }
        return results
    }

    fun create(workTime: WorkTime) {
        val contentValues = ContentValues()
        contentValues.put(WorkTimes.Columns.WORKTIME_DAY, workTime.day)
        contentValues.put(WorkTimes.Columns.WORKTIME_TIME, workTime.time)
        contentValues.put(WorkTimes.Columns.WORKTIME_TIME_FROM, workTime.timeFrom)
        contentValues.put(WorkTimes.Columns.WORKTIME_TIME_TO, workTime.timeTo)
        contentValues.put(WorkTimes.Columns.WORKTIME_SALARY, workTime.salary)
        contentValues.put(WorkTimes.Columns.WORKTIME_SALARY_MODE, workTime.salaryMode)
        contentValues.put(WorkTimes.Columns.WORKTIME_INFO, workTime.info)
        contentValues.put(WorkTimes.Columns.WORKTIME_WORK_ID, workTime.workId)

        dbHelper.writableDatabase.insert(WorkTimes.TABLE_NAME, null, contentValues)
    }

    fun getById(id: Int): WorkTime? {
        val cursor = dbHelper.readableDatabase.rawQuery(
                "SELECT * FROM ${WorkTimes.TABLE_NAME} WHERE ${WorkTimes.Columns.WORKTIME_ID} = $id",
                null
        )
        if (cursor.count == 1) {
            cursor.moveToFirst()
            return mapCursorToNote(cursor)
        }
        return null
    }

    fun getByWorkId(workId: Int): List<WorkTime> {
        val cursor = dbHelper.readableDatabase.rawQuery(
                "SELECT * FROM ${WorkTimes.TABLE_NAME} WHERE ${WorkTimes.Columns.WORKTIME_WORK_ID} = $workId",
                null
        )

        val results = ArrayList<WorkTime>()

        if (cursor.count > 0) {
            while (cursor.moveToNext()) {
                results.add(mapCursorToNote(cursor))
            }
        }
        return results
    }

    fun update(workTime: WorkTime) {
        val contentValues = ContentValues()
        contentValues.put(WorkTimes.Columns.WORKTIME_DAY, workTime.day)
        contentValues.put(WorkTimes.Columns.WORKTIME_TIME, workTime.time)
        contentValues.put(WorkTimes.Columns.WORKTIME_TIME_FROM, workTime.timeFrom)
        contentValues.put(WorkTimes.Columns.WORKTIME_TIME_TO, workTime.timeTo)
        contentValues.put(WorkTimes.Columns.WORKTIME_SALARY, workTime.salary)
        contentValues.put(WorkTimes.Columns.WORKTIME_SALARY_MODE, workTime.salaryMode)
        contentValues.put(WorkTimes.Columns.WORKTIME_INFO, workTime.info)
        contentValues.put(WorkTimes.Columns.WORKTIME_WORK_ID, workTime.workId)

        dbHelper.writableDatabase.update(WorkTimes.TABLE_NAME,
                contentValues,
                " ${WorkTimes.Columns.WORKTIME_ID} = ? ",
                arrayOf(workTime.id.toString())
        )
    }

    fun deleteById(id: Int) {
        dbHelper.writableDatabase.delete(WorkTimes.TABLE_NAME,
                " ${WorkTimes.Columns.WORKTIME_ID} = ? ",
                arrayOf(id.toString()))
    }

    private fun mapCursorToNote(cursor: Cursor): WorkTime {
        val idColumnId = cursor.getColumnIndex(WorkTimes.Columns.WORKTIME_ID)
        val dayColumnId = cursor.getColumnIndex(WorkTimes.Columns.WORKTIME_DAY)
        val timeColumnId = cursor.getColumnIndex(WorkTimes.Columns.WORKTIME_TIME)
        val timeFromColumnId = cursor.getColumnIndex(WorkTimes.Columns.WORKTIME_TIME_FROM)
        val timeToColumnId = cursor.getColumnIndex(WorkTimes.Columns.WORKTIME_TIME_TO)
        val salaryColumnId = cursor.getColumnIndex(WorkTimes.Columns.WORKTIME_SALARY)
        val salaryModeColumnId = cursor.getColumnIndex(WorkTimes.Columns.WORKTIME_SALARY_MODE)
        val infoColumnId = cursor.getColumnIndex(WorkTimes.Columns.WORKTIME_INFO)
        val workIdColumnId = cursor.getColumnIndex(WorkTimes.Columns.WORKTIME_WORK_ID)

        return WorkTime(
                id = cursor.getInt(idColumnId),
                day = cursor.getString(dayColumnId),
                time = cursor.getInt(timeColumnId),
                timeFrom = cursor.getInt(timeFromColumnId),
                timeTo = cursor.getInt(timeToColumnId),
                salary = cursor.getInt(salaryColumnId),
                salaryMode = cursor.getInt(salaryModeColumnId),
                info = cursor.getString(infoColumnId),
                workId = cursor.getInt(workIdColumnId)
        )
    }
}
