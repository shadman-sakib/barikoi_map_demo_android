package com.barikoi.mapdemo.model

import com.google.gson.annotations.SerializedName


data class Instructions (

  @SerializedName("distance"    ) var distance   : Double? = null,
  @SerializedName("heading"     ) var heading    : Double? = null,
  @SerializedName("sign"        ) var sign       : Int?    = null,
  @SerializedName("interval"    ) var interval   : List<Long>? = null,
  @SerializedName("text"        ) var text       : String? = null,
  @SerializedName("time"        ) var time       : Int?    = null,
  @SerializedName("street_name" ) var streetName : String? = null

)