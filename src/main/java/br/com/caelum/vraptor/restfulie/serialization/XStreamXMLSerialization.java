package br.com.caelum.vraptor.restfulie.serialization;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.serialization.Serializer;
import br.com.caelum.vraptor.serialization.SerializerBuilder;
import br.com.caelum.vraptor.serialization.XMLSerialization;
import br.com.caelum.vraptor.serialization.xstream.XStreamSerializer;
import br.com.caelum.vraptor.view.ResultException;

import com.thoughtworks.xstream.XStream;

public class XStreamXMLSerialization implements XMLSerialization {

	private HttpServletResponse response;
	private XStreamBuilder builder;

	@Inject
	public XStreamXMLSerialization(HttpServletResponse response,
			XStreamBuilder builder) {
		this.response = response;
		this.builder = builder;
	}

	public boolean accepts(String format) {
		return "xml".equals(format);
	}

	public <T> Serializer from(T object) {
		response.setContentType("application/xml");
		return getSerializer().from(object);
	}

	protected SerializerBuilder getSerializer() {
		try {
			return new XStreamSerializer(getXStream(), response.getWriter());
		} catch (IOException e) {
			throw new ResultException("Unable to serialize data", e);
		}
	}

	public <T> Serializer from(T object, String alias) {
		response.setContentType("application/xml");
		return getSerializer().from(object, alias);
	}

	/**
	 * You can override this method for configuring XStream before serialization
	 *
	 * @deprecated prefer overwriting XStreamBuilderImpl
	 * @return a configured instance of xstream
	 */
	@Deprecated
	protected XStream getXStream() {
		return builder.xmlInstance();
	}
}
