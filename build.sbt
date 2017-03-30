organization := "com.hashicorp.nomad"
name := "nomad-scala-sdk"
homepage := Some(url("https://github.com/hashicorp/nomad-scala-sdk"))

organizationName := "Hashicorp"
organizationHomepage := Some(url("https://www.hashicorp.com/"))

crossScalaVersions := Seq("2.11.8", "2.12.1")
scalaVersion := crossScalaVersions.value.head

version := "1.0-SNAPSHOT"

resolvers += Resolver.mavenLocal

libraryDependencies += "com.hashicorp.nomad" % "nomad-sdk" % version.value

libraryDependencies += "com.hashicorp.nomad" % "nomad-testkit" % version.value % Test
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1"

publishMavenStyle := true
