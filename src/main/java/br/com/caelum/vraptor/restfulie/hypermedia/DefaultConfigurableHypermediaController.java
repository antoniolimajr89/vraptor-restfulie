package br.com.caelum.vraptor.restfulie.hypermedia;

import java.util.List;

import javax.enterprise.inject.Vetoed;

import br.com.caelum.vraptor.restfulie.relation.Relation;
import br.com.caelum.vraptor.restfulie.relation.RelationBuilder;


public class DefaultConfigurableHypermediaController implements
		ConfigurableHypermediaController {

	private final RelationBuilder delegate;
	private final Object model;

	public DefaultConfigurableHypermediaController(RelationBuilder delegate,
			Object model) {
		this.delegate = delegate;
		this.model = model;
	}

	@SuppressWarnings("unchecked")
	public <T> T getModel() {
		return (T) model;
	}

	public void configureRelations(RelationBuilder builder) {
		for (Relation relation : delegate.getRelations()) {
			builder.add(relation);
		}
	}

	public void add(Relation relation) {
		delegate.add(relation);
	}

	public List<Relation> getRelations() {
		return delegate.getRelations();
	}

	public <T> T relation(Class<T> type) {
		return delegate.relation(type);
	}

	public WithName relation(String name) {
		return delegate.relation(name);
	}

}
