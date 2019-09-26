package controllers

import javax.inject._
import models.{Distance, Temperature, Volume}
import play.api._
import play.api.mvc._
import play.api.libs.json.{JsError, JsResult, JsSuccess, Json}

import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.Future
import scala.util.{Failure, Success}
import Temperature._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
    val convert = new Convert

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }
  
  def explore() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.explore())
  }

  def tutorial() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.tutorial())
  }

  def getName = Action {
    Ok("Jim")
  }

  def getTemperature = Action.async {
    convert
      .convertTemperature(convert.qTemperature.dequeue())
      .map(i => Ok(Json.toJson(i)))
  }

  def getDistance = Action.async {
    convert
      .convertDistance(convert.qDistance.dequeue())
      .map(i => Ok(Json.toJson(i)))
  }

  def getVolume = Action.async {
    convert
      .convertVolume(convert.qVolume.dequeue())
      .map(i => Ok(Json.toJson(i)))
  }

  def getAllConversions() = Action.async {
    convert.convertAll.map(i => Ok(Json.toJson(i)))
  }

  def saveTemperature = Action { request =>
    request.body.asJson match {
      case Some(json) =>
        json.validate[Temperature] match {
          case s: JsSuccess[Temperature] => {
            val temperature: Temperature = s.get
            println(temperature)
            convert.qTemperature.enqueue(temperature)
            Ok
          }
          case e: JsError => {
            println(e)
            Ok
          }
          case _ => Ok
        }
      case _ => BadRequest
    }
  }

  def saveDistance = Action { request =>
    request.body.asJson match {
      case Some(json) =>
        json.validate[Distance] match {
          case s: JsSuccess[Distance] => {
            val distance: Distance = s.get
            println(distance)
            convert.qDistance.enqueue(distance)
            Ok
          }
          case e: JsError => {
            println(e)
            Ok
          }
          case _ => Ok
        }
      case _ => BadRequest
    }
  }

  def saveVolume = Action { request =>
    request.body.asJson match {
      case Some(json) =>
        json.validate[Volume] match {
          case s: JsSuccess[Volume] => {
            val volume: Volume = s.get
            println(volume)
            convert.qVolume.enqueue(volume)
            Ok
          }
          case e: JsError => {
            println(e)
            Ok
          }
          case _ => Ok
        }
      case _ => BadRequest
    }
  }
}
