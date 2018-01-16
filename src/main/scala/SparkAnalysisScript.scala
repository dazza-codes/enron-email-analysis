import org.apache.spark
import org.apache.spark.sql.SparkSession

object SparkAnalysisScript extends App {

  // This is just collection of useful spark-shell commands;
  // it's commented out so it will compile

//  val mailDf = spark.read.parquet("enron_email_records.parquet")
//  println(mailDf.count)
//  for(row <- mailDf.select("uuid").distinct) { println(row) }
//  for(row <- mailDf.select("from").distinct) { println(row) }

}


