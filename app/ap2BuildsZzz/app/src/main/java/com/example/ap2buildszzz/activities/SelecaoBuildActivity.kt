package com.example.ap2buildszzz.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ap2buildszzz.R
import com.example.ap2buildszzz.adapters.BuildAdapter
import com.example.ap2buildszzz.conexaoBanco.RetrofitClient
import com.example.ap2buildszzz.models.Arma
import com.example.ap2buildszzz.models.Build
import com.example.ap2buildszzz.models.Disco
import com.example.ap2buildszzz.models.Personagem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SelecaoBuildActivity : AppCompatActivity() {

    private lateinit var recyclerSelecionar: RecyclerView

    private var personagemId: Long = -1L
    private var personagemNome: String = ""
    private var minhaArmaId: Long = -1L
    private var meuDisco4Id: Long = -1L
    private var meuDisco2Id: Long = -1L
    private var meuSubstatus: Int = 0
    private var meuStatus4: String = ""
    private var meuStatus5: String = ""
    private var meuStatus6: String = ""

    private var listaPersonagens = listOf<Personagem>()
    private var listaArmas = listOf<Arma>()
    private var listaDiscos = listOf<Disco>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selecao_build)

        recyclerSelecionar = findViewById(R.id.recycler_selecionar)
        recyclerSelecionar.layoutManager = LinearLayoutManager(this)

        personagemId = intent.getLongExtra("PERSONAGEM_ID", -1L)
        personagemNome = intent.getStringExtra("PERSONAGEM_NOME") ?: "Agente"
        minhaArmaId = intent.getLongExtra("MINHA_ARMA_ID", -1L)
        meuDisco4Id = intent.getLongExtra("MEU_DISCO4_ID", -1L)
        meuDisco2Id = intent.getLongExtra("MEU_DISCO2_ID", -1L)
        meuSubstatus = intent.getIntExtra("MEU_SUBSTATUS", 0)
        meuStatus4 = intent.getStringExtra("MEU_STATUS_4") ?: ""
        meuStatus5 = intent.getStringExtra("MEU_STATUS_5") ?: ""
        meuStatus6 = intent.getStringExtra("MEU_STATUS_6") ?: ""

        carregarDadosBase()
    }

    private fun carregarDadosBase() {
        lifecycleScope.launch {
            try {
                listaPersonagens = async(Dispatchers.IO) { RetrofitClient.api.listarPersonagens().execute().body() ?: emptyList() }.await()
                listaArmas = async(Dispatchers.IO) { RetrofitClient.api.listarArmas().execute().body() ?: emptyList() }.await()
                listaDiscos = async(Dispatchers.IO) { RetrofitClient.api.listarDiscos().execute().body() ?: emptyList() }.await()

                buscarBuilds()
            } catch (e: Exception) {
                Toast.makeText(this@SelecaoBuildActivity, "Erro ao carregar dados.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun buscarBuilds() {
        RetrofitClient.api.listarBuilds().enqueue(object : Callback<List<Build>> {
            override fun onResponse(call: Call<List<Build>>, response: Response<List<Build>>) {
                val todasBuilds = response.body() ?: emptyList()

                val buildsCompatíveis = todasBuilds.filter { it.personagemId == personagemId }

                if (buildsCompatíveis.isEmpty()) {
                    Toast.makeText(this@SelecaoBuildActivity, "Nenhuma build para este personagem.", Toast.LENGTH_SHORT).show()
                    finish()
                    return
                }

                val adapter = BuildAdapter(buildsCompatíveis, listaPersonagens, listaArmas, listaDiscos) { buildClicada ->
                    calcularScoreEIrParaResultado(buildClicada)
                }
                recyclerSelecionar.adapter = adapter
            }
            override fun onFailure(call: Call<List<Build>>, t: Throwable) {}
        })
    }

    private fun calcularScoreEIrParaResultado(build: Build) {
        var score = 0.0

        if (minhaArmaId == build.armaId) score += 20.0
        if (meuDisco4Id == build.disco4Id) score += 10.0
        if (meuDisco2Id == build.disco2Id) score += 10.0

        if (meuStatus4.equals(build.statusDisco4, ignoreCase = true)) score += 5.0
        if (meuStatus5.equals(build.statusDisco5, ignoreCase = true)) score += 5.0
        if (meuStatus6.equals(build.statusDisco6, ignoreCase = true)) score += 5.0

        val pontosSubstatus = (meuSubstatus / 12.0) * 45.0
        score += pontosSubstatus

        val intent = Intent(this, ResultadoActivity::class.java).apply {
            putExtra("PERSONAGEM_AVALIADO", personagemNome)
            putExtra("SCORE_AVALIADO", score.toInt())
        }
        startActivity(intent)
        finish()
    }
}