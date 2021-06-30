package una.moviles.logic

import java.io.Serializable

class Purchase (

    var id: Int,
    var flightid: Int,
    var isselected: Boolean,
    var totalprice: Double,
    var tickets: Int,
    var returnflightid: Int,
    var userid: String,
    var realTickets: Int
        ) : Serializable



//{"tickets":10,"returnflightid":0,"totalprice":60000,"flightid":1,"id":1,"userid":"User3","realsTickets":10}