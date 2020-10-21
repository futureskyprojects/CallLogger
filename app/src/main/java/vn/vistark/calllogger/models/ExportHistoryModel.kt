package vn.vistark.calllogger.models

class ExportHistoryModel(
    // Mã, khóa chính
    var id: Int = 0,

    // Số điện thoại gọi đến
    var exportContent: String,

    // Số lượng số điện thoại đã xuất
    var phoneNumberCount: Int = 0,

    // Số lượng số điện thoại thực tế cần xuất
    var phoneNumberTotal: Int = 0,

    // Gọi đến lúc
    var exportedAt: String
) {
    companion object {
        const val TABLE_NAME = "export_history"
        const val ID = "id"
        const val EXPORT_CONTENT = "export_content"
        const val PHONE_NUMBER_COUNT = "phone_number_count"
        const val PHONE_NUMBER_TOTAL = "phone_number_total"
        const val EXPORT_AT = "export_at"
    }
}