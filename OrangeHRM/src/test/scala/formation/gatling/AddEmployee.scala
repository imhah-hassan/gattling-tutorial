package formation.gatling

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class AddEmployee extends Simulation {

	val httpProtocol = http
		.baseUrl("https://opensource-demo.orangehrmlive.com")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("en-US,fr;q=0.8,fr-FR;q=0.5,en;q=0.3")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:93.0) Gecko/20100101 Firefox/93.0")

	val headers_0 = Map(
		"Cache-Control" -> "max-age=0",
		"Sec-Fetch-Dest" -> "document",
		"Sec-Fetch-Mode" -> "navigate",
		"Sec-Fetch-Site" -> "none",
		"Sec-Fetch-User" -> "?1",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_1 = Map(
		"Origin" -> "https://opensource-demo.orangehrmlive.com",
		"Sec-Fetch-Dest" -> "document",
		"Sec-Fetch-Mode" -> "navigate",
		"Sec-Fetch-Site" -> "same-origin",
		"Sec-Fetch-User" -> "?1",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Accept" -> "*/*",
		"Sec-Fetch-Dest" -> "empty",
		"Sec-Fetch-Mode" -> "cors",
		"Sec-Fetch-Site" -> "same-origin",
		"X-Requested-With" -> "XMLHttpRequest")

	val headers_4 = Map(
		"Sec-Fetch-Dest" -> "document",
		"Sec-Fetch-Mode" -> "navigate",
		"Sec-Fetch-Site" -> "same-origin",
		"Sec-Fetch-User" -> "?1",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_5 = Map(
		"Accept" -> "application/json, text/javascript, */*; q=0.01",
		"Sec-Fetch-Dest" -> "empty",
		"Sec-Fetch-Mode" -> "cors",
		"Sec-Fetch-Site" -> "same-origin",
		"X-Requested-With" -> "XMLHttpRequest")

	val headers_7 = Map(
		"Content-Type" -> "multipart/form-data; boundary=---------------------------26091326749537329901312335116",
		"Origin" -> "https://opensource-demo.orangehrmlive.com",
		"Sec-Fetch-Dest" -> "document",
		"Sec-Fetch-Mode" -> "navigate",
		"Sec-Fetch-Site" -> "same-origin",
		"Sec-Fetch-User" -> "?1",
		"Upgrade-Insecure-Requests" -> "1")



	val scn = scenario("AddEmployee")
		// HOME
		.exec(http("login")
			.get("/index.php/auth/login")
			.check(status.is(200))
			.check( css("#csrf_token","value").saveAs("csrf_token"))

			//	.headers(headers_0)
		)
		.pause(0)

		// LOGIN
		.exec(http("validateCredentials")
			.post("/index.php/auth/validateCredentials")
			//.headers(headers_1)
			.formParam("actionID", "")
			.formParam("hdnUserTimeZoneOffset", "2")
			.formParam("installation", "")
			.formParam("_csrf_token", "${csrf_token}")
			.formParam("txtUsername", "Admin")
			.formParam("txtPassword", "admin123")
			.formParam("Submit", "LOGIN")
			.check(status.is(200))
			.resources(http("employeeDistribution")
			.get("/index.php/dashboard/employeeDistribution")
				.check(status.is(200))
				,
			//.headers(headers_2),
            http("pendingLeaveRequests")
			.get("/index.php/dashboard/pendingLeaveRequests")
							.check(status.is(200))

				//	.headers(headers_2)
		))
		.pause(0)

//		// ADD EMPLOYEE
//		.exec(http("request_4")
//			.get("/index.php/pim/viewPimModule")
//			.headers(headers_4)
//			.resources(http("request_5")
//			.get("/index.php/pim/getEmployeeListAjax")
//			.headers(headers_5),
//            http("request_6")
//			.get("/index.php/pim/addEmployee")
//			.headers(headers_4)))
//		.pause(19)
//		.exec(http("request_7")
//			.post("/index.php/pim/addEmployee")
//			.headers(headers_7)
//			.body(RawFileBody("formation/gatling/addemployee/0007_request.dat"))
//			.resources(http("request_8")
//			.get("/index.php/leave/getHolidayAjax?year=2021&_=1634564077374")
//			.headers(headers_5),
//            http("request_9")
//			.get("/index.php/leave/getWorkWeekAjax?_=1634564077375")
//			.headers(headers_5)))
//		.pause(29)
//
//		// EMPLOYEE DATAILS
//		.exec(http("request_10")
//			.post("/index.php/pim/viewPersonalDetails")
//			.headers(headers_1)
//			.formParam("personal[_csrf_token]", "b46564e8cd646e7256be3f5d504624c3")
//			.formParam("personal[txtEmpID]", "48")
//			.formParam("personal[txtEmpFirstName]", "Performance")
//			.formParam("personal[txtEmpMiddleName]", "")
//			.formParam("personal[txtEmpLastName]", "Gatling")
//			.formParam("personal[txtEmployeeId]", "9009")
//			.formParam("personal[txtOtherID]", "")
//			.formParam("personal[txtLicenNo]", "")
//			.formParam("personal[txtLicExpDate]", "yyyy-mm-dd")
//			.formParam("personal[txtNICNo]", "")
//			.formParam("personal[txtSINNo]", "")
//			.formParam("personal[optGender]", "1")
//			.formParam("personal[cmbMarital]", "Married")
//			.formParam("personal[cmbNation]", "64")
//			.formParam("personal[DOB]", "1989-11-25")
//			.formParam("personal[txtEmpNickName]", "")
//			.formParam("personal[txtMilitarySer]", "")
//			.resources(http("request_11")
//			.get("/index.php/leave/getWorkWeekAjax?_=1634564107742")
//			.headers(headers_5),
//            http("request_12")
//			.get("/index.php/leave/getHolidayAjax?year=2021&_=1634564107741")
//			.headers(headers_5)))
//		.pause(11)
//
//
//		// SEARCH EMPLOYEE
//		.exec(http("request_13")
//			.get("/index.php/pim/viewEmployeeList/reset/1")
//			.headers(headers_4)
//			.resources(http("request_14")
//			.get("/index.php/pim/getEmployeeListAjax")
//			.headers(headers_5)))
//		.pause(8)
//		.exec(http("request_15")
//			.post("/index.php/pim/viewEmployeeList")
//			.headers(headers_1)
//			.formParam("empsearch[employee_name][empName]", "Gatling")
//			.formParam("empsearch[employee_name][empId]", "48")
//			.formParam("empsearch[id]", "")
//			.formParam("empsearch[employee_status]", "0")
//			.formParam("empsearch[termination]", "1")
//			.formParam("empsearch[supervisor_name]", "")
//			.formParam("empsearch[job_title]", "0")
//			.formParam("empsearch[sub_unit]", "0")
//			.formParam("empsearch[isSubmitted]", "yes")
//			.formParam("empsearch[_csrf_token]", "25ccbbe42911b4d5962a3d6ec6567d43")
//			.formParam("pageNo", "")
//			.formParam("hdnAction", "search")
//			.resources(http("request_16")
//			.get("/index.php/pim/getEmployeeListAjax")
//			.headers(headers_5)))
//		.pause(12)
//
//		// DELETE EMPLOYEE
//		.exec(http("request_17")
//			.post("/index.php/pim/deleteEmployees")
//			.headers(headers_1)
//			.formParam("defaultList[_csrf_token]", "edd286c7e549a3e4c9654cbece8e15c8")
//			.formParam("chkSelectAll", "")
//			.formParam("chkSelectRow[]", "48")
//			.resources(http("request_18")
//			.get("/index.php/pim/getEmployeeListAjax")
//			.headers(headers_5)))
//		.pause(8)
		
		// LOGOUT
		.exec(http("logout")
			.get("/index.php/auth/logout")
			.check(status.is(200))

			//	.headers(headers_4)
		)

	setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}