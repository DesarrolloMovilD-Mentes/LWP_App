package com.example.lwp_lab01.entities

class cls_PDFs {
    var Names: String = ""
    var URL: String = ""

    constructor() {}

    constructor(Names: String, URL: String) {
        this.Names = Names
        this.URL = URL
    }
    override fun toString(): String {
        // Puedes ajustar el formato a tus necesidades
        return "$Names"
    }
}