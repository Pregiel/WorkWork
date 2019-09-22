package pl.pregiel.workwork.data.pojo


data class Work(
        var id: Int = 0,
        var title: String= "",
        var created: String = "",
        var timeMode: Int = 0,
        var timeFrom: Int = 0,
        var timeTo: Int = 0,
        var timeToNow: Int = 0,
        var timeAmount: Int = 0,
        var salary: Int = 0,
        var salaryMode: Int = 0,
        var currency: Int = 0,
        var info: String = ""
) {

    override fun toString(): String {
        return "$id. $title"
    }
}
