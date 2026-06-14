package com.example.ap2buildszzz.activities

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.ap2buildszzz.R
import com.example.ap2buildszzz.conexaoBanco.RetrofitClient
import com.example.ap2buildszzz.models.Build
import com.example.ap2buildszzz.utils.UsuarioLogado
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetalhesBuildActivity : AppCompatActivity() {

    private lateinit var btnSalvarFavorito: Button
    private var buildId: Long = -1L
    private val usuarioLogadoId = UsuarioLogado.usuarioLogado?.id

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes_build)

        buildId = intent.getLongExtra("BUILD_ID", -1)
        val buildOwnerId = intent.getLongExtra("BUILD_USER_ID", -2)
        val personagemNome = intent.getStringExtra("PERSONAGEM_NOME")
        val personagemImg = intent.getStringExtra("PERSONAGEM_IMG")
        val armaNome = intent.getStringExtra("ARMA_NOME")
        val armaImg = intent.getStringExtra("ARMA_IMG")
        val disco4Nome = intent.getStringExtra("DISCO4_NOME")
        val disco4Img = intent.getStringExtra("DISCO4_IMG")
        val disco2Nome = intent.getStringExtra("DISCO2_NOME")
        val disco2Img = intent.getStringExtra("DISCO2_IMG")
        val status4 = intent.getStringExtra("STATUS_4")
        val status5 = intent.getStringExtra("STATUS_5")
        val status6 = intent.getStringExtra("STATUS_6")

        val tvPersonagem = findViewById<TextView>(R.id.tv_detalhe_personagem)
        val tvArma = findViewById<TextView>(R.id.tv_detalhe_arma)
        val tvDisco4 = findViewById<TextView>(R.id.tv_detalhe_disco4)
        val tvDisco2 = findViewById<TextView>(R.id.tv_detalhe_disco2)
        val tvStatus = findViewById<TextView>(R.id.tv_detalhe_status)
        val ivPersonagem = findViewById<ImageView>(R.id.iv_detalhe_personagem)
        val ivArma = findViewById<ImageView>(R.id.iv_detalhe_arma)
        val ivDisco4 = findViewById<ImageView>(R.id.iv_detalhe_disco4)
        val ivDisco2 = findViewById<ImageView>(R.id.iv_detalhe_disco2)
        btnSalvarFavorito = findViewById(R.id.btn_salvar_favorito)

        if (buildOwnerId == usuarioLogadoId) {
            btnSalvarFavorito.text = "SUA BUILD"
            btnSalvarFavorito.isEnabled = false
            btnSalvarFavorito.setBackgroundColor(Color.DKGRAY)
        } else {
            btnSalvarFavorito.text = "Verificando..."
            btnSalvarFavorito.isEnabled = false
            verificarSeBuildJaEstaSalva()
        }

        tvPersonagem.text = personagemNome
        tvArma.text = armaNome
        tvDisco4.text = "Conjunto 4x: $disco4Nome"
        tvDisco2.text = "Conjunto 2x: $disco2Nome"
        tvStatus.text = "Slot IV: $status4\nSlot V: $status5\nSlot VI: $status6"

        Glide.with(this).load(personagemImg).circleCrop().into(ivPersonagem)
        Glide.with(this).load(armaImg).into(ivArma)
        Glide.with(this).load(disco4Img).into(ivDisco4)
        Glide.with(this).load(disco2Img).into(ivDisco2)

        btnSalvarFavorito.setOnClickListener {
            if (usuarioLogadoId == null || buildId == -1L) return@setOnClickListener

            btnSalvarFavorito.text = "Salvando..."
            btnSalvarFavorito.isEnabled = false

            RetrofitClient.api.salvarBuildFavorita(usuarioLogadoId, buildId).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@DetalhesBuildActivity, "Build salva!", Toast.LENGTH_SHORT).show()
                        marcarComoSalva()
                    } else {
                        liberarBotaoSalvar()
                    }
                }
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    liberarBotaoSalvar()
                }
            })
        }
    }

    private fun verificarSeBuildJaEstaSalva() {
        if (usuarioLogadoId != null && buildId != -1L) {
            RetrofitClient.api.listarBuildsSalvas(usuarioLogadoId, usuarioLogadoId).enqueue(object : Callback<List<Build>> {
                override fun onResponse(call: Call<List<Build>>, response: Response<List<Build>>) {
                    if (response.isSuccessful) {
                        val jaEstaSalva = response.body()?.any { it.id == buildId } == true
                        if (jaEstaSalva) marcarComoSalva() else liberarBotaoSalvar()
                    } else liberarBotaoSalvar()
                }
                override fun onFailure(call: Call<List<Build>>, t: Throwable) { liberarBotaoSalvar() }
            })
        }
    }

    private fun marcarComoSalva() {
        btnSalvarFavorito.text = "JÁ SALVA"
        btnSalvarFavorito.isEnabled = false
        btnSalvarFavorito.setBackgroundColor(Color.DKGRAY)
        btnSalvarFavorito.setTextColor(Color.WHITE)
    }

    private fun liberarBotaoSalvar() {
        btnSalvarFavorito.text = "Salvar Build"
        btnSalvarFavorito.isEnabled = true
        btnSalvarFavorito.setBackgroundColor(Color.WHITE)
        btnSalvarFavorito.setTextColor(Color.BLACK)
    }
}