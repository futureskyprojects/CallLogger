package vn.vistark.calllogger.views.call_log

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.vistark.calllogger.R
import vn.vistark.calllogger.models.CallLogModel
import vn.vistark.calllogger.models.repositories.CallLogRepository

class CallLogAdapter(private val callLogs: ArrayList<CallLogModel>) :
    RecyclerView.Adapter<CallLogViewHolder>() {

    var onLongClick: ((CallLogModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallLogViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout._call_log_number_item, parent, false)
        return CallLogViewHolder(v)
    }

    override fun getItemCount(): Int {
        return callLogs.size
    }

    override fun onBindViewHolder(holder: CallLogViewHolder, position: Int) {
        val callLog = callLogs[position]
        holder.bind(callLog)
        holder.clnItemLnRoot.setOnLongClickListener {
            onLongClick?.invoke(callLog)
            return@setOnLongClickListener true
        }
    }
}