package vn.vistark.calllogger.models.repositories

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import vn.vistark.calllogger.models.CallLogModel
import vn.vistark.calllogger.models.DatabaseContext
import vn.vistark.calllogger.models.PhoneCallState
import vn.vistark.calllogger.utils.getBoolean
import vn.vistark.calllogger.utils.getInt
import vn.vistark.calllogger.utils.getString

// https://stackoverflow.com/questions/10600670/sqlitedatabase-query-method

class CallLogRepository(val context: AppCompatActivity) {
    private val instance: DatabaseContext = DatabaseContext(context)

    companion object {
        // Tạo bộ dữ liệu
        fun createDataValues(model: CallLogModel): ContentValues {
            val contentValues = ContentValues()
            contentValues.put(CallLogModel.PHONE_NUMBER, model.phoneNumber)
            contentValues.put(CallLogModel.RECEIVED_AT, model.receivedAt)
            return contentValues
        }
    }

    // Xem bên CampaignRespository
    fun add(model: CallLogModel): Long {
        // Xây dựng bộ dữ liệu
        val contentValues = createDataValues(model)

        // Ghi vào db
        val res =
            instance.writableDatabase.insert(CallLogModel.TABLE_NAME, null, contentValues)

        // Đóng CSDL lại
        instance.writableDatabase.close()

        // Trả về kết quả
        return res
    }

    // Nên coi http://sqlfiddle.com/#!5/d0a2d/2746
    fun getLimit(lastCallLogId: Int, limit: Long): Array<CallLogModel> {
        // Khai báo biến chứa danh sách
        val callLogs = ArrayList<CallLogModel>()
        // Lấy con trỏ
        val cursor = instance.readableDatabase.query(
            true,
            CallLogModel.TABLE_NAME,
            null,
            "${CallLogModel.ID} > ?",
            arrayOf(lastCallLogId.toString()),
            null,
            null,
            "${CallLogModel.ID} ASC",
            limit.toString()
        )
        // Nếu không có bản ghi
        if (!cursor.moveToFirst()) {
            instance.readableDatabase.close()
            cursor.close()
            return callLogs.toTypedArray()
        }

        // Còn có thì tiến hành duyệt
        do {
            try {
                // Gán dữ liệu vào đối tượng
                val callLog = CallLogModel(
                    cursor.getInt(CallLogModel.ID),
                    cursor.getString(CallLogModel.PHONE_NUMBER),
                    cursor.getString(CallLogModel.RECEIVED_AT)
                )

                // Lưu vào danh sách lưu trữ
                callLogs.add(callLog)
            } catch (e: Exception) {
                // Sự đời khó lường trước
                e.printStackTrace()
            }

        } while (cursor.moveToNext())

        // Đóng con trỏ
        cursor.close()

        // Đóng trình đọc
        instance.readableDatabase.close()

        // Trả về dữ liệu
        return callLogs.toTypedArray()
    }

    // Xóa hết các dữ liệu của chiến dịch này
    fun removeAll(): Int {
        val res = instance.writableDatabase.delete(
            CallLogModel.TABLE_NAME,
            "1",
            null
        )
        instance.writableDatabase.close()
        return res
    }
}