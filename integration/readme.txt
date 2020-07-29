SHAGIT02-W10 D:\GitRoot\intern

ssh://gengfo@shagit01-w10:29418/intern.git 
demo2019

mysql db:
root demo123456

CREATE DATABASE IF NOT EXISTS intern_qa DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
CREATE DATABASE IF NOT EXISTS intern_prd DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;

"C:\GitRoot\intern\src\main\resources\sqls\prod-schema.sql"
"C:\GitRoot\intern\src\main\resources\sqls\prod-data.sql"

for qa patch email

update t_user
set email = 'chengr2@demo.com';
commit;


QA http://sha-svp-w7.corp.demo.com:9086/login
PRD  http://shagit02-w10.corp.demo.com:9088/login