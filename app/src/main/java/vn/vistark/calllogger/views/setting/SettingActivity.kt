package vn.vistark.calllogger.views.setting

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_setting.*
import vn.vistark.calllogger.R
import vn.vistark.calllogger.models.CallLogModel
import vn.vistark.calllogger.models.repositories.CallLogRepository
import vn.vistark.calllogger.models.repositories.ExportHistoryRepository
import vn.vistark.calllogger.models.storages.AppStorage
import vn.vistark.calllogger.services.BackgroundService
import vn.vistark.calllogger.utils.ServiceUtils.Companion.isServiceRunning
import vn.vistark.calllogger.views.MainActivity
import vn.vistark.calllogger.views.call_log.CallLogActivity
import java.io.File
import kotlin.random.Random

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        // Thiết lập tiêu đề
        supportActionBar?.title = "Thiết lập ứng dụng"

        // Hiển thị nút trở về
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // Load lại dữ liệu trước đó
//        settingEdtTimerDelay.setText(AppStorage.DelayTimeInSeconds.toString())
        settingEdtTimerCallIn.setText(AppStorage.DelayTimeCallInSeconds.toString())

        // Đặt sự kiện lưu lại thiết lập
        settingBtnConfirmSave.setOnClickListener {
            saveSettingChange()
        }

        settingSwcRunInBackground.isChecked = this.isServiceRunning(BackgroundService::class.java)
        // Sự kiện thay đổi trạng thái services
        settingSwcRunInBackground.setOnClickListener {
            if (settingSwcRunInBackground.isChecked) {
                askForStartService()
            } else {
                askForEndService()
            }
        }

        settingSwcEndCall.isChecked = AppStorage.EnableEndCall
        // Sự kiện thay đổi trạng thái kết thúc cuộc gọi
        settingSwcEndCall.setOnClickListener {
            AppStorage.EnableEndCall = settingSwcEndCall.isChecked
        }

        settingBtnRemoveAllCallLog.setOnClickListener {
            val count = CallLogRepository(this).getCount()
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Xóa tất cả $count SĐT trong danh sách gọi đến? Việc này không thể hoàn tác")
                .setContentText("XÓA HẾT")
                .setCancelButton("Đóng") {
                    it.dismissWithAnimation()
                    it.cancel()
                }
                .setConfirmButton("Xóa") { z ->
                    z.dismissWithAnimation()
                    z.cancel()
                    if (CallLogRepository(this).removeAll() > 0) {
                        SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Đã xóa hoàn tất $count SĐT")
                            .setContentText("HOÀN TẤT")
                            .showCancelButton(false)
                            .setConfirmButton("Đóng") {
                                it.dismissWithAnimation()
                                it.cancel()
                            }.show()
                        CallLogActivity.leaking?.callLogs?.clear()
                        CallLogActivity.leaking?.adapter?.notifyDataSetChanged()
                        CallLogActivity.leaking?.updateCount()
                    }
                }.show()
        }

        settingBtnRemoveAllExportHistory.setOnClickListener {
            val exportHistories = ExportHistoryRepository(this).getAll()
            val count = exportHistories.size
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Xóa tất cả $count bản xuất? Việc này không thể hoàn tác")
                .setContentText("XÓA HẾT")
                .setCancelButton("Đóng") {
                    it.dismissWithAnimation()
                    it.cancel()
                }
                .setConfirmButton("Xóa") {
                    it.dismissWithAnimation()
                    it.cancel()
                    for (exportHistory in exportHistories) {
                        val f = File(exportHistory.exportContent)

                        if (f.exists())
                            f.delete()
                    }
                    SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Đã xóa hoàn tất $count bản xuất")
                        .setContentText("HOÀN TẤT")
                        .showCancelButton(false)
                        .setConfirmButton("Đóng") {
                            it.dismissWithAnimation()
                            it.cancel()
                        }.show()
                }.show()
        }

        settingBtnCreateSample.setOnClickListener {
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Thêm dữ liệu mẫu khi có dữ liệu thật sẽ gây nhiễu loạn và có khả năng dẫn đến sai sót trong danh sách số điện thoại của bạn. Chức năng này chỉ nên được dùng để kiểm tra phần mềm!")
                .setContentText("THÊM 300 MẪU")
                .setCancelButton("Đóng") {
                    it.dismissWithAnimation()
                    it.cancel()
                }
                .setConfirmButton("Vẫn thêm") {
                    for (i in 0 until 300) {
                        val phone = Random.nextInt(100000000, 999999999)
                        CallLogRepository(this).add(CallLogModel(-1, "0$phone", "---------------"))
                    }
                    Toasty.success(
                        this,
                        "Đã thêm thành công, khởi động lại ứng dụng",
                        Toasty.LENGTH_SHORT,
                        true
                    ).show()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    this.startActivity(intent)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        finishAffinity()
                    } else {

                    }
                }.show()
        }
    }


    private fun askForStartService() {
        SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
            .setTitleText("KHỞI ĐỘNG CHẠY NGẦM CHO ỨNG DỤNG")
            .setContentText("CHẠY NGẦM")
            .setConfirmButton("Khởi chạy") {
                it.dismissWithAnimation()
                it.cancel()
                val intent = Intent(this, BackgroundService::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(intent)
                } else {
                    startService(intent)
                }
                AppStorage.EnableService = true
            }
            .setCancelButton("Để sau") {
                it.dismissWithAnimation()
                it.cancel()
                settingSwcRunInBackground.isChecked = false
            }.show()
    }

    private fun askForEndService() {
        SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
            .setTitleText("NGƯNG CHẠY NGẦM CHO ỨNG DỤNG")
            .setContentText("CHẠY NGẦM")
            .setConfirmButton("Tạm ngưng") {
                it.dismissWithAnimation()
                it.cancel()
                val intent = Intent(this, BackgroundService::class.java)
                stopService(intent)
                AppStorage.EnableService = false
            }
            .setCancelButton("Để sau") {
                it.dismissWithAnimation()
                it.cancel()
                settingSwcRunInBackground.isChecked = true
            }.show()
    }

    private fun saveSettingChange() {

        // Lưu thiết lập về thời gian thực hiện cho mỗi cuộc gọi
        if (!saveDelayTimeInCall())
            return

        Toasty.success(this, "Cập nhật thiết lập thành công", Toasty.LENGTH_SHORT, true).show()
    }

    private fun saveDelayTimeInCall(): Boolean {
        // Lấy giá trị mà người dùng đã nhập
        val inpDelayTimeInCall = settingEdtTimerCallIn.text.toString().toIntOrNull()

        // Nếu không thể phân giải thành số
        if (inpDelayTimeInCall == null) {
            Toasty.error(this, "Thời chờ ngắt máy không đúng", Toasty.LENGTH_SHORT, true)
                .show()
            return false
        }

        // Nếu giá trị nhỏ nhơn 2
        if (inpDelayTimeInCall < 1) {
            Toasty.error(
                this,
                "Thời gian tối thiểu chờ cuộc gọi là 1 giây",
                Toasty.LENGTH_SHORT,
                true
            ).show()
            return false
        }

        // Nếu nhập thành công
        AppStorage.DelayTimeCallInSeconds = inpDelayTimeInCall

        // Trả về true
        return true
    }

    // Trở về khi nhấn nút back
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}