package com.example.ap2buildszzz.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ap2buildszzz.R

class ResultadoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_resultado)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_resultado)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val personagem = intent.getStringExtra("PERSONAGEM_AVALIADO") ?: "Agente"
        val score = intent.getIntExtra("SCORE_AVALIADO", 0)

        val tvNomePersonagem = findViewById<TextView>(R.id.tv_resultado_personagem)
        val tvScore = findViewById<TextView>(R.id.tv_score_resultado)
        val tvFeedback = findViewById<TextView>(R.id.tv_resultado_feedback)
        val tvRank = findViewById<TextView>(R.id.tv_resultado_rank)

        val btnCompartilhar = findViewById<Button>(R.id.btn_compartilhar)
        val btnVoltar = findViewById<Button>(R.id.btn_voltar_inicio)

        tvNomePersonagem.text = "Agente: $personagem"
        tvScore.text = "$score%"

        var rankStr = ""

        when {
            score >= 90 -> {
                rankStr = "RANK S"
                tvFeedback.text = "Build Perfeita! Sincronização máxima atingida."
                val colorS = Color.parseColor("#FFD700") // Dourado
                tvScore.setTextColor(colorS)
                tvRank.text = rankStr
                tvRank.setTextColor(colorS)
            }
            score >= 70 -> {
                rankStr = "RANK A"
                tvFeedback.text = "Ótimo trabalho! A build está muito sólida."
                val colorA = Color.parseColor("#03DAC5") // Verde Água
                tvScore.setTextColor(colorA)
                tvRank.text = rankStr
                tvRank.setTextColor(colorA)
            }
            score >= 50 -> {
                rankStr = "RANK B"
                tvFeedback.text = "Build decente, mas ainda precisa de um pouco de farm."
                val colorB = Color.parseColor("#FF9800") // Laranja
                tvScore.setTextColor(colorB)
                tvRank.text = rankStr
                tvRank.setTextColor(colorB)
            }
            else -> {
                rankStr = "RANK C"
                tvFeedback.text = "Sincronização baixa. Revise os discos e substatus!"
                val colorC = Color.parseColor("#CF6679") // Vermelho
                tvScore.setTextColor(colorC)
                tvRank.text = rankStr
                tvRank.setTextColor(colorC)
            }
        }

        btnCompartilhar.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(
                Intent.EXTRA_TEXT,
                "Minha build do(a) $personagem pegou $rankStr ($score% de Sync Rate) no Avaliador Proxy!"
            )
            startActivity(Intent.createChooser(shareIntent, "Compartilhar via"))
        }

        btnVoltar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
}