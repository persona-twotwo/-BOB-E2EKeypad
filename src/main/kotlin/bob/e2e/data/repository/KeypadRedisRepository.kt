//package bob.e2e.data.repository
//
//import bob.e2e.domain.model.Keypad
//import org.springframework.data.redis.core.RedisTemplate
//import org.springframework.stereotype.Repository
//
//@Repository
//class KeypadRedisRepository(
//    private val redisTemplate: RedisTemplate<String, Any>
//) {
//
//
//    fun insert(keypad: Keypad) {
//        val key = keypad.id
//        println("Inserting Keypad with key: $key")
//        val data = keypad.orderKey
//        redisTemplate.opsForValue().set(key, data)
//
//    }
//
//    fun selectBy(id: String): List<Int>{
//
//        val data = redisTemplate.opsForValue().get(id)
//        return data as List<Int>
//    }
//
//}