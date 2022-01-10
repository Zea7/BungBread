package com.example.bungeoppang

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class ServerConnect {
    companion object{
        fun sendStoreInfo(address:String, name:String, user_id:Long, user_name:String, latitude:Double, longitude:Double, menu:JSONArray?, comment:String, activity: Activity) {
            val json = JSONObject()
            val user_json = JSONObject()
            user_json.put("id", user_id)
            user_json.put("nickName", user_name)
            json.put("storeName", name)
            json.put("registrant", user_json)
            json.put("address", address)
            json.put("x", latitude)
            json.put("y", longitude)
            json.put("desc", comment)
            json.put("menus", menu)
            Log.d("FFFF", json.toString())

            val requestQueue = Volley.newRequestQueue(activity)

            val request = object :
                JsonObjectRequest(Method.POST, Variables.SERVER_URL + Variables.ADD_STORE, json, {
                    Log.d("ServerRequestToAddStore", "보낸 정보")
                    Log.d("ServerRequestToAddStore", json.toString())
                    if(it.getBoolean("ok")) {
                        Log.d("ServerRequestToAddStore", "성공적으로 정보를 올렸습니다.")
                        Log.d("ServerRequestToAddStore", it.toString())
                        activity.finish()
                        AddStore.addStoreActivity?.goBackToMain()
                    }
                    else{
                        Log.d("ServerRequestToAddStore", "실패!")
                        Log.d("ServerRequestToAddStore", it.toString())
                    }


                }, {
                    Log.e("ServerRequestToAddStore", "업로드 실패!")
                }){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-Type"] = "application/json"

                    return headers
                }
                }
            requestQueue.add(request)
        }
    }
}