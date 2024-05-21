## Customer account simple app

### Local setup
	- [Install MYSQL server and database] (https://www.mysql.com/downloads/)
	- Ensure DB is accessible thru port 3306
	- [Install maven](https://maven.apache.org/install.html)
	- Pull project repostiroy, open terminal/cmd line and navigate to project root
	- Run "mvn clean install"
	- Start the application, run "mvn spring-boot:run". Local application runs on http://localhost:8080
	- Open http://localhost:8080/register
	- Refer to open.yaml file at /target/open.yaml for API descriptions
	
### Docker deployment setup
	- Install prefer docker examples: [Docker deskstop](https://www.docker.com/products/docker-desktop/)
	- Start docker instance
	- Open terminal/cmd line and navigate to project root
	- Run "mvn clean install"
	- run "docker-compose up", dockerized application runs on http://localhost:6868/
	- Refer to DockerFile and docker-compose.yaml for more info
	
	
### Initial test data created 
	- Username: irwin1
	- Password: wsrfxqZNlZ
	- Open http://localhost:8080/token and use credentials as basic auth
	- Open http://localhost:8080/overview using Token as bearer token auth