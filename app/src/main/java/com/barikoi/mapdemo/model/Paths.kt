package com.barikoi.mapdemo.model

import com.google.gson.annotations.SerializedName


data class Paths (

  @SerializedName("distance"          ) var distance         : Double?                 = null,
  @SerializedName("weight"            ) var weight           : Double?                 = null,
  @SerializedName("time"              ) var time             : Int?                    = null,
  @SerializedName("transfers"         ) var transfers        : Int?                    = null,
  @SerializedName("points_encoded"    ) var pointsEncoded    : Boolean?                = null,
  @SerializedName("bbox"              ) var bbox             : ArrayList<Double>       = arrayListOf(),
  @SerializedName("points"            ) var points           : Points?                 = null,
  @SerializedName("instructions"      ) var instructions     : ArrayList<Instructions> = arrayListOf(),
  @SerializedName("legs"              ) var legs             : ArrayList<String>       = arrayListOf(),
//  @SerializedName("details"           ) var details          : Details?                = Details(),
  @SerializedName("ascend"            ) var ascend           : Int?                    = null,
  @SerializedName("descend"           ) var descend          : Int?                    = null,
  @SerializedName("snapped_waypoints" ) var snappedWaypoints : Points?                 = null

)