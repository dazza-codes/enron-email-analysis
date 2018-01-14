import com.sksamuel.avro4s.AvroSchema
import org.apache.avro.Schema

case class MailAttachment(fileName: String,
                          size: Int,
                          mimeType: String,
                          data: Array[Byte]
                         ) {

  val schema: Schema = AvroSchema[MailRecord]
}
