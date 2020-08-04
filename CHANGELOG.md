# Change Log
All notable changes to this project will be documented in this file.
This project adheres to [Semantic Versioning](http://semver.org/).

More infos about this file : http://keepachangelog.com/

## [Unreleased] - no_due_date

## [v1.2.1] - 2020.08.04

- **Rollback: Remove JDK8 from Travis since we're using `String#isBlank` which is a JDK9+ function**

## [v1.2.0] - 2020.08.03

- **Add new `LibPhoneNumber.isValidNumber` function**
- **Add new `LibPhoneNumber.getExampleNumberForType` function**
- **Add new `LibPhoneNumber.getNumberType` function**
- **Remove JDK8 from Travis since we're using `String#isBlank` which is a JDK9+ function**
- **Update scalafmt config and reformat code**
- **Update sbt**
- **Update sbt plugins**
- **Update dependencies**
- **Add and use Scala 2.13 in the build**
- **Update Scala 2.12**
- **Add Scala 2.13 in the Travis matrix**
- **Add OpenJDK13 in the Travis matrix**
- **Add OpenJDK11 in the Travis matrix**

## [v1.1.0] - 2019.02.15

- **Add a `isPossibleNumber` function**
- **Update `libphonenumber` from v8.10.2 to v8.10.5**

## [v1.0.0] - 2018.12.19

- **Init project**