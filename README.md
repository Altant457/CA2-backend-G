
*This project is meant as start code for CA-3 and exam projects, made by members of group CA2-2-Bornholm-B*

*Projects which are expected to use this start-code are projects that require all, or most of the following technologies:*
 - *JPA and REST*
- *Testing, including database test*
- *Testing, including tests of REST-API's*
- *CI and CONTINUOUS DELIVERY*

## CA2

### Preconditions
*In order to use this code, you should have a local developer setup + a "matching" server on the internet, which you have admin access to.* 

### Getting Started

This document explains how to use this code (build, test and deploy), locally with maven, and remotely with maven controlled by Github actions
 - [How to use](https://docs.google.com/document/d/1rymrRWF3VVR7ujo3k3sSGD_27q73meGeiMYtmUtYt6c/edit?usp=sharing)

Remember to edit the `pom.xml` so the URL specified in `<remote.server>` matches your server.

When you have got the project up and running, run the `main` method in [SetupTestUsers](src/main/java/utils/SetupTestUsers.java) to get a starting point with some test users.

### JPA snippets

### Create entities from database in Intellij (Persistence mappings)
- From inside the Persistence window:
- Right-click a persistence unit, point to Generate Persistence Mapping and select By Database Schema.
- Select the 
  - data source 
  - package
  - tick tables to include
  - open tables to see columns and add the ones with mapped type: Collection<SomeEntity> and SomeEntity
  - click OK.