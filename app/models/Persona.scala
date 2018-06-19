package models

import play.api.libs.json._

case class Persona (nombre: String, apellido: String, edad: Int)

object Persona{

implicit val personaFormat = Json.format[Persona]

}

