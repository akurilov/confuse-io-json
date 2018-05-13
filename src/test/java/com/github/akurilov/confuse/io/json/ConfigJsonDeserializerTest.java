package com.github.akurilov.confuse.io.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import com.github.akurilov.confuse.Config;
import com.github.akurilov.confuse.impl.BasicConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ConfigJsonDeserializerTest {

	private static ObjectMapper objectMapper(final Map<String, Object> schema)
	throws NoSuchMethodException {
		final Module jacksonModule = new SimpleModule()
			.addDeserializer(
				BasicConfig.class, new ConfigJsonDeserializer(BasicConfig.class, "-", schema)
			);
		return new ObjectMapper()
			.registerModule(jacksonModule)
			.enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
			.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY)
			.configure(JsonParser.Feature.ALLOW_COMMENTS, true)
			.configure(JsonParser.Feature.ALLOW_YAML_COMMENTS, true);
	}

	@Test
	public final void testMapValue()
	throws Exception {
		final String jsonData = "{\n" +
			"\t\"a\" : null,\n" +
			"\t\"b\" : {\n" +
			"\t\t\"aa\" : {\n" +
			"\t\t\t\"aaa\" : {\n" +
			"\t\t\t\t\"bar\" : 123,\n" +
			"\t\t\t\t\"foo\" : \"456\"\n" +
			"\t\t\t}\n" +
			"\t\t}\n" +
			"\t}\n" +
			"}";
		final Map<String, Object> schema = new HashMap<String, Object>() {{
			put("a", Object.class);
			put(
				"b",
				new HashMap<String, Object>() {{
					put(
						"aa",
						new HashMap<String, Object>() {{
							put(
								"aaa",
								new HashMap<String, Object>() {{
									put("bar", int.class);
									put("foo", String.class);
								}}
							);
						}}
					);
				}}
			);
		}};
		final Config config = objectMapper(schema)
			.readValue(jsonData, BasicConfig.class);
		final Map<String, Object> v = config.mapVal("b-aa-aaa");
		assertEquals(123, v.get("bar"));
		assertEquals("456", v.get("foo"));
	}

	@Test
	public final void testNullValue()
	throws Exception {
		final String jsonData = "{\n" +
			"\t\"a\" : null\n" +
			"}";
		final Map<String, Object> schema = new HashMap<String, Object>() {{
			put("a", Object.class);
		}};
		final Config config = objectMapper(schema)
			.readValue(jsonData, BasicConfig.class);
		assertNull(config.val("a"));
	}

	@Test
	public final void testListValue()
	throws Exception {
		final String jsonData = "{\n" +
			"\t\"a\" : null,\n" +
			"\t\"b\" : [\n" +
			"\t\t123,\n" +
			"\t\t\"456\",\n" +
			"\t\tnull\n" +
			"\t]\n" +
			"}";
		final Map<String, Object> schema = new HashMap<String, Object>() {{
			put("a", Object.class);
			put("b", List.class);
		}};
		final Config config = objectMapper(schema)
			.readValue(jsonData, BasicConfig.class);
		final List v = config.listVal("b");
		assertEquals(123, v.get(0));
		assertEquals("456", v.get(1));
		assertNull(v.get(2));
	}
}
