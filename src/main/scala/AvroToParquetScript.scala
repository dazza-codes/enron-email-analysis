import java.io.File
import java.nio.file.Paths

import com.sksamuel.avro4s.{AvroInputStream, AvroSchema, RecordFormat}
import org.apache.avro.Schema
import org.apache.avro.generic.GenericRecord
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.parquet.avro.AvroParquetWriter

//import io.eels.component.avro.AvroSource
//import io.eels.component.parquet.ParquetSink


object AvroToParquetScript extends App {

  implicit val conf: Configuration = new Configuration()
  implicit val hadoopFileSystem: FileSystem = FileSystem.get(conf)

  if (args.length < 2) {
    throw new IllegalArgumentException("Input / Output file paths are required")
  }

  val avPath = Paths.get(args(0)).toAbsolutePath.toString
  val avroFilePath = new org.apache.hadoop.fs.Path(s"file://$avPath")

  val pqPath = Paths.get(args(1)).toAbsolutePath.toString
  val parquetFilePath = new org.apache.hadoop.fs.Path(s"file://$pqPath")

  // In testing this io.eels code, it threw an exception
  // org.apache.parquet.io.ParquetEncodingException: empty fields are illegal, the field should be ommited completely instead
//  val source = AvroSource(avroFilePath)
//  val sink = ParquetSink(parquetFilePath)
//  source.toDataStream().to(sink)

  val schema: Schema = AvroSchema[MailRecord]
  val format: RecordFormat[MailRecord] = RecordFormat[MailRecord]

  val parquetWriter = new AvroParquetWriter[GenericRecord](parquetFilePath, schema)

  val avroInputStream = AvroInputStream.data[MailRecord](new File(avPath))
  val emailIterator = avroInputStream.iterator
  emailIterator foreach { record =>
    val genericRecord = format.to(record)
    parquetWriter.write(genericRecord)
  }
  avroInputStream.close()
  parquetWriter.close()


  // To read the parquet file

//  val reader = AvroParquetReader.builder[GenericRecord](parquetFilePath).build().asInstanceOf[ParquetReader[GenericRecord]]
//  // iter is now an Iterator[MailRecord]
//  val iter = Iterator.continually(reader.read).takeWhile(_ != null).map(format.from)
//  // and list is now a List[MailRecord]
//  val list = iter.toList

}
