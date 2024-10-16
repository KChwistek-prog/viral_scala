package controllers

import model.UserRepository
import play.api.libs.json.Json
import play.api.mvc.*

import javax.inject.*
import scala.concurrent.{ExecutionContext, Future}

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

  def checkUser() = Action.async { implicit request: Request[AnyContent] =>
    request.body.asJson match {
      case Some(json) =>
        val maybeUserName = (json \ "userName").asOpt[String]
        val maybePassword = (json \ "password").asOpt[String]

        (maybeUserName, maybePassword) match {
          case (Some(userName), Some(password)) =>
            userRepository.checkCredentials(userName, password).map {
              case true => Ok(Json.obj("status" -> "success", "message" -> "Credentials are valid"))
              case false => Unauthorized(Json.obj("status" -> "failure", "message" -> "Invalid credentials"))
            }
          case _ => Future.successful(BadRequest(Json.obj("status" -> "error", "message" -> "Missing username or password")))
        }
      case None => Future.successful(BadRequest(Json.obj("status" -> "error", "message" -> "Expected Json body")))
    }
  }

  def getUsers() = Action.async { implicit request =>
    userRepository.list().map {
      items => Ok(Json.toJson(items))
    }

  }
}
