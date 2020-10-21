package vn.vistark.calllogger.views.export_history

import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import vn.vistark.calllogger.R
import vn.vistark.calllogger.models.ExportHistoryModel

class ExportHistoryViewHolder(val v: View) : RecyclerView.ViewHolder(v) {
    private val ehtItemLnRoot: LinearLayout = v.findViewById(R.id.ehtItemLnRoot)
    private val ehtItemTvExportTime: TextView = v.findViewById(R.id.ehtItemTvExportTime)
    private val ehtItemCount: TextView = v.findViewById(R.id.ehtItemCount)
    private val ehtItemPbProgress: ProgressBar = v.findViewById(R.id.ehtItemPbProgress)
    private val ehtItemTvPercent: TextView = v.findViewById(R.id.ehtItemTvPercent)

    fun bind(exportHistoryModel: ExportHistoryModel) {
        ehtItemTvExportTime.text = exportHistoryModel.exportedAt
        ehtItemCount.text = "(${exportHistoryModel.phoneNumberCount}/${exportHistoryModel.phoneNumberTotal})"
        val progress: Int =
            ((exportHistoryModel.phoneNumberCount.toDouble() / exportHistoryModel.phoneNumberTotal.toDouble()) * 100).toInt()
        ehtItemPbProgress.progress = progress
        ehtItemTvPercent.text = "$progress%"
    }
}