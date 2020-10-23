package vn.vistark.calllogger.models.repositories

import android.content.ContentValues
import android.content.Context
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import vn.vistark.calllogger.models.CallLogModel
import vn.vistark.calllogger.models.DatabaseContext
import vn.vistark.calllogger.utils.getInt
import vn.vistark.calllogger.utils.getString


// https://stackoverflow.com/questions/10600670/sqlitedatabase-query-method

class CallLogRepository(val context: Context) {
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
        try {
            // Xây dựng bộ dữ liệu
            val contentValues = createDataValues(model)

            // Ghi vào db
            val res =
                instance.writableDatabase.insert(CallLogModel.TABLE_NAME, null, contentValues)

            // Đóng CSDL lại
            instance.writableDatabase.close()

            // Trả về kết quả
            return res
        } catch (_: Exception) {
            return -1
        }
    }

    // Nên coi http://sqlfiddle.com/#!5/d0a2d/2746
    fun getLimit(lastCallLogId: Int, limit: Long, phoneNumber: String = ""): Array<CallLogModel> {
        // Khai báo biến chứa danh sách
        val callLogs = ArrayList<CallLogModel>()
        // Lấy con trỏ
        val cursor = instance.readableDatabase.query(
            true,
            CallLogModel.TABLE_NAME,
            null,
            if (phoneNumber.isNotEmpty()) "${CallLogModel.ID} > ? AND ${CallLogModel.PHONE_NUMBER} LIKE ?" else "${CallLogModel.ID} > ?",
            if (phoneNumber.isNotEmpty()) arrayOf(
                lastCallLogId.toString(),
                "$phoneNumber%"
            ) else arrayOf(lastCallLogId.toString()),
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

    fun getCount(): Int {
        val count =
            DatabaseUtils.queryNumEntries(instance.readableDatabase, CallLogModel.TABLE_NAME)
        instance.readableDatabase.close()
        return count.toInt()
    }

    fun remove(callLogId: Int):Boolean {
        val res = instance.writableDatabase.delete(
            CallLogModel.TABLE_NAME,
            "${CallLogModel.ID} = ?",
            arrayOf(callLogId.toString())
        )
        instance.writableDatabase.close()
        return res > 0
    }

    fun getAll(): Array<CallLogModel> {
        // Khai báo biến chứa danh sách
        val callLogs = ArrayList<CallLogModel>()
        // Lấy con trỏ
        val cursor = instance.readableDatabase.rawQuery(
            "SELECT * FROM ${CallLogModel.TABLE_NAME}",
            null
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