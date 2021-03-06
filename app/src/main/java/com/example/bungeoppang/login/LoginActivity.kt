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
                        //๋ก๊ทธ์ธ ํ์
                    } else {
                        //๊ธฐํ ์๋ฌ
                    }
                } else {
                    getUserData()
                }
            }
        } else {
            //๋ก๊ทธ์ธ ํ์
        }

        loginButton!!.setOnClickListener { login() }
    }

    private fun setButton() {
        Glide.with(baseContext).load(R.drawable.kakao_login_large_narrow).into(loginButton!!)
    }

    fun login() {
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "๋ก๊ทธ์ธ ์คํจ", error)
            }
            else if (token != null) {
                Log.i(TAG, "๋ก๊ทธ์ธ ์ฑ๊ณต ${token.accessToken}")
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
                Log.e(TAG, "์ฌ์ฉ์ ์?๋ณด ์์ฒญ ์คํจ", error)
            }
            else if(user != null){
                var scopes = mutableListOf<String>()

                if(user.kakaoAccount?.profileImageNeedsAgreement == true) {scopes.add("profile")}

                if(scopes.count() > 0){
                    Log.d(TAG, "์ฌ์ฉ์์๊ฒ ์ถ๊ฐ ๋์๋ฅผ ๋ฐ์์ผํฉ๋๋ค")

                    UserApiClient.instance.loginWithNewScopes(this, scopes) { token, error ->
                        if (error != null) {
                            Log.e(TAG, "์ฌ์ฉ์ ์ถ๊ฐ ๋์ ์คํจ", error)
                        } else {
                            Log.d(TAG, "allowed scopes: ${token!!.scopes}")

                            // ์ฌ์ฉ์ ์?๋ณด ์ฌ์์ฒญ
                            UserApiClient.instance.me { user, error ->
                                if (error != null) {
                                    Log.e(TAG, "์ฌ์ฉ์ ์?๋ณด ์์ฒญ ์คํจ", error)
                                }
                                else if (user != null) {
                                    Log.i(TAG, "์ฌ์ฉ์ ์?๋ณด ์์ฒญ ์ฑ๊ณต")
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
                Log.e(TAG, "์ฐ๊ฒฐ ๋๊ธฐ ์คํจ", error)
            }
            else {
                Log.i(TAG, "์ฐ๊ฒฐ ๋๊ธฐ ์ฑ๊ณต. SDK์์ ํ?ํฐ ์ญ์? ๋จ")
            }
        }
    }

    private fun getUserData(){
        val intent1 = Intent(this, NickNameActivity::class.java)

        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "์ฌ์ฉ์ ์?๋ณด ์์ฒญ ์คํจ", error)
            }
            else if (user != null) {
                var s:Boolean = false
                Log.i(TAG, "์ฌ์ฉ์ ์?๋ณด ์์ฒญ ์ฑ๊ณต" +
                        "\nํ์๋ฒํธ: ${user.id}" +
                        "\n์ด๋ฉ์ผ: ${user.kakaoAccount?.email}" +
                        "\n๋๋ค์: ${user.kakaoAccount?.profile?.nickname}" +
                        "\nํ๋กํ์ฌ์ง: ${user.kakaoAccount?.profile?.thumbnailImageUrl}")
                Variables.profileUrl = user.kakaoAccount?.profile?.thumbnailImageUrl!!
                
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
                                String.format("%s๋ ํ์ํฉ๋๋ค!", Variables.USER_NAME),
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(intent1)
                            finish()
                        } else {
                            Log.d("Splash", "Not FOUND")
                            Toast.makeText(baseContext, "๋ก๊ทธ์ธ ์คํจ! ๋ก๊ทธ์ธ์ด ํ์ํฉ๋๋ค.", Toast.LENGTH_SHORT).show()
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
