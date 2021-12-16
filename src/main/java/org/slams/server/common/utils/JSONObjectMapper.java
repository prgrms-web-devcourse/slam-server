package org.slams.server.common.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.Map;

public class JSONObjectMapper {

	private static ObjectMapper objectMapper = customObjectMapper();

	public static <T> String toJson(T data) {
		if(data == null) return null;
		try {
			return objectMapper().valueToTree(data).toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> T fromJson(String json, Class<T> classType) {
		if (json == null) return null;
		try {
			return objectMapper().treeToValue(objectMapper().readTree(json), classType);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> T fromJson(Map<String, Object> json, Class<T> classType) {
		if (json == null) return null;
		try {
			return objectMapper().convertValue(json, classType);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static ObjectMapper objectMapper() {
		return objectMapper;
	}

	private static ObjectMapper customObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		return objectMapper;
	}

}