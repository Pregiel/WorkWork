package pl.pregiel.workwork.data.pojo;


public class Work {
    private Integer id;
    private String title;
    private String created;
    private Integer timeMode;
    private Integer timeFrom;
    private Integer timeTo;
    private Integer timeToNow;
    private Integer timeAmount;
    private Integer salary;
    private Integer salaryMode;
    private Integer currency;
    private String info;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public Integer getTimeMode() {
        return timeMode;
    }

    public void setTimeMode(Integer timeMode) {
        this.timeMode = timeMode;
    }

    public Integer getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(Integer timeFrom) {
        this.timeFrom = timeFrom;
    }

    public Integer getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(Integer timeTo) {
        this.timeTo = timeTo;
    }

    public Integer getTimeToNow() {
        return timeToNow;
    }

    public void setTimeToNow(Integer timeToNow) {
        this.timeToNow = timeToNow;
    }

    public Integer getTimeAmount() {
        return timeAmount;
    }

    public void setTimeAmount(Integer timeAmount) {
        this.timeAmount = timeAmount;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public Integer getSalaryMode() {
        return salaryMode;
    }

    public void setSalaryMode(Integer salaryMode) {
        this.salaryMode = salaryMode;
    }

    public Integer getCurrency() {
        return currency;
    }

    public void setCurrency(Integer currency) {
        this.currency = currency;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return id + ". " + title;
    }
}
