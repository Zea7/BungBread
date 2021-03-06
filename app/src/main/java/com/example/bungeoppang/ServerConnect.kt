package com.example.bungeoppang

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.bungeoppang.addStore.AddStoreActivity
import com.example.bungeoppang.retrofit.DistanceStore
import com.example.bungeoppang.retrofit.DistanceStoreItem
import com.example.bungeoppang.retrofit.RetrofitService
import com.example.bungeoppang.retrofit.UserResponse
import com.example.bungeoppang.retrofit.*
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.lang.Exception

class ServerConnect {

    companion object{
        var answer = false
        val retrofit = Retrofit.Builder().baseUrl(Variables.SERVER_URL).addConverterFactory(
            GsonConverterFactory.create()).build()

        val server: RetrofitService = retrofit.create(RetrofitService::class.java)


        fun sendStoreInfo(address:String, name:String, user_id:Long, user_name:String, latitude:Double, longitude:Double, menu:JSONArray?, comment:String, activity: Activity) {
            val json = JSONObject()
            val user_json = JSONObject()
            user_json.put("id", Variables.USER_ID)
            user_json.put("nickName", Variables.USER_NAME)
            json.put("storeName", name)
            json.put("registrant", user_json)
            json.put("address", address)
            json.put("x", longitude)
            json.put("y", latitude)
            json.put("desc", comment)
            json.put("menus", menu)
            Log.d("FFFF", json.toString())

            val requestQueue = Volley.newRequestQueue(activity)

            val request = object :
                JsonObjectRequest(Method.POST, Variables.SERVER_URL + Variables.ADD_STORE, json, {
                    Log.d("ServerRequestToAddStore", "?????? ??????")
                    Log.d("ServerRequestToAddStore", json.toString())
                    if(it.getBoolean("ok")) {
                        Log.d("ServerRequestToAddStore", "??????????????? ????????? ???????????????.")
                        Log.d("ServerRequestToAddStore", it.toString())
                        activity.finish()
                        AddStoreActivity.addStoreActivity?.goBackToMain()
                    }
                    else{
                        Log.d("ServerRequestToAddStore", "??????!")
                        Log.d("ServerRequestToAddStore", it.toString())
                    }


                }, {
                    Log.d("ServerRequestToAddStore", "?????? ??????")
                    Log.d("ServerRequestToAddStore", json.toString())
                    Log.e("ServerRequestToAddStore", "????????? ??????!")
                }){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-Type"] = "application/json"

                    return headers
                }
                }
            requestQueue.add(request)
        }

        data class UserPost(
            @SerializedName("userId")
            val userId: Long,
            @SerializedName("nickName")
            val nickName: String
        )
        suspend fun registerUser(id:Long, name:String, context:Context): Boolean{
            var answer = false
            var response:Response<UserResponse>? = null
            CoroutineScope(Dispatchers.IO).launch {
                kotlin.runCatching {
                    response = server.registerUser(UserPost(id, name)).execute()
                }
            }.join()
            Log.d("rrr", response!!.body().toString())
            if(response!!.body()?.ok == true) {
                answer = true
                val idd = response!!.body()?.user?.userId?.toLong()
                val namee = response!!.body()?.user?.nickName
                //makeFileOrChange(idd!!, namee!!, context)

                //Toast.makeText(context, "???????????? ?????????????????????.", Toast.LENGTH_SHORT).show()
            }
            else{
                //Toast.makeText(context, "?????? ???????????? ??????????????????.", Toast.LENGTH_SHORT).show()
            }

            return answer
        }

        suspend fun sendComment(id:Long, store_id:String, contents:String){
            var response:Response<CommentResponse>? =null
            CoroutineScope(Dispatchers.IO).launch {
                kotlin.runCatching {
                    response = server.postComment(id, store_id, contents).execute()
                }
            }.join()
            Log.d("CommentAdd", response!!.body()!!.toString())
        }

        suspend fun checkUserWithId(id:Long, context:Context):Boolean{
            var answer:Boolean = false
            var response:Response<UserResponse>? = null

            CoroutineScope(Dispatchers.IO).launch{
                kotlin.runCatching {
                    response = server.checkUserWithId(id).execute()
                    Variables.USER_ID = id
                    Variables.USER_NAME = response!!.body()!!.user.nickName
                }

            }.join()


            Log.d("ServerConnectionToCheckUserWithID", response!!.body().toString())
            if(response!!.body()?.ok == true){
                answer = true

            }
            return answer
        }

        suspend fun checkUser(id:Long, name:String, context:Context):Boolean{
            var answer = false
            var response:Response<UserResponse>? = null

            CoroutineScope(Dispatchers.IO).launch{
                kotlin.runCatching {
                    response = server.checkUserWithName(name).execute()
                }

            }.join()


            Log.d("ServerConnectionToCheckUserWithName", response!!.body().toString())
            if(response!!.body()?.ok == true){
                answer = true

            }
            return answer

        }

        suspend fun getStoreByDistance(distance:Int, latitude:Double, longitude:Double, context:Context):ArrayList<DistanceStoreItem>{
            var response:Response<DistanceStore>? = null
            val jsons = ArrayList<DistanceStoreItem>()

            Log.d("GetStoreByDistance", latitude.toString())

            val coord = JSONObject()
            coord.put("x", longitude)
            coord.put("y", latitude)

            val requestJson = JSONObject()

            CoroutineScope(Dispatchers.IO).launch{
                kotlin.runCatching {
                    requestJson.put("coord", latitude)
                    response = server.getStoresInDistance(distance, longitude, latitude).execute()
                }
            }.join()

            val gson = Gson()

            if(response != null && response!!.body() != null){
                Log.d("GetStoreByDistance", response!!.body()!!.toString())
                for(i in response!!.body()!!){

                    jsons.add(i)
                }
            }
            Log.d("GetStoreByDistance", jsons.toString())
            return jsons
        }

        suspend fun storeget(latitude:Double, longitude:Double, context:Context):Response<StoreLocation>{

            var result: Response<StoreLocation>?=null
            CoroutineScope(Dispatchers.IO).launch{
                kotlin.runCatching {
                    result = server.getStoresByLocation(longitude, latitude).execute()
                }
            }.join()

            return result!!
        }

        suspend fun getStoreByLocation(latitude:Double, longitude: Double, context:Context):StoreLocation{
            var answer:StoreLocation? = null
            lateinit var result:Response<StoreLocation>
            CoroutineScope(Dispatchers.IO).launch{
                result = storeget(latitude, longitude, context)
            }.join()
            val gson = Gson()
            Log.d("Result", result.body()!!.toString())
            if(result.body()!!.ok){
                answer = result.body()!!
            }
            return answer!!
        }


        fun makeFileOrChange(id:Long, name:String, context:Context){
            val file = File(context.filesDir,"login.json")
            try {
                val data = file.readText(Charsets.UTF_8)
                val json = JSONObject(data)
                Log.d(
                    "ServerLogin",
                    json.getLong("id").toString() + " " + json.getString("nickName")
                )
            } catch (e: Exception){
                e.printStackTrace()
                Log.e("ServerLogin", "No file")
            }
        }

        suspend fun pickStoreByUser(id:Long, store_id:String):Boolean{
            var answer = false
            lateinit var response:Response<StorePick>
            CoroutineScope(Dispatchers.IO).launch {
                kotlin.runCatching {
                    response = server.pickStore(id, store_id).execute()
                }
            }.join()
            if(response.body()!!.ok) answer = true
            return answer
        }
    }
}