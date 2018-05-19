package com.github.akurilov.confuse.io.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import com.github.akurilov.confuse.Config;
import com.github.akurilov.confuse.ConfigProvider;
import com.github.akurilov.confuse.impl.BasicConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public abstract class JsonConfigProviderBase
implements ConfigProvider {

	@Override
	public Config config(final String pathSep, final Map<String, Object> schema)
	throws IOException {
		final ConfigJsonDeserializer deserializer;
		try {
			deserializer = new ConfigJsonDeserializer(BasicConfig.class, "-", schema);
		} catch(final NoSuchMethodException e) {
			throw new AssertionError(e);
		}
		final Module module = new SimpleModule()
			.addDeserializer(BasicConfig.class, deserializer);
		final ObjectMapper mapper = new ObjectMapper()
			.registerModule(module)
			.enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
			.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY)
			.configure(JsonParser.Feature.ALLOW_COMMENTS, true)
			.configure(JsonParser.Feature.ALLOW_YAML_COMMENTS, true);
		try(final InputStream input = configInputStream()) {
			return mapper.readValue(input, BasicConfig.class);
		}
	}

	protected abstract InputStream configInputStream()
	throws IOException;
}
