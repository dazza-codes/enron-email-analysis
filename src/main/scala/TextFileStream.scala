import java.io.File
import java.nio.file.Path
import java.nio.file.Paths

import akka.stream.alpakka.file.scaladsl.Directory

import akka.stream._
import akka.stream.scaladsl._
import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.util.ByteString

import scala.concurrent._
import scala.concurrent.duration._

object TextFileStream {

  def directoryWalker(root: Path): Unit = {
    implicit val system: ActorSystem = ActorSystem("TextFileStream")
    implicit val ec: ExecutionContextExecutor = system.dispatcher
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    try {
      val paths: Source[Path, NotUsed] = Directory.walk(root)
      val files: Source[Path, NotUsed] = paths.filter(_.toFile.isFile)
      val txtFiles: Source[Path, NotUsed] = files.filter(_.toFile.getName.endsWith(".txt"))
      val txtFileNames: Source[String, NotUsed] = txtFiles.map(_.toString)
      val result: Future[Done] =
        txtFileNames
          .runForeach(TextFile.print)
      result.onComplete(_ => system.terminate())
    } catch {
      case err: Exception => {
        println("\nERROR:\t" + err.getLocalizedMessage)
        system.terminate()
      }
    }
  }

}
