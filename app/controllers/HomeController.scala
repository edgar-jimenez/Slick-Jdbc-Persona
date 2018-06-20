package controllers

import javax.inject._
import models.Persona
import persistence.{ PersonaRepository}
import play.api.libs.json.Json
import play.api.mvc._
import play.api.Logger
import scala.concurrent.{ExecutionContext, Future}



@Singleton
class HomeController @Inject() (repository: PersonaRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc)  {

  private val logger = play.api.Logger(this.getClass)


  def listar= Action.async{ request: Request[AnyContent] =>
    val resultado: Future[Option[Set[Persona]]] = repository.getPersonas
    resultado
      .map(s => Ok(Json.toJson(s)))
      .recoverWith{
        case error: Exception => {
          Logger.error(error.toString)
          Future.successful( InternalServerError(s"No se encontro ninguna persona con el nombre:"))
        }
      }
  }

  def addPersona() = Action.async(parse.json[Persona]) { request =>
    insertPersona(request.body)
  }

  def eliminarPersona() = Action.async(parse.json[Persona]) { request =>
    deletePersona(request.body)
  }

  def insertPersona(persona: Persona): Future[Result] = {
    repository.setPersona(persona)
      .map(_ => Ok("Persona Agregada Con Exito"))
      .recoverWith{
        case error: Exception => {
          Logger.error(error.toString)
          Future.successful( InternalServerError(s"no se pudo agragr a la persona: $persona"))
        }
      }
  }

  def deletePersona(persona: Persona): Future[Result] = {
    repository.deletePersona(persona)
      .map(resultFuture => {
        if (resultFuture>0) Ok("Persona Eliminado Con Exito") else NotFound(s"Persona [$persona] no existe")
      })
      .recoverWith{
        case error: Exception => {
          Logger.error(error.toString)
          Future.successful( InternalServerError(s"no se pudo agragr a la persona: $persona"))
        }
      }
  }
}
