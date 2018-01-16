import java.io.File
import java.nio.file.Path

import akka.actor.ActorSystem
import akka.stream._
import akka.stream.alpakka.file.scaladsl.Directory
import akka.stream.scaladsl._
import akka.{Done, NotUsed}
import com.sksamuel.avro4s.{AvroDataOutputStream, AvroOutputStream}

import scala.concurrent._

case class MailRecordsAvroStream(path: Path) {

  val avroOutputFile = new File("enron_email_records.avro")
  val avroOutputStream: AvroDataOutputStream[MailRecord] = AvroOutputStream.data[MailRecord](avroOutputFile)

  def directoryWalker(): Unit = {
    implicit val system: ActorSystem = ActorSystem("MailRecordsAvroStream")
    implicit val ec: ExecutionContextExecutor = system.dispatcher
    implicit val materializer: ActorMaterializer = ActorMaterializer()

    try {
      val paths: Source[Path, NotUsed] = Directory.walk(path)
      val files: Source[Path, NotUsed] = paths.filter(_.toFile.isFile)
      val txtFiles: Source[Path, NotUsed] = files.filter(_.toFile.getName.endsWith(".txt"))
      val txtFileNames: Source[String, NotUsed] = txtFiles.map(_.toString)
      val result: Future[Done] =
        txtFileNames
          .runForeach(appendAvroFile)
      result.onComplete(_ => {
        system.terminate()
        avroOutputStream.close()
      })
    } catch {
      case err: Exception => {
        println("\nERROR:\t" + err.getLocalizedMessage)
        system.terminate()
        avroOutputStream.close()
      }
    }
  }

  def appendAvroFile(fileName: String): Unit = {
    val mailRecord = MailParser.recordFromFile(fileName)
    avroOutputStream.write(Seq(mailRecord))
    avroOutputStream.flush()
  }

}
