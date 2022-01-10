package com.example.bungeoppang.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.example.bungeoppang.MainActivity
import com.example.bungeoppang.R
import com.example.bungeoppang.ServerConnect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NickNameActivity : AppCompatActivity() {
    private lateinit var button:Button
    private lateinit var nickname:EditText
    private var name:String? = null
    private var id :Long = 0
    private var intent1 = Intent(this, MainActivity::class.java)

    fun getInfo(){
        id = intent.getLongExtra("id", id)
        Log.d("NickName", String.format("%d", id))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nick_name)
        nickname = findViewById(R.id.enter_nickname)
        button = findViewById(R.id.nickname_save)

        getInfo()
        var s = false

        button.setOnClickListener{
            name = nickname.text.toString()
            CoroutineScope(Dispatchers.Main).launch{
                CoroutineScope(Dispatchers.IO).launch{
                    s = ServerConnect.registerUser(id, name!!, baseContext)
                }.join()

                if(s){
                    startActivity(intent1)
                }
            }

        }
    }
}