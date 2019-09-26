package models

import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._

case class Distance(typeOfDistance:String, disVal:Double)

object Distance {
  import play.api.libs.json._

  implicit val distanceWrites: Writes[Distance] = (
    (JsPath \ "typeOfDistance").write[String] and
      (JsPath \ "disVal").write[Double]
    )(unlift(Distance.unapply))

  implicit val distanceReads: Reads[Distance] = (
    (JsPath \ "typeOfDistance").read[String](minLength[String](1)) and
      (JsPath \ "disVal").read[Double](min(-1000.0) keepAnd max(1000.0))
    )(Distance.apply _)
}
