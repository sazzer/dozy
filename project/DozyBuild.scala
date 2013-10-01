import sbt._
import sbt.Keys._

object Settings {
    val scalaCompilerVersion = "2.10.2"

    lazy val coreSettings = Seq(
        organization := "uk.co.grahamcox.dozy",
        version := "0.1-SNAPSHOT",
        scalaVersion := scalaCompilerVersion
    )

    val Resolvers = Seq (
        "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
        "Sonatype OSS Releases" at "https://oss.sonatype.org/content/repositories/releases"
    )

    lazy val defaultSettings = Defaults.defaultSettings ++ 
        coreSettings ++ Seq(
            resolvers ++= Resolvers,
            scalacOptions in Compile ++= Seq(
                "-encoding", "UTF-8",
                "-target:jvm-1.7",
                "-deprecation",
                "-feature",
                "-unchecked",
                "-Xfatal-warnings"
            )
        ) ++
        net.virtualvoid.sbt.graph.Plugin.graphSettings
}

object Dependencies {
    val jettyVersion = "9.0.5.v20130815"
    val slf4jVersion = "1.7.1"
    val logbackVersion = "1.0.1"
    val springVersion = "3.2.4.RELEASE"

    val Specs = Seq(
        "org.specs2" %% "specs2" % "2.2.2" % "test",
        "org.scalacheck" %% "scalacheck" % "1.10.0" % "test",
        "org.mockito" % "mockito-all" % "1.9.0" % "test",
        "org.hamcrest" % "hamcrest-all" % "1.3" % "test"
    )

    val Servlet = Seq(
        "javax.servlet" % "servlet-api" % "2.5" % "provided"
    )

    val Jetty = Seq(
        "org.eclipse.jetty" % "jetty-webapp" % jettyVersion % "container",
        "org.eclipse.jetty" % "jetty-plus" % jettyVersion % "container"
    )

    val Logging = Seq(
        "org.clapper" %% "grizzled-slf4j" % "1.0.1"
    )

    def LoggingImpl(scope: String = "runtime") = Seq(
        "org.slf4j" % "jcl-over-slf4j" % slf4jVersion % scope,
        "org.slf4j" % "jul-to-slf4j" % slf4jVersion % scope,
        "ch.qos.logback" % "logback-classic" % logbackVersion % scope,
        "ch.qos.logback" % "logback-core" % logbackVersion % scope
    )

    val Spring = Seq(
      "org.springframework" % "spring-context" % springVersion
    )

    val ScalaCore = Seq(
      "org.scala-lang" % "scala-reflect" % Settings.scalaCompilerVersion
    )

    val Core = ScalaCore ++ Logging ++ Specs
}

object DozyBuild extends Build {
    lazy val root = Project(
        id = "dozy",
        base = file("."),
        aggregate = Seq(core, spring, example),
        settings = Settings.defaultSettings
    )

    lazy val core = Project(
        id = "dozy-core",
        base = file("core"),
        settings = Settings.defaultSettings ++ Seq(
            libraryDependencies ++= Dependencies.Core ++ 
                Dependencies.LoggingImpl("test") ++
                Dependencies.Servlet
        )
    )

    lazy val spring = Project(
        id = "dozy-spring",
        base = file("spring"),
        dependencies = Seq(
            core % "compile"
        ),
        settings = Settings.defaultSettings ++
            Seq(
                libraryDependencies ++= Dependencies.Core ++ 
                    Dependencies.LoggingImpl() ++
                    Dependencies.Spring
        )
    )

    lazy val example = Project(
        id = "dozy-example",
        base = file("example"),
        dependencies = Seq(
            core % "compile"
        ),
        settings = Settings.defaultSettings ++
            com.earldouglas.xsbtwebplugin.WebPlugin.webSettings ++ Seq(
                libraryDependencies ++= Dependencies.Core ++ 
                    Dependencies.LoggingImpl() ++ 
                    Dependencies.Jetty
        )
    )
}
