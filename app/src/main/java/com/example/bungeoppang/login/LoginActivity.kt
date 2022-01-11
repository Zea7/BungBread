package com.example.bungeoppang.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.example.bungeoppang.MainActivity
import com.example.bungeoppang.R
import com.example.bungeoppang.ServerConnect
import com.example.bungeoppang.Variables
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.File

class LoginActivity : AppCompatActivity() {
    companion object{
        lateinit var loginActivity: LoginActivity
    }
    private var loginButton: ImageButton? = null
    private var TAG: String = "Login"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //Log.d("hash", com.kakao.util.maps.helper.Utility.getKeyHash(this /* context */))

        Glide.with(this).load(R.drawable.bungplash).into(findViewById(R.id.iv_splash))
        Handler(Looper.getMainLooper()).postDelayed({
            checkLogIn()
            initView()
        }, 1500)
    }

    private fun initView() {
        loginActivity = this
        loginButton = findViewById(R.id.login_button)
        KakaoSdk.init(this, Variables.NATIVE_APP_KEY)

        setButton()

        if (AuthApiClient.instance.hasToken()) {
            UserApiClient.instance.accessTokenInfo { _, error ->
                if (error != null) {
                    if (error is KakaoSdkError && error.isInvalidTokenError()) {
                        //로그인 필요
                    } else {
                        //기타 에러
                    }
                } else {
                    getUserData()
                }
            }
        } else {
            //로그인 필요
        }

        loginButton!!.setOnClickListener { login() }
    }

    private fun setButton() {
        Glide.with(baseContext).load(R.drawable.kakao_login_large_narrow).into(loginButton!!)
    }

    fun login() {
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "로그인 실패", error)
            }
            else if (token != null) {
                Log.i(TAG, "로그인 성공 ${token.accessToken}")
                getUserData()
            }
        }


        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this, callback = callback)
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
        }
        UserApiClient.instance.me {user, error ->
            if(error != null){
                Log.e(TAG, "사용자 정보 요청 실패", error)
            }
            else if(user != null){
                var scopes = mutableListOf<String>()

                if(user.kakaoAccount?.profileImageNeedsAgreement == true) {scopes.add("profile")}

                if(scopes.count() > 0){
                    Log.d(TAG, "사용자에게 추가 동의를 받아야합니다")

                    UserApiClient.instance.loginWithNewScopes(this, scopes) { token, error ->
                        if (error != null) {
                            Log.e(TAG, "사용자 추가 동의 실패", error)
                        } else {
                            Log.d(TAG, "allowed scopes: ${token!!.scopes}")

                            // 사용자 정보 재요청
                            UserApiClient.instance.me { user, error ->
                                if (error != null) {
                                    Log.e(TAG, "사용자 정보 요청 실패", error)
                                }
                                else if (user != null) {
                                    Log.i(TAG, "사용자 정보 요청 성공")
                                    getUserData()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun logout(){
        UserApiClient.instance.unlink { error ->
            if (error != null) {
                Log.e(TAG, "연결 끊기 실패", error)
            }
            else {
                Log.i(TAG, "연결 끊기 성공. SDK에서 토큰 삭제 됨")
            }
        }
    }

    private fun getUserData(){
        val intent1 = Intent(this, NickNameActivity::class.java)

        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "사용자 정보 요청 실패", error)
            }
            else if (user != null) {
                var s:Boolean = false
                Log.i(TAG, "사용자 정보 요청 성공" +
                        "\n회원번호: ${user.id}" +
                        "\n이메일: ${user.kakaoAccount?.email}" +
                        "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                        "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}")
                CoroutineScope(Dispatchers.Main).launch{
                    CoroutineScope(Dispatchers.IO).launch{
                        s = ServerConnect.checkUserWithId(user.id!!, baseContext)
                    }.join()

                    Log.d(TAG, s.toString())
                    if(!s){
                        intent1.putExtra("id", user.id)
                        Log.d(TAG, String.format("%d", user.id))
                        startActivity(intent1)
                        finish()
                    } else{
                        startActivity(Intent(applicationContext , MainActivity::class.java))
                        finish()
                        //findViewById<ConstraintLayout>(R.id.container).visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun checkLogIn(){
        val file = File(this.filesDir,"login.json")
        val intent1 = Intent(this, MainActivity::class.java)

        if(file.exists()){
            try{
                val data = file.readText(Charsets.UTF_8)
                val json = JSONObject(data)
                var s:Boolean

                if(file.exists() && json.has("id"))
                    CoroutineScope(Dispatchers.Main).launch{
                        s = ServerConnect.checkUser(
                            json.getLong("id"),
                            json.getString("nickName"),
                            baseContext
                        )
                        if(s){
                            Variables.USER_ID = json.getLong("id")
                            Variables.USER_NAME = json.getString("nickName")
                            Toast.makeText(
                                baseContext,
                                String.format("%s님 환영합니다!", Variables.USER_NAME),
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(intent1)
                            finish()
                        } else {
                            Log.d("Splash", "Not FOUND")
                            Toast.makeText(baseContext, "로그인 실패! 로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
                            findViewById<ConstraintLayout>(R.id.container).visibility = View.VISIBLE
                        }
                    }


            } catch(e:Exception){
                Log.d("Splash", "No JSON")
                findViewById<ConstraintLayout>(R.id.container).visibility = View.VISIBLE
            }
        } else{
            Log.d("Splash", "No FILE")
            findViewById<ConstraintLayout>(R.id.container).visibility = View.VISIBLE
        }
    }


}
