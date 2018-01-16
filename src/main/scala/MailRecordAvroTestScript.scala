import java.io.{ByteArrayInputStream, ByteArrayOutputStream, File}

import com.sksamuel.avro4s.{AvroInputStream, AvroOutputStream}

object MailRecordAvroTestScript extends App {

  val source = "FileName"
  val uuid = "UUID"
  val from = "fromUser"
  val to = List("toUserA", "toUserB")
  val cc = List("ccUserA", "ccUserB")
  val bcc = List("bccUserA", "bccUserB")
  val subject = "Subject"
  val body = "Body"
  val dateUtcEpoch = 1976.toLong
  val mailFields = Map[String,String](("aField", "aValue"))

  val attachment = MailAttachment("fileName", 0, "mimeType", Array(0))
  val attachments = List(attachment)

  val rec = MailRecord(source, uuid, from, to, cc, bcc, subject, body, attachments, dateUtcEpoch, mailFields)

  /**
    * https://github.com/sksamuel/avro4s#serializing
    */
  val os = AvroOutputStream.data[MailRecord](new File("mail_records.avro"))
  os.write(Seq(rec))
  os.flush()
  os.close()

  val is = AvroInputStream.data[MailRecord](new File("mail_records.avro"))
  val records = is.iterator.toSet
  is.close()
  println(records.mkString("\n"))

  /**
    * https://github.com/sksamuel/avro4s#binary-serializing
    */
  val baos = new ByteArrayOutputStream()
  val output = AvroOutputStream.binary[MailRecord](baos)
  output.write(rec)
  output.close()
  val serializedBytes: Array[Byte] = baos.toByteArray

  val in = new ByteArrayInputStream(serializedBytes)
  val input = AvroInputStream.binary[MailRecord](in)
  val result = input.iterator.toSeq
  //  result shouldBe Vector(rec)
  println(records.mkString("\n"))

  /**
    * https://dzone.com/articles/kafka-avro-scala-example
    */


}

