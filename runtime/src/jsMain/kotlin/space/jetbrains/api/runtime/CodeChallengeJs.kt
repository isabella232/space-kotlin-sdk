package space.jetbrains.api.runtime

import kotlinx.coroutines.await
import org.khronos.webgl.*
import kotlin.js.Promise

private val crypto: dynamic = if (isBrowser) {
    js("((typeof global !== 'undefined') ? global : self)").crypto
} else {
    js("require(\"crypto\")")
}

internal actual suspend fun codeChallengeBytes(codeVerifier: String): Bytes {
    val crypto = crypto
    val codeVerifierBytes = js("new TextEncoder()").encode(codeVerifier).unsafeCast<Uint8Array>()
    val digest = crypto.subtle.digest("SHA-256", codeVerifierBytes)
        .unsafeCast<Promise<ArrayBuffer>>().await()
    return Uint8Array(digest).toBytes()
}

internal actual fun secureRandomBytes(n: Int): Bytes {
    val array = Uint8Array(n)
    crypto.getRandomValues(array)
    return array.toBytes()
}
