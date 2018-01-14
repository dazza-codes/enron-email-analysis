import com.sksamuel.avro4s.AvroSchema
import org.apache.avro.Schema

case class MailRecord(uuid: String,
                      from: String,
                      to: List[String],
                      cc: List[String],
                      bcc: List[String],
                      subject: String,
                      body: String,
                      attachments: List[MailAttachment],
                      dateUtcEpoch: Long,
                      mailFields: Map[String,String]
                      ) {

  val schema: Schema = AvroSchema[MailRecord]
}
