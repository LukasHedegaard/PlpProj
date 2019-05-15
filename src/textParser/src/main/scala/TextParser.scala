object TextParser {

    /** Interface for CommandTransformers
      *
      * A class with this trait can be used to create a textParser.
      *
      * @see textParser
      */
    trait CommandTransformer[T] {
        /** Combines the result of two commands */
        def combine(p: T, q: T): T

        def line(x1: Int, y1: Int, x2: Int, y2: Int): T

        def rectangle(x1: Int, y1: Int, x2: Int, y2: Int): T

        def circle(x: Int, y: Int, r: Int): T

        def textAt(x: Int, y: Int, t: String): T

        def boundingBox(x1: Int, y1: Int, x2: Int, y2: Int): T

        def boundingBox(x1: Int, y1: Int, x2: Int, y2: Int, rest: T): T

        def draw(c: String, rest: T): T

        def fill(c: String, g: T): T
    }

    /** Creates a textParser for a specific CommandTransformer
      *
      * @param transformer is called with commands matching the parsed text. @see CommandTransformer
      * @param onError     is a called with an error message in case the text didn't parse correctly
      * @return a function which parses a text String and returns a value transformed according to the transformer.
      */
    def textParser[T](transformer: CommandTransformer[T], onError: String => Unit = println)
    : String => Option[T] = {
        text: String => {
            try {
                cleanText(text) match {
                    case boundingBoxRestRgx(x1, y1, x2, y2, rest) =>
                        Some(transformer.boundingBox( x1.toInt
                                                    , y1.toInt
                                                    , x2.toInt
                                                    , y2.toInt
                                                    , parseCommandList(rest, transformer)
                            )
                        )
                    case default =>
                        throw new InvalidCommandException(text)
                }
            } catch {
                case e: InvalidCommandException =>
                    onError(s"ERROR parsing: '${e.getMessage}'")
                    None
            }
        }
    }


    class InvalidCommandException(msg: String) extends Exception(msg) {}


    private def cleanText(txt: String): String = {
        txt.replaceAll("""([^\w\s()#,".-]*)""", "")
    }


    private def parseCommandList[T](text: String, transformer: CommandTransformer[T]): T = {
        listRgx.findAllIn(text)
            .map(t => parseCommand(t, transformer))
            .reduce(transformer.combine)
    }


    private def parseCommand[T](text: String, transformer: CommandTransformer[T]): T = text match {
        case lineRgx(x1, y1, x2, y2) =>
            transformer.line(x1.toInt, y1.toInt, x2.toInt, y2.toInt)

        case rectangleRgx(x1, y1, x2, y2) =>
            transformer.rectangle(x1.toInt, y1.toInt, x2.toInt, y2.toInt)

        case circleRgx(x, y, r) =>
            transformer.circle(x.toInt, y.toInt, r.toInt)

        case textAtRgx(x, y, t) =>
            transformer.textAt(x.toInt, y.toInt, t)

        case drawRgx(c, rest) =>
            transformer.draw(c, parseCommand(rest, transformer))

        case fillRgx(c, g) =>
            transformer.fill(c, parseCommand(g, transformer))

        case cmdRestRgx(cmd, arg1, arg2, rest) =>
            transformer.combine( parseCommand(s"($cmd $arg1 $arg2)", transformer)
                               , parseCommand(rest, transformer)
                               )

        case default =>
            throw new InvalidCommandException(text)
    }

    // String patterns
    private val nameRgx = """([\w-]+)""".r
    private val argRgx = """(\([^()]+\)|[^ ]+)""".r
    private val pointRgx = """\((\d+)\s+(\d+)\)""".r
    private val numRgx = """(\d+)""".r
    private val stringRgx = "\"(.*)\"".r
    private val colorRgx = """(#[0-9A-F]{6})""".r
    private val restRgx = """([\s\S]+)""".r
    private val cmdRgx = s"""\\(\\s*$nameRgx\\s+$argRgx\\s+$argRgx\\s*\\)""".r
    private val cmdRestRgx = s"""\\(\\s*$nameRgx\\s+$argRgx\\s+$argRgx\\s*\\)\\s*$restRgx""".r
    private val listRgx = """\(([^\n]+)\)""".r

    private val lineRgx = s"""(?i)\\(\\s*LINE\\s+$pointRgx\\s+$pointRgx\\s*\\)""".r
    private val rectangleRgx = s"""(?i)\\(\\s*RECTANGLE\\s+$pointRgx\\s+$pointRgx\\s*\\)""".r
    private val circleRgx = s"""(?i)\\(\\s*CIRCLE\\s+$pointRgx\\s+$numRgx\\s*\\)""".r
    private val textAtRgx = s"""(?i)\\(\\s*TEXT-AT\\s+$pointRgx\\s+$stringRgx\\s*\\)""".r
    private val boundingBoxRgx = s"""(?i)\\(\\s*BOUNDING-BOX\\s+$pointRgx\\s+$pointRgx\\s*\\)""".r
    private val boundingBoxRestRgx = s"""(?i)\\s*\\(\\s*BOUNDING-BOX\\s+$pointRgx\\s+$pointRgx\\s*\\)\\s*$restRgx""".r
    private val drawRgx = s"""(?i)\\(\\s*DRAW\\s+$colorRgx\\s+$restRgx\\s*\\)""".r
    private val fillRgx = s"""(?i)\\(\\s*FILL\\s+$colorRgx\\s+$restRgx\\s*\\)""".r

}
