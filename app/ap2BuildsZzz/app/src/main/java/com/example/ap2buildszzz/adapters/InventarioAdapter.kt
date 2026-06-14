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
import com.example.ap2buildszzz.activities.DetalhesInventarioActivity
import com.example.ap2buildszzz.models.Arma
import com.example.ap2buildszzz.models.Disco
import com.example.ap2buildszzz.models.Inventario
import com.example.ap2buildszzz.models.Personagem

class InventarioAdapter(
    private val listaInventario: List<Inventario>,
    private val personagens: List<Personagem>,
    private val armas: List<Arma>,
    private val discos: List<Disco>,
    private val onLongClick: (Inventario) -> Unit
) : RecyclerView.Adapter<InventarioAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivPersonagem: ImageView = view.findViewById(R.id.iv_personagem_card)
        val tvNome: TextView = view.findViewById(R.id.tv_nome_personagem_card)
        val tvStatus: TextView = view.findViewById(R.id.tv_status_resumo)
        val ivArma: ImageView = view.findViewById(R.id.iv_arma_card)
        val ivDisco4: ImageView = view.findViewById(R.id.iv_disco4_card)
        val ivDisco2: ImageView = view.findViewById(R.id.iv_disco2_card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listaInventario[position]

        val p = personagens.find { it.id == item.personagemId }
        val a = armas.find { it.id == item.armaId }
        val d4 = discos.find { it.id == item.disco4Id }
        val d2 = discos.find { it.id == item.disco2Id }

        holder.tvNome.text = p?.nome ?: "Desconhecido"
        holder.tvStatus.text = "Substatus: ${item.totalSubstatus}/12 | IV: ${item.statusDisco4} | V: ${item.statusDisco5} | VI: ${item.statusDisco6}"

        Glide.with(holder.itemView.context).load(p?.urlImagem).circleCrop().into(holder.ivPersonagem)
        Glide.with(holder.itemView.context).load(a?.urlImagem).into(holder.ivArma)
        Glide.with(holder.itemView.context).load(d4?.urlImagem).into(holder.ivDisco4)
        Glide.with(holder.itemView.context).load(d2?.urlImagem).into(holder.ivDisco2)

        holder.itemView.setOnLongClickListener {
            onLongClick(item)
            true
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetalhesInventarioActivity::class.java).apply {
                putExtra("INVENTARIO_ID", item.id)
                putExtra("PERSONAGEM_ID", item.personagemId)
                putExtra("ARMA_ID", item.armaId)
                putExtra("DISCO4_ID", item.disco4Id)
                putExtra("DISCO2_ID", item.disco2Id)

                putExtra("PERSONAGEM_NOME", p?.nome)
                putExtra("PERSONAGEM_IMG", p?.urlImagem)

                putExtra("STATUS_4", item.statusDisco4)
                putExtra("STATUS_5", item.statusDisco5)
                putExtra("STATUS_6", item.statusDisco6)
                putExtra("SUBSTATUS", item.totalSubstatus)
            }
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount() = listaInventario.size
}