package persistence

import javax.inject.{Inject, Singleton}
import models.{DatabaseExecutionContext, Persona}
import play.api.Logger
import play.api.db.DBApi
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PersonaRepository @Inject() (dbapi: DBApi) (implicit ec: DatabaseExecutionContext){

  private val db = dbapi.database("default")

  private val personaquery = TableQuery[PersonaTable]

  def all() : Future[Seq[Persona]] = db.run(personaquery.result)

  def add(persona: Persona): Future[Unit] = db.run(personaquery += persona).map(_ => ())

  def delete(persona: Persona) : Future[Int] = {
    val q = personaquery.filter(_.name === persona.nombre)
    val action = q.delete
    db.run(action)
  }

  def buscarPorNombre(nombre:String) : Future[Seq[Persona]] = {
    val q = personaquery.filter(_.name === nombre)
    val action = q.result
    db.run(action)
  }

  private class PersonaTable(tag: Tag) extends Table[Persona] (tag, "PERSONA"){
    def name = column[String]("name", O.PrimaryKey)
    def apellido = column[String]("apellido")
    def edad = column[Int]("edad")

    def * = (name, apellido, edad) <> ( (Persona.apply _).tupled, Persona.unapply)
  }
}
