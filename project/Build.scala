import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "PbghTransit"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
        // Mysql driver
        "mysql" % "mysql-connector-java" % "5.1.18",
        // Apache DateUtils
        "org.apache.commons" % "commons-lang3" % "3.1"
    )

    def customLessEntryPoints(base: File): PathFinder =
        (base / "app" / "assets" / "stylesheets" / "vendor" / "bootstrap"  * "bootstrap.less") +++ 
        (base / "app" / "assets" / "stylesheets" / "vendor" / "bootstrap"  * "responsive.less") +++ 
        (base / "app" / "assets" / "stylesheets" ** "style.less")

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(
        // Less assets compilation
        lessEntryPoints <<= baseDirectory(customLessEntryPoints),

        routesImport += "util.DoubleW._"
    )
}