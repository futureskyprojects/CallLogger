package vn.vistark.calllogger.controller.export_history

import android.os.Bundle
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import vn.vistark.calllogger.views.export_history.ExportHistoryActivity

class ExportHistoryLoader(val context: ExportHistoryActivity) : LoaderManager.LoaderCallbacks<Boolean> {
    var loaderManager: LoaderManager = LoaderManager.getInstance(context)

    init {
        loaderManager.initLoader(-1, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Boolean> {
        return ExportHistoryATL(context)
    }

    override fun onLoadFinished(loader: Loader<Boolean>, data: Boolean?) {
    }

    override fun onLoaderReset(loader: Loader<Boolean>) {
    }
}