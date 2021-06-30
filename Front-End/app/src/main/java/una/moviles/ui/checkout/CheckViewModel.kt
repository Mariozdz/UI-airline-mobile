package una.moviles.ui.checkout

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
import una.moviles.ui.HOST
import una.moviles.ui.PATH_APP
import una.moviles.ui.PORT
import java.util.*

class CheckViewModel : ViewModel() {

    var client: HttpClient? = null
    var purchase: MutableLiveData<List<Purchase>>
    val outputEventChannel: Channel<String> = Channel(10)
    val inputEventChannel: Channel<String> = Channel(10)

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

        if (res.contains("update") or res.contains("state")) {

        }
        else
        {
            val lista = JSONArray(res)

            var fli: ArrayList<Purchase> = ArrayList()

            for (i in 0 until lista.length()) {
                var obj: String = lista.getString(i)
                var jso: JSONObject = JSONObject(obj)


                var returnf: Int = 0
                var id = jso.getInt("id")
                var flightid = jso.getInt("flightid")

                var isselected = jso.getBoolean("isselected")
                var totalprice = jso.getDouble("totalprice")
                var tickets = jso.getInt("tickets")
                if (jso.has("returnflightid")) {
                    returnf = jso.getInt("returnflightid")
                }
                var userid = jso.getString("userid")
                var discount = 0

                fli.add(Purchase(
                        id,
                        flightid,
                        isselected,
                        totalprice,
                        tickets,
                        returnf,
                        userid,
                        discount
                ))
            }
            this.purchase.postValue(fli)
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

    fun purchase(user:String, cant : Int, id : Int ) {

        viewModelScope.launch {
            val gson = Gson()
            val req = Properties()
            req.put("Action", "create")
            req.put("userid", user)
            req.put("tickets", cant)
            req.put("flightid", id)
            outputEventChannel.send(gson.toJson(req))
        }
    }

    companion object {
        private const val PATH_LOGIN = "$PATH_APP/purchase"
    }


}