package com.barikoi.mapdemo.model

import com.google.gson.annotations.SerializedName


data class Hints (

  @SerializedName("visited_nodes.sum"     ) var visitedNodes_sum     : Long? = null,
  @SerializedName("visited_nodes.average" ) var visitedNodes_average : Long? = null

)