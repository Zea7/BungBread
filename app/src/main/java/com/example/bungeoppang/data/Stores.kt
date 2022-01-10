package com.example.bungeoppang.data


import com.google.gson.annotations.SerializedName

class Stores : ArrayList<Stores.StoreItem>(){
    data class StoreItem(
        @SerializedName("address")
        val address: String,
        @SerializedName("comments")
        val comments: List<Any>,
        @SerializedName("createdOrEditedAt")
        val createdOrEditedAt: String,
        @SerializedName("desc")
        val desc: String,
        @SerializedName("_id")
        val id: String,
        @SerializedName("menus")
        val menus: List<Menu>,
        @SerializedName("registrant")
        val registrant: Registrant,
        @SerializedName("storeName")
        val storeName: String,
        @SerializedName("__v")
        val v: Int,
        @SerializedName("x")
        val x: Double,
        @SerializedName("y")
        val y: Double
    ) {
        data class Menu(
            @SerializedName("count")
            val count: Int,
            @SerializedName("_id")
            val id: String,
            @SerializedName("itemName")
            val itemName: String,
            @SerializedName("price")
            val price: Int
        )
    
        data class Registrant(
            @SerializedName("id")
            val id: Int,
            @SerializedName("nickName")
            val nickName: String
        )
    }
}