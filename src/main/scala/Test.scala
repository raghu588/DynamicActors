/*
import java.nio.file.Paths

import akka.util.ByteString

object Test {




  def main(args: Array[String]): Unit = {
    FileIO.fromPath(Paths.get("a.csv"))
      .via(Framing.delimiter(ByteString("\n"), 256, true).map(_.utf8String))
      .runForeach(println)
  }
}

*/
