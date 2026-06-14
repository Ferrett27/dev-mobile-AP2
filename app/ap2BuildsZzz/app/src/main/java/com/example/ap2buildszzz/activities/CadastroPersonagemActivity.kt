package com.example.ap2buildszzz.activities

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.ap2buildszzz.R
import com.example.ap2buildszzz.conexaoBanco.RetrofitClient
import com.example.ap2buildszzz.models.Arma
import com.example.ap2buildszzz.models.Disco
import com.example.ap2buildszzz.models.Inventario
import com.example.ap2buildszzz.models.Personagem
import com.example.ap2buildszzz.utils.UsuarioLogado
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CadastroPersonagemActivity : AppCompatActivity() {

    private lateinit var spAgente: Spinner
    private lateinit var spArma: Spinner
    private lateinit var spSet4: Spinner
    private lateinit var spSet2: Spinner
    private lateinit var spDisco4: Spinner
    private lateinit var spDisco5: Spinner
    private lateinit var spDisco6: Spinner
    private lateinit var spTotalSubstatus: Spinner
    private lateinit var btnSalvar: Button

    private var edicaoId: Long = -1L
    private var isEdicao: Boolean = false

    private var listaAgentesDisponiveis = listOf<Personagem>()
    private var listaArmas = listOf<Arma>()
    private var listaDiscos = listOf<Disco>()

    private val statusDisco4 = arrayOf("Vida %", "Vida Plana", "Ataque %", "Ataque Plano", "Defesa %", "Defesa Plana", "Dano Crítico", "Taxa Crítica", "Proficiência de Anomalia")
    private val statusDisco5 = arrayOf("Vida %", "Vida Plana", "Ataque %", "Ataque Plano", "Defesa %", "Defesa Plana", "Dano Elemental", "Taxa de Perfuração")
    private val statusDisco6 = arrayOf("Vida %", "Vida Plana", "Ataque %", "Ataque Plano", "Defesa %", "Defesa Plana", "Impacto %", "Regeneração de Energia", "Proficiência de Anomalia")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cadastro_personagem)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_cadastro)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        edicaoId = intent.getLongExtra("EDICAO_ID", -1L)
        isEdicao = edicaoId != -1L

        mapearComponentes()

        if (isEdicao) {
            findViewById<TextView>(R.id.tv_titulo_cadastro).text = "EDITAR EQUIPAMENTOS"
            btnSalvar.text = "Carregando Edição..."
        }

        carregarDadosEConfigurarSpinners()

        btnSalvar.setOnClickListener {
            if (isEdicao) {
                atualizarInventarioExistente()
            } else {
                salvarPersonagem()
            }
        }
    }

    private fun mapearComponentes() {
        spAgente = findViewById(R.id.sp_meu_agente)
        spArma = findViewById(R.id.sp_minha_arma)
        spSet4 = findViewById(R.id.sp_meu_set4)
        spSet2 = findViewById(R.id.sp_meu_set2)
        spDisco4 = findViewById(R.id.sp_meu_disco4)
        spDisco5 = findViewById(R.id.sp_meu_disco5)
        spDisco6 = findViewById(R.id.sp_meu_disco6)
        spTotalSubstatus = findViewById(R.id.sp_total_substatus)
        btnSalvar = findViewById(R.id.btn_salvar_inventario)

        btnSalvar.isEnabled = false
        btnSalvar.text = "Carregando..."
    }

    private fun carregarDadosEConfigurarSpinners() {
        val usuarioId = UsuarioLogado.usuarioLogado?.id ?: return

        lifecycleScope.launch {
            try {
                val taskPersonagens = async(Dispatchers.IO) { RetrofitClient.api.listarPersonagens().execute().body() ?: emptyList() }
                val taskArmas = async(Dispatchers.IO) { RetrofitClient.api.listarArmas().execute().body() ?: emptyList() }
                val taskDiscos = async(Dispatchers.IO) { RetrofitClient.api.listarDiscos().execute().body() ?: emptyList() }
                val taskInventario = async(Dispatchers.IO) { RetrofitClient.api.listarInventarioPorUsuario(usuarioId).execute().body() ?: emptyList() }

                val todosPersonagens = taskPersonagens.await()
                listaArmas = taskArmas.await()
                listaDiscos = taskDiscos.await()
                val meuInventario = taskInventario.await()

                if (isEdicao) {
                    listaAgentesDisponiveis = todosPersonagens
                    spAgente.isEnabled = false
                } else {
                    val idsPossuidos = meuInventario.map { it.personagemId }
                    listaAgentesDisponiveis = todosPersonagens.filter { it.id !in idsPossuidos }

                    if (listaAgentesDisponiveis.isEmpty()) {
                        Toast.makeText(this@CadastroPersonagemActivity, "Você já possui todos os agentes!", Toast.LENGTH_LONG).show()
                        finish()
                        return@launch
                    }
                }

                spAgente.adapter = ArrayAdapter(this@CadastroPersonagemActivity, R.layout.spinner_item, listaAgentesDisponiveis.map { it.nome })
                spArma.adapter = ArrayAdapter(this@CadastroPersonagemActivity, R.layout.spinner_item, listaArmas.map { it.nome })
                spSet4.adapter = ArrayAdapter(this@CadastroPersonagemActivity, R.layout.spinner_item, listaDiscos.map { it.nome })
                spSet2.adapter = ArrayAdapter(this@CadastroPersonagemActivity, R.layout.spinner_item, listaDiscos.map { it.nome })
                spDisco4.adapter = ArrayAdapter(this@CadastroPersonagemActivity, R.layout.spinner_item, statusDisco4)
                spDisco5.adapter = ArrayAdapter(this@CadastroPersonagemActivity, R.layout.spinner_item, statusDisco5)
                spDisco6.adapter = ArrayAdapter(this@CadastroPersonagemActivity, R.layout.spinner_item, statusDisco6)
                spTotalSubstatus.adapter = ArrayAdapter(this@CadastroPersonagemActivity, R.layout.spinner_item, (0..12).map { it.toString() }.toTypedArray())

                if (isEdicao) {
                    val inventarioAntigo = meuInventario.find { it.id == edicaoId }

                    if (inventarioAntigo != null) {
                        preencherSpinnersComDadosAntigos(inventarioAntigo)
                        btnSalvar.isEnabled = true
                        btnSalvar.text = "Salvar Alterações"
                    } else {
                        Toast.makeText(this@CadastroPersonagemActivity, "Erro: Registro não encontrado.", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                } else {
                    btnSalvar.isEnabled = true
                    btnSalvar.text = "Salvar Personagem"
                }

            } catch (e: Exception) {
                Toast.makeText(this@CadastroPersonagemActivity, "Erro de rede ao carregar.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun preencherSpinnersComDadosAntigos(inv: Inventario) {
        val indexPersonagem = listaAgentesDisponiveis.indexOfFirst { it.id == inv.personagemId }
        if (indexPersonagem >= 0) spAgente.setSelection(indexPersonagem)

        val indexArma = listaArmas.indexOfFirst { it.id == inv.armaId }
        if (indexArma >= 0) spArma.setSelection(indexArma)

        val indexDisco4 = listaDiscos.indexOfFirst { it.id == inv.disco4Id }
        if (indexDisco4 >= 0) spSet4.setSelection(indexDisco4)

        val indexDisco2 = listaDiscos.indexOfFirst { it.id == inv.disco2Id }
        if (indexDisco2 >= 0) spSet2.setSelection(indexDisco2)

        spDisco4.setSelection(statusDisco4.indexOf(inv.statusDisco4).coerceAtLeast(0))
        spDisco5.setSelection(statusDisco5.indexOf(inv.statusDisco5).coerceAtLeast(0))
        spDisco6.setSelection(statusDisco6.indexOf(inv.statusDisco6).coerceAtLeast(0))

        spTotalSubstatus.setSelection(inv.totalSubstatus)
    }

    private fun atualizarInventarioExistente() {
        val armaId = listaArmas[spArma.selectedItemPosition].id!!
        val disco4Id = listaDiscos[spSet4.selectedItemPosition].id!!
        val disco2Id = listaDiscos[spSet2.selectedItemPosition].id!!
        val substatus = spTotalSubstatus.selectedItem.toString().toInt()

        btnSalvar.isEnabled = false
        btnSalvar.text = "Atualizando..."

        RetrofitClient.api.atualizarInventario(
            id = edicaoId,
            armaId = armaId,
            disco4Id = disco4Id,
            disco2Id = disco2Id,
            statusDisco4 = spDisco4.selectedItem.toString(),
            statusDisco5 = spDisco5.selectedItem.toString(),
            statusDisco6 = spDisco6.selectedItem.toString(),
            totalSubstatus = substatus
        ).enqueue(object : Callback<Inventario> {
            override fun onResponse(call: Call<Inventario>, response: Response<Inventario>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@CadastroPersonagemActivity, "Equipamentos atualizados!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    btnSalvar.isEnabled = true
                    btnSalvar.text = "Salvar Alterações"
                    Toast.makeText(this@CadastroPersonagemActivity, "Erro: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Inventario>, t: Throwable) {
                btnSalvar.isEnabled = true
                btnSalvar.text = "Salvar Alterações"
                Toast.makeText(this@CadastroPersonagemActivity, "Falha na conexão.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun salvarPersonagem() {
        val usuarioId = UsuarioLogado.usuarioLogado?.id ?: return

        val personagemId = listaAgentesDisponiveis[spAgente.selectedItemPosition].id!!
        val armaId = listaArmas[spArma.selectedItemPosition].id!!
        val disco4Id = listaDiscos[spSet4.selectedItemPosition].id!!
        val disco2Id = listaDiscos[spSet2.selectedItemPosition].id!!
        val substatus = spTotalSubstatus.selectedItem.toString().toInt()

        btnSalvar.isEnabled = false
        btnSalvar.text = "Salvando..."

        RetrofitClient.api.cadastrarInventario(
            usuarioId = usuarioId,
            personagemId = personagemId,
            armaId = armaId,
            disco4Id = disco4Id,
            disco2Id = disco2Id,
            statusDisco4 = spDisco4.selectedItem.toString(),
            statusDisco5 = spDisco5.selectedItem.toString(),
            statusDisco6 = spDisco6.selectedItem.toString(),
            totalSubstatus = substatus
        ).enqueue(object : Callback<Inventario> {
            override fun onResponse(call: Call<Inventario>, response: Response<Inventario>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@CadastroPersonagemActivity, "Salvo com sucesso!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    btnSalvar.isEnabled = true
                    btnSalvar.text = "Salvar Personagem"
                    Toast.makeText(this@CadastroPersonagemActivity, "Erro: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Inventario>, t: Throwable) {
                btnSalvar.isEnabled = true
                btnSalvar.text = "Salvar Personagem"
                Toast.makeText(this@CadastroPersonagemActivity, "Falha na conexão.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}