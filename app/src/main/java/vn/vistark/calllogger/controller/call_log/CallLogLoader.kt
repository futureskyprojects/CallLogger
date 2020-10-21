package vn.vistark.calllogger.controller.call_log

import android.os.Bundle
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import vn.vistark.calllogger.views.call_log.CallLogActivity

class CallLogLoader(val context: CallLogActivity) : LoaderManager.LoaderCallbacks<Boolean> {
    var loaderManager: LoaderManager = LoaderManager.getInstance(context)

    init {
        loaderManager.initLoader(-1, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Boolean> {
        return CallLogATL(context)
    }

    override fun onLoadFinished(loader: Loader<Boolean>, data: Boolean?) {
    }

    override fun onLoaderReset(loader: Loader<Boolean>) {
    }
}