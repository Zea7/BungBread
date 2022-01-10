package com.example.bungeoppang.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.bungeoppang.MainActivity
import com.example.bungeoppang.R
import com.example.bungeoppang.ServerConnect
import com.example.bungeoppang.Variables
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.File

class Splash : AppCompatActivity() {
    private fun checkLogIn(){
        val file = File(this.filesDir,"login.json")
        val intent1 = Intent(this, MainActivity::class.java)
        val intent2  = Intent(this, LoginActivity::class.java)

        if(file.exists()){
            try{
                val data = file.readText(Charsets.UTF_8)
                val json = JSONObject(data)
                var s:Boolean = false
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
                            startActivity(intent2)
                            finish()
                        }
                    }


            } catch(e:Exception){
                Log.d("Splash", "No JSON")
                Toast.makeText(this, "로그인 실패! 로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        } else{
            Log.d("Splash", "No FILE")
            Toast.makeText(this, "로그인 실패! 로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        checkLogIn()
    }
}