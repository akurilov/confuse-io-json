package com.github.akurilov.confuse.io.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import com.github.akurilov.confuse.Config;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public final class ConfigJsonDeserializer<C extends Config>
extends StdDeserializer<C> {

	private final Constructor<C> constructor;
	private final String pathSep;
	private final Map<String, Object> schema;

	/**
	 @param pathSep path separator for the new config instance
	 @param implCls the particular config implementation class ref
	 @throws NoSuchMethodException if no constructor is declared with (String, Map) args
	 */
	public ConfigJsonDeserializer(
		final Class<C> implCls, final String pathSep, final Map<String, Object> schema
	) throws NoSuchMethodException {
		super(implCls);
		this.constructor = implCls.getConstructor(String.class, Map.class, Map.class);
		this.pathSep = pathSep;
		this.schema = schema;
	}

	@Override
	public final C deserialize(final JsonParser p, final DeserializationContext ctx)
	throws IOException, JsonProcessingException {
		final Map<String, Object> node = p.readValueAs(
			new TypeReference<HashMap<String, Object>>() {}
		);
		try {
			return constructor.newInstance(pathSep, schema, node);
		} catch(final ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}
}
