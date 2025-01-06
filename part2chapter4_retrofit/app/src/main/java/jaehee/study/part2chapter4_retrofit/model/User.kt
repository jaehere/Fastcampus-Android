package jaehee.study.part2chapter4_retrofit.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("login")
    val username: String,
    @SerializedName("id")
    val id: Int,
)