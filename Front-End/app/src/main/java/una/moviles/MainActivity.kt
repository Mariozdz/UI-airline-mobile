package una.moviles

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import una.moviles.databinding.ActivityMainBinding
import una.moviles.logic.User
import una.moviles.persistence.BD

class MainActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        var et_user_name = binding.etUserName
        var et_password = binding.etPassword
        var btn_register= binding.btnRegister
        var btn_submit = binding.btnSubmit


        loginViewModel = LoginViewModel()

        loginViewModel.user.observe(this){
            user ->

            val intent = Intent(this, MenuActivity::class.java)
            intent.putExtra("user",loginViewModel.user.value)
            startActivity(intent)
        }


        btn_submit.setOnClickListener {
            val user_name = et_user_name.text.toString()
            val password = et_password.text.toString()

            loginViewModel.login(user_name,password)



            if (loginViewModel.user.value == null)
            {
                Toast.makeText(this,"Invalide user or pass", Toast.LENGTH_LONG)
            }

        }
        btn_register.setOnClickListener {

            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        val username = binding.etUserName
        val password = binding.etPassword
        username.setText("")
        password.setText("")
        username.requestFocus()
        loginViewModel.open(lifecycleScope)
    }

    override fun onPause() {
        super.onPause()
        loginViewModel.close()
    }
}