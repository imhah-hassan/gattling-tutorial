package orangehrm;
import java.io.*;
import java.util.*;

import java.time.Duration;
import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
public class Employee extends Simulation {
	FeederBuilder.Batchable employees =	csv("data/employees.csv").random();
	FeederBuilder.Batchable photos = csv("data/photos.csv").random();

	public int GetRandomInt(int max) {
		Random random = new Random();
		int randomNumber = random.nextInt(max);
		return (randomNumber);
	}
	private static String getProperty(String propertyName, String defaultValue) {
		return Optional.ofNullable(System.getenv(propertyName))
			.orElse(System.getProperty(propertyName, defaultValue));
	}

	private static int getPropertyAsInt(String propertyName, String defaultValue) {
		return Integer.parseInt(getProperty(propertyName, defaultValue));
	}

	public static final int PACING = getPropertyAsInt("PACING", "0"); // en secondes
	public static final int usersCount = getPropertyAsInt("USERS", "2");
	public static final int rampDuration = getPropertyAsInt("RAMP_DURATION", "10");	// en secondes
	public static final int MIN_THK = getPropertyAsInt("MIN_THK", "100");	// en millisecondes
	public static final int MAX_THK = getPropertyAsInt("MAX_THK", "300"); // en millisecondes
	public static final int testDuration = getPropertyAsInt("DURATION", "30");	// en secondes

	public static Duration ThinkTime() {
		Random rnd = new Random();
		int thinkTimeMillis = rnd.nextInt(MAX_THK - MIN_THK) + MIN_THK;
		return Duration.ofMillis(thinkTimeMillis);
	}

	public static void writeFile(String filename, String s) throws IOException {
		File file = new File(filename);
		BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
		bw.write(s);
		bw.close();
	}
	public String today() {
		// Generate date in yyyy-mm-dd format
		LocalDate currentDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String formattedDate = currentDate.format(formatter);
//		System.out.println("formattedDate " + formattedDate);
		return (formattedDate);
	}

	private HttpProtocolBuilder httpProtocol = http
		.baseUrl("http://192.168.1.17/orangehrm5")
		// .proxy(Proxy("localhost", 8888))
		.inferHtmlResources(AllowList(), DenyList(".*\\.js", ".*\\.css", ".*\\.gif", ".*\\.jpeg", ".*\\.jpg", ".*\\.ico", ".*\\.woff", ".*\\.woff2", ".*\\.(t|o)tf", ".*\\.png", ".*\\.svg", ".*detectportal\\.firefox\\.com.*"))
		.acceptHeader("application/json")
		.acceptEncodingHeader("gzip, deflate, br")
		.acceptLanguageHeader("fr,fr-FR;q=0.8,en-US;q=0.5,en;q=0.3")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/115.0");

	private Map<CharSequence, String> headers_0 = Map.ofEntries(
		Map.entry("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8"),
		Map.entry("Sec-Fetch-Dest", "document"),
		Map.entry("Sec-Fetch-Mode", "navigate"),
		Map.entry("Sec-Fetch-Site", "none"),
		Map.entry("Sec-Fetch-User", "?1"),
		Map.entry("Upgrade-Insecure-Requests", "1")
	);

	private Map<CharSequence, String> headers_1 = Map.ofEntries(
		Map.entry("If-None-Match", "\"ZyaIRdC+v1MMYqLcalPsK4TmP8dsWiMdswULwhxGboo=\""),
		Map.entry("Sec-Fetch-Dest", "empty"),
		Map.entry("Sec-Fetch-Mode", "cors"),
		Map.entry("Sec-Fetch-Site", "same-origin"),
		Map.entry("contentType", "application/json")
	);

	private Map<CharSequence, String> headers_2 = Map.ofEntries(
		Map.entry("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8"),
		Map.entry("Origin", "http://localhost"),
		Map.entry("Sec-Fetch-Dest", "document"),
		Map.entry("Sec-Fetch-Mode", "navigate"),
		Map.entry("Sec-Fetch-Site", "same-origin"),
		Map.entry("Sec-Fetch-User", "?1"),
		Map.entry("Upgrade-Insecure-Requests", "1")
	);

	private Map<CharSequence, String> headers_4 = Map.ofEntries(
		Map.entry("Accept", "image/avif,image/webp,*/*"),
		Map.entry("Sec-Fetch-Dest", "image"),
		Map.entry("Sec-Fetch-Mode", "no-cors"),
		Map.entry("Sec-Fetch-Site", "same-origin")
	);

	private Map<CharSequence, String> headers_7 = Map.ofEntries(
		Map.entry("Cache-Control", "no-store, no-cache, must-revalidate, post-check=0, pre-check=0"),
		Map.entry("Sec-Fetch-Dest", "empty"),
		Map.entry("Sec-Fetch-Mode", "cors"),
		Map.entry("Sec-Fetch-Site", "same-origin")
	);

	private Map<CharSequence, String> headers_8 = Map.ofEntries(
		Map.entry("Origin", "http://localhost"),
		Map.entry("Sec-Fetch-Dest", "empty"),
		Map.entry("Sec-Fetch-Mode", "cors"),
		Map.entry("Sec-Fetch-Site", "same-origin")
	);

	private Map<CharSequence, String> headers_11 = Map.ofEntries(
		Map.entry("Accept", "application/json, text/plain, */*"),
		Map.entry("Sec-Fetch-Dest", "empty"),
		Map.entry("Sec-Fetch-Mode", "cors"),
		Map.entry("Sec-Fetch-Site", "same-origin")
	);

	private Map<CharSequence, String> headers_15 = Map.ofEntries(
		Map.entry("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8"),
		Map.entry("Sec-Fetch-Dest", "document"),
		Map.entry("Sec-Fetch-Mode", "navigate"),
		Map.entry("Sec-Fetch-Site", "same-origin"),
		Map.entry("Sec-Fetch-User", "?1"),
		Map.entry("Upgrade-Insecure-Requests", "1")
	);

	private Map<CharSequence, String> headers_17 = Map.ofEntries(
		Map.entry("Accept", "image/avif,image/webp,*/*"),
		Map.entry("If-Modified-Since", "Sun, 30 Apr 2023 07:47:18 GMT"),
		Map.entry("If-None-Match", "\"eSWfz0aTMRL2qx3Xp+IS4qMVAXpmiSacwlj2vIk2pmo=\""),
		Map.entry("Sec-Fetch-Dest", "image"),
		Map.entry("Sec-Fetch-Mode", "no-cors"),
		Map.entry("Sec-Fetch-Site", "same-origin")
	);

	private Map<CharSequence, String> headers_31 = Map.ofEntries(
		Map.entry("Content-Type", "application/json"),
		Map.entry("Origin", "http://localhost"),
		Map.entry("Sec-Fetch-Dest", "empty"),
		Map.entry("Sec-Fetch-Mode", "cors"),
		Map.entry("Sec-Fetch-Site", "same-origin")
	);

	private Map<CharSequence, String> headers_45 = Map.ofEntries(
		Map.entry("Accept", "application/json, text/plain, */*"),
		Map.entry("Content-Type", "application/json"),
		Map.entry("Origin", "http://localhost"),
		Map.entry("Sec-Fetch-Dest", "empty"),
		Map.entry("Sec-Fetch-Mode", "cors"),
		Map.entry("Sec-Fetch-Site", "same-origin")
	);

	private Map<CharSequence, String> headers_69 = Map.ofEntries(
		Map.entry("Accept", "image/avif,image/webp,*/*"),
		Map.entry("If-None-Match", "\"eYLGa17GYRw7+UyJjlRKAIyo2w9vk3gWyEKngKB+uNw=\""),
		Map.entry("Sec-Fetch-Dest", "image"),
		Map.entry("Sec-Fetch-Mode", "no-cors"),
		Map.entry("Sec-Fetch-Site", "same-origin")
	);

	ChainBuilder login =
		group("Login").on(
			exec(
				http("00_Home")
				.get("/")
				.check(status().is(200))
				.check( regex(":token=\"&quot;(.*?)&quot;").saveAs("csrf_token"))
				.headers(headers_0)
				.resources(
					http("01_messages_1")
					.get("/web/index.php/core/i18n/messages")
					.headers(headers_1)
				)
			)
			.pause(ThinkTime())
/*
			.exec ( session -> {
				System.out.println(session.getString("csrf_token"));
				return session;
			})
*/
			// LOGIN
			.exec(
			http("02_validate")
			.post("/web/index.php/auth/validate")
			.headers(headers_2)
			.formParam("_token", "#{csrf_token}")
			.formParam("username", "Admin")
			.formParam("password", "Hassan$2023")
			.resources(
				http("03__messages")
				.get("/web/index.php/core/i18n/messages")
				.headers(headers_1),
				http("06_viewPhoto")
				.get("/web/index.php/pim/viewPhoto/empNumber/1")
				.headers(headers_4),
				http("07_dashboard")
				.get("/web/index.php/api/v2/dashboard/employees/time-at-work?timezoneOffset=2&currentDate=#{today}&currentTime=14:06")
				.headers(headers_7),
				http("08_events")
				.post("/web/index.php/events/push")
				.headers(headers_8),
				http("09_locations")
				.get("/web/index.php/api/v2/dashboard/employees/locations")
				.headers(headers_7),
				http("10_subunit")
				.get("/web/index.php/api/v2/dashboard/employees/subunit")
				.headers(headers_7),
				http("11_buzz")
				.get("/web/index.php/api/v2/buzz/feed?limit=5&offset=0&sortOrder=DESC&sortField=share.createdAtUtc")
				.headers(headers_11),
				http("12_action-summary")
				.get("/web/index.php/api/v2/dashboard/employees/action-summary")
				.headers(headers_7),
				http("13_leaves")
				.get("/web/index.php/api/v2/dashboard/employees/leaves?date=#{today}")
				.headers(headers_7),
				http("14_shortcuts")
				.get("/web/index.php/api/v2/dashboard/shortcuts")
				.headers(headers_7)
				)
			)
			.pause(ThinkTime())
		);

	ChainBuilder add_employee =
		// ADD EMPLOYEE
		group("Add Employee").on(

			feed(employees)
		.feed(photos)
/*
		.exec ( session -> {
			System.out.println(session.getString("lastName"));
			System.out.println(session.getString("file"));
			return session;
		})
*/
		.exec(
		http("15_viewAdminModule")
		.get("/web/index.php/admin/viewAdminModule")
		.headers(headers_15)
		.resources(
		http("16_messages")
		.get("/web/index.php/core/i18n/messages")
		.headers(headers_1),
		http("17_viewPhoto")
		.get("/web/index.php/pim/viewPhoto/empNumber/1")
		.headers(headers_17),
		http("18_users")
		.get("/web/index.php/api/v2/admin/users?limit=50&offset=0&sortField=u.userName&sortOrder=ASC")
		.headers(headers_7),
		http("19_viewPimModule")
		.get("/web/index.php/pim/viewPimModule")
		.headers(headers_15),
		http("20_messages")
		.get("/web/index.php/core/i18n/messages")
		.headers(headers_1),
		http("21_viewPhoto")
		.get("/web/index.php/pim/viewPhoto/empNumber/1")
		.headers(headers_17),
		http("22_employees")
		.get("/web/index.php/api/v2/pim/employees?limit=50&offset=0&model=detailed&includeEmployees=onlyCurrent&sortField=employee.firstName&sortOrder=ASC")
		.headers(headers_7),
		http("23_job-titles")
		.get("/web/index.php/api/v2/admin/job-titles?limit=0")
		.headers(headers_7),
		http("24_subunits")
		.get("/web/index.php/api/v2/admin/subunits")
		.headers(headers_7),
		http("25_employment-statuses")
		.get("/web/index.php/api/v2/admin/employment-statuses?limit=0")
		.headers(headers_7),
		http("26_addEmployee")
		.get("/web/index.php/pim/addEmployee")
		.headers(headers_15),
		http("27_messages")
		.get("/web/index.php/core/i18n/messages")
		.headers(headers_1),
		http("28_viewPhoto")
		.get("/web/index.php/pim/viewPhoto/empNumber/1")
		.headers(headers_17),
		http("29_employees")
		.get("/web/index.php/api/v2/pim/employees")
		.headers(headers_7),
		http("30_users")
		.get("/web/index.php/api/v2/admin/users")
		.headers(headers_11)
		)
		)
		.pause(ThinkTime())
		.exec(
		http("31_employees")
		.post("/web/index.php/api/v2/pim/employees")
		.headers(headers_31)
		.body(ElFileBody("orangehrm/employee/0031_request.json"))
		.check(jsonPath("$.data.empNumber").saveAs("empNumber"))
		)
		.pause(ThinkTime())
		.exec(
		http("32_viewPersonalDetails")
		.get("/web/index.php/pim/viewPersonalDetails/empNumber/#{empNumber}")
		.headers(headers_15)
		.resources(
		http("33_messages")
		.get("/web/index.php/core/i18n/messages")
		.headers(headers_1),
		http("34_viewPhoto")
		.get("/web/index.php/pim/viewPhoto/empNumber/1")
		.headers(headers_17),
		http("35_workweek")
		.get("/web/index.php/api/v2/leave/workweek?model=indexed")
		.headers(headers_11),
		http("36_viewPhoto")
		.get("/web/index.php/pim/viewPhoto/empNumber/#{empNumber}")
		.headers(headers_4),
		http("37_employees")
		.get("/web/index.php/api/v2/pim/employees/#{empNumber}")
		.headers(headers_11),
		http("38_attachments")
		.get("/web/index.php/api/v2/pim/employees/#{empNumber}/screen/personal/attachments?limit=50&offset=0")
		.headers(headers_7),
		http("39_custom-fields")
		.get("/web/index.php/api/v2/pim/employees/#{empNumber}/custom-fields?screen=personal")
		.headers(headers_7),
		http("40_personal-details")
		.get("/web/index.php/api/v2/pim/employees/#{empNumber}/personal-details")
		.headers(headers_7),
		http("41_holidays")
		.get("/web/index.php/api/v2/leave/holidays?fromDate=2023-01-01&toDate=2023-12-31")
		.headers(headers_11),
		http("42_workweek")
		.get("/web/index.php/api/v2/leave/workweek?model=indexed")
		.headers(headers_11),
		http("43_holidays")
		.get("/web/index.php/api/v2/leave/holidays?fromDate=2023-01-01&toDate=2023-12-31")
		.headers(headers_11),
		http("44_employees")
		.get("/web/index.php/api/v2/pim/employees")
		.headers(headers_11)
		)
		)
		.pause(ThinkTime())
		);

	ChainBuilder update_employee_details =
		group("Update Employee Details").on(

			// EMPLOYEE DETAILS
		exec(
			http("45_45_personal-details-put")
			.put("/web/index.php/api/v2/pim/employees/#{empNumber}/personal-details")
			.headers(headers_45)
			.body(ElFileBody("orangehrm/employee/0045_request.json"))
		)
		.pause(ThinkTime())
		.exec(
			http("46_viewPhotograph")
			.get("/web/index.php/pim/viewPhotograph/empNumber/#{empNumber}")
			.headers(headers_15)
			.resources(
				http("47_messages")
				.get("/web/index.php/core/i18n/messages")
				.headers(headers_1),
				http("48_viewPhoto")
				.get("/web/index.php/pim/viewPhoto/empNumber/1")
				.headers(headers_17),
				http("49_viewPhoto")
				.get("/web/index.php/pim/viewPhoto/empNumber/#{empNumber}")
				.headers(headers_17),
				http("50_employees")
				.get("/web/index.php/api/v2/pim/employees/#{empNumber}")
				.headers(headers_11)
				)
			)
		.pause(ThinkTime())
		.exec(
			http("51_employees")
			.put("/web/index.php/api/v2/pim/employees/#{empNumber}/picture")
			.headers(headers_45)
			.body(ElFileBody("orangehrm/employee/0051_request.json"))
		)
		.pause(ThinkTime())
		.exec(
		http("52_viewPhotograph")
		.get("/web/index.php/pim/viewPhotograph/empNumber/#{empNumber}")
		.headers(headers_15)
		.resources(
		http("53_messages")
		.get("/web/index.php/core/i18n/messages")
		.headers(headers_1),
		http("54_viewPhoto")
		.get("/web/index.php/pim/viewPhoto/empNumber/#{empNumber}")
		.headers(headers_17),
		http("55_viewPhoto")
		.get("/web/index.php/pim/viewPhoto/empNumber/1")
		.headers(headers_17),
		http("56_employees")
		.get("/web/index.php/api/v2/pim/employees/#{empNumber}")
		.headers(headers_11)
		)
		)
		.pause(ThinkTime())
		);


	ChainBuilder search_employee =
		group("Search Employee").on(

			exec(
		http("57_viewPimModule")
		.get("/web/index.php/pim/viewPimModule")
		.headers(headers_15)
		.resources(
		http("58_messages")
		.get("/web/index.php/core/i18n/messages")
		.headers(headers_1),
		http("59_viewPhoto")
		.get("/web/index.php/pim/viewPhoto/empNumber/1")
		.headers(headers_17),
		http("60_employment-statuses")
		.get("/web/index.php/api/v2/admin/employment-statuses?limit=0")
		.headers(headers_7),
		http("61_employees")
		.get("/web/index.php/api/v2/pim/employees?limit=50&offset=0&model=detailed&includeEmployees=onlyCurrent&sortField=employee.firstName&sortOrder=ASC")
		.headers(headers_7),
		http("62_subunits")
		.get("/web/index.php/api/v2/admin/subunits")
		.headers(headers_7),
		http("63_job-titles")
		.get("/web/index.php/api/v2/admin/job-titles?limit=0")
		.headers(headers_7)
		)
		)
		.pause(ThinkTime())
		.exec(
		http("64_employees")
		.get("/web/index.php/api/v2/pim/employees?nameOrId=#{lastName}&includeEmployees=onlyCurrent")
		.headers(headers_7)
		)
		.pause(ThinkTime())
		.exec(
		http("65_employees")
		.get("/web/index.php/api/v2/pim/employees?limit=50&offset=0&model=detailed&nameOrId=#{lastName}&includeEmployees=onlyCurrent&sortField=employee.firstName&sortOrder=ASC")
		.check(jsonPath("$.data[0].empNumber").saveAs("empNumber"))
		.headers(headers_7)
		)
		.pause(ThinkTime())
		);


	ChainBuilder employee_details =
		// EMPLOYEE INFOS
		group("Employee Details").on(

			exec(
		http("66_viewPersonalDetails")
		.get("/web/index.php/pim/viewPersonalDetails/empNumber/#{empNumber}")
		.headers(headers_15)
		.resources(
		http("67_messages")
		.get("/web/index.php/core/i18n/messages")
		.headers(headers_1),
		http("68_viewPhoto")
		.get("/web/index.php/pim/viewPhoto/empNumber/1")
		.headers(headers_17),
		http("69_viewPhoto")
		.get("/web/index.php/pim/viewPhoto/empNumber/#{empNumber}")
		.headers(headers_69),
		http("70_workweek")
		.get("/web/index.php/api/v2/leave/workweek?model=indexed")
		.headers(headers_11),
		http("71_employees")
		.get("/web/index.php/api/v2/pim/employees/#{empNumber}/personal-details")
		.headers(headers_7),
		http("72_employees")
		.get("/web/index.php/api/v2/pim/employees/#{empNumber}/custom-fields?screen=personal")
		.headers(headers_7),
		http("73_employees")
		.get("/web/index.php/api/v2/pim/employees/#{empNumber}/screen/personal/attachments?limit=50&offset=0")
		.headers(headers_7),
		http("74_holidays")
		.get("/web/index.php/api/v2/leave/holidays?fromDate=2023-01-01&toDate=2023-12-31")
		.headers(headers_11),
		http("75_workweek")
		.get("/web/index.php/api/v2/leave/workweek?model=indexed")
		.headers(headers_11),
		http("76_employees")
		.get("/web/index.php/api/v2/pim/employees/#{empNumber}")
		.headers(headers_11),
		http("77_holidays")
		.get("/web/index.php/api/v2/leave/holidays?fromDate=2023-01-01&toDate=2023-12-31")
		.headers(headers_11),
		http("78_employees")
		.get("/web/index.php/api/v2/pim/employees")
		.headers(headers_11)
		)
		)
		.pause(ThinkTime())
		.exec(
		http("79_contactDetails")
		.get("/web/index.php/pim/contactDetails/empNumber/#{empNumber}")
		.headers(headers_15)
		.resources(
		http("80_messages")
		.get("/web/index.php/core/i18n/messages")
		.headers(headers_1),
		http("81_viewPhoto")
		.get("/web/index.php/pim/viewPhoto/empNumber/1")
		.headers(headers_17),
		http("82_viewPhoto")
		.get("/web/index.php/pim/viewPhoto/empNumber/#{empNumber}")
		.headers(headers_69),
		http("83_contact-details")
		.get("/web/index.php/api/v2/pim/employee/#{empNumber}/contact-details")
		.headers(headers_7),
		http("84_attachments")
		.get("/web/index.php/api/v2/pim/employees/#{empNumber}/screen/contact/attachments?limit=50&offset=0")
		.headers(headers_7),
		http("85_employees")
		.get("/web/index.php/api/v2/pim/employees/#{empNumber}")
		.headers(headers_11),
		http("86_custom-fields")
		.get("/web/index.php/api/v2/pim/employees/#{empNumber}/custom-fields?screen=contact")
		.headers(headers_7),
		http("87_viewEmergencyContacts")
		.get("/web/index.php/pim/viewEmergencyContacts/empNumber/#{empNumber}")
		.headers(headers_15),
		http("88_messages")
		.get("/web/index.php/core/i18n/messages")
		.headers(headers_1),
		http("89_viewPhoto")
		.get("/web/index.php/pim/viewPhoto/empNumber/1")
		.headers(headers_17),
		http("90_viewPhoto")
		.get("/web/index.php/pim/viewPhoto/empNumber/#{empNumber}")
		.headers(headers_69),
		http("91_custom-fields?screen=emergency")
		.get("/web/index.php/api/v2/pim/employees/#{empNumber}/custom-fields?screen=emergency")
		.headers(headers_7),
		http("92_attachments")
		.get("/web/index.php/api/v2/pim/employees/#{empNumber}/screen/emergency/attachments?limit=50&offset=0")
		.headers(headers_7),
		http("93_employees")
		.get("/web/index.php/api/v2/pim/employees/#{empNumber}")
		.headers(headers_11),
		http("94_emergency-contacts")
		.get("/web/index.php/api/v2/pim/employees/#{empNumber}/emergency-contacts?limit=50&offset=0")
		.headers(headers_7),
		http("95_viewDependents")
		.get("/web/index.php/pim/viewDependents/empNumber/#{empNumber}")
		.headers(headers_15),
		http("96_messages")
		.get("/web/index.php/core/i18n/messages")
		.headers(headers_1),
		http("97_viewPhoto")
		.get("/web/index.php/pim/viewPhoto/empNumber/1")
		.headers(headers_17),
		http("98_viewPhoto")
		.get("/web/index.php/pim/viewPhoto/empNumber/#{empNumber}")
		.headers(headers_69),
		http("99_employees")
		.get("/web/index.php/api/v2/pim/employees/#{empNumber}")
		.headers(headers_11),
		http("100_custom-fields?screen=dependents")
		.get("/web/index.php/api/v2/pim/employees/#{empNumber}/custom-fields?screen=dependents")
		.headers(headers_7),
		http("101_dependents")
		.get("/web/index.php/api/v2/pim/employees/#{empNumber}/dependents?limit=50&offset=0")
		.headers(headers_7),
		http("102_dependents/attachments")
		.get("/web/index.php/api/v2/pim/employees/#{empNumber}/screen/dependents/attachments?limit=50&offset=0")
		.headers(headers_7)
		)
		)
		.pause(ThinkTime())
		.exec(
		http("103_viewImmigration")
		.get("/web/index.php/pim/viewImmigration/empNumber/#{empNumber}")
		.headers(headers_15)
		.resources(
		http("104_messages")
		.get("/web/index.php/core/i18n/messages")
		.headers(headers_1),
		http("105_viewPhoto")
		.get("/web/index.php/pim/viewPhoto/empNumber/1")
		.headers(headers_17),
		http("106_employees")
		.get("/web/index.php/api/v2/pim/employees/#{empNumber}")
		.headers(headers_11),
		http("107_immigrations")
		.get("/web/index.php/api/v2/pim/employees/#{empNumber}/immigrations?limit=50&offset=0")
		.headers(headers_7),
		http("108_custom-fields?screen=immigration")
		.get("/web/index.php/api/v2/pim/employees/#{empNumber}/custom-fields?screen=immigration")
		.headers(headers_7),
		http("109_viewPhoto")
		.get("/web/index.php/pim/viewPhoto/empNumber/#{empNumber}")
		.headers(headers_69),
		http("110_immigration/attachments")
		.get("/web/index.php/api/v2/pim/employees/#{empNumber}/screen/immigration/attachments?limit=50&offset=0")
		.headers(headers_7)
		)
		)
		.pause(ThinkTime())
		.exec(
		http("111_viewJobDetails")
		.get("/web/index.php/pim/viewJobDetails/empNumber/#{empNumber}")
		.headers(headers_15)
		.resources(
		http("112_messages")
		.get("/web/index.php/core/i18n/messages")
		.headers(headers_1),
		http("113_viewPhoto")
		.get("/web/index.php/pim/viewPhoto/empNumber/#{empNumber}")
		.headers(headers_69),
		http("114_viewPhoto")
		.get("/web/index.php/pim/viewPhoto/empNumber/1")
		.headers(headers_17),
		http("115_job/attachments")
		.get("/web/index.php/api/v2/pim/employees/#{empNumber}/screen/job/attachments?limit=50&offset=0")
		.headers(headers_7),
		http("116_custom-fields?screen=job")
		.get("/web/index.php/api/v2/pim/employees/#{empNumber}/custom-fields?screen=job")
		.headers(headers_7),
		http("117_job-details")
		.get("/web/index.php/api/v2/pim/employees/#{empNumber}/job-details")
		.headers(headers_7),
		http("118_employees")
		.get("/web/index.php/api/v2/pim/employees/#{empNumber}")
		.headers(headers_11),
		http("119_workweek")
		.get("/web/index.php/api/v2/leave/workweek?model=indexed")
		.headers(headers_11),
		http("120_holidays")
		.get("/web/index.php/api/v2/leave/holidays?fromDate=2023-01-01&toDate=2023-12-31")
		.headers(headers_11),
		http("121_employees")
		.get("/web/index.php/api/v2/pim/employees/#{empNumber}/employment-contract")
		.headers(headers_11),
		http("122_viewSalaryList")
		.get("/web/index.php/pim/viewSalaryList/empNumber/#{empNumber}")
		.headers(headers_15),
		http("123_messages")
		.get("/web/index.php/core/i18n/messages")
		.headers(headers_1),
		http("124_viewPhoto")
		.get("/web/index.php/pim/viewPhoto/empNumber/1")
		.headers(headers_17),
		http("125_viewPhoto")
		.get("/web/index.php/pim/viewPhoto/empNumber/#{empNumber}")
		.headers(headers_69),
		http("126_employees")
		.get("/web/index.php/api/v2/pim/employees/#{empNumber}")
		.headers(headers_11),
		http("127_salary/attachments")
		.get("/web/index.php/api/v2/pim/employees/#{empNumber}/screen/salary/attachments?limit=50&offset=0")
		.headers(headers_7),
		http("128_salary-components")
		.get("/web/index.php/api/v2/pim/employees/#{empNumber}/salary-components?limit=50&offset=0")
		.headers(headers_7),
		http("129_custom-fields?screen=salary")
		.get("/web/index.php/api/v2/pim/employees/#{empNumber}/custom-fields?screen=salary")
		.headers(headers_7)
		)
		)
		.pause(ThinkTime())
		.exec(
		http("130_request_130")
		.get("/web/index.php/pim/viewReportToDetails/empNumber/#{empNumber}")
		.headers(headers_15)
		.resources(
		http("131_request_131")
		.get("/web/index.php/core/i18n/messages")
		.headers(headers_1),
		http("132_request_132")
		.get("/web/index.php/pim/viewPhoto/empNumber/1")
		.headers(headers_17),
		http("133_request_133")
		.get("/web/index.php/api/v2/pim/employees/#{empNumber}")
		.headers(headers_11),
		http("134_request_134")
		.get("/web/index.php/api/v2/pim/employees/#{empNumber}/supervisors?limit=50&offset=0")
		.headers(headers_7),
		http("135_request_135")
		.get("/web/index.php/api/v2/pim/employees/#{empNumber}/subordinates?limit=50&offset=0")
		.headers(headers_7),
		http("136_request_136")
		.get("/web/index.php/pim/viewPhoto/empNumber/#{empNumber}")
		.headers(headers_69),
		http("137_request_137")
		.get("/web/index.php/api/v2/pim/employees/#{empNumber}/screen/report-to/attachments?limit=50&offset=0")
		.headers(headers_7),
		http("138_request_138")
		.get("/web/index.php/api/v2/pim/employees/#{empNumber}/custom-fields?screen=report-to")
		.headers(headers_7),
		http("139_request_139")
		.get("/web/index.php/pim/viewQualifications/empNumber/#{empNumber}")
		.headers(headers_15),
		http("140_request_140")
		.get("/web/index.php/core/i18n/messages")
		.headers(headers_1),
		http("141_request_141")
		.get("/web/index.php/pim/viewPhoto/empNumber/1")
		.headers(headers_17),
		http("142_request_142")
		.get("/web/index.php/pim/viewPhoto/empNumber/#{empNumber}")
		.headers(headers_69),
		http("143_request_143")
		.get("/web/index.php/api/v2/pim/employees/#{empNumber}/languages?limit=50&offset=0")
		.headers(headers_7),
		http("144_request_144")
		.get("/web/index.php/api/v2/pim/employees/#{empNumber}")
		.headers(headers_11),
		http("145_request_145")
		.get("/web/index.php/api/v2/pim/employees/#{empNumber}/work-experiences?limit=50&offset=0")
		.headers(headers_7),
		http("146_request_146")
		.get("/web/index.php/api/v2/pim/employees/#{empNumber}/skills?limit=50&offset=0")
		.headers(headers_7),
		http("147_request_147")
		.get("/web/index.php/api/v2/pim/employees/#{empNumber}/licenses?limit=50&offset=0")
		.headers(headers_7),
		http("148_request_148")
		.get("/web/index.php/api/v2/pim/employees/#{empNumber}/screen/qualifications/attachments?limit=50&offset=0")
		.headers(headers_7),
		http("149_request_149")
		.get("/web/index.php/api/v2/pim/employees/#{empNumber}/custom-fields?screen=qualifications")
		.headers(headers_7),
		http("150_request_150")
		.get("/web/index.php/pim/viewMemberships/empNumber/#{empNumber}")
		.headers(headers_15),
		http("151_request_151")
		.get("/web/index.php/core/i18n/messages")
		.headers(headers_1),
		http("152_request_152")
		.get("/web/index.php/pim/viewPhoto/empNumber/1")
		.headers(headers_17),
		http("153_request_153")
		.get("/web/index.php/api/v2/pim/employees/#{empNumber}/memberships?limit=50&offset=0")
		.headers(headers_7),
		http("154_request_154")
		.get("/web/index.php/pim/viewPhoto/empNumber/#{empNumber}")
		.headers(headers_69),
		http("155_request_155")
		.get("/web/index.php/api/v2/pim/employees/#{empNumber}/custom-fields?screen=membership")
		.headers(headers_7),
		http("156_request_156")
		.get("/web/index.php/api/v2/pim/employees/#{empNumber}")
		.headers(headers_11),
		http("157_request_157")
		.get("/web/index.php/api/v2/pim/employees/#{empNumber}/screen/membership/attachments?limit=50&offset=0")
		.headers(headers_7)
		)
		)
		.pause(ThinkTime())
		);

	ChainBuilder delete_employee =
		// DELETE EMPLOYEE
		group("Delete Employee").on(

			exec(
		http("158_request_158")
		.get("/web/index.php/pim/viewPimModule")
		.headers(headers_15)
		.resources(
		http("159_request_159")
		.get("/web/index.php/core/i18n/messages")
		.headers(headers_1),
		http("160_request_160")
		.get("/web/index.php/pim/viewPhoto/empNumber/1")
		.headers(headers_17),
		http("161_request_161")
		.get("/web/index.php/api/v2/admin/job-titles?limit=0")
		.headers(headers_7),
		http("162_request_162")
		.get("/web/index.php/api/v2/admin/employment-statuses?limit=0")
		.headers(headers_7),
		http("163_request_163")
		.get("/web/index.php/api/v2/pim/employees?limit=50&offset=0&model=detailed&includeEmployees=onlyCurrent&sortField=employee.firstName&sortOrder=ASC")
		.headers(headers_7)
		.check(
		jsonPath("$.data["+ GetRandomInt(10) +"].empNumber").saveAs("empNumber")),
		http("164_request_164")
		.get("/web/index.php/api/v2/admin/subunits")
		.headers(headers_7)
		)
		)
//		.exec ( session -> {
//			System.out.println("Random empNumber " + session.getString("empNumber"));
//			return session;
//		})
		.pause(ThinkTime())
		.exec(
		http("165_delete_employee_165")
		.delete("/web/index.php/api/v2/pim/employees")
		.headers(headers_45)
		.body(ElFileBody("orangehrm/employee/0165_request.json"))
		)
		.pause(ThinkTime())
		.exec(
		http("166_employees_166")
		.get("/web/index.php/api/v2/pim/employees?limit=50&offset=0&model=detailed&includeEmployees=onlyCurrent&sortField=employee.firstName&sortOrder=ASC")
		.headers(headers_7)
		)
		.pause(ThinkTime())
		);

	ChainBuilder logout =
		// LOGOUT
		group("Logout").on(

			exec(
			http("167_request_167")
			.get("/web/index.php/auth/logout")
			.headers(headers_15)
			.resources(
				http("168_request_168")
				.get("/web/index.php/core/i18n/messages")
				.headers(headers_1)
			)
		)
		);

	ChainBuilder random_choice =
			exec ( session -> {
				Session newSession = session.set("choice", GetRandomInt(20));
				return newSession;
			});

	private final ChainBuilder ParcoursComplet =
		exec(session -> {
			Session newSession = session.set("today", today());
			return newSession;
		})
			.exec(login)
			.exec ( random_choice)
			// 1 / 5
			.exec(add_employee)
			.exec(employee_details)
			.exec(search_employee)
			.exec(update_employee_details)
			// 1 / 5
			.doIf(session -> session.getInt("choice") < 3).then (
				exec(delete_employee)
			)
			.exec(logout);

	@Override
	public void before() {
		System.out.println("Running users with " + usersCount + " users");
		System.out.println("Ramping over " + rampDuration + " seconds");
		System.out.println("Total test duration " + testDuration + " seconds");
		System.out.println("User iteration pacing " + PACING + " seconds");
		System.out.println("Request min think time  " + MIN_THK + " milliseconds");
		System.out.println("Request max think time " + MAX_THK + " milliseconds");
	}

	@Override
	public void after() {
		System.out.println("Load test complete !");
	}

	private final ScenarioBuilder scn = scenario("Perf OrangeHRM VM")
		.exec(ParcoursComplet);
		{
			setUp(scn.injectOpen(atOnceUsers(1))).protocols(httpProtocol);
		}

/*
		.during(Duration.ofSeconds(testDuration)).on(
			pace(Duration.ofSeconds(PACING))
				.exec(ParcoursComplet)

		);
		{
			setUp(scn.injectOpen(
				rampUsers(usersCount).during(Duration.ofSeconds(rampDuration))
			)).protocols(httpProtocol);
		}
*/


/*
	{
		setUp(
			scn.injectClosed(
				constantConcurrentUsers(10).during(Duration.ofSeconds(60))
  		)
		).protocols(httpProtocol);
	}
*/

//	{
//		setUp(scn.injectOpen(
//									constantUsersPerSec(2)
//										.during(Duration.ofSeconds(10))))
//							.throttle(
//										reachRps(20).in(10),
//										holdFor(Duration.ofMinutes(1)))
//			.protocols(httpProtocol);
//	}

}
