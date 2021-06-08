package una.moviles.logic

import java.io.Serializable

class Purchase (

    var id: Int,
    var flightId: Int,
    var userId: String,
    var totalPrice: Double,
    var tickets: Int,
    var returnId: Int
        ) : Serializable