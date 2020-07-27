set JAVA_HOME=C:\Java\jdk1.8.0_211

call git pull

cd ..

call mvn clean install -Dmaven.test.skip=true -s .\integration\settings.xml

copy .\target\intern-0.0.1-SNAPSHOT.jar .

call %JAVA_HOME%\bin\java -jar intern-0.0.1-SNAPSHOT.jar --spring.profiles.active=qa >> inter_qa.log 2>&1
