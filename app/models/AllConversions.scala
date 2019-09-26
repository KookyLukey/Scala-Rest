package models

import play.api.libs.functional.syntax.unlift
import play.api.libs.json.Reads.{max, min, minLength}
import play.api.libs.functional.syntax._

import scala.concurrent.Future

case class AllConversions(distance: Distance, temperature: Temperature, volume: Volume)

object AllConversions {
  import play.api.libs.json._

  implicit val allWrites: Writes[AllConversions] = (
    (JsPath \ "distance").write[Distance] and
      (JsPath \ "temperature").write[Temperature] and
      (JsPath \ "volume").write[Volume]
    )(unlift(AllConversions.unapply))

  implicit val allReads: Reads[AllConversions] = (
    (JsPath \ "distance").read[Distance] and
      (JsPath \ "temperature").read[Temperature] and
      (JsPath \ "volume").read[Volume]
    )(AllConversions.apply _)
}
