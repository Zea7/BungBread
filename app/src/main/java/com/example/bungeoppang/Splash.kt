package com.example.bungeoppang

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import org.json.JSONObject
import java.io.File
import java.io.FileNotFoundException

class Splash : AppCompatActivity() {
    private fun checkLogIn(){
        val file = File(application.filesDir,"login.json")

        if(file.exists()){
            try{
                val data = file.readText(Charsets.UTF_8)
                val json = JSONObject(data)
                Variables.USER_ID = json.getLong("id")
                Variables.USER_NAME = json.getString("nickName")
                Toast.makeText(this, String.format("%s님 환영합니다!", Variables.USER_NAME), Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()

            } catch(e:FileNotFoundException){
                Toast.makeText(this, "로그인 실패! 로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        checkLogIn()
    }
}