package model

import play.api.libs.json.{Json, OFormat}

import java.util.UUID

case class User(id: UUID,
                firstName: String,
                lastName: String,
                userName: String,
                hashedPassword: String,
                role: String)

object User {
  implicit val userFormat: OFormat[User] = Json.format[User]
}
