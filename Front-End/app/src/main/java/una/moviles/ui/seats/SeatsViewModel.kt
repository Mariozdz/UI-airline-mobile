package una.moviles.ui.seats

import android.media.Image
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonObject
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
import una.moviles.logic.PlaneType
import una.moviles.ui.HOST
import una.moviles.ui.PATH_APP
import una.moviles.ui.PORT
import java.util.*
import kotlin.collections.ArrayList

class SeatsViewModel : ViewModel() {

    var client: HttpClient? = null
    var flights: MutableLiveData<List<Flight>>
    val outputEventChannel: Channel<String> = Channel(10)
    val inputEventChannel: Channel<String> = Channel(10)

    // For purchase

    var clientP: HttpClient? = null
    var flag : MutableLiveData<Boolean>
    val outputChannel: Channel<String> = Channel(10)
    val inputChannel: Channel<String> = Channel(10)
    var dimension: MutableLiveData<PlaneType>
    var seats : MutableLiveData<List<Pair<Int,Int>>>



    init {
        flights = MutableLiveData()
        flag = MutableLiveData()
        flag.value = false
        dimension = MutableLiveData()
        seats = MutableLiveData()
    }

    fun open(coroutineScope: CoroutineScope, id:Int) {
        client = buildClient()
        coroutineScope.launch {
            client!!.webSocket(
                    path = PATH_LOGIN,
                    host = HOST,
                    port = PORT
            ) {
                usedFields(id)
                val input = launch { output() }
                val output = launch { input() }
                input.join()
                output.join()
            }
            client?.close()
        }
    }

    // For purchase

    fun openPurchase(coroutineScope: CoroutineScope, id: Int) {
        clientP = buildClientPurchase()
        coroutineScope.launch {
            clientP!!.webSocket(
                    path = PATH_LOGIN2,
                    host = HOST,
                    port = PORT
            ) {
                /*solicitarDimension(id)*/
                val input = launch { outputPurchase() }
                val output = launch { inputPurchase() }
                input.join()
                output.join()
            }
            clientP?.close()
        }
    }



    fun close() {
        Log.d("onClose", "onClose")
        client?.close()
        client = null
    }

    fun closePurchase() {
        Log.d("onClosePurchase", "onClose")
        clientP?.close()
        clientP = null
    }


    private suspend fun DefaultClientWebSocketSession.input() {
        try {
            for (frame in incoming) {
                frame as? Frame.Text ?: continue
                try {
                    Log.d("onMessage", "Message received: ${frame.readText()}")
                    Log.d("isEmpty?",  frame.readText())

                    var res: String = frame.readText()

                    if(res.contains("action")) {
                        get_all()
                    }
                    else if (res.contains("column") || res == "[]" )
                    {
                        Log.d("Ocupados", res)
                        parseSeats(res)
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

    private suspend fun DefaultClientWebSocketSession.inputPurchase() {
        try {
            for (frame in incoming) {
                frame as? Frame.Text ?: continue
                try {
                    Log.d("onMessagePurchase", "Message received: ${frame.readText()}")

                    var res: String = frame.readText()

                    if (res.contains("dimension"))
                    {
                        parseType(res)
                    }

                    if(res.contains("action")) {
                        get_all()
                    }

                } catch (e: Throwable) {
                    Log.e("onMessage", "${e.message}", e)
                }
            }
        } catch (e: Throwable) {
            Log.e("onMessage", "${e.message}", e)
        }
    }

    private fun parseType(res : String) {
       val gson = Gson()

        val properties = gson.fromJson(res, Properties::class.java)

        var numberrow = properties.getProperty("numberrow");
        var model = properties.getProperty("model");
        var numbercolums = properties.getProperty("numbercolums");
        var id = properties.getProperty("id");
        var brand = properties.getProperty("brand");

        this.dimension.postValue( PlaneType(numbercolums.toInt(),model,numberrow.toInt(),id.toInt(),brand));
    }


    private fun parseSeats(res: String)
    {
        val gson = Gson()

        val lista = JSONArray(res)

        var tuples : ArrayList<Pair<Int,Int>> = ArrayList()

        for ( i in  0 until lista.length())
        {
            var obj : String = lista.getString(i)
            var jso :  JSONObject = JSONObject(obj)

            tuples.add(
                    Pair(jso.getInt("column"),jso.getInt("row"))
            )
        }

        Log.d("Parser", res + "canti " + lista.length() + "cantires " + tuples.size )
        this.seats.postValue(tuples)
        this.flag.postValue(true)

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
                    jso.getString("destino"),
                    jso.getInt("isreturned"))
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

    // For purchase

    private suspend fun DefaultClientWebSocketSession.outputPurchase() {
        try {
            outputChannel.consumeEach {
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

    private fun buildClientPurchase() = HttpClient {
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

     fun solicitarDimension(id : Int)
    {
        viewModelScope.launch {
            val gson = Gson()
            val req = Properties()
            req.put("Action", "GET_DIMENSIONES")
            req.put("flightid", id)
            outputChannel.send(gson.toJson(req))
        }
    }

    private fun usedFields(id : Int){

        viewModelScope.launch {
            val gson = Gson()
            val req = Properties()
            req.put("Action", "GET_ACQUIRED_FIELDS")
            req.put("flightid", id)
            outputEventChannel.send(gson.toJson(req))
        }
    }

     fun generarTickets(seats : MutableList<String>, id:Int)
    {
        var campos = JSONArray()

        for (i in 0 until seats.size)
        {


            var temp = JSONObject()
            temp.put("column", seats[i].split('-')[1] )
            temp.put("row", seats[i].split('-')[0] )
            temp.put("isreturn",0)

            campos.put(temp)
        }
        Log.d("seats", seats.toString())
        Log.d("pruebas insert", campos.toString(1))


        viewModelScope.launch {
            val req = JSONObject()
            req.put("Action", "Create_tickets")
            req.put("asientos",campos)
            req.put("purchaseid", id )

            outputChannel.send(req.toString())
        }

    }
    companion object {
        private const val PATH_LOGIN = "$PATH_APP/flight"
        private const val PATH_LOGIN2 = "$PATH_APP/purchase"
    }

}