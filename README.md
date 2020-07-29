Intern Management System
---
###Make the project running & Sign in
>1、Run it: 
>Run com.demo.workshop.intern.InternApplication 
>
>2、Sign in with inital data:
>When the system starts, we provide the following data for initialization.

![avatar](src/main/resources/system_example_pictures/initialData.png)

>You can sign in system with domain_id and default Password: 123456
---
###UI Operation
>1、Mananger：The mananger can view, create and update all interns' information. 
>
>2、Team leader：A Team leader can approve or reject the sign-in records of interns who belongs to the leader's department.
>
>3、Intern：Interns can complete their check-in today or earlier.
---
###System Event
>1、Daily Check In Reminder:
>If today is workDay and the intern has not checked in by 4:00.pm, System will send an email to reminder he to check In.
>
>2、Period Attendance Reporter：
>If today is within three days after the settlement date, which is define By rule, the system will send an attenance 
>report email to HRs and Team Leaders, and Copy to Mananger on 8:45.am.

---
###Our DDD Flow
>At the first event storming meeting, we list all the actions in the system and divide them into different domains, as follow:
![avatar](src/main/resources/system_example_pictures/eventstorming/intern_ddd_domain_miro.jpg)

>Then sort out the functions, provided by domain, using for app level. 
>To realize functions with domains has no connection with each other,
> we arrange methods from different domains in app level like below.
>
![avatar](src/main/resources/system_example_pictures/eventstorming/intern_ddd_app_miro.jpg)