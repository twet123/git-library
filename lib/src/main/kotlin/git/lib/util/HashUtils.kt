package git.lib.util

import java.nio.charset.StandardCharsets
import java.security.MessageDigest

object HashUtils {
    fun calculateSHA1(data: String) : String {
        val digest = MessageDigest.getInstance("SHA-1")
        val hash = digest.digest(data.toByteArray(StandardCharsets.UTF_8))
        return bytesToHex(hash)
    }

    private fun bytesToHex(bytes: ByteArray): String {
        val result = StringBuilder()
        bytes.forEach { byte: Byte ->
            result.append(String.format("%02x"), byte)
        }
        return result.toString()
    }
}