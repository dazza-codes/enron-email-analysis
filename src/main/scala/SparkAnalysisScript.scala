import org.apache.spark
import org.apache.spark.sql.SparkSession

object SparkAnalysisScript extends App {

  // This is a collection of useful spark-shell commands;
  // it's commented out so it will not compile

//  val mailDf = spark.read.parquet("enron_email_records.parquet")
//  println(mailDf.count)
//  for(row <- mailDf.select("uuid").distinct) { println(row) }
//  for(row <- mailDf.select("from").distinct) { println(row) }


//  val subject = mailDf.select("subject","dateEpochUTC").distinct.collect
//
//  val subject = mailDf.select("subject").distinct.collect
//
//  subject(0)(0).toString.replaceAll("(?i)RE:","").trim
//  subject(0)(0).toString.replaceAll("(?i)FW:","").trim
//
//  // self join ?
//  df.as("df1").join(df.as("df2"), $"df1.foo" === $"df2.foo")


  // Some messages have no subject - discard those

  // From Klimpt & Yang paper, section 5:
  //We have also briefly analyzed the nature of email threads in this corpus. For
  //this analysis, membership in a thread was determined by two factors. Emails
  //were considered to be in the same thread if they contained the same words in
  //their subjects and they were among the same users (addresses).

//  val selection = mailDf.orderBy(asc("dateEpochUTC")).select("from", "subject", "dateEpochUTC", "body").collect
//  val it = selection.iterator
//  while(it.hasNext) {
//    val item = it.next
//    val email = item(0).toString
//    val subject = item(1).toString
//    val date = item(2).asInstanceOf[Long]
//    val body = item(3).toString
//
//    // modify the subject to extract useful words
//
//    // find all emails that contain the same subject (as a substring)
//    val matches = mailDf.filter(s"subject like '%$subject%'").collect
//
//    // would be good here to say hey, these all belong to a "thread" and then
//    // remove all these emails from the dataframe so they can't match any other
//    // threads (assume that every email belongs to only one thread).  By odering the
//    // messages by date, all the remaining messages should be part of the thread started
//    // by the current item (the corpus does not begin at time-zero, some early messages are part
//    // of a threads that were begun before the corpus was collected). Removing
//    // any of the matching messages from the dataframe should probably also entail removing them
//    // from the iterator too (but that's nasty stuff, might blow up).
//    // Considering assignment of message-ID to keys in a MAP and just adding "matches"
//    // to the values of that map; a later reduction must group all the message keys by
//    // the "similarity" of their values (e.g. how many words or subjects they have in common?).
//
//    // a more sophisticated thread-match, in case subjects are changed during a thread:
//    // - find all emails where the intersection of their subject words is "meaningful"
//    // - the word intersection could result in a count and use a "meaningful" threshold (% of words in common?)
//

  //  user defined function to group by subject?
  //  h(rdd.keyBy(f).groupByKey().mapValues(g).collect())


//  }
}


