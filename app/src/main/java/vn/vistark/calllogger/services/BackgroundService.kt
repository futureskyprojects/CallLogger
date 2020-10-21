package vn.vistark.calllogger.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.widget.Toast.makeText
import androidx.core.app.NotificationCompat
import es.dmoral.toasty.Toasty
import vn.vistark.calllogger.R
import vn.vistark.calllogger.utils.SPUtils
import java.util.*

class BackgroundService : Service() {
    private val PERIOD = 5000.toLong()

    // Phần khai báo liên quan đến thông báo (Notification)
    private val mNotificationChannelId = "Settings"
    private val mNotificationId = 140398
    private lateinit var timer: Timer

    override fun onCreate() {
        super.onCreate()
        // Khởi tạo các hằng số cơ bản và bộ nhớ lưu trữ cục bộ
        SPUtils.init(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notiManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // A. Tạo notification channel cho android phiên bản từ O đổ lên
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(
                    mNotificationChannelId,
                    "Cài đặt",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    setShowBadge(true)
                }
            channel.lightColor = Color.BLUE
            channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE

            notiManager.createNotificationChannel(channel)
        }

        // B. Tạo pendingIntent cho notify
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        // c. Hiển thị noti và chạy services ngầm
        val notification: Notification = NotificationCompat.Builder(this, mNotificationChannelId)
            .setContentTitle(getString(R.string.app_name))
            .setContentText("Đang đợi cuộc gọi đến")
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notiManager.notify(mNotificationId, notification)

        // C. Tiến hành chạy Forefround (chạy dưới nền)
        startForeground(mNotificationId, notification)

        // Thực hiện các tác vụ tại đây
        intervalTask()
        return START_STICKY
    }

    private fun intervalTask() {
        timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
            }
        }, 1000, PERIOD)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        timer.cancel()
        super.onDestroy()
    }

}
