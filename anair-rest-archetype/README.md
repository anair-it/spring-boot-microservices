# BOOTiful Rest archetype
This is a template to create a spring-boot rest service.

## Getting started
- Navigate to your workspace home directory
- Run `mvn clean install` on this project
- Generate new bootiful service project from this archetype
    - Replace package, artifactId in the bellow command as required
    `mvn archetype:generate -DarchetypeGroupId=org.anair.archetype -DarchetypeArtifactId=anair-rest-archetype -DarchetypeVersion=0.0.1-SNAPSHOT -DgroupId=org.anair.services -Dversion=0.0.1-SNAPSHOT -DartifactId=anair-service-a -Dpackage=org.anair.services.a`
- New project will be created in the current directory
- Update new service pom.xml as required. It may have dependencies that is not required.
