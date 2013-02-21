# Spring PetClinic Sample Application

## What does it look like?
spring-petclinic has been deployed here on cloudfoundry: http://spring-petclinic.cloudfoundry.com/

## Running petclinic locally
```
git clone https://github.com/SpringSource/spring-petclinic.git
mvn tomcat7:run
```
You can then access petclinic here: http://localhost:9966/petclinic/

## Working with Petclinic in Eclipse/STS

### prerequisites
The following items should be installed in your system:
* Maven 3 (http://www.sonatype.com/books/mvnref-book/reference/installation.html)
* git command line tool (https://help.github.com/articles/set-up-git)
* Eclipse with the m2e plugin (m2e is installed by default when using the STS (http://www.springsource.org/sts) distribution of Eclipse)

Note: when m2e is available, there is an m2 icon in Help -> About dialog.
If m2e is not there, just follow the install process here: http://eclipse.org/m2e/download/


### Steps:

In the command line
```
git clone https://github.com/SpringSource/spring-petclinic.git
```
Inside Eclipse: 
```
File -> Import -> Maven -> Existing Maven project
```


## Looking for something in particular?

<table>
  <tr>
    <th>Web layer</th><th>Files</th>
  </tr>
  <tr>
    <td>Spring MVC- Atom integration</td>
    <td>
      <a href="/SpringSource/spring-petclinic/blob/master/src/main/java/org/springframework/samples/petclinic/web/VisitsAtomView.java">VisitsAtomView</a>
      <a href="/SpringSource/spring-petclinic/blob/master/src/main/webapp/WEB-INF/mvc-view-config.xml">mvc-view-config.xml</a>
    </td>
  </tr>
  <tr>
    <td>Spring MVC - XML integration</td>
    <td><a href="/SpringSource/spring-petclinic/blob/master/src/main/webapp/WEB-INF/mvc-view-config.xml">mvc-view-config.xml</a></td>
  </tr>
  <tr>
    <td>Spring MVC Test Framework</td>
    <td><a href="/SpringSource/spring-petclinic/blob/master/src/test/java/org/springframework/samples/petclinic/web/VisitsAtomViewTest.java">VisitsAtomViewTest.java</a></td>
  </tr>
  <tr>
    <td>JSP custom tags</td>
    <td>
      <a href="/SpringSource/spring-petclinic/tree/master/src/main/webapp/WEB-INF/tags">WEB-INF/tags</a></td>
  </tr>
  <tr>
    <td>webjars</td>
    <td>
      <a href="/SpringSource/spring-petclinic/tree/master/pom.xml#L171">webjars declaration inside pom.xml</a> <br />
      <a href="/SpringSource/spring-petclinic/blob/master/src/main/webapp/WEB-INF/mvc-core-config.xml#L24">Resource mapping in Spring configuration</a> <br />
      <a href="/SpringSource/spring-petclinic/blob/master/src/main/webapp/WEB-INF/jsp/fragments/headTag.jsp#L12">sample usage in JSP</a></td>
    </td>
  </tr>
</table>
