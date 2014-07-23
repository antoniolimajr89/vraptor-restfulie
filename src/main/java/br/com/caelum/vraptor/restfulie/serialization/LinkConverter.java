package br.com.caelum.vraptor.restfulie.serialization;

import br.com.caelum.vraptor.config.Configuration;
import br.com.caelum.vraptor.restfulie.Restfulie;
import br.com.caelum.vraptor.restfulie.hypermedia.ConfigurableHypermediaController;
import br.com.caelum.vraptor.restfulie.hypermedia.HypermediaController;
import br.com.caelum.vraptor.restfulie.relation.Relation;
import br.com.caelum.vraptor.restfulie.relation.RelationBuilder;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class LinkConverter implements Converter {

	private final Restfulie restfulie;
	private final Converter base;
	private final Configuration config;

	public LinkConverter(Converter base, Restfulie restfulie,
			Configuration config) {
		this.base = base;
		this.restfulie = restfulie;
		this.config = config;
	}

	public void marshal(Object root, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		if (root instanceof ConfigurableHypermediaController) {
			context.convertAnother(((ConfigurableHypermediaController) root)
					.getModel());
		} else {
			base.marshal(root, writer, context);
		}

		HypermediaController controller = (HypermediaController) root;
		RelationBuilder builder = restfulie.newRelationBuilder();
		controller.configureRelations(builder);
		for (Relation t : builder.getRelations()) {
			writer.startNode("atom:link");
			writer.addAttribute("rel", t.getName());
			writer.addAttribute("href",
					config.getApplicationPath() + t.getUri());
			writer.addAttribute("xmlns:atom", "http://www.w3.org/2005/Atom");
			writer.endNode();
		}
	}

	@SuppressWarnings("unused")
	private Object getRoot(Object root) {
		if (root instanceof ConfigurableHypermediaController) {
			return ((ConfigurableHypermediaController) root).getModel();
		}
		return root;
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
