package com.agera.thirdplatfomdemo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.agera.thirdplatfomdemo.Login.Platform
import com.agera.thirdplatfomdemo.Login.QQEvent
import com.agera.thirdplatfomdemo.Login.WBEvent
import com.agera.thirdplatfomdemo.bean.QQAuth
import com.google.android.agera.Repositories
import com.google.android.agera.Repository
import com.google.android.agera.Result
import com.google.android.agera.Updatable
import com.google.gson.Gson
import com.tencent.connect.common.Constants

/**
 * Created by Agera K
 */
class MainActivity : Activity(), Updatable {

    var mRep: Repository<Result<String>>? = null
    var Sina: WBEvent? = null
    var QQ: QQEvent? = null
    var signal: Platform? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        initEvents()
        mRep?.let { it.addUpdatable(this) }
    }

    private fun initEvents() {
        Sina = WBEvent(this)
        QQ = QQEvent(this)
        findViewById(R.id.btn_sina).setOnClickListener { view ->
            signal = Platform.Sina
            Sina!!.onClick(view)
        }

        findViewById(R.id.btn_qq).setOnClickListener { view ->
            signal = Platform.QQ
            QQ!!.onClick(view)
        }

        mRep = Repositories.repositoryWithInitialValue(Result.absent<String>())
                .observe(Sina, QQ)
                .onUpdatesPerLoop()
                .attemptGetFrom {
                    if (signal == null) Result.failure() else Result.success("skip first income")
                }
                .orSkip()
                .thenGetFrom {
                    if (signal == Platform.Sina) Sina!!.get() //Sina
                    else QQ!!.get()   //QQ
                }
                .notifyIf { _, v2 ->
                    v2.succeeded() && !TextUtils.isEmpty(v2.get())
                }
                .compile()
    }

    override fun update() {
        var intent: Intent = Intent(this, InfoActivity::class.java)
        intent.putExtra("platform", signal)
        intent.putExtra("info", mRep!!.get().get())
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        mRep?.let { it.removeUpdatable(this) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUEST_LOGIN) {
            var d = data?.extras?.getString("key_response")
            if (!TextUtils.isEmpty(d)) {
                var auth = Gson().fromJson(d, QQAuth::class.java)
                QQ!!.accept(Pair(auth.access_token, auth.openid))
            }
        }
    }
}