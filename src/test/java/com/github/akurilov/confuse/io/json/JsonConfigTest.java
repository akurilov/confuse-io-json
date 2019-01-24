package com.github.akurilov.confuse.io.json;

import com.github.akurilov.confuse.Config;
import com.github.akurilov.confuse.ConfigProvider;
import com.github.akurilov.confuse.SchemaProvider;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.lang.Math.PI;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class JsonConfigTest
extends JsonConfigProviderBase {

	@Test
	public final void test()
	throws Exception {
		final Map<String, Object> schema = SchemaProvider.resolveAndReduce(
			"test", getClass().getClassLoader()
		);
		final List<Config> configs = ConfigProvider.resolve(
			"test", getClass().getClassLoader(), "-", schema
		);
		assertTrue(configs.size() != 0);
		final Config config = configs.get(0);
		assertNull(config.val("objects-any"));
		assertEquals("yohoho", config.stringVal("objects-aString"));
		assertEquals(Arrays.asList(1, 2, 3, 4), config.listVal("objects-aList"));
		final Map<String, String> mv = config.mapVal("objects-aMap");
		assertEquals("val", mv.get("key"));
		assertEquals("bar", mv.get("foo"));
		assertTrue(config.boolVal("primitives-aBoolean"));
		assertEquals(123456, config.intVal("primitives-anInt"));
		assertEquals(9876543210L, config.longVal("primitives-aLong"));
		assertEquals(PI, config.doubleVal("primitives-aDouble"), 0.0000001);
	}

	@Override
	protected InputStream configInputStream()
	throws IOException {
		final URL res = getClass().getResource("/test-config.json");
		if(res == null) {
			throw new FileNotFoundException("resources://test-config.json");
		}
		return res.openStream();
	}

	@Override
	public String id() {
		return "test";
	}
}
