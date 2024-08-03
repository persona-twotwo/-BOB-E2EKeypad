package bob.e2e.domain.model


data class Keypad (
    val id: String,
    val numberHashArray: List<String>,
    val keypadPhoto: String,
    val orderKey: List<Int>
)