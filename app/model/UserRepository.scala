package model

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import slick.lifted.ProvenShape

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api.*

  private class UserTable(tag: Tag) extends Table[User](tag, "users") {
    def id: Rep[UUID] = column[UUID]("id", O.PrimaryKey)

    def firstName: Rep[String] = column[String]("first_name")

    def lastName: Rep[String] = column[String]("last_name")

    def userName: Rep[String] = column[String]("username", O.Unique)

    def hashedPassword: Rep[String] = column[String]("hashed_password")

    def role: Rep[String] = column[String]("role")

    def * = (id, firstName, lastName, userName, hashedPassword, role) <> ((User.apply _).tupled, User.unapply)
  }

  private val users = TableQuery[UserTable]

  def createUser(firstName: String, lastName: String, userName: String, hashedPassword: String, role: String): Future[User] = {
    val newId = UUID.randomUUID()

    db.run {
      (users.map(u => (u.id, u.firstName, u.lastName, u.userName, u.hashedPassword, u.role))
        returning users.map(_.id)
        into { case (id, user) =>
        User(newId, firstName, lastName, userName, hashedPassword, role)
      }
        ) += (newId, firstName, lastName, userName, hashedPassword, role)
    }
  }

  def list(): Future[Seq[User]] = db.run {
    users.result
  }

}
