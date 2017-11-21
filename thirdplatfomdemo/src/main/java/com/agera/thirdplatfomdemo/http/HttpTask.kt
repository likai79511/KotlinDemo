package com.agera.thirdplatfomdemo.http

import com.agera.thirdplatfomdemo.core.TaskDriver
import com.google.android.agera.Result
import com.google.android.agera.net.HttpResponse
import java.util.concurrent.FutureTask
import java.util.concurrent.TimeUnit

/**
 * Created by Agera K
 */
class HttpTask(cb: HttpCallable) : FutureTask<Result<HttpResponse>>(cb) {

    var error: Throwable? = null

    override fun run() {
        try {
            TaskDriver.instance().mTaskControl.acquire()
            if (!Thread.currentThread().isInterrupted) {
                super.run()
            }
        } catch (e: Throwable) {
            error = e
        } finally {
            TaskDriver.instance().mTaskControl.release()
        }
    }

    override fun get(): Result<HttpResponse> {
        if (error != null)
            return Result.failure(error!!)
        return super.get()
    }

    override fun get(timeout: Long, unit: TimeUnit?): Result<HttpResponse> {
        if (error != null)
            return Result.failure(error!!)
        return super.get(timeout, unit)
    }

}