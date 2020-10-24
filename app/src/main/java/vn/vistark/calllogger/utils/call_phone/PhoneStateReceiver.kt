package vn.vistark.calllogger.utils.call_phone

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import vn.vistark.calllogger.models.CallLogModel
import vn.vistark.calllogger.models.repositories.CallLogRepository
import vn.vistark.calllogger.models.storages.AppStorage
import vn.vistark.calllogger.views.call_log.CallLogActivity
import java.lang.reflect.Method
import java.text.SimpleDateFormat
import java.util.*


// https://stackoverflow.com/questions/9684866/how-to-detect-when-phone-is-answered-or-rejected
class PhoneStateReceiver : PhonecallReceiver() {
    var WeekDays = arrayOf(
        "",
        "CN",
        "T2",
        "T3",
        "T4",
        "T5",
        "T6",
        "T7"
    )

    override fun onIncomingCallReceived(
        context: Context?,
        incomingNumber: String?,
        start: Date?
    ) {

        println("Nhận cuộc gọi đến từ số $incomingNumber")
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("hh:mm dd/MM/yyyy")
        val dayName = WeekDays[calendar.get(Calendar.DAY_OF_WEEK) % WeekDays.size]

        // Nếu cho phép chạy dịch vụ, tiến hành ngắt cuộc gọi và lưu
        println("Chế độ ngắt cuộc gọi: ${AppStorage.EnableEndCall}")
        if (AppStorage.EnableEndCall) {
            println("Ngắt cuộc gọi sau: ${AppStorage.DelayTimeCallInSeconds} giây")
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    killCall(context!!)
                }
            }, AppStorage.DelayTimeCallInSeconds * 1000L)
        }

        if (incomingNumber.isNullOrEmpty())
            return

        val callLog = CallLogModel(
            -1,
            incomingNumber,
            "${dateFormat.format(calendar.time)} ($dayName)"
        )

        print("Lưu số điện thoại $incomingNumber ")
        if (CallLogRepository(context!!).add(callLog) > 0) {
            CallLogActivity.leaking?.addCallLog(callLog)
            println("[Thành công]")
        } else {
            println("[Không thành công]")
        }
    }

    override fun onIncomingCallAnswered(
        ctx: Context?,
        number: String?,
        start: Date?
    ) {
        //
    }

    override fun onIncomingCallEnded(
        ctx: Context?,
        number: String?,
        start: Date?,
        end: Date?
    ) {
        //
    }

    override fun onOutgoingCallStarted(
        ctx: Context?,
        number: String?,
        start: Date?
    ) {
        //
    }

    override fun onOutgoingCallEnded(
        ctx: Context?,
        number: String?,
        start: Date?,
        end: Date?
    ) {
        //
    }

    override fun onMissedCall(
        ctx: Context?,
        number: String?,
        start: Date?
    ) {
        //
    }

//    private fun KillCallTimer(context: Context) {
//        // Thời gian chờ
//        var timeDelay = AppStorage.DelayTimeCallInSeconds * 1000L + 380L
//
//        // Nếu không phải lần đầu, +1s
//        if (!isFirstTime) {
//            timeDelay += 1450
//            isFirstTime = false
//        }
//
//        println("READY KILL =>")
//        // Đếm ngược
//        object : CountDownTimer(timeDelay, 750) {
//            override fun onTick(millisUntilFinished: Long) {}
//            override fun onFinish() {
//                // Kết thúc cuộc gọi ngay lập tức
//                killCall(context)
//            }
//        }.start()
//    }

    private fun killCall(context: Context): Boolean {
        try {
            // Get the boring old TelephonyManager
            val telephonyManager =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

            // Get the getITelephony() method
            val classTelephony =
                Class.forName(telephonyManager.javaClass.name)
            val methodGetITelephony: Method = classTelephony.getDeclaredMethod("getITelephony")

            // Ignore that the method is supposed to be private
            methodGetITelephony.isAccessible = true

            // Invoke getITelephony() to get the ITelephony interface
            val telephonyInterface: Any? = methodGetITelephony.invoke(telephonyManager)

            // Get the endCall method from ITelephony
            val telephonyInterfaceClass =
                Class.forName(telephonyInterface?.javaClass?.name ?: "ERROR?")
            val methodEndCall: Method = telephonyInterfaceClass.getDeclaredMethod("endCall")

            // Invoke endCall()
            methodEndCall.invoke(telephonyInterface)
        } catch (ex: Exception) { // Many things can go wrong with reflection calls
//            Toasty.error(
//                context,
//                "Ứng dụng không thể can thiệp vào hệ thống để ngưng cuộc gọi",
//                Toasty.LENGTH_SHORT
//            ).show()
            println("Ứng dụng không thể can thiệp vào hệ thống để ngưng cuộc gọi")
            return false
        }
        return true
    }
}