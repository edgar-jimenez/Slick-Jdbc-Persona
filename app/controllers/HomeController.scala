package controllers

import javax.inject._
import models.Persona
import persistence.PersonaRepository
import play.api.libs.json.Json
import play.api.mvc._
import play.api.Logger


import scala.concurrent.{ExecutionContext, Future}



@Singleton
class HomeController @Inject()( cc: ControllerComponents, personaRepository: PersonaRepository)(implicit executionContext: ExecutionContext)
  extends AbstractController(cc) {

  def listarPersonas() = Action.async{ request: Request[AnyContent] =>
    val fPersonas: Future[Seq[Persona]] = personaRepository.all()

    fPersonas.map(s => Ok( Json.toJson(s)))
  }

  def addPersona() = Action.async(parse.json[Persona]) { request =>
    insertPersona(request.body)
  }

  def eliminarPersona() = Action.async(parse.json[Persona]) {request =>
    deletePersona(request.body)
  }

  def buscar(nombre: String)= Action.async{ request: Request[AnyContent] =>
    val resultado: Future[Seq[Persona]] = personaRepository.buscarPorNombre(nombre)
    resultado
      .map(s => Ok(Json.toJson(s.head)))
      .recoverWith{
        case error: Exception => {
          Logger.error(error.toString)
          Future.successful( InternalServerError(s"No se encontro ninguna persona con el nombre: $nombre"))
        }
      }
  }

  private def deletePersona(persona: Persona): Future[Result] = {

    personaRepository.delete(persona)
      .map(resultado => {
        if (resultado>0) Ok("Persona Elimino Con Exito: "+resultado) else NotFound("Persona no Existe")
      })
      .recoverWith{
        case error: Exception => {
          Logger.error(error.toString)
          Future.successful( InternalServerError("no se pudo eliminar a la persona "))
        }
      }
  }

  private def insertPersona(persona: Persona): Future[Result] = {
    personaRepository.add(persona)
      .map(_ => Ok("Persona Agregada Con Exito"))
      .recoverWith{
        case error: Exception => {
          Logger.error(error.toString)
          Future.successful( InternalServerError(s"no se pudo agragr a la persona: $persona"))
        }
      }
  }

}
