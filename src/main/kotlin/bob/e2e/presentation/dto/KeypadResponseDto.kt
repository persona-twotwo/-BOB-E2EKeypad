package bob.e2e.presentation.dto

import bob.e2e.domain.model.Keypad

data class KeypadResponseDto(
    var id: String,
    val keypadPhoto: String,
    val numberHashArray: List<String>,
) {
    companion object {
        fun from(keypad: Keypad): KeypadResponseDto {
            val numberHashArray = mutableListOf<String>()
            for (i in 0 until 12) {
                if (keypad.orderKey[i] == -1) {
                    numberHashArray.add("")
                } else {
                    numberHashArray.add(keypad.numberHashArray[keypad.orderKey[i]])
                }
            }
            return KeypadResponseDto(
                id = keypad.id,
                numberHashArray = numberHashArray,
                keypadPhoto = keypad.keypadPhoto
            )
        }
    }
}
