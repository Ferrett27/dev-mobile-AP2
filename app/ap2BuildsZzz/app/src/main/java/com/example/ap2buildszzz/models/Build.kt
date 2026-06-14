package com.example.ap2buildszzz.models

data class Build(
    val id: Long? = null,
    val usuarioId: Long,
    val personagemId: Long,
    val armaId: Long,
    val disco4Id: Long,
    val disco2Id: Long,
    val statusDisco4: String,
    val statusDisco5: String,
    val statusDisco6: String
)