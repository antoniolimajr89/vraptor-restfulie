package br.com.caelum.vraptor.restfulie.serialization;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.serialization.JSONSerialization;
import br.com.caelum.vraptor.serialization.NoRootSerialization;
import br.com.caelum.vraptor.serialization.Serializer;
import br.com.caelum.vraptor.serialization.SerializerBuilder;
import br.com.caelum.vraptor.serialization.xstream.XStreamSerializer;
import br.com.caelum.vraptor.view.ResultException;

public class XStreamJSONSerialization implements JSONSerialization {

	protected HttpServletResponse response;
	protected XStreamBuilder builder;

	public XStreamJSONSerialization(HttpServletResponse response,
			XStreamBuilder builder) {
		this.response = response;
		this.builder = builder;
	}

	public boolean accepts(String format) {
		return "json".equals(format);
	}

	public <T> Serializer from(T object) {
		return from(object, null);
	}

	public <T> Serializer from(T object, String alias) {
		response.setContentType("application/json");
		return getSerializer().from(object, alias);
	}

	protected SerializerBuilder getSerializer() {
		try {
			return new XStreamSerializer(builder.jsonInstance(),
					response.getWriter());
		} catch (IOException e) {
			throw new ResultException("Unable to serialize data", e);
		}
	}

	/**
	 * You can override this method for configuring Driver before serialization
	 */
	public <T> NoRootSerialization withoutRoot() {
		builder.withoutRoot();
		return this;
	}

	public JSONSerialization indented() {
		builder.indented();
		return this;
	}
}
