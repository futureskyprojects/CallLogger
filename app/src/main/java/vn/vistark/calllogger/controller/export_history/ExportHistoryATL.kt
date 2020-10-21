package vn.vistark.calllogger.controller.export_history

import androidx.loader.content.AsyncTaskLoader
import vn.vistark.calllogger.models.repositories.ExportHistoryRepository
import vn.vistark.calllogger.views.export_history.ExportHistoryActivity

class ExportHistoryATL(val context: ExportHistoryActivity) : AsyncTaskLoader<Boolean>(context) {
    private val exportHistoryRepository = ExportHistoryRepository(context)

    init {
        onContentChanged()
        context.showLoading()
    }

    override fun onStartLoading() {
        if (takeContentChanged())
            forceLoad()
    }

    override fun stopLoading() {
        cancelLoad()
    }

    override fun loadInBackground(): Boolean? {
        var lastExportHistoryId = 0
        while (true) {
            // Lấy x phần tử đầu tiên
            val exportHistories = exportHistoryRepository.getLimit(lastExportHistoryId, 10)
            // Nếu không có thì ngưng
            if (exportHistories.isEmpty())
                break

            // Có thì lấy ID của phần tử cuối cùng
            lastExportHistoryId = exportHistories.last().id

            // Lặp và thêm vào ds hiển tị
            exportHistories.forEach { eh ->
                context.addCampaign(eh)
            }
        }

        context.hideLoading()
        return true
    }

}