package pl.pregiel.workwork.data.pojo


data class WorkTime(
        var id: Int = 0,
        var day: String = "",
        var time: Int = 0,
        var timeFrom: Int = 0,
        var timeTo: Int = 0,
        var salary: Int = 0,
        var salaryMode: Int = 0,
        var info: String = "",
        var workId: Int = 0
)
