import java.nio.file.Paths

import akka.stream._
import akka.stream.scaladsl._

import akka.{ NotUsed, Done }
import akka.actor.ActorSystem
import akka.util.ByteString

import scala.concurrent._
import scala.concurrent.duration._

object ReadStream extends App {

  implicit val system: ActorSystem = ActorSystem("ReadStream")
  implicit val ec: ExecutionContextExecutor = system.dispatcher
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val source: Source[Int, NotUsed] = Source(1 to 100)

  val done: Future[Done] = source.runForeach(i => println(i))(materializer)

  done.onComplete(_ => system.terminate())

  /*
  import actorSystem.dispatcher

  // read lines from a log file
  val logFile = new File("src/main/resources/log.txt")

  val source = Source.synchronousFile(logFile)

  // parse chunks of bytes into lines
  val flow = Framing.delimiter(ByteString(System.lineSeparator()),
    maximumFrameLength = 512,
    allowTruncation = true).map(_.utf8String)

  val sink = Sink.foreach(println)

  source.via(flow).runWith(sink).andThen {
     case _ =>
      actorSystem.shutdown()
      actorSystem.awaitTermination()
  }
  */
}

