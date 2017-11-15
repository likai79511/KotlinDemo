package com.agera.thirdplatfomdemo

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.google.android.agera.BaseObservable
import com.google.android.agera.Updatable

/**
 * Created by Administrator on 2017/11/15 0015.
 */
class Main : Activity(),Updatable {

    var clickEvent_QQ:LoginListener = LoginListener()
    var clickEvent_Sina:LoginListener = LoginListener()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }

    override fun update() {
    }

    class LoginListener:BaseObservable(),View.OnClickListener{
        override fun onClick(v: View?) {
            dispatchUpdate()
        }
    }
}