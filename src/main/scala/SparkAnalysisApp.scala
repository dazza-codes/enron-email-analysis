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
    // - assumptions:
    //   - there are no email headers that specifically identify threads
    //   - a message belongs to only one thread
    //   - users can modify the subject in any thread, but the majority of
    //     the thread topic will remain the same (this may or may not exclude "off-topic"
    //     messages)
    // - approach
    //   - look for subject in subjects (maybe tokenize the words in subject)
    //   - find all emails where the intersection of their subject words is "meaningful"
    //   - the word intersection could result in a count and use a "meaningful" threshold (% of words in common?)

    val subjectCleanup = udf((subject:String) =>
      subject
        .replaceAll("(?i)RE:","") // remove 'RE:', case insensitive
        .replaceAll("(?i)FW:","") // remove 'FW:', case insensitive
        .replaceAll("\"", "'")
        .replaceAll("'", "")
        .replaceAll("[ ,;-]+","") // remove some punctuation
      // could tokenize subject here, but using SQL LIKE operator for now
    )

    // replaced this UDF with a SQL LIKE filter instead
//    val subjectStringMatch = udf((subject:String, substring:String) =>
//      subject.contains(substring)
//    )

    val cleanSubject = mailDf
      .withColumn("subject", subjectCleanup($"subject"))

    val cleanDf = cleanSubject
      .filter("subject != ''")
      .orderBy(asc("dateEpochUTC"))

    // might be useful to explode DS and map data with a case class?

    // iterate on messages and keep track of all the message-ids (uuid) that have been
    // matched already, skip them if they have been seen/matched already; the ordering by
    // date makes it possible to assume that messages later in the iteration should match
    // one prior message (the original message) if they belong to a thread.

    val threads = collection.mutable.Map[String,Array[String]]()
    val uuidSeen = collection.mutable.Set[String]()

    val subjects = cleanDf.select("uuid", "subject").collect
    val it = subjects.iterator
    while(it.hasNext) {
      val item = it.next
      val uuid = item(0).toString
      val subj = item(1).toString
      if( ! uuidSeen.contains(uuid) ) {
        uuidSeen += uuid
        // find all emails that contain the same subject (as a substring)
        val filt = s"subject like '%$subj%'"
        val matches: Array[String] = cleanDf.filter(filt).select("uuid").map(_ (0).toString).collect
        threads(uuid) = matches
        uuidSeen ++= matches
      }
    }

    // TODO: save out threads information
    // - keys in threads are the origin message for a thread
    // - values in threads are all the messages that belong to the origin
    // - because the iteration short-circuits by uuidSeen, the threads are not nested
    // - more sophisticated nesting involves analysis of to/from/cc/bcc and body details
    //   - it's possible it could be a recursive function

    // TODO: fields: unique ID, Sender User ID, Thread ID, E-mail content, Sent-at timestamp


    // Thread depth
    threads.size
//    res3: Int = 957

    threads.values.map(_.size)
//    res2: Iterable[Int] = List(1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 3, 2, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 2, 1, 1, 1,
    // 1, 7, 1, 2, 47, 3, 1, 2, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 13, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1, 1, 1,
    // 2, 1, 1, 2, 1, 4, 1, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 2, 2, 1, 1, 2, 1, 2, 1, 1, 2, 2, 1, 1, 1, 1, 1,
    // 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 2, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 169, 1, 2, 1, 1,
    // 1, 1, 3, 1, 2, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 47, 1, 2, 1, 1, 1, 1, 2, 2, 1, 2, 2, 1, 1, 1,
    // 6, 1, 1, 1, 1, 2, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 8, 1, 8, 1, 7, 1, 1, 1, 1, 1, 2, 1, 1, 1, 2, 1, 1, 1, 2, 1,
    // 1, 1, 3, 1, 1, 1, 1, ...



    spark.stop()
  }

}
