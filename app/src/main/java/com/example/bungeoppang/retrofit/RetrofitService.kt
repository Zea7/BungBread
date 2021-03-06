package com.example.bungeoppang.retrofit

import com.example.bungeoppang.ServerConnect
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
    fun registerUser(@Body userpost: ServerConnect.Companion.UserPost):Call<UserResponse>

    @GET("stores/{distance}/{x}/{y}")
    fun getStoresInDistance(@Path("distance") distance:Int, @Path("x") x:Double, @Path("y") y:Double):Call<DistanceStore>

    @GET("stores/store/coord/{x}/{y}")
    fun getStoresByLocation(@Path("x") x:Double, @Path("y") y:Double):Call<StoreLocation>

    @POST("users/pick")
    fun pickStore(@Query("user_id") id:Long, @Query("store_id") store_id:String):Call<StorePick>

    @POST("stores/addComment")
    fun postComment(@Query("user_id") id:Long, @Query("store_id") storeId:String, @Query("contents") contents: String):Call<CommentResponse>
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
    val comments: List<Comment>,
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


data class StoreLocation(
    val ok: Boolean,
    val store: Store
)

data class Store(
    val __v: Int,
    val _id: String,
    val address: String,
    val comments: List<Comment>,
    val createdOrEditedAt: String,
    val desc: String,
    val menus: List<Menu>,
    val registrant: Registrant,
    val storeName: String,
    val x: Double,
    val y: Double
)


data class Comment(
    val __v: Int,
    val _id: String,
    val contents: String,
    val createdAt: String,
    val writer: Int
)


data class StorePick(
    val ok: Boolean,
    val user: User
)


data class CommentResponse(
    val ok: Boolean,
    val result: Comment
)
