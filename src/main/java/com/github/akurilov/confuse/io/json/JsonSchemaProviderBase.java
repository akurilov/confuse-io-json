package com.github.akurilov.confuse.io.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import com.github.akurilov.confuse.SchemaProvider;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public abstract class JsonSchemaProviderBase
implements SchemaProvider {

	@Override
	public Map<String, Object> schema()
	throws IOException  {
		final var module = new SimpleModule()
			.addDeserializer(String.class, new TypeJsonDeserializer(String.class));
		final var m = new ObjectMapper()
			.registerModule(module)
			.enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
			.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY)
			.configure(JsonParser.Feature.ALLOW_COMMENTS, true)
			.configure(JsonParser.Feature.ALLOW_YAML_COMMENTS, true);
		final var typeRef = new TypeReference<Map<String, Object>>() {};
		try(final var input = schemaInputStream()) {
			return m.readValue(input, typeRef);
		}
	}

	protected abstract InputStream schemaInputStream()
	throws IOException;
}
