# SecureApplicationPractices
Project for Project study course in University. Online study platform teaching good security practices during application development.

### Technology stack
 1. java-1.11.0-openjdk-amd64 (11.0.4)
 2. Apache Maven 3.6.0
 3. Docker
 4. Google Cloud Platform
 5. PostgreSQL
 
### Presentation
[Presentation in polish language can be found at link](Presentation_PL.pdf)

---
### Functionality
Swagger api is available at [LINK WHILE IN LOCAL DEPLOYMENT](http://localhost:8080/swagger-ui.html#/course-controller) \
Or at [LINK WHILE IN REMOTE Google Cloud Platform DEPLOYMENT](https://braided-tracker-259922.appspot.com/swagger-ui.html)
---
### BACK END To do
    1. [DONE] 3 Different datasources: 
        1. [DONE] postgres - used only for migrations; 
        2. [DONE] limited safe - for safe api; 
        3. [DONE] limited unsafe - for unsafe api;
    2. [DONE] Safe endpoints: GET (by id & all), POST, PUT, DELETE
    3. [DONE] Endpoinds exposed to SQLi (SQL Injection)
        1. [DONE] Adding and modifying
    4. [DONE] Unified classes connected to Secure & Unsecure Api, includin test classes
    5. Deplyoment of newest version to GCP
    6. Modify tests so they would work on GCP
    7. [Maybe in future] Creating on request new schema, table and user - one per request for end user to safely play around 
---
### FRONT END To do
    1. [DONE] Create paginated SQL Injection course.
    2. [DONE] Improve hint visual side.
    3. [DONE] Further extend information about SQL Injection course.
    4. Create login functionality.
---
### Useful links
[Lombok package & plugin config](https://www.baeldung.com/lombok-ide) \
[Test tutorial article](https://www.mkyong.com/spring-boot/spring-boot-junit-5-mockito/) \
[Rest Client usage](https://stackoverflow.com/questions/42365266/call-another-rest-api-from-my-server-in-spring-boot) \
[Response error handling](https://www.baeldung.com/spring-rest-template-error-handling) \
[Spring boot tutorial](https://www.youtube.com/watch?v=vtPkZShrvXQ) \
[Docker / PostgreSQL](https://www.youtube.com/watch?v=aHbE3pTyG-Q) \
[Configuring a DataSource Programmatically in Spring Boot (Multiple Databases)](https://www.baeldung.com/spring-boot-configure-data-source-programmatic) \
[Flyway FAQ - Multiple Schemas](https://flywaydb.org/documentation/faq#multiple-schemas) \
[Flyway Database Migrations](https://www.baeldung.com/database-migrations-with-flyway) \
[Multiple DataSources](https://www.baeldung.com/spring-data-jpa-multiple-databases) \
[Heroku relational database / env variables guide](https://devcenter.heroku.com/articles/connecting-to-relational-databases-on-heroku-with-java) \
[Heroku deploy guide](https://devcenter.heroku.com/articles/deploying-spring-boot-apps-to-heroku) \
[Google cloud platform - Postgres & Console](https://www.youtube.com/watch?v=wAV0KbrKXF8) \
[Google cloud platform - Connecting to GCP Postgres with remote, external java application](https://cloud.google.com/sql/docs/postgres/connect-external-app?hl=pl#java) \
[Fake json - double quotes in java json string](https://stackoverflow.com/questions/54584696/how-add-quotes-in-a-json-string-using-java-when-the-value-is-a-date)
[Google cloud platform - Connecting Engine App to Cloud SQL - Datasource](https://cloud.google.com/sql/docs/mysql/connect-app-engine) \
[Google cloud platform - Connecting Engine App to Cloud SQL - IAM Permissions](https://cloud.google.com/compute/docs/access/iam-permissions) \
[Google cloud platform - Connecting Engine App to Cloud SQL - Cloud SQL Roles](https://cloud.google.com/iam/docs/understanding-roles#cloud-sql-roles) \
[Google cloud platform - Connecting Engine App to Cloud SQL - Deployment descriptor .yaml / .xml](https://cloud.google.com/appengine/docs/flexible/java/configuring-your-app-with-app-yaml)
[Google cloud platform - Connecting Engine App to Cloud SQL - Spring boot quickstart](https://cloud.google.com/appengine/docs/flexible/java/quickstart) \
[Google cloud platform - Connecting Engine App to Cloud SQL - Spring - Unix Postgres Socket](https://cloud.google.com/sql/docs/postgres/connect-app-engine) \
[Java - Environment properties](https://stackoverflow.com/questions/23506471/spring-access-all-environment-properties-as-a-map-or-properties-object) \
[Programmatically change Spring boot Application listening port](https://www.baeldung.com/spring-boot-change-port)
---

### To run JAR file
1. right-click `target` folder or open in terminal `cd ~/IdeaProjects/SecureAppPractices/target`
2. `java -jar SecureAppPractices-0.0.1-SNAPSHOT.jar`
3. Swagger api is available at [API](http://localhost:8080/swagger-ui.html#/course-controller)

### To run Front
1. First of all you need to have npm installed. On linux run:
	curl -sL https://deb.nodesource.com/setup_10.x | sudo -E bash -
	sudo apt install nodejs
2. Cd into folder where package.json is.
3. Run 'npm install'
4. Run 'npm run serve', if you are using local backend, then run 'npm run serve -- --port 3000' where 3000 can be replaced with any other port except for 8080.

### How to Github:

1. git clone https://github.com/axal25/SecureApplicationPractices
2. copy contents from diractory created by 'git clone' command to local of your existing project
4. git add *
5. git config --global user.email user_name@host
6. git commit -m "first commit from terminal"
7. git remote add origin https://github.com/user_name/repository_name
8. git remote -v
9. git push -u origin master
10. git pull origin master

#### to remove a file:
11. git rm <file>

#### to host via github pages:
12. have index.html file in home directory of your repo directory (where .git is)
13. go to repository page ( example: https://axal25.github.io/SecureApplicationPractices )
14. go to settings
15. scroll down to 'Github pages'
16. choose Source (example: master branch)

### Docker & PostgreSQL

#### Docker
1. check version of your OS
2. for unix use command: \
`lsb_release -a` \
    result: \
`No LSB modules are available.` \
`Distributor ID:	Ubuntu` \
`Description:	Ubuntu 18.04.3 LTS` \
`Release:	18.04` \
`Codename:	bionic` \
    for additional information use commands: \
`cat /proc/cpuinfo` \
or \
`uname -m`
3. Visit https://hub.docker.com/editions/community/docker-ce-server-ubuntu
4. If your OS isn't named in Prerequisites try https://docs.docker.com/install/linux/docker-ce/ubuntu/
    1. Follow one of 3 way of Docker installation:
        1. Docker’s repositories
        2. Package installation
        3. Convenience script
            1. Visit https://github.com/docker/docker-install
            2. `curl -fsSL https://get.docker.com -o get-docker.sh`
                1. To install curl \
                `sudo apt install curl` 
            3. `sudo sh get-docker.sh`
            4. Test installation with: \
            `make check`
            5. To use docked as non-root user (you won't have to type `sudo` before every `docker` command): \
            First check if docker group exists: \
            `sudo groupadd docker` \
            then: \
            `sudo usermod -aG docker YOUR_USER_NAME`
5. Check docker installation using: \
    `docker -help` 

#### Postgres
1. Visit https://hub.docker.com/_/postgres to check for versions, find instructions like 'How to use this image', 'start a postgres instance'
2. Pull image \
`sudo docker pull postgres:alpine`
3. To see downloaded images   
`sudo docker images` \
Result: \
`REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE` \
 `postgres            alpine              5b681acb1cfc        3 weeks ago         72.8MB`
4. To run downloaded image:
`sudo docker run --name some-postgres -e POSTGRES_PASSWORD=mysecretpassword -d -p 5432:5432 postgres:alpine ` \
Example: \
`sudo docker run --name postgresPASSWORD -e POSTGRES_PASSWORD=password -d -p 5432:5432 postgres:alpine` \
Result: \
`f0ef0e1f1beef62b9da666b2bd23f6d55cf6436e90583e076174786f2567efbc` \
Where: 
    1. last parameter `postgres:alpine` is the `name` of the image with `tag` after `:`
    2. `mysecretpassword` is password of our choosing
    3. `some-postgres` after `--name` parameter is name of our choosing 
5. Check with: \
`sudo docker ps`
Result: \
`CONTAINER ID        IMAGE               COMMAND                  CREATED              STATUS              PORTS                    NAMES` \
`f0ef0e1f1bee        postgres:alpine     "docker-entrypoint.s…"   About a minute ago   Up About a minute   0.0.0.0:5432->5432/tcp   postgresPASSWORD`
6. Other useful commands: 
    * `sudo docker kill NUMBER_OF_CONTAINER`
        * the number of container is given as a result of running one
        * example: `f0ef0e1f1beef62b9da666b2bd23f6d55cf6436e90583e076174786f2567efbc` \
    * `sudo docker rm NAME_OF_CONTAINER`
        * the name of container is the name you gave it while creating one
        * example: `postgresPASSWORD`
7. To use the container: \
`sudo docker exec -it postgresPASSWORD bash`
    * `postgresPASSWORD` is our container name
8. Then to run psql: \ 
`psql -U postgres`
    * `postgres` is postgres root access username
9. To see all users: `\du`
Result: \
` List of roles ` \
` Role name |                         Attributes                         | Member of `\
` -----------+------------------------------------------------------------+----------- `  
` postgres | Superuser, Create role, Create DB, Replication, Bypass RLS | {} `
10. To see all databases: `\l`
11. To add database: `create database NAME_OF_DATABASE;` (don't forget about colon)
12. To connect to database `\c NAME_OF_DATABASE` \
Result:
`You are now connected to database "test" as user "postgres".` 
13. Show all relations: `\d`
14. To run previously created (named) container which is now stopped (killed): \
`sudo docker start postgresPASSWORD` \
Result: `postgresPASSWORD` \
Check using: `sudo docker ps` \
Result: \
`CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS                    NAMES` \
`f0ef0e1f1bee        postgres:alpine     "docker-entrypoint.s…"   13 hours ago        Up 5 seconds        0.0.0.0:5432->5432/tcp   postgresPASSWORD` 
15. To check which container ports are exposed: `sudo docker port postgresPASSWORD` \
Result: `5432/tcp -> 0.0.0.0:5432` 


#### Connecting to Docker/Postgres from your machine
1. Check if you have installed psql using command `psql`
    * To install postgres \
    `sudo apt update` \
    `sudo apt install postgresql postgresql-contrib` \
    `sudo -i -u postgres` \
    `psql` \
    `\q`
2. Connect from local machine to postgres instance inside of the docker container: \
`psql -h localhost -p 5432 -U postgres` \
Type your password


#### GCP (Google Cloud Platform) - Creating Postgres & connecting through Console
1. (GCP) Navigation menu (Hamburger menu - 3 horizontal lines - Left-Top corner) > SQL > Create Instance > PostgreSQL > Next
    1. Instance ID: YOUR_POSTGRES_NAME (gcp-remote-postgres)
    2. Default user password: YOUR_POSTGRES_PASSWORD (jacekoles_lukaszstawowy_studioprojektowe_2019)
    3. Region: europe-west1, Zone: Any
    4. Database version: PostgreSQL 11
2. Configuration options >  
    1. Configure machine type and storage > 
        1. Enable automatic storage increases
    2. Enable auto backups and high availability > 
        1. Automate backup
        2. High availability (regional)
3. Create > Wait for instance to prepare (Spinner will turn into green checkmark)
4. Click on the created instance to go to instance details
5. Click on 'Connect using Cloud Shell'
6. Confirm prepared command by pressing enter
    1. You may be greeted by message: \
    `Cloud SQL Admin API has not been used in project 476885512536 before or it is disabled.` \
    `Enable it by visiting https://console.developers.google.com/apis/api/sqladmin.googleapis.com/overview?project=476885512536 then retry.` \
    `If you enabled this API recently, wait a few minutes for the action to propagate to our systems and retry.`
    2. Visit the link by clicking on it
    3. Click 'Enable' on Cloud SQL Admin API
    4. Retry previous command in GCP command line
7. Enter YOUR_POSTGRES_PASSWORD
8. Now you can work with your remote database

#### GCP - Connecting from remote / external applications to GCP Postgres
1. Determinate your IP address ([visit](http://ipv4.whatismyv6.com/)) (87.207.67.37)
2. Authorize your application's IP address to connect to your instance. 
    1. Go to the Cloud SQL Instances page in the Google Cloud Console.
    2. Go to the Cloud SQL Instances page
    3. Click the instance name to open its Instance details page.
    4. Select the Connections tab.
    5. Select the Public IP checkbox.
    6. Click Add network.
    7. In the Network field, enter the IP address or address range you want to allow connections from.
        1. Use CIDR notation.
        2. Optionally, enter a name for this entry.
    8. Click Done.
    9. Click Save to update the instance.
3. You can find the IP address assigned to your instance in its Instance details page. \
   This is the value you need for your application's connection string. \
   Example:
   Connect to the instance: \
   Public IP adress `34.76.176.166` \
   Connection instance name: `braided-tracker-259922:europe-west1:gcp-remote-postgres`
4. Modify CustomDataSourceProperties.java

#### GCP (Google Cloud Platform) - Creating remote App & connecting through Console
1. (GCP) Navigation menu (Hamburger menu - 3 horizontal lines - Left-Top corner) > Compute > App Engine > Panel > Create instance
2. Choose region: europe-west > Next
3. Choose programming language: Java, Environment: Standard
    1. [Documentation for Java in App Engine](https://cloud.google.com/appengine/docs/standard/java/?hl=pl)
    2. [Code samples in standard Java](https://github.com/GoogleCloudPlatform/java-docs-samples/)
    3. [Download Cloud SDK](https://cloud.google.com/sdk/?hl=pl)
4. Click `I'll do it later` 
5. You will get message: \
"Your application App Engine has been created. To help you deploy your application we can direct to recommended resources depending on programming language use by you."
    1. If you'd like to go back to previous screen:
        1. Click `Let's start`
        2. Again, choose programming language and environment (Java, Standard)
6. Click 'google cloud console' icon in the top right corner next to notification icon
8. Let your App Engine connect to Postgres SQL database on GCP
    1. Go to (GCP) Navigation menu (Hamburger menu - 3 horizontal lines - Left-Top corner)
    2. Administration > Permissions 
    3. In the overview you should be seeing table view of \
    `Permission in project "YOUR_PROJECT_NAME"` 
    4. Check row containing
    `App Engine default service account` | `braided-tracker-259922@appspot.gserviceaccount.com `   
    for `Cloud SQL` roles
        1. Click edit icon (pencil) in the last column of the row
        2. Click add another role
        3. Choose `Cloud SQL` and then `Admin`, `Editor` or `Client`
        4. Now that role show for example `Cloud SQL Admin` safe change with `safe` button
7. Clone your repository from github   
`git clone https://github.com/axal25/SecureApplicationPractices.git`
8. Change folders to your github local repository location \
`cd SecureApplicationPractices/SecureAppPractices`
9. Compile with maven so it contains .jar
    1. First change jdk from 8 (default) to 11 \
    `sudo update-java-alternatives -s java-1.11.0-openjdk-amd64 && export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64` 
        1. Ignore error:  
        `update-alternatives: error: no alternatives for mozilla-javaplugin.so`
        1. To go back: \
        `sudo update-java-alternatives -s java-1.8.0-openjdk-amd64 && export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64/jre`
        3. Confirm with: \
        `mvn -v` \
        `java -version` \
        `javac -version`
    2. Then  
    `mvn clean install -DskipTests`
10. Initialize google cloud api \
`gcloud init`
11. Answer 'Pick configuration to use'
12. Answer 'Confirm email address'
13. Answer 'Pick cloud project'
14. Deploy application \
`mvn appengine:deploy` 
    1. If that doesn't work try `gcloud app deploy`
    2. Answer 'Services to deploy'
    3. Deploying ... Done! \
Type `gcloud app browse`
17. Enter the address to your browser
18. In case of issues:
`gcloud app logs tail -s default`
---