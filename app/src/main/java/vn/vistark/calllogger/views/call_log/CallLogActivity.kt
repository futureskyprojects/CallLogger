package vn.vistark.calllogger.views.call_log

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_call_log.*
import vn.vistark.calllogger.R
import vn.vistark.calllogger.models.CallLogModel
import vn.vistark.calllogger.models.repositories.CallLogRepository
import vn.vistark.calllogger.views.export_history.ExportHistoryActivity
import vn.vistark.calllogger.views.setting.SettingActivity

class CallLogActivity : AppCompatActivity() {
    lateinit var callLogRepository: CallLogRepository
    var lastCallLogIndex = 0
    var numberForSearch = ""

    companion object {
        var leaking: CallLogActivity? = null
    }

    // Nơi chứa dữ liệu danh sách các số gọi đến
    val callLogs = ArrayList<CallLogModel>()

    // Adapter để hiển thị và điều khiển dữ liệu
    lateinit var adapter: CallLogAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call_log)

        callLogRepository = CallLogRepository(this)

        // Thiết lập tiêu đề
        supportActionBar?.title = "Lịch sử gọi đến"

        // Khởi tạo adapter
        adapter = CallLogAdapter(callLogs)

        // Khởi tạo RecyclerView để chứa danh sách
        aclRvCallLogs.layoutManager = LinearLayoutManager(this)
        aclRvCallLogs.setHasFixedSize(true)
        aclRvCallLogs.adapter = adapter

        // Gọi sự kiện khi kéo đến gần cuối danh sách
        initLoadMoreEvents()

        // Load 200 record đầu
        loadMore()

        leaking = this

        aclBtnSearch.setOnClickListener {
            numberForSearch = aclEdtPhoneNumber.text.toString()
            lastCallLogIndex = 0
            callLogs.clear()
            updateCount()
            loadMore()
        }
    }

    // Phương thức cập nhật, thêm mới lịch sử cuộc gọi vào danh sách
    fun addCallLog(callLog: CallLogModel) {
        callLogs.add(callLog)
        runOnUiThread {
            if (aclRvCallLogs.visibility != View.VISIBLE)
                aclRvCallLogs.visibility = View.VISIBLE
            adapter.notifyDataSetChanged()
            updateCount()
        }
    }

    private fun initLoadMoreEvents() {
        aclRvCallLogs.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
        val _callLogs = callLogRepository.getLimit(lastCallLogIndex, 100, numberForSearch)

        if (_callLogs.isEmpty()) {
            hideLoading()
            return
        }

        lastCallLogIndex = _callLogs[_callLogs.size - 1].id
        _callLogs.forEach { cl ->
            addCallLog(cl)
        }
        runOnUiThread {
            hideLoading()
            adapter.notifyDataSetChanged()
        }
    }

    @SuppressLint("SetTextI18n")
    fun updateCount() {
        aclTvCount.text = "Danh sách số gọi đến (${callLogs.size})"
    }


    // Khởi tạo và chèn menu vào thanh ứng dụng ở trên, bên phải
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_top_right, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.trMenuExportPhoneNumbers -> {
                Toasty.success(this, "Xuất danh sách số điện thoại", Toasty.LENGTH_SHORT, true)
                    .show()
                return true
            }
            R.id.trMenuExportHistory -> {
                val intent = Intent(this, ExportHistoryActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.trMenuSetting -> {
                return settingApp()
            }
            else -> {
                Toasty.error(this, "Không tìm thấy tùy chọn này", Toasty.LENGTH_SHORT, true).show()
            }
        }
        return false
    }


    // Khởi động màn hình thiết lập
    private fun settingApp(): Boolean {
        val intent = Intent(this, SettingActivity::class.java)
        startActivity(intent)
        return true
    }

    // Phương thức cho chạy loading
    fun showLoading() {
        aclPbLoading.visibility = View.VISIBLE
    }

    // Phương thức cho ẩn loading
    fun hideLoading() {
        aclPbLoading.post {
            aclPbLoading.visibility = View.GONE
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
}