package com.example.bungeoppang.retrofit

import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {
    @GET("users/userName")
    fun checkUserWithName(@Query("user_name") name:String): Call<UserResponse>

    @GET("users/user")
    fun checkUserWithId(@Query("user_id") id:Long):Call<UserResponse>

    @POST("users/register")
    fun registerUser(@Body user_body:JSONObject):Call<UserResponse>

    @GET("stores/{distance}/{x}/{y}")
    fun getStoresInDistance(@Path("distance") distance:Int, @Path("x") x:Double, @Path("y") y:Double):Call<DistanceStore>
}


// 회원 정보 조회 응답 바디
data class UserResponse(
    val ok: Boolean,
    val user: User
)

data class User(
    val __v: Int,
    val _id: String,
    val comments: List<Any>,
    val nickName: String,
    val picked: List<String>,
    val registered: List<Any>,
    val userId: Int
)

// 가게 조회 응답 바디
class DistanceStore : ArrayList<DistanceStoreItem>()

data class DistanceStoreItem(
    val __v: Int,
    val _id: String,
    val address: String,
    val comments: List<Any>,
    val createdOrEditedAt: String,
    val desc: String,
    val menus: List<Menu>,
    val registrant: Registrant,
    val storeName: String,
    val x: Double,
    val y: Double
)

data class Menu(
    val _id: String,
    val count: Int,
    val itemName: String,
    val price: Int
)

data class Registrant(
    val id: Int,
    val nickName: String
)