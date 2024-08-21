package bob.e2e.presentation.controller

import bob.e2e.domain.service.KeypadService
import bob.e2e.presentation.dto.KeypadRequestDto
import bob.e2e.presentation.dto.KeypadResponseDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/keypad")
@RestController
class KeypadController(
    private val keypadService: KeypadService,
) {
    @PostMapping("/create")
    fun getKeypads(): ResponseEntity<KeypadResponseDto> {
        return ResponseEntity.status(HttpStatus.OK).body(
            KeypadResponseDto.from(keypadService.createKeypad())
        )
    }

    @PostMapping("/input")
    fun processKeypadInput(@RequestBody requestDto: KeypadRequestDto): ResponseEntity<String> {
        // DTO를 EncKeypad로 변환
        val encKeypad = requestDto.toEntity()
        // 서비스 레이어로 EncKeypad 전달
        val decryptStr = keypadService.processKeypadInput(encKeypad)
        return ResponseEntity.status(HttpStatus.OK).body(decryptStr)
    }
}
