package com.zl.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class MyRedisConfig {
    /*
     * @Bean public RedisTemplate<Object, Object> redisTemplate(
     * RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
     * RedisTemplate<Object, Object> template = new RedisTemplate<>();
     * template.setConnectionFactory(redisConnectionFactory);
     * Jackson2JsonRedisSerializer<Object> serializer = new
     * Jackson2JsonRedisSerializer<>(Object.class); //
     * 设置值（value）的序列化采用FastJsonRedisSerializer。
     * template.setValueSerializer(serializer);
     * template.setHashValueSerializer(serializer); //
     * 设置键（key）的序列化采用StringRedisSerializer。 template.setKeySerializer(new
     * StringRedisSerializer()); template.setHashKeySerializer(new
     * StringRedisSerializer()); template.setDefaultSerializer(serializer);
     * template.afterPropertiesSet(); System.err.println("序列化完成？？"); return
     * template; }
     */


    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(mapper);
        template.setValueSerializer(new StringRedisSerializer());
        template.setKeySerializer(new StringRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }




	/*@Bean
	public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory)
			throws UnknownHostException {
		RedisTemplate<Object, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
		// 设置值（value）的序列化采用FastJsonRedisSerializer。
		//template.setValueSerializer(serializer);
		//template.setHashValueSerializer(serializer);
		// 设置键（key）的序列化采用StringRedisSerializer。
		//template.setKeySerializer(new StringRedisSerializer());
		//template.setHashKeySerializer(new StringRedisSerializer());
		template.setDefaultSerializer(serializer);
		return template;
	}*/

    /*@Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        redisCacheConfiguration.serializeValuesWith(SerializationPair
                .fromSerializer(serializer));
        RedisCacheManager.RedisCacheManagerBuilder cacheManagerBuilder = RedisCacheManager.builder(redisConnectionFactory).cacheDefaults(redisCacheConfiguration);
        RedisCacheManager cacheManager = cacheManagerBuilder.build();
        Map<String, RedisCacheConfiguration> cacheConfigurations = cacheManager.getCacheConfigurations();
        return cacheManager;
    }*/
    //@Bean
    public RedisCacheConfiguration redisCacheConfiguration() {
        //使用Jackson2JsonRedisSerializer反序列化时的转换异常
        Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        //而使用GenericJackson2JsonRedisSerializer就可以避免这种情况。
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        return redisCacheConfiguration.serializeValuesWith(SerializationPair.fromSerializer(new StringRedisSerializer())).serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }

}
