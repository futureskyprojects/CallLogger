package vn.vistark.calllogger.controller.call_log

import androidx.loader.content.AsyncTaskLoader
import vn.vistark.calllogger.models.repositories.CallLogRepository
import vn.vistark.calllogger.views.call_log.CallLogActivity

class CallLogATL(val context: CallLogActivity, val phoneNumber: String) :
    AsyncTaskLoader<Boolean>(context) {
    private val callLogRepository = CallLogRepository(context)

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
        var lastCallLogId = 0
        while (true) {
            // Lấy x phần tử đầu tiên
            val callLogs = callLogRepository.getLimit(lastCallLogId, 10)
            // Nếu không có thì ngưng
            if (callLogs.isEmpty())
                break

            // Có thì lấy ID của phần tử cuối cùng
            lastCallLogId = callLogs.last().id

            // Lặp và thêm vào ds hiển tị
            callLogs.forEach { campaign ->
                context.addCallLog(campaign)
            }
        }

        context.hideLoading()
        return true
    }

}