package com.example.bungeoppang

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.net.URL
import javax.net.ssl.HttpsURLConnection


class Temp : AppCompatActivity(){
    private var requestQueue:RequestQueue? = null
    private val SERVER_URL = "http://192.249.18.168/api/"
    private val LOGIN_URI = "users/register"
    private var getId:EditText? = null
    private var getName:EditText? = null
    private var button: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temp)
        requestQueue = Volley.newRequestQueue(applicationContext)
        getId = findViewById(R.id.id)
        getName = findViewById(R.id.name)
        button = findViewById(R.id.send)
        button?.setOnClickListener{
            sendUserAddRequest()
        }
    }

    private fun sendUserAddRequest(){
        val requestJson = JSONObject()
        requestJson.put("userId", getId?.text)
        requestJson.put("nickName", getName?.text)

        print(requestJson)

        val request = object:JsonObjectRequest(Method.POST, SERVER_URL + LOGIN_URI, requestJson,
            {
              try{
                  val answerJson = it.getJSONObject("userInfo")
                  val id = answerJson.getInt("userId")
                  val nickname = answerJson.getString("nickName")
                  Log.d("ddd", String.format("%d %s", id, nickname))
              }
              catch (e: JSONException){
                  e.printStackTrace()
              }

        }, {
            Log.e("ddd", "RequestFailed")
                Log.e("ddd", it.message!!)


        }){
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"

                return headers
            }

        }
        requestQueue?.add(request)

    }
}