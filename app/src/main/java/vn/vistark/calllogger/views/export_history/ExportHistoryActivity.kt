package vn.vistark.calllogger.views.export_history

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_export_history.*
import vn.vistark.calllogger.R
import vn.vistark.calllogger.models.ExportHistoryModel
import vn.vistark.calllogger.models.repositories.ExportHistoryRepository

class ExportHistoryActivity : AppCompatActivity() {
    lateinit var exportHistoryRepository: ExportHistoryRepository
    var lastExportHistoryIndex = 0

    // Nơi chứa dữ liệu danh sách các bản đã xuất
    val exportHistories = ArrayList<ExportHistoryModel>()

    // Adapter để hiển thị và điều khiển dữ liệu
    lateinit var adapter: ExportHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_export_history)

        // Thiết lập tiêu đề
        supportActionBar?.title = "Lịch sử xuất"

        // Hiển thị nút trở về
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // Khởi tạo adapter
        adapter = ExportHistoryAdapter(exportHistories)

        // Khởi tạo RecyclerView để chứa danh sách
        aehRvExportHistories.layoutManager = LinearLayoutManager(this)
        aehRvExportHistories.setHasFixedSize(true)
        aehRvExportHistories.adapter = adapter

        // Gọi sự kiện khi kéo đến gần cuối danh sách
        initLoadMoreEvents()

        // Load 200 record đầu
        loadMore()
    }

    // Phương thức cập nhật, thêm mới lịch sử cuộc gọi vào danh sách
    fun addExportHistory(exportHistory: ExportHistoryModel) {
        exportHistories.add(exportHistory)
        runOnUiThread {
            if (aehRvExportHistories.visibility != View.VISIBLE)
                aehRvExportHistories.visibility = View.VISIBLE
            adapter.notifyDataSetChanged()
            updateCount()
        }
    }

    @SuppressLint("SetTextI18n")
    fun updateCount() {
        aehTvCount.text = "Danh sách số gọi đến (${exportHistories.size})"
    }

    // Phương thức cho chạy loading
    fun showLoading() {
        aehPbLoading.visibility = View.VISIBLE
    }

    // Phương thức cho ẩn loading
    fun hideLoading() {
        aehPbLoading.post {
            aehPbLoading.visibility = View.GONE
        }
    }

    private fun initLoadMoreEvents() {
        aehRvExportHistories.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    loadMore()
                }
            }
        })
    }

    private fun loadMore() {
        showLoading()
        val _exportHistories = exportHistoryRepository.getLimit(lastExportHistoryIndex, 100)

        if (_exportHistories.isEmpty()) {
            hideLoading()
            return
        }

        lastExportHistoryIndex = _exportHistories[_exportHistories.size - 1].id
        _exportHistories.forEach { cl ->
            addExportHistory(cl)
        }
        runOnUiThread {
            hideLoading()
            adapter.notifyDataSetChanged()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Nếu kết quả trả về là của màn hình tạo chiến dịch, và kết quả thành công
//        if (requestCode == CampaignCreateActivity.REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
//            // Lấy mã của chiến dịch mới
//            val newCampaignId = data.getIntExtra(CampaignModel.ID, -1)
//
//            // Nếu mã không đúng, bỏ qua
//            if (newCampaignId <= 0)
//                return
//
//            // Lấy chi tiết chiến dịch, nếu không có thì bỏ qua
//            val campaign = ExportHistoryRepository(this).get(newCampaignId) ?: return
//
//            // Tiến hành thêm vào view hiển thị
//            addCampaign(campaign)
//
//        }
    }

    // Trở về khi nhấn nút back
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}