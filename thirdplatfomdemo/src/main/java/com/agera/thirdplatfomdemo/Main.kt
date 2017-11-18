package com.agera.thirdplatfomdemo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.agera.thirdplatfomdemo.bean.QQAuth
import com.agera.thirdplatfomdemo.core.TaskDriver
import com.agera.thirdplatfomdemo.http.HttpCallable
import com.agera.thirdplatfomdemo.http.HttpTask
import com.agera.thirdplatfomdemo.http.RESTful
import com.google.android.agera.BaseObservable
import com.google.android.agera.Repository
import com.google.android.agera.Result
import com.google.android.agera.Updatable
import com.google.android.agera.net.HttpRequest
import com.google.android.agera.net.HttpResponse
import com.google.gson.Gson
import com.sina.weibo.sdk.auth.Oauth2AccessToken
import com.sina.weibo.sdk.auth.WbAuthListener
import com.sina.weibo.sdk.auth.WbConnectErrorMessage
import com.sina.weibo.sdk.auth.sso.SsoHandler
import com.tencent.connect.UserInfo
import com.tencent.connect.auth.QQToken
import com.tencent.connect.common.Constants
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import java.util.concurrent.atomic.AtomicBoolean


/**
 * Created by Administrator on 2017/11/15 0015.
 */
class Main : Activity(), Updatable {

    var listener: IUiListener = object : IUiListener {
        override fun onComplete(p0: Any?) {
            Log.e("---", "--qq onComplete:" + p0)
          /*  var auth:String = p0 as String
            var QA:QQAuth = Gson().fromJson(auth,QQAuth::class.java)

            if (QA.ret == 100030){
                Log.e("---", "--qq reAuth:")
                mTencent?.reAuth(this@Main,"add_topic",this)
            }*/

        }

        override fun onCancel() {
            Log.e("---", "--qq onCancel:")
        }

        override fun onError(p0: UiError?) {
            Log.e("---", "--qq onError:" + p0)
        }
    }


    var clickEvent_QQ: LoginListener = LoginListener()
    var clickEvent_Sina: LoginListener = LoginListener()
    var QQ_Observale: Repository<Result<HttpResponse>>? = null
    var Sina_Observale: Repository<Result<HttpResponse>>? = null
    var activeOnceQQ: AtomicBoolean = AtomicBoolean(false)
    var activeOnceSina: AtomicBoolean = AtomicBoolean(false)
    var WeiBoHandler: SsoHandler? = null
    var mWBListener: WBListener? = null

    var mTencent: Tencent? = null
    var SCOPE: String = "get_user_info"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mTencent = Tencent.createInstance("1106465319", this)
        init()
        initEvents()
        initObservable()
    }

    private fun init() {
        WeiBoHandler = SsoHandler(this)
        mWBListener = WBListener(this)
    }

    private val function = {
        mTencent?.let { it.login(this, SCOPE, listener) }
    }

    private fun initEvents() {
//        findViewById(R.id.btn_qq).setOnClickListener(clickEvent_QQ)
        findViewById(R.id.btn_sina).setOnClickListener {
            WeiBoHandler?.let { it.authorize(mWBListener) }
        }


        findViewById(R.id.btn_qq).setOnClickListener {
            mTencent?.let { it.login(this, SCOPE, listener) }
        }
    }

    private fun initObservable() {
    }


    override fun update() {
    }

    class LoginListener : BaseObservable(), View.OnClickListener {
        override fun onClick(v: View?) {
            dispatchUpdate()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUEST_LOGIN) {
            var d: Bundle? = data?.extras
            var detail: String = ""
            d?.keySet()?.forEach {
                detail += "$it=${d.get(it)}\n"
            }

            var value: String = data?.extras?.get("key_response") as String
            var gson: Gson = Gson()
            var qq: QQAuth = gson.fromJson(value, QQAuth::class.java)
            Log.e("---", "--ret:${qq.ret}\naccess_token:${qq.access_token}\ndetail:$detail")
            mTencent?.let {
                it.openId = qq.openid
                var t1: QQToken = it.qqToken
                t1.setAccessToken(qq.access_token,it.expiresIn.toString())
                Log.e("---", "--appid:${t1.appId}\naccessToken:${t1.accessToken}\nopenId:${t1.openId}")
                var info: UserInfo = UserInfo(this, t1)
                info.getUserInfo(listener)
            }
        }
    }

    class WBListener(var act: Activity) : WbAuthListener {
        override fun onSuccess(p0: Oauth2AccessToken?) {
            Log.e("---", "---onSuccess:" + p0)
            p0?.let {
                if (it.isSessionValid) {
                    var req: HttpRequest = RESTful.getWeiBoUserInfo(p0.token, p0.uid)
                    var task = HttpTask(HttpCallable(req))
                    TaskDriver.instance().execute(task)
                    task.get()
                            .ifSucceededSendTo { s ->
                                Log.e("---", "--result:" + s.bodyString.get())
                            }
                            .ifFailedSendTo { e ->
                                Log.e("---", "--error:" + e.message)
                            }
                }
            }
        }

        override fun onFailure(p0: WbConnectErrorMessage?) {
            Log.e("---", "---onFailure:" + p0)
        }

        override fun cancel() {
            Log.e("---", "---cancel:")
        }

    }
}