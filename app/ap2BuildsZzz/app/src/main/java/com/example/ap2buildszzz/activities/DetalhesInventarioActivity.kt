package com.example.ap2buildszzz.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.ap2buildszzz.R
import com.example.ap2buildszzz.conexaoBanco.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetalhesInventarioActivity : AppCompatActivity() {

    private var inventarioId: Long = -1L
    private var personagemId: Long = -1L
    private var minhaArmaId: Long = -1L
    private var meuDisco4Id: Long = -1L
    private var meuDisco2Id: Long = -1L
    private var meuSubstatus: Int = 0
    private var personagemNome: String = "Desconhecido"

    private var meuStatus4: String = ""
    private var meuStatus5: String = ""
    private var meuStatus6: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalhes_inventario)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        inventarioId = intent.getLongExtra("INVENTARIO_ID", -1L)
        personagemId = intent.getLongExtra("PERSONAGEM_ID", -1L)
        minhaArmaId = intent.getLongExtra("ARMA_ID", -1L)
        meuDisco4Id = intent.getLongExtra("DISCO4_ID", -1L)
        meuDisco2Id = intent.getLongExtra("DISCO2_ID", -1L)
        meuSubstatus = intent.getIntExtra("SUBSTATUS", 0)
        personagemNome = intent.getStringExtra("PERSONAGEM_NOME") ?: "Desconhecido"

        val personagemImg = intent.getStringExtra("PERSONAGEM_IMG")
        meuStatus4 = intent.getStringExtra("STATUS_4") ?: ""
        meuStatus5 = intent.getStringExtra("STATUS_5") ?: ""
        meuStatus6 = intent.getStringExtra("STATUS_6") ?: ""

        val tvNome = findViewById<TextView>(R.id.tv_detalhe_personagem)
        val tvStatus = findViewById<TextView>(R.id.tv_detalhe_status)
        val ivPersonagem = findViewById<ImageView>(R.id.iv_detalhe_personagem)
        val btnEditar = findViewById<Button>(R.id.btn_editar)
        val btnAvaliar = findViewById<Button>(R.id.btn_avaliar)
        val btnDeletar = findViewById<Button>(R.id.btn_deletar)

        tvNome.text = personagemNome
        tvStatus.text = "Substatus úteis: $meuSubstatus/12\nSlot IV: $meuStatus4\nSlot V: $meuStatus5\nSlot VI: $meuStatus6"
        Glide.with(this).load(personagemImg).circleCrop().into(ivPersonagem)

        btnEditar.setOnClickListener {
            startActivity(Intent(this, CadastroPersonagemActivity::class.java).apply { putExtra("EDICAO_ID", inventarioId) })
            finish()
        }

        btnDeletar.setOnClickListener { confirmarDelecao() }

        btnAvaliar.setOnClickListener {
            val intent = Intent(this, SelecaoBuildActivity::class.java).apply {
                putExtra("PERSONAGEM_ID", personagemId)
                putExtra("PERSONAGEM_NOME", personagemNome)
                putExtra("MINHA_ARMA_ID", minhaArmaId)
                putExtra("MEU_DISCO4_ID", meuDisco4Id)
                putExtra("MEU_DISCO2_ID", meuDisco2Id)
                putExtra("MEU_STATUS_4", meuStatus4)
                putExtra("MEU_STATUS_5", meuStatus5)
                putExtra("MEU_STATUS_6", meuStatus6)
                putExtra("MEU_SUBSTATUS", meuSubstatus)
            }
            startActivity(intent)
        }
    }

    private fun confirmarDelecao() {
        AlertDialog.Builder(this)
            .setTitle("Deletar Agente?")
            .setMessage("Esta ação removerá o personagem do seu inventário.")
            .setPositiveButton("Sim") { _, _ -> deletarInventario() }
            .setNegativeButton("Cancelar", null).show()
    }

    private fun deletarInventario() {
        RetrofitClient.api.deletarInventario(inventarioId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@DetalhesInventarioActivity, "Removido!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@DetalhesInventarioActivity, "Erro ao remover.", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@DetalhesInventarioActivity, "Falha de rede.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}