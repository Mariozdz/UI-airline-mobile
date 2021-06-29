package una.moviles

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import io.ktor.client.*
import io.ktor.client.features.websocket.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import una.moviles.logic.User
import una.moviles.ui.HOST
import una.moviles.ui.PATH_APP
import una.moviles.ui.PORT

import java.util.*


class LoginViewModel : ViewModel() {

    var client: HttpClient? = null
    var user: MutableLiveData<User>
    val outputEventChannel: Channel<String> = Channel(10)
    val inputEventChannel: Channel<String> = Channel(10)

    init {
        user = MutableLiveData()
    }


    fun open(coroutineScope: CoroutineScope) {
        client = buildClient()
        coroutineScope.launch {
            client!!.webSocket(
                path = PATH_LOGIN,
                host = HOST,
                port = PORT
            ) {
                val input = launch { output() }
                val output = launch { input() }
                input.join()
                output.join()
            }
            client?.close()
        }
    }

    fun close() {
        Log.d("onClose", "onClose")
        client?.close()
        client = null
    }


    private suspend fun DefaultClientWebSocketSession.input() {
        try {
            for (frame in incoming) {
                frame as? Frame.Text ?: continue
                try {
                    Log.d("onMessage", "Message received: ${frame.readText()}")
                    parseRes(frame.readText())
                } catch (e: Throwable) {
                    Log.e("onMessage", "${e.message}", e)
                }
            }
        } catch (e: Throwable) {
            Log.e("onMessage", "${e.message}", e)
        }
    }

    private fun parseRes(res: String) {
        val gson = Gson()
        val properties = gson.fromJson(res, Properties::class.java)

        if (!properties.contains("none")) {

            var latitud = properties.getProperty("latitud")
            var longitud = properties.getProperty("longitud")
            var password = properties.getProperty("password")
            var name = properties.getProperty("name")
            var usertype = properties.get("usertype")
            var cellphone = properties.getProperty("cellphone")
            var id = properties.getProperty("id")
            var surnames = properties.getProperty("surnames")

            user.postValue(
                User(
                    id,
                    "heredia",
                    name,
                    surnames,
                    cellphone,
                    "mariozdz@gmail.com",
                    password
                )
            )
        }


    }

    private suspend fun DefaultClientWebSocketSession.output() {
        try {
            outputEventChannel.consumeEach {
                Log.d("onOutput", it)
                send(it)
            }
        } catch (e: Throwable) {
            Log.e("", "${e.message}", e)
        }
    }

    private fun buildClient() = HttpClient {
        install(WebSockets)
    }

    fun login(email: String, password: String) {

        viewModelScope.launch {
            val gson = Gson()
            val req = Properties()
            req.put("id", email)
            req.put("password", password)
            req.put("Action", "login")
            outputEventChannel.send(gson.toJson(req))
        }
    }

    companion object {
        private const val PATH_LOGIN = "$PATH_APP/user"
    }

}