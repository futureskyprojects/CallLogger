package vn.vistark.calllogger.models

class CallLogModel(
    // Mã, khóa chính
    var id: Int = 0,

    // Số điện thoại gọi đến
    var phoneNumber: String,

    // Gọi đến lúc
    var receivedAt: String
) {
    companion object {
        const val TABLE_NAME = "call_log"
        const val ID = "id"
        const val PHONE_NUMBER = "phone_number"
        const val RECEIVED_AT = "received_at"
    }
}