package vn.vistark.calllogger.models.repositories

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import vn.vistark.calllogger.models.DatabaseContext
import vn.vistark.calllogger.models.ExportHistoryModel
import vn.vistark.calllogger.utils.getInt
import vn.vistark.calllogger.utils.getString

class ExportHistoryRepository(val context: AppCompatActivity) {
    private val instance: DatabaseContext = DatabaseContext(context)

    // Thêm mới chiến dịch và trả về ID của chiến dịch đó (-1 là lỗi)
    fun add(campaignModel: ExportHistoryModel): Long {
        // Xây dựng bộ dữ liệu
        val contentValues = ContentValues()
        contentValues.put(ExportHistoryModel.EXPORT_CONTENT, campaignModel.exportContent)
        contentValues.put(ExportHistoryModel.PHONE_NUMBER_COUNT, campaignModel.phoneNumberCount)
        contentValues.put(ExportHistoryModel.PHONE_NUMBER_TOTAL, campaignModel.phoneNumberTotal)
        contentValues.put(ExportHistoryModel.EXPORT_AT, campaignModel.exportedAt)

        // Ghi vào db
        val res = instance.writableDatabase.insert(ExportHistoryModel.TABLE_NAME, null, contentValues)

        // Đóng CSDL lại
        instance.writableDatabase.close()

        // Trả về kết quả
        return res
    }

    fun getMaxId(): Long {
        val cursor = instance.readableDatabase.rawQuery(
            "SELECT MAX(${ExportHistoryModel.ID}) as ${ExportHistoryModel.ID} FROM ${ExportHistoryModel.TABLE_NAME}",
            null
        )

        // Nếu không có, trả về 0
        if (!cursor.moveToFirst()) {
            cursor.close()
            instance.readableDatabase.close()
            return 0
        }

        // Hoặc trả về MAX ID
        val res = cursor.getInt(0)

        cursor.close()
        instance.readableDatabase.close()

        return res.toLong()
    }

    fun getLimit(lastExportHistoryId: Int, limit: Long): Array<ExportHistoryModel> {

        // Khai báo biến chứa danh sách
        val campaigns = ArrayList<ExportHistoryModel>()

        // Lấy con trỏ
        val cursor = instance.readableDatabase.query(
            true,
            ExportHistoryModel.TABLE_NAME,
            null,
            "${ExportHistoryModel.ID} > ?",
            arrayOf(lastExportHistoryId.toString()),
            null,
            null,
            "${ExportHistoryModel.ID} ASC",
            limit.toString()
        )

        // Nếu không có bản ghi
        if (!cursor.moveToFirst()) {
            cursor.close()
            instance.readableDatabase.close()
            return campaigns.toTypedArray()
        }

        // Còn có thì tiến hành duyệt
        do {
            try {
                // Gán dữ liệu vào đối tượng
                val campaign = ExportHistoryModel(
                    cursor.getInt(ExportHistoryModel.ID),
                    cursor.getString(ExportHistoryModel.EXPORT_CONTENT),
                    cursor.getInt(ExportHistoryModel.PHONE_NUMBER_COUNT),
                    cursor.getInt(ExportHistoryModel.PHONE_NUMBER_TOTAL),
                    cursor.getString(ExportHistoryModel.EXPORT_AT)
                )
                // Theeo vào danh sách lưu trữ
                campaigns.add(campaign)
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
        return campaigns.toTypedArray()
    }

    fun get(id: Int): ExportHistoryModel? {

        // Lấy con trỏ
        val cursor = instance.readableDatabase.query(
            true,
            ExportHistoryModel.TABLE_NAME,
            null,
            "${ExportHistoryModel.ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            "${ExportHistoryModel.ID} DESC",
            null
        )

        // Nếu không có bản ghi
        if (!cursor.moveToFirst()) {
            cursor.close()
            return null
        }

        // Còn có thì tiến hành duyệt
        do {
            try {
                // Gán dữ liệu vào đối tượng
                val campaign = ExportHistoryModel(
                    cursor.getInt(ExportHistoryModel.ID),
                    cursor.getString(ExportHistoryModel.EXPORT_CONTENT),
                    cursor.getInt(ExportHistoryModel.PHONE_NUMBER_COUNT),
                    cursor.getInt(ExportHistoryModel.PHONE_NUMBER_TOTAL),
                    cursor.getString(ExportHistoryModel.EXPORT_AT)
                )
                // Đóng con trỏ
                cursor.close()
                // Đóng đọc
                instance.readableDatabase.close()
                // Trả về
                return campaign
            } catch (e: Exception) {
                // Sự đời khó lường trước
                e.printStackTrace()
            } finally {
                // Đóng con trỏ
                cursor.close()
                instance.readableDatabase.close()
            }

        } while (cursor.moveToNext())

        // Đóng con trỏ
        cursor.close()

        // Đóng trình đọc
        instance.readableDatabase.close()

        // Trả về dữ liệu
        return null
    }

    // Xóa chiến dịch, trả về số hàng bị xóa, 0 nếu không có
    fun remove(id: Long): Int {
        val res = instance.writableDatabase.delete(
            ExportHistoryModel.TABLE_NAME,
            "${ExportHistoryModel.ID}=?",
            arrayOf(id.toString())
        )
        instance.writableDatabase.close()
        return res
    }
}