package com.example.ap2buildszzz.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ap2buildszzz.R
import com.example.ap2buildszzz.activities.DetalhesBuildActivity
import com.example.ap2buildszzz.models.Arma
import com.example.ap2buildszzz.models.Build
import com.example.ap2buildszzz.models.Disco
import com.example.ap2buildszzz.models.Personagem

class BuildAdapter(
    private var listaBuilds: List<Build>,
    private val listaPersonagens: List<Personagem>,
    private val listaArmas: List<Arma>,
    private val listaDiscos: List<Disco>,
    private val onLongClick: ((Build) -> Unit)? = null // Parâmetro opcional para o clique longo
) : RecyclerView.Adapter<BuildAdapter.BuildViewHolder>() {

    // Guarda a lista original para quando o usuário apagar o texto da pesquisa
    private var listaOriginal = listaBuilds.toList()

    class BuildViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivPersonagem: ImageView = itemView.findViewById(R.id.iv_personagem_card)
        val tvNomePersonagem: TextView = itemView.findViewById(R.id.tv_nome_personagem_card)
        val tvStatus: TextView = itemView.findViewById(R.id.tv_status_resumo)
        val ivArma: ImageView = itemView.findViewById(R.id.iv_arma_card)
        val ivDisco4: ImageView = itemView.findViewById(R.id.iv_disco4_card)
        val ivDisco2: ImageView = itemView.findViewById(R.id.iv_disco2_card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuildViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return BuildViewHolder(view)
    }

    override fun onBindViewHolder(holder: BuildViewHolder, position: Int) {
        val build = listaBuilds[position]

        val personagem = listaPersonagens.find { it.id == build.personagemId }
        val arma = listaArmas.find { it.id == build.armaId }
        val disco4 = listaDiscos.find { it.id == build.disco4Id }
        val disco2 = listaDiscos.find { it.id == build.disco2Id }

        holder.tvNomePersonagem.text = personagem?.nome ?: "Agente Desconhecido"
        holder.tvStatus.text = "IV: ${build.statusDisco4}  |  V: ${build.statusDisco5}  |  VI: ${build.statusDisco6}"

        personagem?.let {
            Glide.with(holder.itemView.context).load(it.urlImagem).circleCrop().into(holder.ivPersonagem)
        }
        arma?.let {
            Glide.with(holder.itemView.context).load(it.urlImagem).into(holder.ivArma)
        }
        disco4?.let {
            Glide.with(holder.itemView.context).load(it.urlImagem).into(holder.ivDisco4)
        }
        disco2?.let {
            Glide.with(holder.itemView.context).load(it.urlImagem).into(holder.ivDisco2)
        }

        holder.itemView.setOnClickListener {
            val contexto = holder.itemView.context
            val intent = Intent(contexto, DetalhesBuildActivity::class.java).apply {
                putExtra("BUILD_ID", build.id)
                putExtra("BUILD_USER_ID", build.usuarioId)
                putExtra("PERSONAGEM_NOME", personagem?.nome)
                putExtra("PERSONAGEM_IMG", personagem?.urlImagem)
                putExtra("ARMA_NOME", arma?.nome)
                putExtra("ARMA_IMG", arma?.urlImagem)
                putExtra("DISCO4_NOME", disco4?.nome)
                putExtra("DISCO4_IMG", disco4?.urlImagem)
                putExtra("DISCO2_NOME", disco2?.nome)
                putExtra("DISCO2_IMG", disco2?.urlImagem)
                putExtra("STATUS_4", build.statusDisco4)
                putExtra("STATUS_5", build.statusDisco5)
                putExtra("STATUS_6", build.statusDisco6)
            }
            contexto.startActivity(intent)
        }

        holder.itemView.setOnLongClickListener {
            onLongClick?.invoke(build)
            true
        }
    }

    override fun getItemCount(): Int = listaBuilds.size

    fun filtrarPorPersonagem(busca: String) {
        listaBuilds = if (busca.isEmpty()) {
            listaOriginal
        } else {
            listaOriginal.filter { build ->
                val personagem = listaPersonagens.find { it.id == build.personagemId }
                personagem?.nome?.contains(busca, ignoreCase = true) == true
            }
        }
        notifyDataSetChanged()
    }
}