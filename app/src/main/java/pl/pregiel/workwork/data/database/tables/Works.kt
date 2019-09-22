package pl.pregiel.workwork.data.database.tables


interface Works {

    interface Columns {
        companion object {
            const val WORK_ID = "id"
            const val WORK_TITLE = "title"
            const val WORK_CREATED = "created"
            const val WORK_TIME_MODE = "time_mode"
            const val WORK_TIME_FROM = "time_from"
            const val WORK_TIME_TO = "time_to"
            const val WORK_TIME_TO_NOW = "time_to_now"
            const val WORK_TIME_AMOUNT = "time_amount"
            const val WORK_SALARY = "salary"
            const val WORK_SALARY_MODE = "salary_mode"
            const val WORK_CURRENCY = "currency"
            const val WORK_INFO = "info"
        }
    }

    companion object {
        const val TABLE_NAME = "works"
    }
}
