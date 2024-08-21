package bob.e2e.domain.model

import java.time.LocalDateTime

data class Keypad (
    val id: String,
    val numberHashArray: List<String>,
    val keypadPhoto: String,
    val orderKey: List<Int>,
    val expiresAt: LocalDateTime
)