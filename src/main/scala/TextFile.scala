/** Read a text file, safely
  *
  * Derived from https://alvinalexander.com/scala/how-to-open-read-text-files-in-scala-cookbook-examples
  */
object TextFile {

  def using[A <: { def close(): Unit }, B](resource: A)(f: A => B): B =
    try {
      f(resource)
    } finally {
      resource.close()
    }

  def read(filename: String): Option[List[String]] = {
    try {
      val lines = using(io.Source.fromFile(filename)) { source =>
        (for (line <- source.getLines) yield line).toList
      }
      Some(lines)
    } catch {
      case err: Exception => None
    }
  }

  def print(filename: String): Unit = {
    read(filename) foreach { strings =>
      strings.foreach(println)
    }
  }
}
