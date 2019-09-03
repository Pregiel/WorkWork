package pl.pregiel.workwork.data.database.tables;


public interface Works {
    String TABLE_NAME = "works";

    interface Columns {
        String WORK_ID = "id";
        String WORK_TITLE = "title";
    }
}
