package errorHandler

import play.api.http.HttpErrorHandler
import play.api.mvc.*
import play.api.mvc.Results.{InternalServerError, Status}

import javax.inject.Inject
import scala.concurrent.*


class CustomErrorHandler extends HttpErrorHandler {
  def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] = {
    Future.successful(
      Status(statusCode)("A client error occured: " + message)
    )
  }

  override def onServerError(request: RequestHeader, exception: Throwable): Future[Result] = {
    Future.successful(
      InternalServerError("A server error occurred: " + exception.getMessage)
    )
  }
}
