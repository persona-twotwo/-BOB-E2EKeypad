package com.config

import org.springframework.context.annotation.Configuration

@Configuration
class RedisConfig {

//    @Bean
//    fun redisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, Any> {
//        val template = RedisTemplate<String, Any>()
//        template.setConnectionFactory(redisConnectionFactory)
//        template.setValueSerializer(GenericToStringSerializer(Any::class.java))
//        return template
//    }
}