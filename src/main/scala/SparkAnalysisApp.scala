import org.apache.spark.sql.SparkSession
import java.nio.file.Paths
import java.sql.{Connection, Statement}
//import java.util.Properties

//// Scala ActiveRecord
//// https://github.com/aselab/scala-activerecord-sample/tree/master/sample/src/main/scala
//import com.github.aselab.activerecord._
//import com.github.aselab.activerecord.dsl._
//import models._

object SparkAnalysisApp {

  def main(args: Array[String]) {

//    val connectionProperties = new Properties()
//    connectionProperties.put("user", "enron")
//    connectionProperties.put("password", "enronPass")

//    val jdbcURL = "jdbc:mysql://localhost:3306/enron_email_records?user=enron&password=enronPass"
//    val connection: Connection = DaoUtil.getConnection(jdbcURL)
//    connection.isClosed
//
//    val statement: Statement = connection.createStatement()
//    statement.setQueryTimeout(30)


    if (args.length == 0) {
      throw new IllegalArgumentException("Input Parquet file is required")
    }

    val pqPath = Paths.get(args(0)).toAbsolutePath.toString

    val spark = SparkSession.builder.appName("Enron-Email-Analysis").getOrCreate()

    val mailDf = spark.read.parquet(pqPath)

    val email_count = mailDf.count
    println(s"Emails loaded: $email_count")

    // Populate the User table
    for(row <- mailDf.select("from").distinct) {
      val email = row(0).toString
      println(email)
//      statement.executeUpdate(s"insert into users values('$email')")
    }

    spark.stop()
  }

}
