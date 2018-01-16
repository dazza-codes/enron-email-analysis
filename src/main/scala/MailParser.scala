import java.time.{LocalDateTime, ZoneOffset}
import java.time.format.DateTimeFormatter

//
/**
  * runMain MailParserScript "/data/src/enron_emails/enron_with_categories/1/9083.txt"
  *
  * this example email file is a forwarded message with in-line body of previous message
  */


object MailParser {

  val dateFormat = "EEE, d MMM yyyy HH:mm:ss Z (z)"

  def recordFromFile(fileName: String): MailRecord = {
    var uuid = "Message-ID:"
    var from = "From:"
    val to = collection.mutable.ListBuffer.empty[String]
    val cc = collection.mutable.ListBuffer.empty[String]
    val bcc = collection.mutable.ListBuffer.empty[String]
    val body = collection.mutable.ListBuffer.empty[String]
    var dateEpochUTC: Long = 0
    var mailFields = Map[String,String](("aField", "aValue"))
    var mimeVersion = "Mime-Version:"
    var subject = "Subject:"

    var attachment = MailAttachment("fileName", 0, "mimeType", Array(0))
    var attachments = List(attachment)

    val fileContent: Option[List[String]] = TextFile.read(fileName)
    fileContent foreach { content =>
      var line = ""
      val it = content.iterator
      while(it.hasNext) {
        line = it.next().trim
        if(line.startsWith(uuid) && uuid == "Message-ID:") {
          uuid = line.replaceFirst("Message-ID:","").trim

        } else if(line.startsWith("Date:") && dateEpochUTC == 0) {
          val dateStr = line.replaceFirst("Date:","").trim
          val parseFormatter = DateTimeFormatter.ofPattern(dateFormat)
          val dateTime = LocalDateTime.parse(dateStr, parseFormatter)
          dateEpochUTC = dateTime.atOffset(ZoneOffset.UTC).toInstant.toEpochMilli

        } else if(line.startsWith("From:") && from == "From:") {
          from = line.replaceFirst("From:","").trim

        } else if(line.startsWith("To:") && to.isEmpty) {
          val toStr = line.replaceFirst("To:","").trim
          to ++= toStr.split(",").map(_.trim)
          // read lines until the next key is available
          while(line.endsWith(",") && it.hasNext) {
            line = it.next().trim
            to ++= line.split(",").map(_.trim)
          }

        } else if(line.startsWith("Cc:") && cc.isEmpty) {
          val toStr = line.replaceFirst("Cc:","").trim
          cc ++= toStr.split(",").map(_.trim)
          // read lines until the next key is available
          while(line.endsWith(",") && it.hasNext) {
            line = it.next().trim
            cc ++= line.split(",").map(_.trim)
          }
        } else if(line.startsWith("Bcc:") && bcc.isEmpty) {
          val toStr = line.replaceFirst("Bcc:","").trim
          bcc ++= toStr.split(",").map(_.trim)
          // read lines until the next key is available
          while(line.endsWith(",") && it.hasNext) {
            line = it.next().trim
            bcc ++= line.split(",").map(_.trim)
          }
        } else if(line.startsWith("Subject:") && subject == "Subject:") {
          subject = line.replaceFirst("Subject:","").trim
        } else if(line.startsWith("Mime-Version:")) {
          mimeVersion = line.replaceFirst("Mime-Version:","").trim
        } else if(line.startsWith("Content-Type:")) {
          // skip content type
        } else if(line.startsWith("Content-Transfer-Encoding:")) {
          // skip content transfer encoding
        } else if(line.startsWith("X-")) {
          // skip details from the X-{x} headers
//        } else if(line.startsWith("---------------------- Forwarded")) {
//          // gather details from the forwarded message
//          // do not replace any details of the existing message
        } else {
          body += line
        }

      }
    }
    MailRecord(uuid, from, to.toList, cc.toList, bcc.toList, subject, body.mkString("\n"), attachments, dateEpochUTC, mailFields)
  }

}
