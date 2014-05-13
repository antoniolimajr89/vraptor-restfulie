package br.com.caelum.vraptor.restfulie.hypermedia;

import br.com.caelum.vraptor.restfulie.relation.RelationBuilder;

public interface ConfigurableHypermediaController extends HypermediaController, RelationBuilder{
	<T> T getModel();
}
