package una.moviles

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import una.moviles.logic.User
import una.moviles.persistence.BD

class RegisterActivity : AppCompatActivity() {

    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        registerViewModel = RegisterViewModel()

        var back = findViewById<Button>(R.id.go_back)
        var register = findViewById<Button>(R.id.reg_summit)

        back.setOnClickListener {
            finish()
        }

        register.setOnClickListener{

            var name = findViewById<EditText>(R.id.reg_name).text.toString()
            var surnames =  findViewById<EditText>(R.id.reg_surnames).text.toString()
            var address = findViewById<EditText>(R.id.reg_address).text.toString()
            var cellphone = findViewById<EditText>(R.id.reg_cellphone).text.toString()
            var email = findViewById<EditText>(R.id.reg_email).text.toString()
            var password = findViewById<EditText>(R.id.reg_password).text.toString()


            if (name != "" && email != "" && password != "" && surnames != "" && cellphone != "" && password != "")
            {
                val us: User = User( email, address,name,surnames,cellphone,email,password,0)
                registerViewModel.registrar(us)
                val toast1 = Toast.makeText(applicationContext,
                        "Registrado con exito", Toast.LENGTH_LONG)
                toast1.show()
                finish()
            } else{
                val toast1 = Toast.makeText(applicationContext,
                        "Complete todos los campos o ingrese un email valido", Toast.LENGTH_LONG)
                toast1.show()
            }

        }
    }

    fun validate(email:String) : Boolean{

        for(i in BD.users)
        {
            if(i.email == email)
                return false
        }
        return true;
    }


    override fun onResume() {
        super.onResume()
        registerViewModel.open(lifecycleScope)
    }

    override fun onPause() {
        super.onPause()
        registerViewModel.close()
    }
}