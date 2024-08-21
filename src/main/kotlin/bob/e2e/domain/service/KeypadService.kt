package bob.e2e.domain.service

import bob.e2e.domain.model.EncKeypad
import bob.e2e.domain.model.Keypad
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*
import javax.imageio.ImageIO

@Service
class KeypadService {

    // 간단한 인메모리 저장소 (키패드 ID를 키로, Keypad 객체를 값으로 저장)
    private val keypadStore: MutableMap<String, Keypad> = mutableMapOf()

    private val numbers = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, -1, -1)

    private val imagesPath: Map<Int, String> = mapOf(
        0 to "_0.png",
        1 to "_1.png",
        2 to "_2.png",
        3 to "_3.png",
        4 to "_4.png",
        5 to "_5.png",
        6 to "_6.png",
        7 to "_7.png",
        8 to "_8.png",
        9 to "_9.png",
        -1 to "_blank.png"
    )

    fun createKeypadPhoto(order: List<Int>): String {
        val images = order.map { number ->
            val path = ("keypad/" + imagesPath[number]) ?: throw IllegalArgumentException("Invalid number: $number")
            val resource = ClassPathResource(path)
            ImageIO.read(resource.inputStream)
        }

        val imageWidth = images[0].width
        val imageHeight = images[0].height

        val mergedWidth = imageWidth * 4
        val mergedHeight = imageHeight * 3

        val mergedImage = BufferedImage(mergedWidth, mergedHeight, BufferedImage.TYPE_INT_ARGB)
        val g2d: Graphics2D = mergedImage.createGraphics()

        for (i in images.indices) {
            val left = (i % 4) * imageWidth
            val top = (i / 4) * imageHeight
            g2d.drawImage(images[i], left, top, null)
        }
        g2d.dispose()

        val byteArrayOutputStream = ByteArrayOutputStream()
        ImageIO.write(mergedImage, "png", byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.getEncoder().encodeToString(byteArray)
    }

    fun createKeypad(): Keypad {
        // numbers 리스트를 섞습니다.
        val orderKey = numbers.shuffled()
        // Keypad 객체를 생성합니다.
        val keypad = Keypad(
            id = UUID.randomUUID().toString(),
            orderKey = orderKey, // 섞인 리스트를 사용합니다.
            numberHashArray = List(10) {
                UUID.randomUUID().toString()
            },
            keypadPhoto = createKeypadPhoto(orderKey),
            expiresAt = LocalDateTime.now().plus(5, ChronoUnit.MINUTES)
        )

        // 생성된 키패드를 인메모리 저장소에 저장합니다.
        keypadStore[keypad.id] = keypad

        return keypad
    }

    fun processKeypadInput(encKeypad: EncKeypad): String {
        // 클라이언트로부터 전송된 데이터를 콘솔에 출력
        println("Received encrypted ID: ${encKeypad.id}")
        println("Received encrypted data: ${encKeypad.encryptedData}")

        // 인메모리 저장소에서 해당 키패드 ID로 데이터를 조회합니다.
        val storedKeypad = keypadStore[encKeypad.id]

        return if (storedKeypad != null) {
            println(LocalDateTime.now().toString())
            println(storedKeypad.expiresAt)
            if (LocalDateTime.now().isAfter(storedKeypad.expiresAt)){
                "ERROR - KEypad has expired"
            }else{
                // List<String>을 Map<String, String>으로 변환
                val mapRepresentation = storedKeypad.numberHashArray.withIndex().associate { (index, value) ->
                    index.toString() to value
                }
                // 외부 서버에 데이터 전송 및 응답 수신
                val response = sendToExternalServer(encKeypad.encryptedData, mapRepresentation)
//            println("Stored numberHashArray as Map: $mapRepresentation")
                response
            }

        } else {
//            println("No matching Keypad found for ID: ${encKeypad.id}")
            "ERROR - No matching Keypad found"
        }
    }

    private fun sendToExternalServer(userInput: String, keyHashMap: Map<String, String>): String {
        val url = URL("http://146.56.119.112:8081/auth")
        val connection = url.openConnection() as HttpURLConnection

        connection.requestMethod = "POST"
        connection.doOutput = true
        connection.setRequestProperty("Content-Type", "application/json; utf-8")
        connection.setRequestProperty("Accept", "application/json")

        val requestPayload = """{
            "userInput": "$userInput",
            "keyHashMap": ${keyHashMap.map { "\"${it.key}\":\"${it.value}\"" }.joinToString(", ", "{", "}") }
        }""".trimIndent()

        connection.outputStream.use { os ->
            val input = requestPayload.toByteArray(StandardCharsets.UTF_8)
            os.write(input, 0, input.size)
        }

        val response = connection.inputStream.bufferedReader().use { it.readText() }
        return response
    }
}
