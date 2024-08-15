package bob.e2e.domain.service

import bob.e2e.domain.model.Keypad
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.util.*
import javax.imageio.ImageIO

@Service
class KeypadService(
//    private val redisTemplate: RedisTemplate<String, Any>
) {
    private var id = 0
    private val numbers = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, -1, -1)

    private val imagesPath : Map<Int,String> = mapOf(
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

    fun createKeypadPhoto(order:List<Int>):String{
        val images = order.map{ number ->
            val path = ("keypad/"+imagesPath[number]) ?: throw IllegalArgumentException("Invalid number: $number")
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
            val left = (i%4) * imageWidth
            val top = (i/4) * imageHeight
            g2d.drawImage(images[i],left,top,null)
        }
        g2d.dispose()

        val byteArrayOutputSream = ByteArrayOutputStream()
        ImageIO.write(mergedImage, "png", byteArrayOutputSream)
        val byteArray = byteArrayOutputSream.toByteArray()
        return Base64.getEncoder().encodeToString(byteArray)

    }

    fun createKeypad(): Keypad {
        // numbers 리스트를 섞습니다.
        val orderKey = numbers.shuffled()
        // Keypad 객체를 생성합니다.
        val keypad = Keypad(
            id = UUID.randomUUID().toString(),
            orderKey = orderKey, // 섞인 리스트를 사용합니다.
            numberHashArray = List(10){UUID.randomUUID().toString()},
            keypadPhoto = createKeypadPhoto(orderKey)
        )

        // KeypadResponseDto 객체를 반환합니다.
        return keypad
    }
}
