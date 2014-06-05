package br.com.caelum.vraptor.restfulie.serialization;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.config.Configuration;
import br.com.caelum.vraptor.restfulie.Restfulie;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.ReflectionConverter;

/**
 * Custom serialization process provides a way to add links to your resource
 * representations.
 */
@RequestScoped
public class RestfulSerialization extends XStreamXMLSerialization {

	private Restfulie restfulie;
	private  Configuration config;

	/**
	 * @deprecated CDI eyes only
	 */

	public RestfulSerialization() {
		super(null, null);
	}
	
	@Inject
	public RestfulSerialization(HttpServletResponse response,
			Restfulie restfulie, Configuration config, XStreamBuilder builder) {
		super(response, builder);
		this.restfulie = restfulie;
		this.config = config;
	}

	/**
	 * You can override this method for configuring XStream before
	 * serialization. It configures the xstream instance with a link converter
	 * for all StateResource implementations.
	 */
	@Override
	protected XStream getXStream() {
		@SuppressWarnings("deprecation")
		XStream xStream = super.getXStream();
		MethodValueSupportConverter converter = new MethodValueSupportConverter(
				new ReflectionConverter(xStream.getMapper(),
						xStream.getReflectionProvider()));
		xStream.registerConverter(new LinkConverter(converter, restfulie,
				config));
		return xStream;
	}
}
