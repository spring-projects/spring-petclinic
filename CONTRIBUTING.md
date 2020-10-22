# Spring PetClinic Sample Application [![Build Status](https://travis-ci.org/spring-projects/spring-petclinic.png?branch=main)](https://travis-ci.org/spring-projects/spring-petclinic/)

## Spring PetClinic Sample Community Information

Petclinic is a [Spring Boot](https://spring.io/guides/gs/spring-boot) application built using [Maven](https://spring.io/guides/gs/maven/). 


This page provides an overview of ways to interact with Spring PetClinic Sample but focuses primarily on the process to fix bugs and contribute with new code.

## Issues

We use GitHub [issues tracker](https://github.com/spring-projects/spring-petclinic/issues) is the preferred channel for bug reports, features requests and submitting pull requests.

-- Please check if anyone else has filed the same issue before creating a duplicate. If it has been filed, feel free to add any new information you have found about it.

### When to open issues

There is somewhat of a fine line that differentiates them but in general you can think of it like this:

- 'Something doesn't work correctly' 
- 'There is an exception when I do something in the admin'
- 'I'll be interesting to implement new feature X, what do you think?'

### How to open BUG issues
All BUG issues should include, at a minimum, the following:

- Prefix title with [BUG]
- Software version you are using
- Branch you are using if it's differt from main
- Steps to reproduce
- Any stack traces that you receive (if applicable)
- Any additional information that allows us to help you faster
- Any attempts you have tried to fix it
- Any tested and confirmed fixes

### How to open ENHANCEMENT issues
All ENHANCEMENT issues should include, at a minimum, the following:

- Prefix title with [ENHANCEMENT]
- Description that could help developers to implement the issue.


## Fix it yourself with a Pull Request
You can solve [BUG] or implement [ENHANCEMENT] yourself of course! [How to do](https://help.github.com/articles/using-pull-requests)
For pull requests, editor preferences are available in the [editor config](.editorconfig) for easy use in common text editors. Read more and download plugins at <https://editorconfig.org>. If you have not previously done so, please fill out and submit the [Contributor License Agreement](https://cla.pivotal.io/sign/spring).
 
- First of all you must fork the Spring PetClinic Sample project and submit. 
- Use Git Flow to start a BugFix or a new feature.
- Push your branch on your GitHub repository.
- Start a pull request with the main branch of spring-project/spring-petclinic.
- [Comment](https://docs.github.com/en/free-pro-team@latest/github/collaborating-with-issues-and-pull-requests/commenting-on-a-pull-request) the pull request using [close] with the issue number like : close #555
- Send the pull request
 
Pull requests will receive priority access to getting reviewed and a fix in place. While we will not necessarily accept all pull requests, we would love for you to kick off a discussion around the fix.


## Interaction with other open source projects

One of the best parts about working on the Spring Petclinic application is that we have the opportunity to work in direct contact with many Open Source projects. We found some bugs/suggested improvements on various topics such as Spring, Spring Data, Bean Validation and even Eclipse! In many cases, they've been fixed/implemented in just a few days.
Here is a list of them:

| Name | Issue |
|------|-------|
| Spring JDBC: simplify usage of NamedParameterJdbcTemplate | [SPR-10256](https://jira.springsource.org/browse/SPR-10256) and [SPR-10257](https://jira.springsource.org/browse/SPR-10257) |
| Bean Validation / Hibernate Validator: simplify Maven dependencies and backward compatibility |[HV-790](https://hibernate.atlassian.net/browse/HV-790) and [HV-792](https://hibernate.atlassian.net/browse/HV-792) |
| Spring Data: provide more flexibility when working with JPQL queries | [DATAJPA-292](https://jira.springsource.org/browse/DATAJPA-292) |
