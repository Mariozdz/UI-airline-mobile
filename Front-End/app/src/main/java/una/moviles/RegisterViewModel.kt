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
import org.json.JSONArray
import org.json.JSONObject
import una.moviles.logic.Purchase
import una.moviles.logic.User
import una.moviles.ui.HOST
import una.moviles.ui.PATH_APP
import una.moviles.ui.PORT
import java.util.*

class RegisterViewModel : ViewModel() {

    var client: HttpClient? = null
    var purchase: MutableLiveData<List<Purchase>>
    val outputEventChannel: Channel<String> = Channel(10)
    val inputEventChannel: Channel<String> = Channel(10)

    var istrue: Boolean = false

    var use = ""

    init {
        purchase = MutableLiveData()
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
                    Log.d("onMessage", "purchase: ${frame.readText()}")
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

        if (res.contains("update") or res.contains("state") or res.contains("estado")) {

            istrue = true
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

    fun registrar(user: User) {

        viewModelScope.launch {
            val gson = Gson()
            val req = Properties()
            req.put("Action", "create")
            req.put("id", user.email )
            req.put("password", user.password)
            req.put("longitud", 1.1111)
            req.put("latitud", 1.1111)
            req.put("name", user.name)
            req.put("cellphone", user.cellphone)
            req.put("surnames", user.surnames)
            req.put("usertype",1)
            outputEventChannel.send(gson.toJson(req))
        }
    }

    companion object {
        private const val PATH_LOGIN = "$PATH_APP/user"
    }

}