package com.agera.thirdplatfomdemo

import android.graphics.BitmapFactory
import android.widget.ImageView
import com.agera.thirdplatfomdemo.core.TaskDriver
import com.agera.thirdplatfomdemo.http.HttpCallable
import com.agera.thirdplatfomdemo.http.HttpTask
import com.google.android.agera.net.HttpRequests

/**
 * Created by Agera K
 */
class Utils {

    companion object {
        private var utils: Utils = Utils()
        fun instance(): Utils {
            return utils
        }
    }

    fun setImageResourse(view: ImageView, url: String) {
        var task = HttpTask(HttpCallable(HttpRequests.httpGetRequest(url).connectTimeoutMs(10_000)
                .readTimeoutMs(10_000)
                .compile()))
        TaskDriver.instance().execute(task)
        task.get()
                .ifSucceededSendTo { result ->
                    view.setImageBitmap(BitmapFactory.decodeByteArray(result.body, 0, result.body.size))
                }
    }


}