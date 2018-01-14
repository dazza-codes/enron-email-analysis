import java.nio.file.{Path, Paths}

object TextFileScript extends App {

  if (args.length == 0) {
    throw new IllegalArgumentException("Input directory is required")
  }
  val root: Path = Paths.get(args(0))
  TextFileStream.directoryWalker(root)

}
