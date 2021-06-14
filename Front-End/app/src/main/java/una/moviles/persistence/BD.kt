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
            Flight( "22/4/2020","Argentina","7:30",4,60,58,1,23500,"9:50",1,10,"USA"),
            Flight( "22/4/2020","Usa","7:30",4,60,58,1,23500,"9:50",1,20,"Argentina")
        )


        purchase.value = listOf(Purchase(1,1,25620.0,5,1,"User3",10),
            Purchase(1,1,25620.0,5,1,"p1",10),
            Purchase(1,1,25620.0,5,0,"p1",5),
            Purchase(1,1,25620.0,5,1,"p1",7),
            Purchase(1,1,25620.0,4,1,"p1",8)
        )

    }

    fun filterFlight(origen: String)
    {
        flight.value = flight.value!!.filter { it.origen.toLowerCase().contains(origen.toLowerCase()) or it.destino.toLowerCase().contains(origen.toLowerCase()) }
    }

    fun filterPurchase(origen: String)
    {
        purchase.value = purchase.value!!.filter { it.userid.toLowerCase().contains(origen.toLowerCase()) or it.tickets.toString().toLowerCase().contains(origen.toLowerCase()) }
    }

    fun filterByDiscount()
    {
        flight.value = flight.value!!.filter { it.discount > 0 }
    }


    var users :ArrayList<User> = arrayListOf(

            User("p0","Costa Rica","Mario","Arguello Borge","80828584","p0","p0"),
            User("p1", "San Jose","Mario","Arguello Borge","88995632","mariozdz@gmail.com","p1"),
            User("p2", "Heredia","Braslyn","Rodriguez Ramirez","85964123","braslynrrr@gmail.com","p2"),
            User("p3", "San Jose","Heiner","Leon Herrera","88888888","heineken@gmail.com","p3")
    )

    fun validateUser(email: String, password: String) : User?
    {
        for(i in users)
        {
            if(i.email == email && i.password == password)
                return i
        }
        return null
    }

    fun addUser(us: User)
    {
        this.users.add(us)
    }

    fun setList(array : ArrayList<User>)
    {
        this.users = array
    }


}