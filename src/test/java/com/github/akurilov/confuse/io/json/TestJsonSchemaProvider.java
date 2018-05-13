package com.github.akurilov.confuse.io.json;

import com.github.akurilov.confuse.SchemaProvider;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class TestJsonSchemaProvider
extends JsonSchemaProviderBase
implements SchemaProvider  {

	@Override
	protected InputStream schemaInputStream()
	throws IOException  {
		final URL res = getClass().getResource("/test-schema.json");
		if(res == null) {
			throw new FileNotFoundException("resources://test-schema.json");
		}
		return res.openStream();
	}

	@Override
	public String id() {
		return "test";
	}
}
