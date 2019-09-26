package models

import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._

case class Temperature(typeOfTemp: String, tempVal: Double)

object Temperature {
  import play.api.libs.json._

  implicit val temperatureWrites: Writes[Temperature] = (
    (JsPath \ "typeOfTemp").write[String] and
      (JsPath \ "tempVal").write[Double]
    )(unlift(Temperature.unapply))

  implicit val tempReads: Reads[Temperature] = (
    (JsPath \ "typeOfTemp").read[String](minLength[String](1)) and
      (JsPath \ "tempVal").read[Double](min(-1000.0) keepAnd max(1000.0))
    )(Temperature.apply _)
}
