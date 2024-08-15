package bob.e2e.presentation.controller

import bob.e2e.domain.service.KeypadService
//import bob.e2e.presentation.dto.KeypadResponseDtoforDebug
import bob.e2e.presentation.dto.KeypadResponseDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RequestMapping("/api/createKeypad")
@RestController
class KeypadController(
    private val keypadService: KeypadService,
) {
    @PostMapping
    fun getKeypads(): ResponseEntity<KeypadResponseDto> {

        return ResponseEntity.status(HttpStatus.OK).body(
            KeypadResponseDto.from(keypadService.createKeypad())
        )
    }

//    fun getKeypads(): ResponseEntity<KeypadResponseDtoforDebug> {
//
//        return ResponseEntity.status(HttpStatus.OK).body(
//            KeypadResponseDtoforDebug.from(keypadService.createKeypad())
//        )
//    }


}