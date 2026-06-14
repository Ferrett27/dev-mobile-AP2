package com.example.ap2buildszzz.activities

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.ap2buildszzz.R
import com.example.ap2buildszzz.conexaoBanco.RetrofitClient
import com.example.ap2buildszzz.models.Usuario
import com.example.ap2buildszzz.utils.UsuarioLogado
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etEmail = findViewById<EditText>(R.id.et_email)
        val etSenha = findViewById<EditText>(R.id.et_senha)
        val btnEntrar = findViewById<Button>(R.id.btn_entrar)
        val tvLinkCadastro = findViewById<TextView>(R.id.tv_link_cadastro)

        btnEntrar.setOnClickListener {
            val email = etEmail.text.toString()
            val senha = etSenha.text.toString()

            if (email.isNotEmpty() && senha.isNotEmpty()) {
                RetrofitClient.api.login(email, senha).enqueue(object : Callback<Usuario> {
                    override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                        if (response.isSuccessful) {
                            UsuarioLogado.usuarioLogado = response.body()
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@LoginActivity, "Login falhou!", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<Usuario>, t: Throwable) {
                        Toast.makeText(this@LoginActivity, "Erro de conexão", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }

        tvLinkCadastro.setOnClickListener {
            val intent = Intent(this, CadastroActivity::class.java)
            startActivity(intent)
        }
    }
}