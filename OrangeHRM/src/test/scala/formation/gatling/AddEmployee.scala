package formation.gatling

import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.language.postfixOps
import scala.util.Random
import com.typesafe.config.ConfigFactory
import io.gatling.jdbc.Predef._

class AddEmployee extends Simulation {
	val conf = ConfigFactory.load()
	val server = conf.getString("server")
	val base_url = "http://" + server +":90"
	val admin_pwd = conf.getString("admin_pwd")
	val mysql_pwd = conf.getString("mysql_pwd")
	//
	// Ajouter le connecteur Mysql JDBC (mysql-connector-java-8.0.27.jar) dans les librairies du projet
	// Click droit Projet --> Open Module Settings --> Librairies --> Add
	//
	val existing_employees = jdbcFeeder("jdbc:mysql://"+server+":3306/orangehrm_db?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", mysql_pwd,
			"SELECT emp_number as empId, employee_id as matricule, emp_lastname as nom , emp_firstname as first_name FROM hs_hr_employee WHERE emp_number != 1 order by emp_number;").random

	val PACING:Int = getProperty("PACING", "10").toInt										// seconds
	val MIN_THK:Int = getProperty("MIN_THK", "1000").toInt								// miliseconds
	val MAX_THK:Int = getProperty("MAX_THK", "3000").toInt								// miliseconds
	val USERS_COUNT:Int = getProperty("USERS", "10").toInt
	val RAMP_DURATION:Int = getProperty("RAMP_DURATION", "2").toInt				// minutes
	val TEST_DURATION:Int = getProperty("DURATION", "5").toInt						// minutes
	val ITERATIONS:Int = getProperty("ITERATIONS", "1").toInt


	def getProperty (propertyName: String, defaultValue:String) ={
		Option (System.getenv(propertyName))
			.orElse(Option(System.getProperty(propertyName)))
			.getOrElse(defaultValue)
	}
	object Utils {
		def randomCode( ) : String = {
			val rnd = new Random()
			return (10000 + rnd.nextInt(88888)).toString()
		}
		def thinktime( ) : FiniteDuration = {
			val rnd = new Random()
			return (rnd.nextInt(MAX_THK-MIN_THK) + MIN_THK).milliseconds
		}
		def randomRow( ) : Int = {
			val rnd = new Random()
			return (rnd.nextInt(10) + 2)
		}
		def randomInt(max:Int ) : Int = {
			val rnd = new Random()
			return (rnd.nextInt(max-1) + 1)
		}
		def randomString(length: Int): String = {
			val rnd = new Random()
			rnd.alphanumeric.filter(_.isDigit).take(length).mkString
		}
	}

	val boundary = Utils.randomString(28)

	val httpProtocol = http
		.baseUrl(base_url)
		//.proxy(Proxy("localhost", 8888))
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8")
		.silentResources	// Supprime les ressources statiques du rapport
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("en-US,fr;q=0.8,fr-FR;q=0.5,en;q=0.3")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:93.0) Gecko/20100101 Firefox/93.0")

	val employees = separatedValues("employees.csv", ';').eager.random

	object Employee {
		def home = {
				exec(http("login")
					.get("/index.php/auth/login")
					.check(status.is(200))
					.check( css("#csrf_token","value").saveAs("csrf_token"))
					//	.headers(headers_0)
				)
				.pause(Utils.thinktime())

		}
		def login = {
			group("Connexion") {
				// LOGIN
				exec (session => session.set ("loggedIn", false))
				.exec (this.home)
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
					.check(regex("Welcome Hassan").exists)
					.check(css("#welcome").optional.saveAs("welcome"))
					.resources(http("employeeDistribution")
						.get("/index.php/dashboard/employeeDistribution")
						.check(status.is(200)),
						//.headers(headers_2),
						http("pendingLeaveRequests")
							.get("/index.php/dashboard/pendingLeaveRequests")
							.check(status.is(200))
					))
					.doIf("${welcome.exists()}") {
						exec (session => session.set ("loggedIn", true))
					}
					.pause(Utils.thinktime())
			}
		}
		def setLanguage = {
				// Set language
				exec(http("localization")
					.get("/index.php/admin/localization")
					.check(status.is(200))
					.check( css("#localization__csrf_token","value").saveAs("localization_csrf_token"))
					//	.headers(headers_0)
				)
				.pause(Utils.thinktime())
					.exec(http("changeLocalization")
						.post("/index.php/admin/localization")
						.formParam("localization[_csrf_token]", "${localization_csrf_token}")
						.formParam("localization[dafault_language]", "en_US") //fr   en_US
						.formParam("localization[default_date_format]", "Y-m-d")
						.check(status.is(200))
					)
					.pause(Utils.thinktime())
		}
		def add = {
			// ADD EMPLOYEE
			doIf(session => session("loggedIn").as[Boolean]) {
				group("Ajout salarié") {
					feed(employees)
						.exec(session => {
							println("Add Employee : " + session("nom").as[String] + " , " + session("prenom").as[String] + " , " + session("matricule").as[String])
							session
						})
						.exec (session => session.set ("created", false))
						.exec(http("viewPimModule")
							.get("/index.php/pim/viewPimModule")
							// .headers(headers_4)
							.resources(http("getEmployeeListAjax")
								.get("/index.php/pim/getEmployeeListAjax"),
								//.headers(headers_5),
								http("addEmployeeGet")
									.get("/index.php/pim/addEmployee")
									.check(status.is(200))
									.check(css("#csrf_token", "value").saveAs("csrf_token"))
									.check(css("#empNumber", "value").saveAs("empNumber"))
							))
						.pause(Utils.thinktime())
						.exec(http("addEmployeePost")
							.post("/index.php/pim/addEmployee")
							.headers(Map("Content-Type" -> "multipart/form-data; boundary=---------------------------26091326749537329901312335116"))
							.body(ElFileBody("formation/gatling/addemployee/0007_request.dat"))
							.check(status.is(200))
							.check(substring("Échec de Sauvegarde").notExists)
							.check(css("#personal_txtEmpID", "value").saveAs("empId"))
							.check(css("#personal__csrf_token", "value").saveAs("personal_csrf_token"))
							.resources(http("getHolidayAjax")
								.get("/index.php/leave/getHolidayAjax?year=2021&_=1634564077374"),
								//.headers(headers_5),
								http("getWorkWeekAjax")
									.get("/index.php/leave/getWorkWeekAjax?_=1634564077375")
								//.headers(headers_5)
							))
						.doIf("${empId.exists()}") {
							exec (session => session.set ("created", true))
						}
						.pause(Utils.thinktime())
						.doIf(session => session("created").as[Boolean]) {
							// EMPLOYEE DATAILS
							exec(http("viewPersonalDetails")
								.post("/index.php/pim/viewPersonalDetails")
								//.headers(headers_1)
								.formParam("personal[_csrf_token]", "${personal_csrf_token}")
								.formParam("personal[txtEmpID]", "${empId}")
								.formParam("personal[txtEmpFirstName]", "${prenom}")
								.formParam("personal[txtEmpMiddleName]", "")
								.formParam("personal[txtEmpLastName]", "${nom}")
								.formParam("personal[txtEmployeeId]", "${matricule}")
								.formParam("personal[txtOtherID]", "")
								.formParam("personal[txtLicenNo]", "")
								.formParam("personal[txtLicExpDate]", "yyyy-mm-dd")
								.formParam("personal[txtNICNo]", "")
								.formParam("personal[txtSINNo]", "")
								.formParam("personal[optGender]", "${genre}")
								.formParam("personal[cmbMarital]", "Married")
								.formParam("personal[cmbNation]", "64")
								.formParam("personal[DOB]", "${date_de_naissance}")
								.formParam("personal[txtEmpNickName]", "")
								.formParam("personal[txtMilitarySer]", "")
								.resources(http("getWorkWeekAjax")
									.get("/index.php/leave/getWorkWeekAjax?_=1634564107742"),
									//.headers(headers_5),
									http("getHolidayAjax")
										.get("/index.php/leave/getHolidayAjax?year=2021&_=1634564107741")
									//.headers(headers_5)
								))
								.pause(Utils.thinktime())
								// address
								.exec(http("viewContactDetails")
									.get("/index.php/pim/contactDetails/empNumber/${empId}")
									.check(status.is(200))
									.check(regex("<h1>Contact Details</h1>").exists)
									.check(css("#contact__csrf_token", "value").saveAs("csrf_token")))
								.exec(http("contactDetails")
									.post("/index.php/pim/contactDetails")
									.formParam("contact[_csrf_token]", "${csrf_token}")
									.formParam("contact[empNumber]", "${empId}")
									.formParam("contact[street1]", "${rue}")
									.formParam("contact[street2]", "")
									.formParam("contact[city]", "${ville}")
									.formParam("contact[province]", "${ville}")
									.formParam("contact[state]", "")
									.formParam("contact[emp_zipcode]", "${cp}")
									.formParam("contact[country]", "FR")
									.formParam("contact[emp_hm_telephone]", "${telephone}")
									.formParam("contact[emp_mobile]", "")
									.formParam("contact[emp_work_telephone]", "")
									.formParam("contact[emp_work_email]", "${email}")
									.formParam("contact[emp_oth_email]", "")
									.check(status.is(200))
									.check(css("#contact_emp_work_email", "value").is("${email}"))
								)
								.pause(Utils.thinktime)
						}
				}
			}
		}
		def getRandomEmployee = {
			exec(session => session.set("row", Utils.randomRow()))
				.exec(http("viewEmployeeList")
				.get("/index.php/pim/viewEmployeeList/reset/1")
				.check(status.is(200))
				)
				.exec(http("viewEmployeeList")
					.get("/index.php/pim/viewEmployeeList?sortField=employeeId&sortOrder=DESC")
					.check(status.is(200))
					.check(css("#empsearch__csrf_token", "value").saveAs("empsearch_csrf_token"))
					.check(css("#resultTable>tbody>tr:nth-of-type(${row})>td:nth-of-type(1)>input[type='checkbox']", "value").saveAs("empId"))
					.check(css("#resultTable>tbody>tr:nth-of-type(${row})>td:nth-of-type(2)>a").saveAs("empNumber"))
					.check(css("#resultTable>tbody>tr:nth-of-type(${row})>td:nth-of-type(4)>a").saveAs("nom"))
					.check(css("#defaultList__csrf_token", "value").saveAs("defaultList_csrf_token"))

				)
				//.exec(session => {
				//	println("Random Employee : " + session("nom").as[String] + " with id : " + session("empId").as[String])
				//	session
				//})

		}
		def search = {
			// SEARCH EMPLOYEE
			//feed(existing_employees)
				//.exec(session => {
				//	println("Existing Employee from database : " + session("nom").as[String] + " with id : " + session("empId").as[String])
				//	session
				//})
			doIf(session => session("loggedIn").as[Boolean]) {
				group("Rechercher Salarié") {
					exec (this.getRandomEmployee)
						.exec(session => {
							println("Search employee : " + session("nom").as[String] )
							session
						})
						.exec(http("viewEmployeeList")
							.post("/index.php/pim/viewEmployeeList")
							//.headers(headers_1)
							.formParam("empsearch[employee_name][empName]", "${nom}")
							.formParam("empsearch[employee_name][empId]", "")
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
							.check(css("#resultTable>tbody>tr:nth-of-type(1)>td:nth-of-type(1)>input[type='checkbox']", "value").saveAs("empId"))
							.resources(http("getEmployeeListAjax")
								.get("/index.php/pim/getEmployeeListAjax")
								//.headers(headers_5)
							))
						.exec(session => {
							println("Found : " + session("empId").as[String] )
							session
						})
						// Details
						.exec(http("viewEmployee")
							.get("/index.php/pim/viewEmployee/empNumber/${empId}")
							.check(status.is(200))
							.check(regex("<h1>Personal Details</h1>").exists)
						)
						// Job
						.exec(http("viewContactDetails")
							.get("/index.php/pim/contactDetails/empNumber/${empId}")
							.check(status.is(200))
							.check(regex("<h1>Contact Details</h1>").exists)
						)
						.exec(http("viewEmployee")
							.get("/index.php/pim/viewJobDetails/empNumber/${empId}")
							.check(status.is(200))
							.check(regex("<h1>Job</h1>").exists)
						)

						.pause(Utils.thinktime())
				}
			}
		}
		def updatePhoto = {
			// photo
			doIf(session => session("loggedIn").as[Boolean]) {
				group("MAJ Photo") {
					exec(this.getRandomEmployee)
						.exec(session => {
							println("Update photo : " + session("nom").as[String] + " with id : " + session("empId").as[String])
							session
						})
						.exec(http("viewPhotograph")
							.get("/index.php/pim/viewPhotograph/empNumber/${empId}")
							.check(status.is(200))
							.check(regex("<h1>Photograph</h1>").exists)
							.check(css("#csrf_token", "value").saveAs("csrf_token"))
						)
						.pause(Utils.thinktime)
						.exec(session => session.set("id", Utils.randomInt(15)))
						.exec(http("updatePhotograph")
							.post("/index.php/pim/viewPhotograph")
							.formParam("_csrf_token", "${csrf_token}")
							.formParam("emp_number", "${empId}")
							.bodyPart(RawFileBodyPart("photofile", "photos/${id}.jpg"))
							.check(status.is(200))
						)
						.pause(Utils.thinktime)
				}
			}
		}
		def addJobDetails = {
			// Add Job Details
			exec(session => {
					println("Add Job Details : " + session("nom").as[String] + " with id : " + session("empId").as[String])
					session
				})
				.exec(http("viewPhotograph")
					.get("/index.php/pim/viewJobDetails/empNumber/${empId}")
					.check(status.is(200))
					.check(regex("<h1>Job</h1>").exists)
					.check(css("#job__csrf_token", "value").saveAs("job_csrf_token"))
				)
				.pause(Utils.thinktime)
				.exec (session => session.set("boundary", Utils.randomString(28)))
				.exec(http("updatePhotograph")
					.post("/index.php/pim/viewJobDetails")
					.header("Content-Type", "multipart/form-data; boundary=-----------------------------${boundary}--")
					.bodyPart(StringBodyPart("job[_csrf_token]", "${job_csrf_token}")).asMultipartForm
					.bodyPart(StringBodyPart("job[emp_number]", "${empId}")).asMultipartForm
					.bodyPart(StringBodyPart("job[job_title]", "2")).asMultipartForm
					.bodyPart(StringBodyPart("job[emp_status]", "2")).asMultipartForm
					.bodyPart(StringBodyPart("job[eeo_category]", "10")).asMultipartForm
					.bodyPart(StringBodyPart("job[joined_date]", "2021-12-01")).asMultipartForm
					.bodyPart(StringBodyPart("job[sub_unit]", "2")).asMultipartForm
					.bodyPart(StringBodyPart("job[location]", "2")).asMultipartForm
					.bodyPart(StringBodyPart("job[contract_start_date]", "2021-12-01")).asMultipartForm
					.bodyPart(StringBodyPart("job[contract_end_date]", "yyyy-mm-dd")).asMultipartForm
					.bodyPart(StringBodyPart("job[contract_file]", "")).asMultipartForm
					.check(status.is(200))
				)
				.pause(Utils.thinktime)

		}
		def delete = {
			 // DELETE EMPLOYEE
			doIf(session => session("loggedIn").as[Boolean]) {
				// Supprimer un des 10 premiers de la liste
				group("Supprimer Salarié") {
					exec (this.getRandomEmployee)
						.exec(session => {
							println("Delete employee : " + session("nom").as[String] + " with id : " + session("empId").as[String])
							session
						})
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
						.pause(Utils.thinktime())
				}
			}
		}
		def navigate = {
			// NAVIGATE
			doIf(session => session("loggedIn").as[Boolean]) {
				group("Navigation") {
					exec(http("viewEmployeeList")
						.get("/index.php/pim/viewEmployeeList/reset/1")
						.check(status.is(200))
						.check(regex("Employee Information</h1>").exists)
						.resources(http("getEmployeeListAjax")
							.get("/index.php/pim/getEmployeeListAjax")
							.check(status.is(200))
						))
						.pause(Utils.thinktime())
						.exec(http("addEmployeePage")
							.get("/index.php/pim/addEmployee")
							.check(regex("Add Employee").exists)
							.check(status.is(200))
						)
						.pause(Utils.thinktime())
						.exec(http("viewDefinedPredefinedReports")
							.get("/index.php/core/viewDefinedPredefinedReports/reportGroup/3/reportType/PIM_DEFINED")
							.check(regex("Employee Reports").exists)
							.check(status.is(200))
						)
						.pause(Utils.thinktime())
						.exec(http("viewMyDetails")
							.get("/index.php/pim/viewMyDetails")
							.check(status.is(200))
							.check(regex("Personal Details").exists)
							.resources(http("getWorkWeekAjax")
								.get("/index.php/leave/getWorkWeekAjax?_=1635768128976")
								.check(status.is(200))
								,
								http("getHolidayAjax")
									.get("/index.php/leave/getHolidayAjax?year=2021&_=1635768128975")
									.check(status.is(200))
							))
						.pause(Utils.thinktime())
						.exec(http("dashboard")
							.get("/index.php/dashboard")
							.check(status.is(200))
							.check(regex("Dashboard").exists)
							.resources(http("pendingLeaveRequests")
								.get("/index.php/dashboard/pendingLeaveRequests")
								.check(status.is(200)),
								http("employeeDistribution")
									.get("/index.php/dashboard/employeeDistribution")
									.check(status.is(200))
							))
						.pause(Utils.thinktime())
						.exec(http("viewOrganizationGeneralInformation")
							.get("/index.php/admin/viewOrganizationGeneralInformation")
							.check(status.is(200))
							.check(regex("General Information").exists)
						)
						.pause(Utils.thinktime())
						.exec(http("viewLocations")
							.get("/index.php/admin/viewLocations")
							.check(status.is(200))
							.check(regex("Locations").exists)
						)
						.pause(Utils.thinktime())
						.exec(http("viewCompanyStructure")
							.get("/index.php/admin/viewCompanyStructure")
							.check(status.is(200))
							.check(regex("Organization Structure").exists)
						)
						.pause(Utils.thinktime())
						.exec(http("localization")
							.get("/index.php/admin/localization")
							.check(status.is(200))
							.check(regex("Localization").exists)
						)
						.pause(Utils.thinktime())
						.exec(http("listMailConfiguration")
							.get("/index.php/admin/listMailConfiguration")
							.check(status.is(200))
							.check(regex("Mail Configuration").exists)
						)
						.pause(Utils.thinktime())
						.exec(http("viewSystemUsers")
							.get("/index.php/admin/viewSystemUsers")
							.check(status.is(200))
							.check(regex("System Users").exists)
						)
						.pause(Utils.thinktime())
						.exec(http("viewJobTitleList")
							.get("/index.php/admin/viewJobTitleList")
							.check(status.is(200))
							.check(regex("Job Titles").exists)
						)
						.pause(Utils.thinktime())
						.exec(http("viewPayGrades")
							.get("/index.php/admin/viewPayGrades")
							.check(status.is(200))
							.check(regex("Pay Grades").exists)
						)
						.pause(Utils.thinktime())
						.exec(http("employmentStatus")
							.get("/index.php/admin/employmentStatus")
							.check(status.is(200))
							.check(regex("").exists)
						)
						.pause(Utils.thinktime())
						.exec(http("jobCategory")
							.get("/index.php/admin/jobCategory")
							.check(status.is(200))
							.check(regex("Employment Status").exists)
						)
						.pause(Utils.thinktime())
						.exec(http("workShift")
							.get("/index.php/admin/workShift")
							.check(status.is(200))
							.check(regex("Work Shifts").exists)
						)
						.pause(Utils.thinktime())
				}
			}
		}
		def logout = {
			// LOGOUT
			doIf(session => session("loggedIn").as[Boolean]) {

				exec(http("logout")
					.get("/index.php/auth/logout")
					.check(status.is(200))
				)
			}
		}
	}

	object Parcours {
		def navigate = {
			exec (Employee.login)
				.repeat(ITERATIONS) {
					exec(Employee.navigate)
					.pause(PACING.seconds)
				}
				.exec (Employee.logout)

		}
		def loginLogout = {
			exec (Employee.login)
				.exec (Employee.logout)
		}
		def addEmployee = {
			exec (Employee.home)
				.exec (Employee.login)
				.repeat(ITERATIONS) {
					exec(Employee.add)
						.exec(Employee.addJobDetails)
						.pause(PACING.seconds)
				}
				.exec (Employee.logout)
		}
		def searchEmployee = {
			exec (Employee.home)
				.exec (Employee.login)
				.repeat(ITERATIONS) {
					exec (Employee.search)
						.pause(PACING.seconds)
				}
				.exec (Employee.logout)
		}
		def updatePhoto = {
			exec (Employee.home)
				.exec (Employee.login)
				.repeat(ITERATIONS) {
					exec (Employee.search)
					.exec (Employee.updatePhoto)
						.pause(PACING.seconds)
				}
				.exec (Employee.logout)
		}
		def deleteEmployee = {
			exec (Employee.home)
				.exec (Employee.login)
				.repeat(ITERATIONS) {
					exec (Employee.delete)
						.pause(PACING.seconds)
				}
				.exec (Employee.logout)
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


	val scn = scenario("OrangerHRM employee")
		// HOME
		.during (TEST_DURATION.minutes) {
			pace (PACING seconds)
			randomSwitch(
				0d->(Parcours.navigate),
				100d->(Parcours.searchEmployee),
				0d->(Parcours.addEmployee),
				0d->(Parcours.updatePhoto),
			)
		}

	setUp(scn.inject(
		rampUsers (USERS_COUNT) during (RAMP_DURATION.minutes)
	)).protocols(httpProtocol)

	val scn_debug = scenario("AddEmployee")
		.exec (Employee.login)
		.exec (Employee.setLanguage)
		.repeat(1) {
			exec(Employee.navigate)
			//exec(Employee.add)
			//.exec (Employee.addJobDetails)
			//exec (Employee.search)
			//.exec (Employee.delete)
		}
		.exec (Employee.logout)

//	setUp(scn_debug.inject(atOnceUsers(1))).protocols(httpProtocol)

}