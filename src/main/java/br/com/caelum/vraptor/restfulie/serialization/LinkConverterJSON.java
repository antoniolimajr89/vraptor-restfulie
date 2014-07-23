package br.com.caelum.vraptor.restfulie.serialization;

import java.util.List;

import br.com.caelum.vraptor.config.Configuration;
import br.com.caelum.vraptor.restfulie.Restfulie;
import br.com.caelum.vraptor.restfulie.hypermedia.ConfigurableHypermediaController;
import br.com.caelum.vraptor.restfulie.hypermedia.HypermediaController;
import br.com.caelum.vraptor.restfulie.relation.Relation;
import br.com.caelum.vraptor.restfulie.relation.RelationBuilder;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.ExtendedHierarchicalStreamWriterHelper;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * Reads all transitions from your controller object and converts them into link
 * elements.<br>
 * The converter passed in the constructor will be used to marshall the rest of
 * the object.
 *
 */
public class LinkConverterJSON implements Converter {

	private final Restfulie restfulie;
	private final Converter base;
	private final Configuration config;

	public LinkConverterJSON(Converter base, Restfulie restfulie, Configuration config) {
		this.base = base;
		this.restfulie = restfulie;
		this.config = config;
	}

	public void marshal(Object root, HierarchicalStreamWriter writer, MarshallingContext context) {
		if (root instanceof ConfigurableHypermediaController) {
			context.convertAnother(((ConfigurableHypermediaController) root).getModel());
		} else {
			base.marshal(root, writer, context);
		}

		HypermediaController controller = (HypermediaController) root;
		RelationBuilder builder = restfulie.newRelationBuilder();
		controller.configureRelations(builder);

		if( !builder.getRelations().isEmpty() ) {
			ExtendedHierarchicalStreamWriterHelper.startNode(writer, "links", List.class);
			Link link = null;
			for (Relation t : builder.getRelations()) {
				link = new Link(t.getName(), config.getApplicationPath() + t.getUri());
				ExtendedHierarchicalStreamWriterHelper.startNode(writer, "link", String.class);
				context.convertAnother(link);
				writer.endNode();
			}
		writer.endNode();
		}
	}

	public Object unmarshal(HierarchicalStreamReader arg0,
			UnmarshallingContext arg1) {
		return base.unmarshal(arg0, arg1);
	}

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class type) {
		return HypermediaController.class.isAssignableFrom(type)
				&& base.canConvert(type);
	}

}
