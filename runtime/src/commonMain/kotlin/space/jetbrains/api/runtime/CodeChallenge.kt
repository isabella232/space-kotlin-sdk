package space.jetbrains.api.runtime

internal expect fun secureRandomBytes(n: Int): Bytes

internal expect suspend fun codeChallengeBytes(codeVerifier: String): Bytes

internal suspend fun codeChallenge(codeVerifier: String): String {
    return base64UrlSafe(codeChallengeBytes(codeVerifier))
}