package com.agera.thirdplatfomdemo.core

import android.os.Handler
import android.os.Looper
import java.util.concurrent.*

/**
 * Created by Administrator on 2017/11/15 0015.
 */
class TaskDriver private constructor() {

    private var instance: TaskDriver? = null

    companion object {
        fun instance(): TaskDriver {
            return TaskDriver()
        }
    }

    val cpuCount: Int = Runtime.getRuntime().availableProcessors()
    val mTaskControl: Semaphore = Semaphore(cpuCount + 1)
    val mCore: ExecutorService = Executors.newFixedThreadPool(cpuCount + 1)
    val mMainExecutor: Executor = Executor {
        Handler(Looper.getMainLooper()).post(it)
    }


    fun <T> execute(task: FutureTask<in T>) {
        mCore.submit(task)
    }
}