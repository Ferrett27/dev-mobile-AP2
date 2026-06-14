package com.example.ap2buildszzz.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ap2buildszzz.R
import com.example.ap2buildszzz.activities.CadastroPersonagemActivity
import com.example.ap2buildszzz.activities.LoginActivity
import com.example.ap2buildszzz.adapters.BuildAdapter
import com.example.ap2buildszzz.adapters.InventarioAdapter
import com.example.ap2buildszzz.conexaoBanco.RetrofitClient
import com.example.ap2buildszzz.models.Arma
import com.example.ap2buildszzz.models.Build
import com.example.ap2buildszzz.models.Disco
import com.example.ap2buildszzz.models.Inventario
import com.example.ap2buildszzz.models.Personagem
import com.example.ap2buildszzz.utils.UsuarioLogado
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PerfilFragment : Fragment() {

    private lateinit var recyclerProfile: RecyclerView
    private lateinit var tabLayout: TabLayout
    private lateinit var fabAddPersonagem: FloatingActionButton

    private var listaPersonagens = listOf<Personagem>()
    private var listaArmas = listOf<Arma>()
    private var listaDiscos = listOf<Disco>()

    private val usuarioId = UsuarioLogado.usuarioLogado?.id

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_perfil, container, false)

        recyclerProfile = view.findViewById(R.id.recycler_profile)
        tabLayout = view.findViewById(R.id.tab_layout_profile)
        fabAddPersonagem = view.findViewById(R.id.fab_add_personagem)

        val tvUserName = view.findViewById<TextView>(R.id.tv_user_name)
        val tvUserEmail = view.findViewById<TextView>(R.id.tv_user_email)
        val btnLogout = view.findViewById<Button>(R.id.btn_logout)

        recyclerProfile.layoutManager = LinearLayoutManager(requireContext())

        UsuarioLogado.usuarioLogado?.let {
            tvUserName.text = it.nome
            tvUserEmail.text = it.email
        }

        fabAddPersonagem.setOnClickListener {
            val intent = Intent(requireContext(), CadastroPersonagemActivity::class.java)
            startActivity(intent)
        }

        btnLogout.setOnClickListener {
            UsuarioLogado.usuarioLogado = null

            val intent = Intent(requireContext(), LoginActivity::class.java)

            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
            requireActivity().finish()
        }

        configurarAcoesDasAbas()
        carregarDadosBase()

        return view
    }

    private fun configurarAcoesDasAbas() {
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        fabAddPersonagem.show()
                        recyclerProfile.adapter = null
                        carregarInventario()
                    }
                    1 -> {
                        fabAddPersonagem.hide()
                        carregarMinhasBuilds()
                    }
                    2 -> {
                        fabAddPersonagem.hide()
                        carregarBuildsSalvas()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun carregarDadosBase() {
        lifecycleScope.launch {
            try {
                listaPersonagens = async(Dispatchers.IO) { RetrofitClient.api.listarPersonagens().execute().body() ?: emptyList() }.await()
                listaArmas = async(Dispatchers.IO) { RetrofitClient.api.listarArmas().execute().body() ?: emptyList() }.await()
                listaDiscos = async(Dispatchers.IO) { RetrofitClient.api.listarDiscos().execute().body() ?: emptyList() }.await()

                tabLayout.getTabAt(1)?.select()
            } catch (e: Exception) {
                Toast.makeText(context, "Erro ao carregar os dados base.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun carregarInventario() {
        if (usuarioId == null) return
        RetrofitClient.api.listarInventarioPorUsuario(usuarioId).enqueue(object : Callback<List<Inventario>> {
            override fun onResponse(call: Call<List<Inventario>>, response: Response<List<Inventario>>) {
                val lista = response.body() ?: emptyList()
                recyclerProfile.adapter = InventarioAdapter(lista, listaPersonagens, listaArmas, listaDiscos) { item ->
                }
            }
            override fun onFailure(call: Call<List<Inventario>>, t: Throwable) {
                Toast.makeText(context, "Erro ao carregar inventário.", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun carregarMinhasBuilds() {
        if (usuarioId == null) return

        RetrofitClient.api.listarBuilds().enqueue(object : Callback<List<Build>> {
            override fun onResponse(call: Call<List<Build>>, response: Response<List<Build>>) {
                val todasBuilds = response.body() ?: emptyList()
                val minhasBuilds = todasBuilds.filter { it.usuarioId == usuarioId }

                val adapter = BuildAdapter(minhasBuilds, listaPersonagens, listaArmas, listaDiscos) { buildClicada ->
                    confirmarAcao(buildClicada, "Deletar Build?", "Esta ação removerá sua build do aplicativo.") {
                        deletarMinhaBuild(buildClicada.id!!)
                    }
                }
                recyclerProfile.adapter = adapter
            }
            override fun onFailure(call: Call<List<Build>>, t: Throwable) {}
        })
    }

    private fun carregarBuildsSalvas() {
        if (usuarioId == null) return

        RetrofitClient.api.listarBuildsSalvas(usuarioId, usuarioId).enqueue(object : Callback<List<Build>> {
            override fun onResponse(call: Call<List<Build>>, response: Response<List<Build>>) {
                val buildsSalvas = response.body() ?: emptyList()

                val adapter = BuildAdapter(buildsSalvas, listaPersonagens, listaArmas, listaDiscos) { buildClicada ->
                    confirmarAcao(buildClicada, "Desfavoritar?", "Esta build não aparecerá mais nos seus favoritos.") {
                        removerBuildDosFavoritos(buildClicada.id!!)
                    }
                }
                recyclerProfile.adapter = adapter
            }
            override fun onFailure(call: Call<List<Build>>, t: Throwable) {}
        })
    }

    private fun confirmarAcao(build: Build, titulo: String, mensagem: String, acaoConfirmada: () -> Unit) {
        AlertDialog.Builder(requireContext())
            .setTitle(titulo)
            .setMessage(mensagem)
            .setPositiveButton("Sim") { _, _ -> acaoConfirmada() }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun deletarMinhaBuild(buildId: Long) {
        RetrofitClient.api.deletarBuild(buildId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "Build deletada!", Toast.LENGTH_SHORT).show()
                    carregarMinhasBuilds()
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {}
        })
    }

    private fun removerBuildDosFavoritos(buildId: Long) {
        if (usuarioId == null) return
        RetrofitClient.api.removerBuildSalva(usuarioId, buildId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "Build removida dos favoritos!", Toast.LENGTH_SHORT).show()
                    carregarBuildsSalvas()
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {}
        })
    }
}