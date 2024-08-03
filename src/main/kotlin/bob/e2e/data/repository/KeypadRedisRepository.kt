package bob.e2e.data.repository

import bob.e2e.domain.model.Keypad
import org.springframework.stereotype.Repository

@Repository
class KeypadRedisRepository : KeypadRepository {
    override fun insert(keypad: Keypad) {
        TODO("Not yet implemented")
    }

    override fun selectBy(id: String): Keypad {
        TODO()
    }

}