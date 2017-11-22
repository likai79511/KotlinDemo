package com.agera.thirdplatfomdemo.core

import android.os.Handler
import android.os.Looper
import java.util.concurrent.*

/**
 * Created by Agera K
 */
class TaskDriver private constructor() {
    companion object {
        private var instance = TaskDriver()
        fun instance(): TaskDriver {
            return instance
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