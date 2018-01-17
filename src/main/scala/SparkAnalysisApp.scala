import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

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

    if (args.length == 0) {
      throw new IllegalArgumentException("Input Parquet file is required")
    }

    val pqPath = Paths.get(args(0)).toAbsolutePath.toString

    val spark = SparkSession.builder.appName("Enron-Email-Analysis").getOrCreate()
    import spark.implicits._

    val mailDf = spark.read.parquet(pqPath)

    val email_count = mailDf.count
    println(s"Emails loaded: $email_count")


    /**
      * Email Users
      */

    val from = mailDf.select("from").distinct.collect
    from.foreach( row => {
      val email = row(0).toString
      println(email)
    })

//    dbutils.fs.rm("/tmp/enron-users.parquet", true)
    mailDf.select("from")
      .distinct
      .write.parquet("enron-users.parquet")

    // Populate the User table  -  not working
    // Exception in thread "main" org.apache.spark.SparkException: Task not serializable
//    mailDf.select("from").distinct.foreachPartition ( row_it => {
//      while(row_it.hasNext) {
//        val row = row_it.next
//        val email = row(0).toString
//        val statement: Statement = connection.createStatement()
//        statement.setQueryTimeout(30)
//        statement.executeUpdate(s"insert into users values('$email')")
//        println(email)
//      }
//    })


    /**
      * Email Threads
      */

    // From Klimpt & Yang paper, section 5:
    //We have also briefly analyzed the nature of email threads in this corpus. For
    //this analysis, membership in a thread was determined by two factors. Emails
    //were considered to be in the same thread if they contained the same words in
    //their subjects and they were among the same users (addresses).

    // a thread-match algorithm:
    // - tokenize the words in subject
    // - find all emails where the intersection of their subject words is "meaningful"
    // - the word intersection could result in a count and use a "meaningful" threshold (% of words in common?)

    val selection = mailDf
      .select("from", "subject", "dateEpochUTC")
      .filter("subject != ''")
      .orderBy(asc("dateEpochUTC"))

    val tokenSubject = selection.select("subject")
      .map { row => {
        row(0).toString
          .replaceAll("(?i)RE:","")
          .replaceAll("(?i)FW:","")
          .split("[ ,;-]+")
          .filter(_.nonEmpty)
      }}

    //  user defined function to group by subject?
    //  h(rdd.keyBy(f).groupByKey().mapValues(g).collect())

    spark.stop()
  }

}
