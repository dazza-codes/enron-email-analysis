import java.nio.file.{Path, Paths}

/**
  *  Cc & Bcc example:  /data/src/enron_emails/enron_with_categories/1/70706.txt
  *  Forwarded example: /data/src/enron_emails/enron_with_categories/1/9083.txt
  */


object MailParserScript extends App {

  if (args.length == 0) {
    throw new IllegalArgumentException("Input directory or file is required")
  }

  val inPath: Path = Paths.get(args(0))

  if (inPath.toFile.isFile) {
    println(s"Processing file: $inPath")
    val rec: MailRecord = MailParser.recordFromFile(inPath.toString)
    println(rec.toString)
  } else if (inPath.toFile.isDirectory) {
    println(s"Processing directory: $inPath")
    MailParserStream.directoryWalker(inPath)
  } else {
    println(s"Unknown input: $inPath")
  }

}
