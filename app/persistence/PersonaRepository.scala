package persistence

import java.sql._

import javax.inject.{Inject, Singleton}
import models.Persona
import play.api.Logger
import play.api.db.DBApi

import scala.concurrent.Future

@Singleton
class PersonaRepository @Inject() (dbapi: DBApi) (implicit ec: DatabaseExecutionContext){

  private val db = dbapi.database("default")

  def getPersonas:Future[Option[Set[Persona]]] = Future {
    val conn = db.getConnection()
    var lista = Set[Persona]()
    try {
      val stmt = conn.createStatement
      val rs = stmt.executeQuery("SELECT * FROM persona")

      while (rs.next()) {
        val persona = Persona(rs.getString("name"), rs.getString("apellido"),rs.getInt("edad"))
        lista += persona
      }
      Some(lista)
    } finally {
      conn.close()
    }
  }

  def setPersona(persona: Persona) : Future[Unit] = Future{
    val conn = db.getConnection()
    val insertSql = """
                      |insert into persona (name,apellido,edad)
                      |values (?,?,?)
                    """.stripMargin
    try {
      val preparedStmt: PreparedStatement = conn.prepareStatement(insertSql)
      preparedStmt.setString (1, persona.nombre)
      preparedStmt.setString (2, persona.apellido)
      preparedStmt.setInt    (3, persona.edad)
      preparedStmt.execute
    } finally {
      conn.close()
    }
  }

  def deletePersona(persona: Persona) : Future[Int] = Future{
    val conn = db.getConnection()
    val Sql = """DELETE FROM  persona WHERE name = (?)"""
    try {
      val preparedStmt: PreparedStatement = conn.prepareStatement(Sql)
      preparedStmt.setString (1, persona.nombre)
      preparedStmt.executeUpdate()
    } finally {
      conn.close()
    }
  }
}
