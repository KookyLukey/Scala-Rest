package controllers

import java.sql.{Connection, DriverManager}

import akka.http.scaladsl.model.HttpHeader.ParsingResult.Ok
import models.{AllConversions, Distance, Temperature, Volume}
import play.api.libs.json.{JsValue, Json}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.Future
import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.util.{Success, Try}

class Convert {

  val exTemp = new Temperature("F", 95)
  val exDis = new Distance("M", 100)
  val exVol = new Volume("Inch", 302)

  var qTemperature = mutable.Queue[Temperature]()
  var qVolume      = mutable.Queue[Volume]()
  var qDistance    = mutable.Queue[Distance]()

  def fetchTemperature(): Future[Temperature] = Future {
    val url = "jdbc:sqlite:C:\\Users\\luke.j.oneill\\Documents\\ScalaProjects\\scalaTest.db"
    val driver = "com.sqlite.jdbc.Driver"
    val username = "root"
    val password = "root"
    var connection:Connection = null
    var tempFinal = Temperature("", 0.0)
    try {
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)
      val statement = connection.createStatement
      val rs = statement.executeQuery("SELECT Type, Value FROM temperature")
      while (rs.next) {
        val tempType = rs.getString("Type")
        val tempValue = rs.getDouble("Value")
        tempFinal = new Temperature(tempType, tempValue)
        println("Type = %s, Value = %s".format(tempType,tempValue))
      }
    }
    connection.close
    tempFinal
  }

  def convertAll: Future[AllConversions] =  {
    val tempFuture = convertTemperature(qTemperature.dequeue())
    val disFuture = convertDistance(qDistance.dequeue())
    val volFuture = convertVolume(qVolume.dequeue())

    for {
      tRes <- tempFuture
      dRes <- disFuture
      vRes <- volFuture
    } yield AllConversions(dRes, tRes, vRes)
  }

  def convertTemperature(cTemp:Temperature) : Future[Temperature] = Future {
    //If typeOfTemp is F convert it to C and vise versa
    val typeOfTemp = cTemp.typeOfTemp
    val tempVal = cTemp.tempVal

    typeOfTemp match {
      case "F" => new Temperature("C", (tempVal - 32) * 5 / 9)
      case "C" => new Temperature("F", (tempVal / 5) + 32)
    }
  }

  def convertVolume(cVolume:Volume) : Future[Volume] = Future {
    //If typeOfVolume is Inch convert it to Liters and vise versa
    val typeOfVolume = cVolume.typeOfVolume
    val volVal = cVolume.volVal

    typeOfVolume match {
      case "Inch" => new Volume("Liters", volVal / 61.024)
      case "Liters" => new Volume("Inch", volVal * 61.024)
    }
  }

  def convertDistance(cDistance:Distance) : Future[Distance] = Future {
    //If typeOfDistance is M convert it to KM and vise versa
    val typeOfDistance = cDistance.typeOfDistance
    val disVal = cDistance.disVal

    typeOfDistance match {
      case "M" => new Distance("KM", disVal / 0.62137)
      case "KM" => new Distance("M", disVal * 0.62137)
    }
  }
}
