package com.labancztamas.mypokemonapp.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TypeResponse(
    @SerialName("results")
    val types: List<Result>
)

@Serializable
data class Result(
    @SerialName("name")
    val name: String,
)