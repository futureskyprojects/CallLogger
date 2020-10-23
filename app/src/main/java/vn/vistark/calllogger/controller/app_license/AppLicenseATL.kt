package vn.vistark.calllogger.controller.app_license

import androidx.loader.content.AsyncTaskLoader
import com.google.gson.Gson
import vn.vistark.calllogger.views.MainActivity
import vn.vistark.calllogger.models.app_license.AppLicense
import java.net.URL

class AppLicenseATL(val context: MainActivity) : AsyncTaskLoader<AppLicense>(context) {
    private val address =
        "https://raw.githubusercontent.com/futureskyprojects/CallLogger/master/license.json"

    init {
        onContentChanged()
    }

    override fun onStartLoading() {
        if (takeContentChanged())
            forceLoad()
    }

    override fun stopLoading() {
        cancelLoad()
    }

    override fun loadInBackground(): AppLicense? {
        val appLicense = try {
            val json = URL(address).readText(Charsets.UTF_8)
            Gson().fromJson(json, AppLicense::class.java)
        } catch (e: Exception) {
            AppLicense()
        }

        context.contiguousTask(appLicense)

        return appLicense
    }

}