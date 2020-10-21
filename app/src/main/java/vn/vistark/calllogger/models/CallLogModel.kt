package vn.vistark.calllogger.models

import java.sql.Date

class CallLogModel(
    // Mã, khóa chính
    var id: Int = 0,

    // Số điện thoại gọi đến
    var phoneNumber: String,

    // Gọi đến lúc
    var receivedAt: String
) {

}