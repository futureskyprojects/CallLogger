package vn.vistark.calllogger.controller.export

import android.content.Intent
import android.net.Uri
import android.os.Environment
import androidx.core.content.ContextCompat.startActivity
import androidx.loader.content.AsyncTaskLoader
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_call_log.*
import vn.vistark.calllogger.models.ExportHistoryModel
import vn.vistark.calllogger.models.repositories.CallLogRepository
import vn.vistark.calllogger.models.repositories.ExportHistoryRepository
import vn.vistark.calllogger.views.call_log.CallLogActivity
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class ExportATL(val context: CallLogActivity) : AsyncTaskLoader<Boolean>(context) {
    private val callLogRepository = CallLogRepository(context)

    var WeekDays = arrayOf(
        "",
        "CN",
        "T2",
        "T3",
        "T4",
        "T5",
        "T6",
        "T7"
    )


    init {
        onContentChanged()
    }

    override fun onStartLoading() {
        if (takeContentChanged())
            forceLoad()
    }

    override fun stopLoading() {
        cancelLoad()
    }

    override fun loadInBackground(): Boolean? {
        context.aclBtnSearch.post {
            context.showLoadingDialog()
        }

        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("hh:mm:ss dd/MM/yyyy")
        val dayName = WeekDays[calendar.get(Calendar.DAY_OF_WEEK) % WeekDays.size]

        val date = "${dateFormat.format(calendar.time)} ($dayName)"
        val filename = "$date.txt".replace("/", "-").replace(":", ".").replace(" ", "_")

        // Lấy danh sách các số đã lưu
        val callLogs = callLogRepository.getAll()

        val root = context.externalCacheDir!!.path + File.separator + "ExportHistory/"
        println("ROOT: $root >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")

        val directory = File(root)
        if (!directory.exists())
            directory.mkdirs()

        val file = File(directory, filename)
        println(">>>>>>>>>>>>>>>> ${file.path}")

        if (!file.exists())
            file.createNewFile()
        val fos = FileOutputStream(file, true); // save

        var imported = 0

        // Lặp trong từng phần tử
        callLogs.forEach {
            val temp = "${it.phoneNumber}\r\n"
            imported++
            fos.write(temp.toByteArray())
        }

        fos.close()

        // Lưu thông tin của tệp vào CSDL
        ExportHistoryRepository(context).add(
            ExportHistoryModel(
                -1,
                file.path,
                imported,
                callLogs.size,
                date
            )
        )
        // End lưu

        context.aclBtnSearch.post {
            context.hideLoadingDialog()
            Toasty.success(context, "Hoàn tất", Toasty.LENGTH_SHORT, true).show()
        }

        return true
    }

}