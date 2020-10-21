package vn.vistark.calllogger.models.storages

import vn.vistark.calllogger.models.app_license.AppLicense
import vn.vistark.calllogger.utils.SPUtils

class AppStorage {
    companion object {
        const val MAX_LOGIN_FAIL = 5

        // Cho phép service khởi chạy hay không
        var EnableService: Boolean
            get() {
                return SPUtils.sp?.getBoolean("EnableService", true)
                    ?: true
            }
            set(enable) {
                SPUtils.sp?.edit()?.apply {
                    putBoolean("EnableService", enable)
                }?.apply()
            }

        // Mật khẩu mặc định để truy cập ứng dụng
        var AppPassword: String
            get() {
                return SPUtils.sp?.getString("AppPassword", AppLicense().appPassword)
                    ?: AppLicense().appPassword
            }
            set(appPassword) {
                SPUtils.sp?.edit()?.apply {
                    putString("AppPassword", appPassword)
                }?.apply()
            }

        // Mật khẩu mà người dùng đã nhập
        var UserPassword: String
            get() {
                return SPUtils.sp?.getString("UserPassword", "")
                    ?: ""
            }
            set(appPassword) {
                SPUtils.sp?.edit()?.apply {
                    putString("UserPassword", appPassword)
                }?.apply()
            }

        // Số lần đăng nhập sai
        var LoginFail: Int
            get() {
                return SPUtils.sp?.getInt("LoginFail", 0)
                    ?: 0
            }
            set(count) {
                SPUtils.sp?.edit()?.apply {
                    putInt("LoginFail", count)
                }?.apply()
            }

        // Thời gian delay sau mỗi cuộc gọi, đơn vị tính bằng giây
        var DelayTimeInSeconds: Int
            get() {
                return SPUtils.sp?.getInt("DelayTimeInSeconds", 10) ?: 10
            }
            set(value) {
                SPUtils.sp?.edit()?.apply {
                    putInt("DelayTimeInSeconds", value)
                }?.apply()
            }

        // Thời gian thực hiện mỗi cuộc gọi
        var DelayTimeCallInSeconds: Int
            get() {
                return SPUtils.sp?.getInt("DelayTimeCallInSeconds", 6) ?: 6
            }
            set(value) {
                SPUtils.sp?.edit()?.apply {
                    putInt("DelayTimeCallInSeconds", value)
                }?.apply()
            }
    }
}