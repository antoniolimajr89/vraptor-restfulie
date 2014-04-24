package br.com.caelum.vraptor.restfulie.hypermedia;

import br.com.caelum.vraptor.restfulie.relation.RelationBuilder;

public interface ConfigurableHypermediaResource extends HypermediaResource, RelationBuilder{
	<T> T getModel();
}
