package vn.vistark.calllogger.models.app_license

data class AppState(
    val allowRun: Boolean = false,
    val message: String = "Vui lòng thanh toán số tiền còn lại trước khi có thể tiến hành sử dụng tiếp ứng dụng"
)