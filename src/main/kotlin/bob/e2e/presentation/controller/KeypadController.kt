package bob.e2e.presentation.controller

import bob.e2e.domain.model.Keypad
import bob.e2e.domain.service.KeypadService
import bob.e2e.presentation.dto.KeypadResponseDto
import bob.e2e.presentation.dto.KeypadResponseDtoforDebug
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RequestMapping("/createKeypads")
@RestController
class KeypadController(
    private val keypadService: KeypadService,
) {
    @GetMapping
//    fun getKeypads(): KeypadResponseDto {
//
//        return KeypadResponseDto.from(keypadService.createKeypad())
//    }

    fun getKeypads(): KeypadResponseDtoforDebug {

        return KeypadResponseDtoforDebug.from(keypadService.createKeypad())
    }
}