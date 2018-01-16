import java.nio.file.Paths
import org.apache.spark.sql.SparkSession

object SparkAnalysisApp {

  def main(args: Array[String]) {

    if (args.length == 0) {
      throw new IllegalArgumentException("Input Parquet file is required")
    }

    val pqPath = Paths.get(args(0)).toAbsolutePath.toString

    val spark = SparkSession.builder.appName("Enron-Email-Analysis").getOrCreate()

    val mailDf = spark.read.parquet(pqPath)

    val email_count = mailDf.count

    println(s"Emails loaded: $email_count")
    spark.stop()
  }

}
