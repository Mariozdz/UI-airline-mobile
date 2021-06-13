package una.moviles

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import una.moviles.logic.User
import una.moviles.persistence.BD

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var et_user_name = findViewById<EditText>(R.id.et_user_name)
        var et_password = findViewById<EditText>(R.id.et_password)
        var btn_register= findViewById<Button>(R.id.btn_register)
        var btn_submit = findViewById<Button>(R.id.btn_submit)

        btn_submit.setOnClickListener {
            val user_name = et_user_name.text.toString()
            val password = et_password.text.toString()


            val intent = Intent(this, MenuActivity::class.java)


            var us: User? = BD.validateUser(user_name,password)



            if (us != null)
            {
                intent.putExtra("user",us)
                startActivity(intent)
            } else {
                val toast1 = Toast.makeText(applicationContext,
                        "Datos erroneos", Toast.LENGTH_LONG)
                toast1.show()
            }

        }

        btn_register.setOnClickListener {

            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }


    }
}