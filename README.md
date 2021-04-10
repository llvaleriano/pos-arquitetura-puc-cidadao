# Módulo de Serviços ao Cidadão

Essa aplicação é um microsserviço planejado para ser parte de uma arquitetura de microsserviços.

Para que ela possa ser utilizada corretamente, alguns serviços de que ela depende devem estar corretamente configurados.

Esses serviços estão previamente configurados através de scripts para que sejam instanciados via docker ou docker-compose. A lista abaixo indica quais os serviços de que a aplicação depende:

**1. JHipster registry**

Através desse serviço temos a disposição o registro de serviço, descoberta de serviço e configuração centralizada.

Iniciar com o comando:

```
docker run -p 8761:8761 --name puc_service_registry -d jhipster/jhipster-registry
```

**2. Postgresql**

Servidor de banco de dados responsável pelo armazenamento dos dados desse microsserviço exclusivamente.  
Iniciar com o comando abaixo, a partir do diretório raiz do projeto:

```
docker-compose -f src/main/docker/postgresql.yml up -d

```

**3. Kafka**

Responsável pelo serviço de mensageria.  
Iniciar com o comando abaixo, a partir do diretório raiz do projeto

```
docker-compose -f src/main/docker/kafka.yml up -d
```

**4. Elasticsearch**

Responsável pela pesquisa centralizada de dados provenientes de todos os outros serviços.  
Iniciar com o comando abaixo, a partir do diretório raiz do projeto

```
docker-compose -f src/main/docker/elasticsearch.yml up -d
```

## Development

To start your application in the dev profile, run:

```
./mvnw
```

For further instructions on how to develop with JHipster, have a look at [Using JHipster in development][].

## Building for production

### Packaging as jar

To build the final jar and optimize the cidadao application for production, run:

```

./mvnw -Pprod clean verify


```

To ensure everything worked, run:

```

java -jar target/*.jar


```

Refer to [Using JHipster in production][] for more details.

### Packaging as war

To package your application as a war in order to deploy it to an application server, run:

```

./mvnw -Pprod,war clean verify


```

## Testing

To launch your application's tests, run:

```
./mvnw verify
```

For more information, refer to the [Running tests page][].

### Code quality

Sonar is used to analyse code quality. You can start a local Sonar server (accessible on http://localhost:9001) with:

```
docker-compose -f src/main/docker/sonar.yml up -d
```

You can run a Sonar analysis with using the [sonar-scanner](https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner) or by using the maven plugin.

Then, run a Sonar analysis:

```
./mvnw -Pprod clean verify sonar:sonar
```

If you need to re-run the Sonar phase, please be sure to specify at least the `initialize` phase since Sonar properties are loaded from the sonar-project.properties file.

```
./mvnw initialize sonar:sonar
```

For more information, refer to the [Code quality page][].

## Using Docker to simplify development (optional)

You can use Docker to improve your JHipster development experience. A number of docker-compose configuration are available in the [src/main/docker](src/main/docker) folder to launch required third party services.

For example, to start a postgresql database in a docker container, run:

```
docker-compose -f src/main/docker/postgresql.yml up -d
```

To stop it and remove the container, run:

```
docker-compose -f src/main/docker/postgresql.yml down
```

You can also fully dockerize your application and all the services that it depends on.
To achieve this, first build a docker image of your app by running:

```
./mvnw -Pprod verify jib:dockerBuild
```

Then run:

```
docker-compose -f src/main/docker/app.yml up -d
```

For more information refer to [Using Docker and Docker-Compose][], this page also contains information on the docker-compose sub-generator (`jhipster docker-compose`), which is able to generate docker configurations for one or several JHipster applications.

## Continuous Integration (optional)

To configure CI for your project, run the ci-cd sub-generator (`jhipster ci-cd`), this will let you generate configuration files for a number of Continuous Integration systems. Consult the [Setting up Continuous Integration][] page for more information.

[jhipster homepage and latest documentation]: https://www.jhipster.tech
[jhipster 6.10.5 archive]: https://www.jhipster.tech/documentation-archive/v6.10.5
[doing microservices with jhipster]: https://www.jhipster.tech/documentation-archive/v6.10.5/microservices-architecture/
[using jhipster in development]: https://www.jhipster.tech/documentation-archive/v6.10.5/development/
[service discovery and configuration with the jhipster-registry]: https://www.jhipster.tech/documentation-archive/v6.10.5/microservices-architecture/#jhipster-registry
[using docker and docker-compose]: https://www.jhipster.tech/documentation-archive/v6.10.5/docker-compose
[using jhipster in production]: https://www.jhipster.tech/documentation-archive/v6.10.5/production/
[running tests page]: https://www.jhipster.tech/documentation-archive/v6.10.5/running-tests/
[code quality page]: https://www.jhipster.tech/documentation-archive/v6.10.5/code-quality/
[setting up continuous integration]: https://www.jhipster.tech/documentation-archive/v6.10.5/setting-up-ci/
