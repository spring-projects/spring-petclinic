# spring-petclinic Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]
### Fixed
- Upgrade spring boot to version 2.5.12 as a precaution to fix the [RCE CVE-2022-22965]( https://nvd.nist.gov/vuln/detail/CVE-2022-22965). The spring petclinic was generally not affected by this CVE as it runs with an embedded tomcat instead of a standalone version.
