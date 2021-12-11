import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String, day: String) = File("src/$day", "$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

val String.asDecimal: Int
    get() = Integer.valueOf(this, 2)

val String.binaryInvert: String get() = map { if (it == '0') '1' else '0' }.joinToString(separator = "")

val List<String>.asIntList get() = if (any { it.contains(",") }) {
    flatMap { it.split(',') }.map { it.toInt() }
} else {
    map { it.toInt() }
}

val Char.bitFlip: Char get() = if (this == '1') '0' else '1'

val Char.toInt: Int get() = toString().toInt()