# Training on Gatling Academy
Helpfull training at : https://academy.gatling.io/

# Initialize Gatling project with maven
mvn archetype:generate -DarchetypeGroupId=io.gatling.highcharts -DarchetypeArtifactId=gatling-highcharts-maven-archetype -DgroupId=tutorial.gatling -DartifactId=OrangeHRM

# Record
Run recorder  
Record script on https://opensource-demo.orangehrmlive.com  
- Login
- Add Employee
- Employee Details
- Search Employee
- Delete Employee
- Logout

# First Correlation
## LoginLogout
Comment all request except Login and Logout  
Rename requests  

## Correlate
Use firefox F12 and search token

```
/orangehrm/symfony/web/index.php/auth/login
.check(status.is(200))
.check( css("#csrf_token","value").saveAs("csrf_token"))
```

Add .check(status.is(200)) to all requests
Set all pause to 0

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

