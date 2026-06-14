package com.example.ap2buildszzz.conexaoBanco

import com.example.ap2buildszzz.models.Arma
import com.example.ap2buildszzz.models.Build
import com.example.ap2buildszzz.models.Disco
import com.example.ap2buildszzz.models.Inventario
import com.example.ap2buildszzz.models.Personagem
import com.example.ap2buildszzz.models.Usuario
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("api/builds")
    fun listarBuilds(): Call<List<Build>>

    @POST("api/builds")
    fun criarBuild(
        @Query("usuarioId") usuarioId: Long,
        @Query("personagemId") personagemId: Long,
        @Query("armaId") armaId: Long,
        @Query("disco4Id") disco4Id: Long,
        @Query("disco2Id") disco2Id: Long,
        @Query("status4") status4: String,
        @Query("status5") status5: String,
        @Query("status6") status6: String
    ): Call<Build>

    @POST("api/usuarios/criar")
    fun criarUsuario(
        @Query("nome") nome: String,
        @Query("email") email: String,
        @Query("senha") senha: String
    ): Call<Usuario>

    @POST("api/usuarios/login")
    fun login(
        @Query("email") email: String,
        @Query("senha") senha: String
    ): Call<Usuario>

    @GET("api/personagens")
    fun listarPersonagens(): Call<List<Personagem>>

    @GET("api/armas")
    fun listarArmas(): Call<List<Arma>>

    @GET("api/discos")
    fun listarDiscos(): Call<List<Disco>>

    @POST("api/inventarios")
    fun cadastrarInventario(
        @Query("usuarioId") usuarioId: Long,
        @Query("personagemId") personagemId: Long,
        @Query("armaId") armaId: Long,
        @Query("disco4Id") disco4Id: Long,
        @Query("disco2Id") disco2Id: Long,
        @Query("statusDisco4") statusDisco4: String,
        @Query("statusDisco5") statusDisco5: String,
        @Query("statusDisco6") statusDisco6: String,
        @Query("totalSubstatus") totalSubstatus: Int
    ): Call<Inventario>

    @POST("api/usuarios/{usuarioId}/builds-salvas/{buildId}")
    fun salvarBuildFavorita(
        @Path("usuarioId") usuarioId: Long,
        @Path("buildId") buildId: Long
    ): Call<Void>

    @GET("api/usuarios/{caminhoId}/builds-salvas")
    fun listarBuildsSalvas(
        @Path("caminhoId") caminhoId: Long,
        @Query("usuarioId") queryId: Long
    ): Call<List<Build>>

    @DELETE("api/builds/{id}")
    fun deletarBuild(@Path("id") id: Long): Call<Void>

    @DELETE("api/usuarios/{usuarioId}/builds-salvas/{buildId}")
    fun removerBuildSalva(
        @Path("usuarioId") usuarioId: Long,
        @Path("buildId") buildId: Long
    ): Call<Void>

    @GET("api/inventarios/usuario/{usuarioId}")
    fun listarInventarioPorUsuario(@Path("usuarioId") usuarioId: Long): Call<List<Inventario>>

    @DELETE("api/inventarios/{id}")
    fun deletarInventario(@Path("id") id: Long): Call<Void>

    @PUT("api/inventarios/{id}")
    fun atualizarInventario(
        @Path("id") id: Long,
        @Query("armaId") armaId: Long,
        @Query("disco4Id") disco4Id: Long,
        @Query("disco2Id") disco2Id: Long,
        @Query("statusDisco4") statusDisco4: String,
        @Query("statusDisco5") statusDisco5: String,
        @Query("statusDisco6") statusDisco6: String,
        @Query("totalSubstatus") totalSubstatus: Int
    ): Call<Inventario>

}