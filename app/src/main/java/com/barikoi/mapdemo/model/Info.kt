package com.barikoi.mapdemo.model

import com.google.gson.annotations.SerializedName


data class Info (

  @SerializedName("copyrights" ) var copyrights : ArrayList<String> = arrayListOf(),
  @SerializedName("took"       ) var took       : Int?              = null

)