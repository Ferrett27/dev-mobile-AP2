package com.example.ap2buildszzz.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ap2buildszzz.R
import com.example.ap2buildszzz.activities.CriacaoBuildActivity
import com.example.ap2buildszzz.adapters.BuildAdapter
import com.example.ap2buildszzz.conexaoBanco.RetrofitClient
import com.example.ap2buildszzz.models.Arma
import com.example.ap2buildszzz.models.Disco
import com.example.ap2buildszzz.models.Personagem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import android.widget.ProgressBar
import com.example.ap2buildszzz.utils.UsuarioLogado

class BuildsFragment : Fragment() {

    private lateinit var recyclerBuilds: RecyclerView
    private lateinit var progressBar: ProgressBar

    private var listaPersonagens = listOf<Personagem>()
    private var listaArmas = listOf<Arma>()
    private var listaDiscos = listOf<Disco>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_builds, container, false)

        val fabNewBuild = view.findViewById<FloatingActionButton>(R.id.fab_new_build)
        recyclerBuilds = view.findViewById(R.id.recycler_builds)
        progressBar = view.findViewById(R.id.progress_bar_builds)

        recyclerBuilds.layoutManager = LinearLayoutManager(requireContext())

        fabNewBuild.setOnClickListener {
            val intent = Intent(requireContext(), CriacaoBuildActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        carregarDadosEBuilds()
    }

    private fun carregarDadosEBuilds() {
        progressBar.visibility = View.VISIBLE
        recyclerBuilds.visibility = View.GONE

        lifecycleScope.launch {
            try {
                val taskPersonagens = async(Dispatchers.IO) { RetrofitClient.api.listarPersonagens().execute().body() ?: emptyList() }
                val taskArmas = async(Dispatchers.IO) { RetrofitClient.api.listarArmas().execute().body() ?: emptyList() }
                val taskDiscos = async(Dispatchers.IO) { RetrofitClient.api.listarDiscos().execute().body() ?: emptyList() }
                val taskBuilds = async(Dispatchers.IO) { RetrofitClient.api.listarBuilds().execute().body() ?: emptyList() }

                listaPersonagens = taskPersonagens.await()
                listaArmas = taskArmas.await()
                listaDiscos = taskDiscos.await()
                val builds = taskBuilds.await()

                val usuarioId = UsuarioLogado.usuarioLogado?.id
                val buildsComunitarias = builds.filter { it.usuarioId != usuarioId }

                val adapter = BuildAdapter(buildsComunitarias, listaPersonagens, listaArmas, listaDiscos)
                recyclerBuilds.adapter = adapter

                val etPesquisa = view?.findViewById<EditText>(R.id.et_pesquisa_personagem)
                etPesquisa?.addTextChangedListener(object : android.text.TextWatcher {
                    override fun afterTextChanged(s: android.text.Editable?) {}
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        adapter.filtrarPorPersonagem(s.toString())
                    }
                })

                progressBar.visibility = View.GONE
                recyclerBuilds.visibility = View.VISIBLE

            } catch (e: Exception) {
                progressBar.visibility = View.GONE
                Toast.makeText(context, "Erro ao carregar dados. Verifique a conexão.", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }
}