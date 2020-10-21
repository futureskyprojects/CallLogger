package vn.vistark.calllogger.controller.app_license

import android.os.Bundle
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import vn.vistark.calllogger.MainActivity
import vn.vistark.calllogger.models.app_license.AppLicense
import vn.vistark.calllogger.views.call_log.CallLogActivity

class AppLicenseLoader(val context: MainActivity) : LoaderManager.LoaderCallbacks<AppLicense> {
    var loaderManager: LoaderManager = LoaderManager.getInstance(context)

    init {
        loaderManager.initLoader(-1, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<AppLicense> {
        return AppLicenseATL(context)
    }

    override fun onLoadFinished(loader: Loader<AppLicense>, data: AppLicense?) {

    }

    override fun onLoaderReset(loader: Loader<AppLicense>) {
    }
}