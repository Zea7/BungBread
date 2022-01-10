package com.example.bungeoppang.api

import com.example.bungeoppang.data.Coord
import com.example.bungeoppang.data.Stores
import com.example.bungeoppang.data.User
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {
    @GET("users/user")
    fun getUserByUserId(
        @Query("user_id") userId: Int
    ): Call<User>

    @GET("stores/user/{user_id}")
    fun getStoresByUserId(
        @Path("user_id") userId: Int,
    ): Call<Stores>

    @GET("stores/store/{store_id}")
    fun getStoreByStoreId(
        @Path("store_id") storeId: String,
    ): Call<Stores.StoreItem>

    @GET("stores/{distance}/{x}/{y}")
    fun getStoresByDistance(
        @Path("distance") distance: Int,
        @Path("x") x: Double,
        @Path("y") y: Double
    ): Call<Stores>
}