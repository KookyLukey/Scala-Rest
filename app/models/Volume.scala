package models

import play.api.libs.functional.syntax.unlift
import play.api.libs.json.Reads.{max, min, minLength}
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._

case class Volume(typeOfVolume:String, volVal:Double)

object Volume {
  import play.api.libs.json._

  implicit val volumeWrites: Writes[Volume] = (
    (JsPath \ "typeOfVolume").write[String] and
      (JsPath \ "volVal").write[Double]
    )(unlift(Volume.unapply))

  implicit val volumeReads: Reads[Volume] = (
    (JsPath \ "typeOfVolume").read[String](minLength[String](1)) and
      (JsPath \ "volVal").read[Double](min(-1000.0) keepAnd max(1000.0))
    )(Volume.apply _)
}
