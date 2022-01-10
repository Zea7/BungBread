package com.example.bungeoppang.data


import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("ok")
    val ok: Boolean,
    @SerializedName("user")
    val user: UserItem
) {
    data class UserItem(
        @SerializedName("comments")
        val comments: List<Any>,
        @SerializedName("_id")
        val id: String,
        @SerializedName("nickName")
        val nickName: String,
        @SerializedName("picked")
        val picked: List<String>,
        @SerializedName("registered")
        val registered: List<Any>,
        @SerializedName("userId")
        val userId: Int,
        @SerializedName("__v")
        val v: Int
    )
}