package una.moviles.persistence

import androidx.lifecycle.MutableLiveData
import org.json.JSONArray
import org.json.JSONObject
import una.moviles.logic.Flight
import una.moviles.logic.Purchase
import una.moviles.logic.User

object BD {

    var user: User? = null
    val flight = MutableLiveData<List<Flight>>()

    val purchase = MutableLiveData<List<Purchase>>()

    init {

        flight.value = listOf(

            Flight( "22/4/2020","Costa Rica","7:30",4,60,58,1,23500,"9:50",1,0,"China"),
            Flight( "22/4/2020","China","7:30",4,60,58,1,23500,"9:50",1,0,"Costa Rica"),
            Flight( "22/4/2020","Argentina","7:30",4,60,58,1,23500,"9:50",1,0,"USA"),
            Flight( "22/4/2020","Usa","7:30",4,60,58,1,23500,"9:50",1,0,"Argentina")
        )


        purchase.value = listOf(Purchase(1,1,25620.0,5,1,"User3",10),
            Purchase(1,1,25620.0,5,1,"User3",10),
            Purchase(1,1,25620.0,5,0,"User3",5),
            Purchase(1,1,25620.0,5,1,"User3",7),
            Purchase(1,1,25620.0,4,1,"User3",8)
        )

    }

    fun filterFlight(origen: String)
    {
        flight.value = flight.value!!.filter { it.origen.toLowerCase().contains(origen.toLowerCase()) }
    }

    fun filterPurchase(origen: String)
    {
        purchase.value = purchase.value!!.filter { it.userid.toLowerCase().contains(origen.toLowerCase()) }
    }

}