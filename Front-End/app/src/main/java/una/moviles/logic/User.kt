package una.moviles.logic

import java.io.Serializable

class User(

    var id: String,
    var address: String,
    var name: String,
    var surnames: String,
    var cellphone: String,
    var email: String,
    var password: String,
    var usertype : Int): Serializable