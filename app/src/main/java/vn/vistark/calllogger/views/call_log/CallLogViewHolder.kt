package vn.vistark.calllogger.views.call_log

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import vn.vistark.calllogger.R
import vn.vistark.calllogger.models.CallLogModel

class CallLogViewHolder(val v: View) : RecyclerView.ViewHolder(v) {
    private val clnItemLnRoot: LinearLayout = v.findViewById(R.id.clnItemLnRoot)
    private val clnItemTvPhoneNumber: TextView = v.findViewById(R.id.clnItemTvPhoneNumber)
    private val clnItemTvTime: TextView = v.findViewById(R.id.clnItemTvTime)

    fun bind(callLogs: CallLogModel) {
        clnItemTvPhoneNumber.text = "Số điện thoại: ${callLogs.phoneNumber}"
        clnItemTvPhoneNumber.text = "Nhận lúc: ${callLogs.receivedAt}"
    }
}