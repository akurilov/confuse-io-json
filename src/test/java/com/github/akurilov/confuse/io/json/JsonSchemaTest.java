package com.github.akurilov.confuse.io.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.akurilov.confuse.Config;
import com.github.akurilov.confuse.SchemaProvider;
import com.github.akurilov.confuse.exceptions.InvalidValueTypeException;
import com.github.akurilov.confuse.impl.BasicConfig;
import org.junit.Test;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class JsonSchemaTest {

	private static ObjectMapper objectMapper(final Map<String, Object> schema)
	throws NoSuchMethodException {
		final Module module = new SimpleModule()
			.addDeserializer(
				BasicConfig.class, new ConfigJsonDeserializer(BasicConfig.class, "-", schema)
			);
		return new ObjectMapper()
			.registerModule(module)
			.enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
			.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY)
			.configure(JsonParser.Feature.ALLOW_COMMENTS, true)
			.configure(JsonParser.Feature.ALLOW_YAML_COMMENTS, true);
	}

	@Test
	public final void testResolvedSchemaContent()
	throws Exception {
		final Map<String, Object> schema = SchemaProvider.resolve(
			"test", getClass().getClassLoader()
		);
		assertNotNull(schema);
		assertEquals(Object.class, ((Map<String, Class>) schema.get("objects")).get("any"));
		assertEquals(String.class, ((Map<String, Class>) schema.get("objects")).get("aString"));
		assertEquals(List.class, ((Map<String, Class>) schema.get("objects")).get("aList"));
		assertEquals(Map.class, ((Map<String, Class>) schema.get("objects")).get("aMap"));
		assertEquals(boolean.class, ((Map<String, Class>) schema.get("primitives")).get("aBoolean"));
		assertEquals(int.class, ((Map<String, Class>) schema.get("primitives")).get("anInt"));
		assertEquals(long.class, ((Map<String, Class>) schema.get("primitives")).get("aLong"));
		assertEquals(double.class, ((Map<String, Class>) schema.get("primitives")).get("aDouble"));
	}

	@Test
	public final void testResolvedSchemaConfigMatches()
	throws Exception {
		final Map<String, Object> schema = SchemaProvider.resolve(
			"test", getClass().getClassLoader()
		);
		try(
			final InputStream
				configInput = getClass().getResource("/test-config.json").openStream()
		) {
			objectMapper(schema).readValue(configInput, BasicConfig.class);
		}
	}

	@Test
	public final void testResolvedSchemaConfigMismatch()
	throws Exception {
		final Map<String, Object> schema = SchemaProvider.resolve(
			"test", getClass().getClassLoader()
		);
		final Config config = new BasicConfig("-", schema);

		final List listVal = Arrays.asList(1, 2, 3);
		try {
			config.val("objects-aString", listVal);
			fail();
		} catch(final InvalidValueTypeException e) {
			assertEquals("objects-aString", e.path());
			assertEquals(String.class, e.expectedType());
			assertEquals(listVal.getClass(), e.actualType());
		}

		try {
			config.val("primitives-aBoolean", 0);
			fail();
		} catch(final InvalidValueTypeException e) {
			assertEquals("primitives-aBoolean", e.path());
			assertEquals(boolean.class, e.expectedType());
			assertEquals(Integer.class, e.actualType());
		}
	}
}
