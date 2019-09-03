package pl.pregiel.workwork.data.database.tables;


public interface WorkTimes {
    String TABLE_NAME = "worktimes";

    interface Columns {
        String WORKTIME_ID = "id";
        String WORKTIME_TIME = "time";
        String WORKTIME_TIME_FROM = "time_from";
        String WORKTIME_TIME_TO = "time_to";
        String WORKTIME_SALARY = "salary";
        String WORKTIME_NOTES = "notes";
        String WORKTIME_WORK_ID = "work_id";
    }
}
