package com.example.proyectofinal.Utils

class TextUtils {
    companion object {
        fun isEmptyUser(email: String, password: String): Boolean{
            return (email.isEmpty() || password.isEmpty())
        }

        fun isLong(password: String):Boolean{
            return (password.length >= 6)
        }

        fun containEmail(email: String):Boolean{
            return email.contains("@")
        }

        fun userisValidated(isEmpty: Boolean, isLong: Boolean, containEmail:Boolean):Boolean{
            return !isEmpty && isLong && containEmail
        }

        fun isEmptyNewUser(nombre: String, email: String, telefono: String, password: String, confirm_password: String, pregunta: String, respuesta:String): Boolean {
            return nombre.isEmpty() || email.isEmpty() || telefono.isEmpty() || password.isEmpty() || confirm_password.isEmpty() || pregunta.isEmpty() || respuesta.isEmpty()
        }


        fun isPasswordSame(password: String, confirm_password: String): Boolean{
            return password.equals(confirm_password)
        }

        fun isPhone(telefono: String): Boolean{
            return telefono.length == 9
        }

        fun isNewUserValidated(isEmpty: Boolean, isLong: Boolean, containEmail: Boolean, passwordSame: Boolean, isPhone: Boolean) : Boolean{
            return !isEmpty && isLong && containEmail && passwordSame && isPhone
        }

        fun correccionEncrypt(encriptado: String):String{
            return encriptado.replace("/","9")
        }
    }
}