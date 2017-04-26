organization := "com.hashicorp.nomad"
organizationName := "Hashicorp"
organizationHomepage := Some(url("https://www.hashicorp.com/"))

name := "nomad-scala-sdk"
homepage := Some(url("https://github.com/hashicorp/nomad-scala-sdk"))
scmInfo := homepage.value.map { repo =>
  ScmInfo(
    browseUrl = repo,
    connection = s"scm:git:$repo.git"
  )
}
licenses := Seq("Mozilla Public License, version 2.0" -> url("https://www.mozilla.org/en-US/MPL/2.0/"))

crossScalaVersions := Seq("2.11.8", "2.12.1")
scalaVersion := crossScalaVersions.value.head

resolvers += Resolver.mavenLocal

libraryDependencies += "com.hashicorp.nomad" % "nomad-sdk" % version.value

libraryDependencies += "com.hashicorp.nomad" % "nomad-testkit" % version.value % Test
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % Test

publishMavenStyle := true
pomExtra :=
  <developers>
    <developer>
      <organization>{organizationName.value}</organization>
      <organizationUrl>{organizationHomepage.value}</organizationUrl>
    </developer>
  </developers>

releaseCrossBuild := true
releaseProcess := {
  import ReleaseTransformations._
  Seq[ReleaseStep](
    inquireVersions,
    setReleaseVersion,
    checkSnapshotDependencies,
    runClean,
    ReleaseStep(action = Command.process("scalastyle", _)),
    runTest,
    commitReleaseVersion,
    tagRelease,
    ReleaseStep(action = Command.process("publishSigned", _), enableCrossBuild = true),
    setNextVersion,
    commitNextVersion,
    ReleaseStep(action = Command.process("sonatypeReleaseAll", _), enableCrossBuild = true),
    pushChanges
  )
}
