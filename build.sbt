ThisBuild / organization := "com.guizmaii"
ThisBuild / scalaVersion := "2.13.3"
ThisBuild / crossScalaVersions := Seq("2.12.12", "2.13.3")
ThisBuild / scalafmtOnCompile := true
ThisBuild / scalafmtCheck := true
ThisBuild / scalafmtSbtCheck := true

lazy val projectName = "safe-libphonenumber"

lazy val testKitLibs = Seq(
  "org.scalacheck" %% "scalacheck" % "1.14.3",
  "org.scalactic"  %% "scalactic"  % "3.0.8",
  "org.scalatest"  %% "scalatest"  % "3.0.8"
).map(_ % Test)

lazy val commonsConfig = Seq(
  scalacOptions ++= List("-release", "8"),
  addCompilerPlugin("org.typelevel" % "kind-projector" % "0.11.0" cross CrossVersion.full),
  addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1")
)

lazy val root        =
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
        "com.googlecode.libphonenumber" % "libphonenumber" % "8.12.7"
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
    credentials += Credentials(Path.userHome / ".bintray" / ".credentials"),
    licenses := Seq("Apache-2.0" -> url("http://opensource.org/licenses/https://opensource.org/licenses/Apache-2.0")),
    homepage := Some(url("https://github.com/guizmaii/safe-libphonenumber")),
    bintrayOrganization := Some("guizmaii"),
    bintrayReleaseOnPublish := true,
    publishMavenStyle := true,
    pomExtra := (
      <scm>
        <url>git@github.com:guizmaii/safe-libphonenumber.git</url>
        <connection>scm:git:git@github.com:guizmaii/safe-libphonenumber.git</connection>
      </scm>
        <developers>
          <developer>
            <id>guizmaii</id>
            <name>Jules Ivanic</name>
          </developer>
        </developers>
    )
  )
)
