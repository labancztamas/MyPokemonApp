package com.labancztamas.mypokemonapp.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonResponse(
    val abilities: List<Ability>,
    val height: Int,
    val name: String,
    @SerialName("sprites")
    val image: Sprites,
    val weight: Int,
    val types: List<PokemonType>
)

@Serializable
data class PokemonType(
    val type: TypeX
)

@Serializable
data class TypeX(
    val name: String,
)

@Serializable
data class Sprites(
    val other: Other
)

@Serializable
data class Other(
    val home: Home,
)

@Serializable
data class Home(
    @SerialName("front_default")
    val imageUrl: String,
)

@Serializable
data class Ability(
    val ability: AbilityX,
    @SerialName("is_hidden")
    val isHidden: Boolean,
)

@Serializable
data class AbilityX(
    val name: String
)
