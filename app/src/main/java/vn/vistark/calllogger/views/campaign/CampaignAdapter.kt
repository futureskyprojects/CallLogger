package vn.vistark.calllogger.views.campaign

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.vistark.calllogger.R
import vn.vistark.calllogger.models.CampaignModel

class CampaignAdapter(val campaigns: ArrayList<CampaignModel>) :
    RecyclerView.Adapter<CampaignViewHolder>() {

    var onLongClick: ((CampaignModel) -> Unit)? = null
    var onClick: ((CampaignModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CampaignViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout._campaign_item, parent, false)
        return CampaignViewHolder(v)
    }

    override fun getItemCount(): Int {
        return campaigns.size
    }

    override fun onBindViewHolder(holder: CampaignViewHolder, position: Int) {
        val campaign = campaigns[position]
        holder.bind(campaign)
        holder.cItemLnRoot.setOnLongClickListener {
            onLongClick?.invoke(campaign)
            return@setOnLongClickListener true
        }
        holder.cItemLnRoot.setOnClickListener {
            onClick?.invoke(campaign)
        }
    }
}