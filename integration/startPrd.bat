call git pull

cd ..

call mvn clean install -Dmaven.test.skip=true

copy .\target\intern-0.0.1-SNAPSHOT.jar .

call java -jar intern-0.0.1-SNAPSHOT.jar --spring.profiles.active=prd >> inter_prd.log 2>&1
