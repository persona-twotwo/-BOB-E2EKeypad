package bob.e2e.presentation.dto

import bob.e2e.domain.model.Keypad

data class KeypadResponseDtoforDebug(
    var id: String,
    val keypadPhoto: String,
    val numberHashArray: List<String>,
    val orderKey: List<Int>,
    val hashArray: List<String>,
) {
    companion object {
        fun from(keypad: Keypad): KeypadResponseDtoforDebug {
            val numberHashArray = mutableListOf<String>()
            for (i in 0 until 12) {
                if (keypad.orderKey[i] == -1) {
                    numberHashArray.add("")
                } else {
                    numberHashArray.add(keypad.numberHashArray[keypad.orderKey[i]])
                }
            }
            var debugString = "ID : ${keypad.id}\n"
            for (i in 0 until 12) {
                debugString += "\t- "+keypad.orderKey[i].toString()+" "+numberHashArray[i]+"\n"
            }
            println(debugString)
            return KeypadResponseDtoforDebug(
                id = keypad.id,
                numberHashArray = numberHashArray,
                keypadPhoto = keypad.keypadPhoto,
                orderKey = keypad.orderKey,
                hashArray = keypad.numberHashArray
            )
        }
    }
}
