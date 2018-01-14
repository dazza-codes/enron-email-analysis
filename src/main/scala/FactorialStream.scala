
import java.nio.file.Paths

import akka.stream._
import akka.stream.scaladsl._

import akka.{ NotUsed, Done }
import akka.actor.ActorSystem
import akka.util.ByteString

import scala.concurrent._
import scala.concurrent.duration._

object FactorialStream extends App {

  implicit val system: ActorSystem = ActorSystem("FactorialStream")
  implicit val ec: ExecutionContextExecutor = system.dispatcher
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val source: Source[Int, NotUsed] = Source(1 to 100)
  val factorials = source.scan(BigInt(1))((acc, next) => acc * next)

//  val result: Future[IOResult] =
//    factorials
//      .map(num => ByteString(s"$num\n"))
//      .runWith(FileIO.toPath(Paths.get("factorials.txt")))

//  def lineSink(filename: String): Sink[String, Future[IOResult]] =
//    Flow[String]
//      .map(s ⇒ ByteString(s + "\n"))
//      .toMat(FileIO.toPath(Paths.get(filename)))(Keep.right)
//  val result: Future[IOResult] =
//    factorials
//      .map(_.toString)
//      .runWith(lineSink("factorials.txt"))

  val result: Future[Done] =
    factorials
      .zipWith(Source(0 to 100))((num, idx) ⇒ s"$idx! = $num")
      .throttle(1, 1.second, 1, ThrottleMode.shaping)
      .runForeach(println)

  result.onComplete(_ => system.terminate())
}
