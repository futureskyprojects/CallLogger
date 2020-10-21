package vn.vistark.calllogger.views.login

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_login.*
import vn.vistark.calllogger.MainActivity
import vn.vistark.calllogger.R
import vn.vistark.calllogger.models.storages.AppStorage
import vn.vistark.calllogger.services.BackgroundService
import vn.vistark.calllogger.utils.ServiceUtils.Companion.isServiceRunning
import vn.vistark.calllogger.views.call_log.CallLogActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Nếu người dùng đã có mật khẩu và mật khẩu ấy đúng
        if (AppStorage.UserPassword.isNotEmpty() && AppStorage.UserPassword == AppStorage.AppPassword) {
            // Thông báo đăng nhập thành công
            Toasty.success(this, "Tự động đăng nhập thành công", Toasty.LENGTH_SHORT, true).show()
            // Đến trang gọi
            gotoCallLogs()
        }

        // Nếu người dùng có mật khẩu mà nó lại khác với mật khẩu ứng dụng
        if (AppStorage.UserPassword.isNotEmpty() && AppStorage.UserPassword != AppStorage.AppPassword)
        // Thông báo cần làm mới mật khẩu
            Toasty.error(
                this,
                "Mật khẩu ứng dụng đã bị đổi, vui lòng nhập lại mật khẩu mới",
                Toasty.LENGTH_SHORT,
                true
            ).show()

        // Khi nhấn vào nút xác nhận
        loginEdtConfirmButton.setOnClickListener {
            // Tiến hành kiểm tra mật khẩu
            checkPassword()
        }
    }

    private fun checkPassword() {
        // Lấy mật khẩu người dùng nhập
        val inputPass = loginEdtPassword.text.toString()

        // Nếu chưa nhập mà bấm
        if (inputPass.isEmpty()) {
            Toasty.error(this, "Bạn chưa nhập mật khẩu", Toasty.LENGTH_SHORT, true).show()
            return
        }

        // Nếu sai mật khẩu và vượt quá số lần quy định
        if (inputPass != AppStorage.AppPassword && AppStorage.LoginFail > AppStorage.MAX_LOGIN_FAIL) {
            gotoMain()
            return
        }

        // Nếu sai mật khẩu nhưng chưa vượt quá số lần quy định
        if (inputPass != AppStorage.AppPassword && AppStorage.LoginFail <= AppStorage.MAX_LOGIN_FAIL) {

            // Nếu là thử lần cuối
            if (AppStorage.MAX_LOGIN_FAIL - AppStorage.LoginFail == 0)
                Toasty.error(
                    this,
                    "Sai mật khẩu, hãy thử lại một lần cuối cùng.",
                    Toasty.LENGTH_SHORT,
                    true
                ).show()
            else
                Toasty.error(
                    this,
                    "Sai mật khẩu, bạn còn ${AppStorage.MAX_LOGIN_FAIL - AppStorage.LoginFail} lần thử.",
                    Toasty.LENGTH_SHORT,
                    true
                ).show()
            // Tăng đếm số lần sai
            AppStorage.LoginFail += 1
            return
        }

        // Xóa số lần sai
        AppStorage.LoginFail = 0

        // Lưu mật khẩu ng dùng đã nhập
        AppStorage.UserPassword = inputPass

        // Thông báo thành công
        Toasty.success(
            this,
            "Đăng nhập thành công!",
            Toasty.LENGTH_SHORT,
            true
        ).show()

        // Nếu service chưa chạy, khởi động nó
        if (AppStorage.EnableService && !this.isServiceRunning(BackgroundService::class.java)) {
            val intent = Intent(this, BackgroundService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
        }

        // Khi đăng nhập thành công, sang màn hình gọi
        gotoCallLogs()
    }

    private fun gotoCallLogs() {
        val intent = Intent(this, CallLogActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun gotoMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}