package com.agera.thirdplatfomdemo.http

import com.google.android.agera.Result
import com.google.android.agera.net.HttpFunctions
import com.google.android.agera.net.HttpRequest
import com.google.android.agera.net.HttpResponse
import java.util.concurrent.Callable

/**
 * Created by Administrator on 2017/11/17 0017.
 */
class HttpCallable(var req: HttpRequest) : Callable<Result<HttpResponse>> {
    override fun call(): Result<HttpResponse>? {
        if (req == null) return null
        return HttpFunctions.httpFunction().apply(req)
    }
}