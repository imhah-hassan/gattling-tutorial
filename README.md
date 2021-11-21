Helpfull training at : https://academy.gatling.io/

mvn archetype:generate -DarchetypeGroupId=io.gatling.highcharts -DarchetypeArtifactId=gatling-highcharts-maven-archetype -DgroupId=tutorial.gatling -DartifactId=OrangeHRM

Run recorder
Record script on https://opensource-demo.orangehrmlive.com
	1-Login
	2-Add Employee
	3-Employee Details
	4-Search Employee
	5-Delete Employee
	6-Logout

LoginLogout
============
Comment all request except Login and Logout
Rename requests

Correlate
===========
Use firefox F12 and search token

/orangehrm/symfony/web/index.php/auth/login
.check(status.is(200))
.check( css("#csrf_token","value").saveAs("csrf_token"))

Add .check(status.is(200)) to all requests
Set pause to 0

Log and conf
=====================
logback-test.xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} [%-5level] %logger{15} - %msg%n%rEx</pattern>
        </encoder>
        <immediateFlush>false</immediateFlush>
    </appender>
    <appender name="FILE" class="ch.qos.logback.core.FileAppender">
        <file>C:\\temp\\debug.log</file>
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} [%-5level] %logger{15} - %msg%n%rEx</pattern>
        </encoder>
        <immediateFlush>false</immediateFlush>
    </appender>
    <!-- uncomment and set to DEBUG to log all failing HTTP requests -->
    <!-- uncomment and set to TRACE to log all HTTP requests -->
    <!--<logger name="io.gatling.http.engine.response" level="TRACE" />-->
    <root level="WARN">
        <appender-ref ref="CONSOLE" />
    </root>
</configuration>

gatling.conf
runDescription = "Webinaire Gatling"

Add employee Random EmployeeID
========================================


import scala.util.Random
object Utils {
// Génération d’un matricule sur 5 chiffres
    def randomCode( ) : String = {
      val rnd = new Random()
      return (10000 + rnd.nextInt(88888)).toString()
    }
}
exec (session => session.set("employeeRandomNumber", Utils.randomCode()))


.check(status.is(200))
.check(css("#csrf_token", "value").saveAs("csrf_token"))
.check(css("#empNumber", "value").saveAs("empNumber"))

ElFileBody

.check(css("#personal__csrf_token", "value").saveAs("personal_csrf_token"))
.check(css("#personal_txtEmpID", "value").saveAs("personal_txtEmpID"))


Search employee
==========================
.check(status.is(200))
.check(css("#empsearch__csrf_token", "value").saveAs("empsearch_csrf_token"))

Démo Chropath
table#resultTable>tbody>tr:nth-of-type(1)>td:nth-of-type(1)>input

.check(status.is(200))
.check(css("table#resultTable>tbody>tr:nth-of-type(1)>td:nth-of-type(1)>input", "value").saveAs("id"))



.check(css("#contact__csrf_token", "value").saveAs("contact__csrf_token"))

${employeeRandomNumber}

Debug 
.exec(session => {
     println("**** personal_txtEmpID : ****" + session("personal_txtEmpID").as[String]) session
})

Debug Fiddler
.proxy(Proxy("localhost", 8888))


Delete
.check(css("#defaultList__csrf_token", "value").saveAs("defaultList__csrf_token"))
