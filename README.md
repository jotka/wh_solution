###building the fat jar with all dependencies (like hibernate, mysql driver etc).

mvn clean package

###running

java -jar target\parser-jar-with-dependencies.jar --startDate=2017-01-01.00:00:00 --duration=daily --threshold=500 --accesslog=.\access.log

java -jar target\parser-jar-with-dependencies.jar --startDate=2017-01-01.15:00:00 --duration=hourly --threshold=200 --accesslog=.\access.log