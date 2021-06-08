package una.moviles.logic

import java.io.Serializable
import java.util.*

class Flight (
    var id: Int,
    var outboundId: Int,
    var outboundDate: Date,
    var planeId: Int,
    var arriveTime: Date
        ) : Serializable