package pl.pregiel.workwork.data.database.tables


interface WorkTimes {

    interface Columns {
        companion object {
            const val WORKTIME_ID = "id"
            const val WORKTIME_DAY = "day"
            const val WORKTIME_TIME = "time"
            const val WORKTIME_TIME_FROM = "time_from"
            const val WORKTIME_TIME_TO = "time_to"
            const val WORKTIME_SALARY = "salary"
            const val WORKTIME_SALARY_MODE = "salary_mode"
            const val WORKTIME_INFO = "info"
            const val WORKTIME_WORK_ID = "work_id"
        }
    }

    companion object {
        const val TABLE_NAME = "worktimes"
    }
}
