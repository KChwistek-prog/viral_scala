package controllers

import model.UserRepository
import play.api.libs.json.Json
import play.api.mvc.*

import javax.inject.*
import scala.concurrent.ExecutionContext

@Singleton
class UserController @Inject()(cc: ControllerComponents, userRepository: UserRepository)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def createUser() = Action.async { implicit request: Request[AnyContent] =>
    val json = request.body.asJson.get

    val firstName = (json \ "firstName").as[String]
    val lastName = (json \ "lastName").as[String]
    val userName = (json \ "userName").as[String]
    val hashedPassword = (json \ "hashedPassword").as[String]
    val role = (json \ "role").as[String]

    userRepository.createUser(firstName, lastName, userName, hashedPassword, role).map { user =>
      Created(Json.toJson(user))
    }.recover {
      case ex: Exception =>
        InternalServerError("An error occurred: " + ex.getMessage)
    }
  }

  def getUsers() = Action.async { implicit request =>
    userRepository.list().map {
      items => Ok(Json.toJson(items))
    }

  }
}
