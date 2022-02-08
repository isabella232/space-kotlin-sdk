package space.jetbrains.api.runtime

import java.security.MessageDigest
import java.security.SecureRandom

internal actual suspend fun codeChallengeBytes(codeVerifier: String): Bytes {
    val bytes = MessageDigest.getInstance("SHA-256")
        .also { it.update(codeVerifier.toByteArray(Charsets.UTF_8)) }
        .digest()
    return Bytes(bytes)
}

private val secureRandom by lazy { SecureRandom() }

internal actual fun secureRandomBytes(n: Int): Bytes {
    val array = ByteArray(n)
    secureRandom.nextBytes(array)
    return Bytes(array)
}
