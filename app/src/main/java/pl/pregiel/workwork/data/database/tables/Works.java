package pl.pregiel.workwork.data.database.tables;


public interface Works {
    String TABLE_NAME = "works";

    interface Columns {
        String WORK_ID = "id";
        String WORK_TITLE = "title";
        String WORK_CREATED = "created";
        String WORK_TIME_MODE = "time_mode";
        String WORK_TIME_FROM = "time_from";
        String WORK_TIME_TO = "time_to";
        String WORK_TIME_TO_NOW = "time_to_now";
        String WORK_TIME_AMOUNT = "time_amount";
        String WORK_SALARY = "salary";
        String WORK_SALARY_MODE = "salary_mode";
        String WORK_CURRENCY = "currency";
        String WORK_INFO = "info";
    }
}
