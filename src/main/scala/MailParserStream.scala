import java.nio.file.Path

import akka.actor.ActorSystem
import akka.stream._
import akka.stream.alpakka.file.scaladsl.Directory
import akka.stream.scaladsl._
import akka.{Done, NotUsed}

import scala.concurrent._

object MailParserStream {

  def directoryWalker(root: Path): Unit = {
    implicit val system: ActorSystem = ActorSystem("MailParserStream")
    implicit val ec: ExecutionContextExecutor = system.dispatcher
    implicit val materializer: ActorMaterializer = ActorMaterializer()

    try {
      val paths: Source[Path, NotUsed] = Directory.walk(root)
      val files: Source[Path, NotUsed] = paths.filter(_.toFile.isFile)
      val txtFiles: Source[Path, NotUsed] = files.filter(_.toFile.getName.endsWith(".txt"))
      val txtFileNames: Source[String, NotUsed] = txtFiles.map(_.toString)
      val result: Future[Done] =
        txtFileNames
          .runForeach(fileName => {
            val mailRecord = MailParser.recordFromFile(fileName)
            println(mailRecord.toString)
          })
      result.onComplete(_ => system.terminate())
    } catch {
      case err: Exception => {
        println("\nERROR:\t" + err.getLocalizedMessage)
        system.terminate()
      }
    }
  }

}
