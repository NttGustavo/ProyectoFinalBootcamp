package com.example.proyectofinal.Models

class Lugar(var nombre: String, var duracion: String, var departamento: String, var id: String, var imagen: String, var coordenadas: Coordenadas) {
    constructor(): this("","","","","",Coordenadas("",""))
}