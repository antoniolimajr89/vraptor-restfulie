package br.com.caelum.vraptor.restfulie.serialization;

import java.io.Writer;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.caelum.vraptor.serialization.Serializee;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JsonWriter;

import br.com.caelum.vraptor.interceptor.TypeNameExtractor;
import br.com.caelum.vraptor.serialization.xstream.VRaptorXStream;
import br.com.caelum.vraptor.serialization.xstream.XStreamConverters;

@Dependent
public class XStreamBuilderImpl implements XStreamBuilder {

	private final XStreamConverters converters;
	private final TypeNameExtractor extractor;
	private final Serializee serializee;
	private final ReflectionProvider reflectionProvider;
	private boolean indented = false;
	private boolean withoutRoot = false;

	/**
	 * @deprecated CDI eyes only
	 */
	protected XStreamBuilderImpl() {
		this(null, null, null, null);
	}

	@Inject
	public XStreamBuilderImpl(XStreamConverters converters,
			TypeNameExtractor extractor, Serializee serializee, ReflectionProvider reflectionProvider) {
		this.converters = converters;
		this.extractor = extractor;
		this.serializee = serializee;
		this.reflectionProvider = reflectionProvider;
	}

	public XStream xmlInstance() {
		return configure(new VRaptorXStream(extractor, serializee));
	}

	public XStream jsonInstance() {
		return configure(new VRaptorXStream(extractor,
				getHierarchicalStreamDriver(),serializee));
	}

	protected static final String DEFAULT_NEW_LINE = "";
	protected static final char[] DEFAULT_LINE_INDENTER = {};

	protected static final String INDENTED_NEW_LINE = "\n";
	protected static final char[] INDENTED_LINE_INDENTER = { ' ', ' ' };


	public XStream configure(XStream xstream) {
		converters.registerComponents(xstream);
		return xstream;
	}

	protected HierarchicalStreamDriver getHierarchicalStreamDriver() {
		final String newLine = (indented ? INDENTED_NEW_LINE : DEFAULT_NEW_LINE);
		final char[] lineIndenter = (indented ? INDENTED_LINE_INDENTER
				: DEFAULT_LINE_INDENTER);

		return new JsonHierarchicalStreamDriver() {
			@SuppressWarnings("deprecation")
			public HierarchicalStreamWriter createWriter(Writer writer) {
				if (withoutRoot) {
					return new JsonWriter(writer, lineIndenter, newLine,
							JsonWriter.DROP_ROOT_MODE);
				}

				return new JsonWriter(writer, lineIndenter, newLine);
			}
		};
	}
	
	public XStreamBuilder indented() {
		indented = true;
		return this;
	}

	public XStreamBuilder withoutRoot() {
		withoutRoot = true;
		return this;
	}

	boolean isIndented() {
		return indented;
	}
}
