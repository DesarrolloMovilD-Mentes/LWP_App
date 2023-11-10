package com.example.lwp_lab01.entities

class cls_Usuario {
    var token: String = ""
    var nombre: String = ""
    var email: String = ""
    var contra: String = ""
    var schoolgrade: String = ""
    var whoAmI: String = ""
    var photo: String = ""

    constructor() {}

    constructor(id: String, nombre: String, email: String, contra: String, schoolgrade:String, whoAmI:String, photo:String) {
        this.token = id
        this.nombre = nombre
        this.email = email
        this.contra = contra
        this.schoolgrade = schoolgrade
        this.whoAmI = whoAmI
        this.photo = photo

    }
}