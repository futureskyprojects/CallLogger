package vn.vistark.calllogger.views.export_history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.vistark.calllogger.R
import vn.vistark.calllogger.models.ExportHistoryModel

class ExportHistoryAdapter(private val exportHistoryModels: ArrayList<ExportHistoryModel>) :
    RecyclerView.Adapter<ExportHistoryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExportHistoryViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout._export_history_item, parent, false)
        return ExportHistoryViewHolder(v)
    }

    override fun getItemCount(): Int {
        return exportHistoryModels.size
    }

    override fun onBindViewHolder(holder: ExportHistoryViewHolder, position: Int) {
        val exportHistory = exportHistoryModels[position]
        holder.bind(exportHistory)
    }
}