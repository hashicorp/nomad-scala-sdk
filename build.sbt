import sbt.KeyRanks.APlusSetting

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

crossScalaVersions := Seq( "2.11.12" , "2.12.15" , "2.13.8")
scalaVersion := crossScalaVersions.value.head

resolvers += Resolver.mavenLocal

val nomadJavaSdkVersion = SettingKey[String]("nomadJavaSdkVersion", "The version of the Java nomad-sdk dependency.")
nomadJavaSdkVersion := "0.11.3.0"

libraryDependencies += "com.hashicorp.nomad" % "nomad-sdk" % nomadJavaSdkVersion.value

libraryDependencies += "com.hashicorp.nomad" % "nomad-testkit" % nomadJavaSdkVersion.value % Test
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.11" % Test

usePgpKeyHex("7D65AD3D5B24A0EA035BDA2BDC6367189CC3BC7C")

publishMavenStyle := true
pomExtra :=
  <developers>
    <developer>
      <organization>{organizationName.value}</organization>
      <organizationUrl>{organizationHomepage.value}</organizationUrl>
    </developer>
  </developers>

releaseCrossBuild := true
releaseVersion := (_ => nomadJavaSdkVersion.value)
releaseProcess := {
  import ReleaseTransformations._
  Seq[ReleaseStep](
    checkSnapshotDependencies,
    inquireVersions,
    runClean,
    ReleaseStep(action = Command.process("scalastyle", _)),
    runTest,
    setReleaseVersion,
    commitReleaseVersion,
    tagRelease,
    ReleaseStep(action = Command.process("publishSigned", _), enableCrossBuild = true),
    setNextVersion,
    commitNextVersion,
    ReleaseStep(action = Command.process("sonatypeReleaseAll", _), enableCrossBuild = true),
    pushChanges
  )
}
