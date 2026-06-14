package com.example.ap2buildszzz.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ap2buildszzz.R
import com.example.ap2buildszzz.conexaoBanco.RetrofitClient
import com.example.ap2buildszzz.models.Arma
import com.example.ap2buildszzz.models.Build
import com.example.ap2buildszzz.models.Disco
import com.example.ap2buildszzz.models.Personagem
import com.example.ap2buildszzz.utils.UsuarioLogado
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CriacaoBuildActivity : AppCompatActivity() {

    private var listaPersonagens = listOf<Personagem>()
    private var listaArmas = listOf<Arma>()
    private var listaDiscos = listOf<Disco>()

    private lateinit var spPersonagem: Spinner
    private lateinit var spArma: Spinner
    private lateinit var spSet4: Spinner
    private lateinit var spSet2: Spinner
    private lateinit var spDisco4: Spinner
    private lateinit var spDisco5: Spinner
    private lateinit var spDisco6: Spinner
    private lateinit var spSub1: Spinner
    private lateinit var spSub2: Spinner
    private lateinit var btnSalvarBuild: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_criacao_build)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_criar_build)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnSalvarBuild = findViewById(R.id.btn_salvar_build)
        spPersonagem = findViewById(R.id.sp_personagem)
        spArma = findViewById(R.id.sp_arma)
        spSet4 = findViewById(R.id.sp_set4)
        spSet2 = findViewById(R.id.sp_set2)
        spDisco4 = findViewById(R.id.sp_disco4)
        spDisco5 = findViewById(R.id.sp_disco5)
        spDisco6 = findViewById(R.id.sp_disco6)
        spSub1 = findViewById(R.id.sp_sub1)
        spSub2 = findViewById(R.id.sp_sub2)

        val subStatus = arrayOf("Taxa Crítica", "Dano Crítico", "Ataque %", "Defesa %", "Vida %", "Ataque Plano", "Defesa Plana", "Vida Plana", "Proficiência de Anomalia")
        spSub1.adapter = criarAdapter(subStatus)
        spSub2.adapter = criarAdapter(subStatus)

        val statusDisco4 = arrayOf("Vida %", "Vida Plana", "Ataque %", "Ataque Plano", "Defesa %", "Defesa Plana", "Dano Crítico", "Taxa Crítica", "Proficiência de Anomalia")
        val statusDisco5 = arrayOf("Vida %", "Vida Plana", "Ataque %", "Ataque Plano", "Defesa %", "Defesa Plana", "Dano Elemental", "Taxa de Perfuração")
        val statusDisco6 = arrayOf("Vida %", "Vida Plana", "Ataque %", "Ataque Plano", "Defesa %", "Defesa Plana", "Impacto %", "Regeneração de Energia", "Proficiência de Anomalia")

        spDisco4.adapter = criarAdapter(statusDisco4)
        spDisco5.adapter = criarAdapter(statusDisco5)
        spDisco6.adapter = criarAdapter(statusDisco6)

        carregarDadosSincronizados()

        btnSalvarBuild.setOnClickListener {
            executarCadastroBuild()
        }
    }

    private fun criarAdapter(dados: Array<String>): ArrayAdapter<String> {
        return ArrayAdapter(this, R.layout.spinner_item, dados)
    }

    private fun carregarDadosSincronizados() {
        val api = RetrofitClient.api

        api.listarPersonagens().enqueue(object : Callback<List<Personagem>> {
            override fun onResponse(call: Call<List<Personagem>>, response: Response<List<Personagem>>) {
                if (response.isSuccessful) {
                    listaPersonagens = response.body() ?: emptyList()
                    val nomes = listaPersonagens.map { it.nome }.toTypedArray()
                    spPersonagem.adapter = criarAdapter(nomes)
                }
            }
            override fun onFailure(call: Call<List<Personagem>>, t: Throwable) {
                Toast.makeText(this@CriacaoBuildActivity, "Falha ao carregar personagens", Toast.LENGTH_SHORT).show()
            }
        })

        api.listarArmas().enqueue(object : Callback<List<Arma>> {
            override fun onResponse(call: Call<List<Arma>>, response: Response<List<Arma>>) {
                if (response.isSuccessful) {
                    listaArmas = response.body() ?: emptyList()
                    val nomes = listaArmas.map { it.nome }.toTypedArray()
                    spArma.adapter = criarAdapter(nomes)
                }
            }
            override fun onFailure(call: Call<List<Arma>>, t: Throwable) {
                Toast.makeText(this@CriacaoBuildActivity, "Falha ao carregar armas", Toast.LENGTH_SHORT).show()
            }
        })

        api.listarDiscos().enqueue(object : Callback<List<Disco>> {
            override fun onResponse(call: Call<List<Disco>>, response: Response<List<Disco>>) {
                if (response.isSuccessful) {
                    listaDiscos = response.body() ?: emptyList()
                    val nomes = listaDiscos.map { it.nome }.toTypedArray()
                    spSet4.adapter = criarAdapter(nomes)
                    spSet2.adapter = criarAdapter(nomes)
                }
            }
            override fun onFailure(call: Call<List<Disco>>, t: Throwable) {
                Toast.makeText(this@CriacaoBuildActivity, "Falha ao carregar discos", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun executarCadastroBuild() {
        val usuario = UsuarioLogado.usuarioLogado

        if (usuario?.id == null) {
            Toast.makeText(this, "Erro de sessão: Faça login novamente", Toast.LENGTH_SHORT).show()
            return
        }

        if (listaPersonagens.isEmpty() || listaArmas.isEmpty() || listaDiscos.isEmpty()) {
            Toast.makeText(this, "Aguarde o carregamento dos dados da API...", Toast.LENGTH_SHORT).show()
            return
        }

        val personagemSelecionado = listaPersonagens[spPersonagem.selectedItemPosition]
        val armaSelecionada = listaArmas[spArma.selectedItemPosition]
        val disco4Selecionado = listaDiscos[spSet4.selectedItemPosition]
        val disco2Selecionado = listaDiscos[spSet2.selectedItemPosition]

        val status4 = spDisco4.selectedItem.toString()
        val status5 = spDisco5.selectedItem.toString()
        val status6 = spDisco6.selectedItem.toString()

        RetrofitClient.api.criarBuild(
            usuarioId = usuario.id!!,
            personagemId = personagemSelecionado.id!!,
            armaId = armaSelecionada.id!!,
            disco4Id = disco4Selecionado.id!!,
            disco2Id = disco2Selecionado.id!!,
            status4 = status4,
            status5 = status5,
            status6 = status6
        ).enqueue(object : Callback<Build> {
            override fun onResponse(call: Call<Build>, response: Response<Build>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@CriacaoBuildActivity, "Build salva com sucesso na base de dados!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@CriacaoBuildActivity, "Erro no servidor: Código ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Build>, t: Throwable) {
                Toast.makeText(this@CriacaoBuildActivity, "Erro de comunicação: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}