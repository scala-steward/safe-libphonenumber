ThisBuild / organization := "com.guizmaii"
ThisBuild / scalaVersion := "2.13.5"
ThisBuild / crossScalaVersions := Seq("2.12.12", "2.13.5")
ThisBuild / scalafmtOnCompile := true
ThisBuild / scalafmtCheck := true
ThisBuild / scalafmtSbtCheck := true

lazy val projectName = "safe-libphonenumber"

lazy val testKitLibs   = Seq(
  "org.scalacheck" %% "scalacheck" % "1.15.4",
  "org.scalactic"  %% "scalactic"  % "3.2.8",
  "org.scalatest"  %% "scalatest"  % "3.2.8"
).map(_ % Test)

lazy val commonsConfig = Seq(
  scalacOptions ++= List("-release", "8"),
  addCompilerPlugin("org.typelevel" % "kind-projector"     % "0.13.0" cross CrossVersion.full),
  addCompilerPlugin("com.olegpy"   %% "better-monadic-for" % "0.3.1")
)

lazy val root =
  Project(id = projectName, base = file("."))
    .settings(moduleName := "root")
    .settings(noPublishSettings: _*)
    .aggregate(core, jruby)
    .dependsOn(core, jruby)

lazy val core =
  project
    .settings(moduleName := projectName)
    .settings(commonsConfig: _*)
    .settings(
      libraryDependencies ++= Seq(
        "com.googlecode.libphonenumber" % "libphonenumber" % "8.12.23"
      ) ++ testKitLibs
    )

lazy val jruby =
  project
    .settings(moduleName := s"jruby-$projectName")
    .settings(commonsConfig: _*)
    .dependsOn(core)

/**
 * Copied from Cats
 */
lazy val noPublishSettings = Seq(
  publish := {},
  publishLocal := {},
  publishArtifact := false
)

inThisBuild(
  List(
    organization := "com.guizmaii",
    homepage := Some(url("https://github.com/guizmaii/safe-libphonenumber")),
    licenses := Seq("Apache-2.0" -> url("http://opensource.org/licenses/https://opensource.org/licenses/Apache-2.0")),
    developers := List(
      Developer(
        "guizmaii",
        "Jules Ivanic",
        "jules.ivanic@gmail.com",
        url("https://blog.jules-ivanic.com/#/")
      )
    )
  )
)
