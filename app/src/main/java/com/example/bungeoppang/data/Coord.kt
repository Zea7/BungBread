package com.example.bungeoppang.data


import com.google.gson.annotations.SerializedName

data class Coord(
    @SerializedName("x")
    val x: Double,
    @SerializedName("y")
    val y: Double
)