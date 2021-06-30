package una.moviles.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
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
import una.moviles.logic.Flight
import una.moviles.logic.User
import una.moviles.ui.HOST
import una.moviles.ui.PATH_APP
import una.moviles.ui.PORT
import java.util.*

class HomeViewModel : ViewModel() {

    var client: HttpClient? = null
    var flights: MutableLiveData<List<Flight>>
    val outputEventChannel: Channel<String> = Channel(10)
    val inputEventChannel: Channel<String> = Channel(10)

    init {
        flights = MutableLiveData()

    }

    fun open(coroutineScope: CoroutineScope) {
        client = buildClient()
        coroutineScope.launch {
            client!!.webSocket(
                path = PATH_LOGIN,
                host = HOST,
                port = PORT
            ) {
                get_all()
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

                    var res: String = frame.readText()

                    if(res.contains("action")) {
                        get_all()
                    }
                    else {
                        parseRes(res)
                    }

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

        val lista = JSONArray(res)

        var fli : ArrayList<Flight> = ArrayList()

        for (i in 0 until lista.length())
        {
            var obj : String = lista.getString(i)
            var jso : JSONObject = JSONObject(obj)

            fli.add( Flight(
                jso.getString("sdate"),
                jso.getString("origen"),
                jso.getString("arrivetime"),
                jso.getInt("stime"),
                jso.getInt("cantidadasientos"),
                jso.getInt("disponibles"),
                jso.getInt("outbound"),
                jso.getInt("price"),
                jso.getString("outbounddate"),
                jso.getInt("id"),
                jso.getInt("discount"),
                jso.getString("destino"))
            )
        }
        this.flights.postValue( fli)

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

    fun get_all() {

        viewModelScope.launch {
            val gson = Gson()
            val req = Properties()
            req.put("Action", "get_all")
            outputEventChannel.send(gson.toJson(req))
        }
    }

    companion object {
        private const val PATH_LOGIN = "$PATH_APP/flight"
    }

}