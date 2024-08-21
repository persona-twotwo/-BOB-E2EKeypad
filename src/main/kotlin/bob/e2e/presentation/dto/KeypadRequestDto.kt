package bob.e2e.presentation.dto

import bob.e2e.domain.model.EncKeypad

data class KeypadRequestDto(
    val id: String,
    val encStr: String
) {
    fun toEntity(): EncKeypad {
        return EncKeypad(
            id = id,
            encryptedData = encStr
        )
    }
}
