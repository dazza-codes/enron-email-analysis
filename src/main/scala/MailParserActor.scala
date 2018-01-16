import MailParserActor.MailRecordPrint
import akka.actor.{Actor, Props}

object MailParserActor {

  sealed trait MailParserActorMsg

  case class MailRecordPrint(fileName: String) extends MailParserActorMsg

  def props: Props = Props[MailParserActor]
}

class MailParserActor extends Actor {

  def receive: PartialFunction[Any, Unit] = {
    case MailRecordPrint(fileName) =>
      println("MailParserActor to print $fileName")
      println(mailRecord(fileName).toString)
    case _ =>
      println("MailParserActor received unknown message")
  }

  def mailRecord(fileName: String): MailRecord = {
    MailParser.recordFromFile(fileName)
  }

}
