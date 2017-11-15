package com.agera.thirdplatfomdemo

import java.util.concurrent.Callable
import java.util.concurrent.FutureTask

/**
 * Created by Administrator on 2017/11/15 0015.
 */
class  HttpTask(cb:Callable<>):FutureTask<>(cb){
}