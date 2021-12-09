package formation.gatling

import com.typesafe.config.ConfigFactory
import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.Random

class StaticHtml extends Simulation {
	def getProperty (propertyName: String, defaultValue:String) ={
		Option (System.getenv(propertyName))
			.orElse(Option(System.getProperty(propertyName)))
			.getOrElse(defaultValue)
	}
	def thinktime( ) : FiniteDuration = {
		val rnd = new Random()
		return (rnd.nextInt(MAX_THK-MIN_THK) + MIN_THK).milliseconds
	}

	val PACING:Int = getProperty("PACING", "10").toInt										// seconds
	val MIN_THK:Int = getProperty("MIN_THK", "1000").toInt								// miliseconds
	val MAX_THK:Int = getProperty("MAX_THK", "3000").toInt								// miliseconds
	val USERS_COUNT:Int = getProperty("USERS", "10").toInt
	val RAMP_DURATION:Int = getProperty("RAMP_DURATION", "2").toInt				// minutes
	val TEST_DURATION:Int = getProperty("DURATION", "5").toInt						// minutes
	val ITERATIONS:Int = getProperty("ITERATIONS", "1").toInt



	val httpProtocol = http
		.baseUrl("http://192.168.1.208")
		.disableCaching
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("en-US,fr;q=0.8,fr-FR;q=0.5,en;q=0.3")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:93.0) Gecko/20100101 Firefox/93.0")

	object web {
		def home = {
				exec(http("GetRandomPhoto")
					.get("/RandomPhoto/index.html")
					.check(status.is(200))
				)
				.pause(thinktime())

		}
	}


	before {
		println(s"Running users with ${USERS_COUNT} users")
		println(s"Ramping over ${RAMP_DURATION} minutes")
		println(s"Total test duration ${TEST_DURATION} minutes")

		println(s"User iteration pacing ${PACING} seconds")
		println(s"Request min think time  ${MIN_THK} milliseconds")
		println(s"Request max think time ${MAX_THK} milliseconds")
	}
	after{
		println( "Load test complete !")
	}


	val scn = scenario("StaticHtml")
		// HOME
		.during (TEST_DURATION.minutes) {
			pace (PACING seconds)
			randomSwitch(
				100d->(web.home),
			)
		}

	setUp(scn.inject(
		rampUsers (USERS_COUNT) during (RAMP_DURATION.minutes)
	)).protocols(httpProtocol)


}