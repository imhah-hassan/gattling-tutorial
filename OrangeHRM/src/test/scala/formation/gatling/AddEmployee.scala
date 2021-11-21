package formation.gatling

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import scala.util.Random

class AddEmployee extends Simulation {
	//val base_url = "https://opensource-demo.orangehrmlive.com"
	//val admin_pwd = "admin123"

	val base_url = "http://192.168.1.208:90"
	val admin_pwd = "Hassan$2022"
	object Utils {
		def randomCode( ) : String = {
			val rnd = new Random()
			return (10000 + rnd.nextInt(88888)).toString()
		}
	}


		val httpProtocol = http
		.baseUrl( base_url )
		// .proxy(Proxy("localhost", 8888))
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
		"Origin" ->  base_url ,
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
		"Origin" ->  base_url ,
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
			.formParam("txtPassword", admin_pwd)
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

		// ADD EMPLOYEE
		.exec (session => session.set("code", Utils.randomCode()))
		.exec(http("viewPimModule")
			.get("/index.php/pim/viewPimModule")
			.check(status.is(200))
			// .headers(headers_4)
			.resources(http("getEmployeeListAjax")
			.get("/index.php/pim/getEmployeeListAjax"),
			//.headers(headers_5),
            http("addEmployeeGet")
			.get("/index.php/pim/addEmployee")
			.check(status.is(200))
			.check(css("#csrf_token", "value").saveAs("csrf_token"))
			.check(css("#empNumber", "value").saveAs("empNumber"))

				//.headers(headers_4)
			))
		.pause(0)
		.exec(http("addEmployeePost")
			.post("/index.php/pim/addEmployee")
			.headers(headers_7)
			.body(ElFileBody("formation/gatling/addemployee/0007_request.dat"))
			.check(status.is(200))
			.check(css("#personal__csrf_token", "value").saveAs("personal_csrf_token"))
			.check(css("#personal_txtEmpID", "value").saveAs("personal_txtEmpID"))

			.resources(http("getHolidayAjax")
			.get("/index.php/leave/getHolidayAjax?year=2021&_=1634564077374"),
			//.headers(headers_5),
            http("getWorkWeekAjax")
			.get("/index.php/leave/getWorkWeekAjax?_=1634564077375")
			//.headers(headers_5)
			))
		.pause(0)

		// EMPLOYEE DATAILS
		.exec(http("viewPersonalDetails")
			.post("/index.php/pim/viewPersonalDetails")
			//.headers(headers_1)
			.formParam("personal[_csrf_token]", "${personal_csrf_token}")
			.formParam("personal[txtEmpID]", "${personal_txtEmpID}")
			.formParam("personal[txtEmpFirstName]", "Performance${code}")
			.formParam("personal[txtEmpMiddleName]", "")
			.formParam("personal[txtEmpLastName]", "Gatling${code}")
			.formParam("personal[txtEmployeeId]", "${code}")
			.formParam("personal[txtOtherID]", "")
			.formParam("personal[txtLicenNo]", "")
			.formParam("personal[txtLicExpDate]", "yyyy-mm-dd")
			.formParam("personal[txtNICNo]", "")
			.formParam("personal[txtSINNo]", "")
			.formParam("personal[optGender]", "1")
			.formParam("personal[cmbMarital]", "Married")
			.formParam("personal[cmbNation]", "64")
			.formParam("personal[DOB]", "1989-11-25")
			.formParam("personal[txtEmpNickName]", "")
			.formParam("personal[txtMilitarySer]", "")
			.check(status.is(200))
			.resources(http("getWorkWeekAjax")
			.get("/index.php/leave/getWorkWeekAjax?_=1634564107742"),
			//.headers(headers_5),
            http("getHolidayAjax")
			.get("/index.php/leave/getHolidayAjax?year=2021&_=1634564107741")
			//.headers(headers_5)
			))
		.pause(0)


		// SEARCH EMPLOYEE
		.exec(http("viewEmployeeList")
			.get("/index.php/pim/viewEmployeeList/reset/1")
			.check(status.is(200))
			.check(css("#defaultList__csrf_token", "value").saveAs("defaultList_csrf_token"))
			.check(css("#empsearch__csrf_token", "value").saveAs("empsearch_csrf_token"))
			.check(css("#resultTable>tbody>tr:nth-of-type(1)>td:nth-of-type(1)>input[type='checkbox']", "value").saveAs("empId"))
			.check(css("#resultTable>tbody>tr:nth-of-type(1)>td:nth-of-type(2)>a").saveAs("empNumber"))

			//.headers(headers_4)
			.resources(http("getEmployeeListAjax")
			.get("/index.php/pim/getEmployeeListAjax")
			//.headers(headers_5)
			))
		.pause(8)
		.exec(http("viewEmployeeList")
			.post("/index.php/pim/viewEmployeeList")
			//.headers(headers_1)
			.formParam("empsearch[employee_name][empName]", "Gatling")
			.formParam("empsearch[employee_name][empId]", "${empId}")
			.formParam("empsearch[id]", "")
			.formParam("empsearch[employee_status]", "0")
			.formParam("empsearch[termination]", "1")
			.formParam("empsearch[supervisor_name]", "")
			.formParam("empsearch[job_title]", "0")
			.formParam("empsearch[sub_unit]", "0")
			.formParam("empsearch[isSubmitted]", "yes")
			.formParam("empsearch[_csrf_token]", "${empsearch_csrf_token}")
			.formParam("pageNo", "")
			.formParam("hdnAction", "search")
			.check(status.is(200))
			.resources(http("getEmployeeListAjax")
			.get("/index.php/pim/getEmployeeListAjax")
			//.headers(headers_5)
			))
		.pause(0)

		// DELETE EMPLOYEE
		.exec(http("deleteEmployees")
			.post("/index.php/pim/deleteEmployees")
			//.headers(headers_1)
			.formParam("defaultList[_csrf_token]", "${defaultList_csrf_token}")
			.formParam("chkSelectAll", "")
			.formParam("chkSelectRow[]", "${empId}")
			.check(status.is(200))
			.resources(http("getEmployeeListAjax")
			.get("/index.php/pim/getEmployeeListAjax")
			//.headers(headers_5)
			))
		.pause(0)
		
		// LOGOUT
		.exec(http("logout")
			.get("/index.php/auth/logout")
			.check(status.is(200))

			//	.headers(headers_4)
		)

	setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}