package vn.vistark.calllogger.controller.campaign_create

import androidx.appcompat.app.AppCompatActivity
import vn.vistark.calllogger.models.repositories.CampaignRepository
import java.text.SimpleDateFormat
import java.util.*

class CampaignCreateController {
    companion object {
        fun generateCampaignName(context: AppCompatActivity): String {
            val campaignMaxId = CampaignRepository(context).getMaxId() + 1
            return "#$campaignMaxId. ngày " + SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                .format(Date())
        }

    }
}