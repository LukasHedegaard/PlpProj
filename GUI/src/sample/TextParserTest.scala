package sample

import org.scalatest._

import TextParser.{textParser, CommandTransformer}

class MockCommandTransformer extends CommandTransformer[String] {
    def combine(p: String, q: String): String = {
        s"$p and $q"
    }
    def line(x1: Int, y1: Int, x2:Int, y2:Int): String = {
        s"Line from ($x1, $y1) to ($x2, $y2)"
    }
    def rectangle(x1: Int, y1: Int, x2: Int, y2: Int): String = {
        s"Rectangle from ($x1, $y1) to ($x2, $y2)"
    }
    def circle(x: Int, y: Int, r: Int): String = {
        s"Circle at ($x, $y) with radius $r"
    }
    def textAt(x: Int, y: Int, t: String): String = {
        s"""Text at ($x, $y) with value \"$t\""""
    }
    def boundingBox(x1: Int, y1: Int, x2: Int, y2: Int): String = {
        s"Bounding box from ($x1, $y1) to ($x2, $y2)"
    }
    def boundingBox(x1: Int, y1: Int, x2: Int, y2: Int, rest: String): String = {
        s"Bounding box from ($x1, $y1) to ($x2, $y2) containing $rest"
    }
    def draw(c: String, rest: String): String = {
        s"""Draw color $c on $rest"""
    }
    def fill(c: String, g: String): String = {
        s"Fill color $c on $g"
    }
}


class TextParserTest extends FlatSpec with Matchers {

    "A textParser" should "parse a single BOUNDING-BOX command" in {
        val testParser = textParser(new MockCommandTransformer, println)
        val text =
            """(BOUNDING-BOX (1 2) (21 22))
              |(LINE (1 2)   (21 22))
            """.stripMargin
        val expOutput = "Bounding box from (1, 2) to (21, 22) containing Line from (1, 2) to (21, 22)"
        testParser(text) should contain (expOutput)
    }

    it should "be insensitive to extra spaces in commands and newline between commands" in {
        val testParser = textParser(new MockCommandTransformer)
        val text =
            """
              |(  BOUNDING-BOX   (1 2)  (21 22))
              |
              |(LiNe  (1 2)   (21 22)   )
              |
            """.stripMargin
        val expOutput = "Bounding box from (1, 2) to (21, 22) containing Line from (1, 2) to (21, 22)"
        testParser(text) should contain (expOutput)
    }

    it should "be insensitive insensitive to lower caps" in {
        val testParser = textParser(new MockCommandTransformer)
        val text =
            """(bounding-BoX (1 2) (21 22))
              |(LINE (1 2)   (21 22))
            """.stripMargin
        val expOutput = "Bounding box from (1, 2) to (21, 22) containing Line from (1, 2) to (21, 22)"
        testParser(text) should contain (expOutput)
    }

    it should "parse single iteration commands properly" in {
        val testParser = textParser(new MockCommandTransformer)
        val text =
            """(BOUNDING-BOX (1 2) (21 22))
              |(LINE (1 2) (21 22))
              |(RECTANGLE (1 2) (21 22))
              |(CIRCLE (1 2) 10)
              |(TEXT-AT (1 2) \"Hello World\")
            """.stripMargin
        val expOutput =
            """Bounding box from (1, 2) to (21, 22) containing Line from (1, 2) to (21, 22) and Rectangle from (1, 2) to (21, 22) and Circle at (1, 2) with radius 10 and Text at (1, 2) with value "Hello World""""

        testParser(text) should contain (expOutput)
    }

    it should "parse multi iteration commands properly" in {
        val testParser = textParser(new MockCommandTransformer)
        val text =
            """(BOUNDING-BOX (1 2) (21 22))
              |(DRAW #F123456 (RECTANGLE (1 2) (21 22)))
              |(DRAW #F123456 (RECTANGLE (1 2) (21 22)) (LINE (1 2) (21 22)))
              |(DRAW #F123456 (RECTANGLE (1 2) (21 22)) (LINE (1 2) (21 22)) (TEXT-AT (1 2) \"Hello World\"))
              |(FILL #F123456 (RECTANGLE (1 2) (21 22)))
            """.stripMargin
        val expOutput =
            """Bounding box from (1, 2) to (21, 22) containing Draw color #F123456 on Rectangle from (1, 2) to (21, 22) and Draw color #F123456 on Rectangle from (1, 2) to (21, 22) and Line from (1, 2) to (21, 22) and Draw color #F123456 on Rectangle from (1, 2) to (21, 22) and Line from (1, 2) to (21, 22) and Text at (1, 2) with value "Hello World" and Fill color #F123456 on Rectangle from (1, 2) to (21, 22)"""

        testParser(text) should contain (expOutput)
    }

    it should "throw an error if BOUNDING-BOX isn't the first command" in {
        var errMsg : String = ""
        val onError = (txt: String) => {errMsg = txt}
        val testParser = textParser(new MockCommandTransformer, onError)
        val text =
            """(LINE (1 2)   (21 22))
              |(BOUNDING-BOX (1 2) (21 22))
            """.stripMargin
        testParser(text) shouldBe empty
        errMsg should be (s"ERROR parsing: '$text'")
    }

    it should "throw an error on missing end parenthesis" in {
        var errMsg : String = ""
        val onError = (txt: String) => {errMsg = txt}
        val testParser = textParser(new MockCommandTransformer, onError)
        val text =
            """(BOUNDING-BOX (1 2) (21 22))
              |(LINE (1 2)   (21 22)
            """.stripMargin
        errMsg shouldBe empty
        testParser(text) shouldBe empty
        errMsg should not be empty
    }

    it should "throw an error on missing parenthesis in middle" in {
        var errMsg : String = ""
        val onError = (txt: String) => {errMsg = txt}
        val testParser = textParser(new MockCommandTransformer, onError)
        val text =
            """(BOUNDING-BOX (1 2) (21 22)
              |(LINE (1 2)   (21 22))
            """.stripMargin
        errMsg shouldBe empty
        testParser(text) shouldBe empty
        errMsg should be (s"ERROR parsing: '$text'")
    }

    it should "throw an error on malformed command" in {
        var errMsg : String = ""
        val onError = (txt: String) => {errMsg = txt}
        val testParser = textParser(new MockCommandTransformer, onError)
        val text =
            """(BOUNDING-BOX (1 2) (21 22))
              |(LANE (1 2)  (21 22))
            """.stripMargin
        errMsg shouldBe empty
        testParser(text) shouldBe empty
        errMsg should not be empty
    }

    it should "throw an error on malformed argument" in {
        var errMsg : String = ""
        val onError = (txt: String) => {errMsg = txt}
        val testParser = textParser(new MockCommandTransformer, onError)
        val text =
            """(BOUNDING-BOX (1 2) (21 22))
              |(LINE 42  (21 22))
            """.stripMargin
        errMsg shouldBe empty
        testParser(text) shouldBe empty
        errMsg should not be empty
    }
}


