# SecureApplicationPractices
Project for Project study course in University. Online study platform teaching good security practices during application development.

### Technology stack
 1. java-1.11.0-openjdk-amd64 (11.0.4)
 2. Apache Maven 3.6.0

---
### Functionality


---
### Useful links
[Lombok package & plugin config](https://www.baeldung.com/lombok-ide) \
[Test tutorial article](https://www.mkyong.com/spring-boot/spring-boot-junit-5-mockito/) \
[Rest Client usage](https://stackoverflow.com/questions/42365266/call-another-rest-api-from-my-server-in-spring-boot) \
[Response error handling](https://www.baeldung.com/spring-rest-template-error-handling) \
[Spring boot tutorial](https://www.youtube.com/watch?v=vtPkZShrvXQ) \
[Docker / PostgreSQL](https://www.youtube.com/watch?v=aHbE3pTyG-Q) \
[Configuring a DataSource Programmatically in Spring Boot (Multiple Databases)](https://www.baeldung.com/spring-boot-configure-data-source-programmatic)
---
### To do
    1. [DONE] Safe endpoints: GET (by id & all), POST, PUT, DELETE
    2. Endpoinds exposed to SQLi (SQL Injection)
        1. Creating on request new database - for 1 user to play around

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
            5. To use docked as non-root user: \
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


#### Connecting to Docker/Postgres from application
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
3. Now to proper connection from an application
 
---
---
---

**Edit a file, create a new file, and clone from Bitbucket in under 2 minutes**

When you're done, you can delete the content in this README and update the file with details for others getting started with your repository.

*We recommend that you open this README in another tab as you perform the tasks below. You can [watch our video](https://youtu.be/0ocf7u76WSo) for a full demo of all the steps in this tutorial. Open the video in a new tab to avoid leaving Bitbucket.*

---

## Edit a file

You’ll start by editing this README file to learn how to edit a file in Bitbucket.

1. Click **Source** on the left side.
2. Click the README.md link from the list of files.
3. Click the **Edit** button.
4. Delete the following text: *Delete this line to make a change to the README from Bitbucket.*
5. After making your change, click **Commit** and then **Commit** again in the dialog. The commit page will open and you’ll see the change you just made.
6. Go back to the **Source** page.

---

## Create a file

Next, you’ll add a new file to this repository.

1. Click the **New file** button at the top of the **Source** page.
2. Give the file a filename of **contributors.txt**.
3. Enter your name in the empty file space.
4. Click **Commit** and then **Commit** again in the dialog.
5. Go back to the **Source** page.

Before you move on, go ahead and explore the repository. You've already seen the **Source** page, but check out the **Commits**, **Branches**, and **Settings** pages.

---

## Clone a repository

Use these steps to clone from SourceTree, our client for using the repository command-line free. Cloning allows you to work on your files locally. If you don't yet have SourceTree, [download and install first](https://www.sourcetreeapp.com/). If you prefer to clone from the command line, see [Clone a repository](https://confluence.atlassian.com/x/4whODQ).

1. You’ll see the clone button under the **Source** heading. Click that button.
2. Now click **Check out in SourceTree**. You may need to create a SourceTree account or log in.
3. When you see the **Clone New** dialog in SourceTree, update the destination path and name if you’d like to and then click **Clone**.
4. Open the directory you just created to see your repository’s files.

Now that you're more familiar with your Bitbucket repository, go ahead and add a new file locally. You can [push your change back to Bitbucket with SourceTree](https://confluence.atlassian.com/x/iqyBMg), or you can [add, commit,](https://confluence.atlassian.com/x/8QhODQ) and [push from the command line](https://confluence.atlassian.com/x/NQ0zDQ).
