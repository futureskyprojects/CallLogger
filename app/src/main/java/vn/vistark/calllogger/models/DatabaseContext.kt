package vn.vistark.calllogger.models

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity


class DatabaseContext(val context: AppCompatActivity) : SQLiteOpenHelper(
    context,
    DATABASE_NAME, null, 2009292
) {
    companion object {
        const val DATABASE_NAME = "CallLogger.db"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Chạy lệnh tạo bảng
        db?.execSQL(
            "CREATE TABLE IF NOT EXISTS \"${CallLogModel.TABLE_NAME}\" (\n" +
                    "\t\"${CallLogModel.ID}\"\tINTEGER NOT NULL,\n" +
                    "\t\"${CallLogModel.PHONE_NUMBER}\"\tTEXT NOT NULL,\n" +
                    "\t\"${CallLogModel.RECEIVED_AT}\"\tTEXT NOT NULL,\n" +
                    "\tPRIMARY KEY(\"${CallLogModel.ID}\" AUTOINCREMENT)\n" +
                    ");"
        )

        db?.execSQL(
            "CREATE TABLE IF NOT EXISTS \"${ExportHistoryModel.TABLE_NAME}\" (\n" +
                    "\t\"${ExportHistoryModel.ID}\"\tINTEGER NOT NULL,\n" +
                    "\t\"${ExportHistoryModel.EXPORT_CONTENT}\"\tTEXT NOT NULL,\n" +
                    "\t\"${ExportHistoryModel.PHONE_NUMBER_COUNT}\"\tINTEGER NOT NULL DEFAULT 0,\n" +
                    "\t\"${ExportHistoryModel.PHONE_NUMBER_TOTAL}\"\tINTEGER NOT NULL DEFAULT 0,\n" +
                    "\t\"${ExportHistoryModel.EXPORT_AT}\"\tTEXT NOT NULL,\n" +
                    "\tPRIMARY KEY(\"${ExportHistoryModel.ID}\" AUTOINCREMENT)\n" +
                    ");"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {

        // Xóa các bảng cũ
        db?.execSQL("DROP TABLE IF EXISTS ${CallLogModel.TABLE_NAME}")
        db?.execSQL("DROP TABLE IF EXISTS ${ExportHistoryModel.TABLE_NAME}")

        // Tọa lại bảng mới
        onCreate(db)
    }


}