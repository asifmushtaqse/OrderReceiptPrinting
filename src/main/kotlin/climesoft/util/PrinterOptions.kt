package climesoft.util

class PrinterOptions {
    var commandSet = ""
    fun initialize(): String {
        val Init = byteArrayOf(27, 64)
        commandSet += String(Init)
        return String(Init)
    }

    fun chooseFont(Options: Int): String {
        var s = ""
        val ChooseFontA = byteArrayOf(27, 77, 0)
        val ChooseFontB = byteArrayOf(27, 77, 1)
        val ChooseFontC = byteArrayOf(27, 77, 48)
        val ChooseFontD = byteArrayOf(27, 77, 49)
        s = when (Options) {
            1 -> String(ChooseFontA)
            2 -> String(ChooseFontB)
            3 -> String(ChooseFontC)
            4 -> String(ChooseFontD)
            else -> String(ChooseFontB)
        }
        commandSet += s
        return s
    }

    fun feedBack(lines: Byte): String {
        val Feed = byteArrayOf(27, 101, lines)
        val s = String(Feed)
        commandSet += s
        return s
    }

    fun feed(lines: Byte): String {
        val Feed = byteArrayOf(27, 100, lines)
        val s = String(Feed)
        commandSet += s
        return s
    }

    fun alignLeft(): String {
        val AlignLeft = byteArrayOf(27, 97, 48)
        val s = String(AlignLeft)
        commandSet += s
        return s
    }

    fun alignCenter(): String {
        val AlignCenter = byteArrayOf(27, 97, 49)
        val s = String(AlignCenter)
        commandSet += s
        return s
    }

    fun alignRight(): String {
        val AlignRight = byteArrayOf(27, 97, 50)
        val s = String(AlignRight)
        commandSet += s
        return s
    }

    fun newLine(): String {
        val LF = byteArrayOf(10)
        val s = String(LF)
        commandSet += s
        return s
    }

    fun reverseColorMode(enabled: Boolean): String {
        val ReverseModeColorOn = byteArrayOf(29, 66, 1)
        val ReverseModeColorOff = byteArrayOf(29, 66, 0)
        var s = ""
        s = if (enabled) String(ReverseModeColorOn) else String(ReverseModeColorOff)
        commandSet += s
        return s
    }

    fun doubleStrik(enabled: Boolean): String {
        val DoubleStrikeModeOn = byteArrayOf(27, 71, 1)
        val DoubleStrikeModeOff = byteArrayOf(27, 71, 0)
        var s = ""
        s = if (enabled) String(DoubleStrikeModeOn) else String(DoubleStrikeModeOff)
        commandSet += s
        return s
    }

    fun doubleHeight(enabled: Boolean): String {
        val DoubleHeight = byteArrayOf(27, 33, 17)
        val UnDoubleHeight = byteArrayOf(27, 33, 0)
        var s = ""
        s = if (enabled) String(DoubleHeight) else String(UnDoubleHeight)
        commandSet += s
        return s
    }

    fun emphasized(enabled: Boolean): String {
        val EmphasizedOff = byteArrayOf(27, 0)
        val EmphasizedOn = byteArrayOf(27, 1)
        var s = ""
        s = if (enabled) String(EmphasizedOn) else String(EmphasizedOff)
        commandSet += s
        return s
    }

    fun underLine(Options: Int): String {
        val UnderLine2Dot = byteArrayOf(27, 45, 50)
        val UnderLine1Dot = byteArrayOf(27, 45, 49)
        val NoUnderLine = byteArrayOf(27, 45, 48)
        var s = ""
        s = when (Options) {
            0 -> String(NoUnderLine)
            1 -> String(UnderLine1Dot)
            else -> String(UnderLine2Dot)
        }
        commandSet += s
        return s
    }

    fun color(Options: Int): String {
        val ColorRed = byteArrayOf(27, 114, 49)
        val ColorBlack = byteArrayOf(27, 114, 48)
        var s = ""
        s = when (Options) {
            0 -> String(ColorBlack)
            1 -> String(ColorRed)
            else -> String(ColorBlack)
        }
        commandSet += s
        return s
    }

    fun finit(): String {
        val FeedAndCut = byteArrayOf(29, 'V'.toByte(), 66, 0)
        var s = String(FeedAndCut)
        val DrawerKick = byteArrayOf(27, 70, 0, 60, 120)
        s += String(DrawerKick)
        commandSet += s
        return s
    }

    fun addLineSeperator(): String {
        val lineSpace = "------------------------------------------"
        commandSet += lineSpace
        return lineSpace
    }

    fun resetAll() {
        commandSet = ""
    }

    fun setText(s: String) {
        commandSet += s
    }

    fun finalCommandSet(): String {
        return commandSet
    }
}