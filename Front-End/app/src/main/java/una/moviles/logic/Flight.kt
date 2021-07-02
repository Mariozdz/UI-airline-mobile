package una.moviles.logic

import java.io.Serializable
import java.util.*

class Flight (
    var sdate: String,
    var origen: String,
    var arrivetime: String,
    var stime: Int,
    var cantidadasientos: Int,
    var disponibles: Int,
    var outbound: Int,
    var price: Int,
    var outbounddate: String,
    var id: Int,
    var discount: Int,
    var destino: String,
    var isreturned: Int
        ) : Serializable {


                fun getIsreturned() : String
                {
                        if(isreturned != 0)
                                return "Have return"
                        return "Don´t have return"
                }
        }



// {"sdate":"07:30","arrivetime":"23:59","discount":0,"stime":4,"origen":"Costa Rica","duration":6,"cantidadasientos":60,"disponibles":60,"outbound":18,"price":1100,"planeid":101,"outbounddate":"2003-05-10","id":6,"destino":"Canada"