package br.com.caelum.vraptor.restfulie.relation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Vetoed;

import net.vidageek.mirror.dsl.Mirror;
import br.com.caelum.vraptor.http.route.Router;
import br.com.caelum.vraptor.proxy.MethodInvocation;
import br.com.caelum.vraptor.proxy.Proxifier;
import br.com.caelum.vraptor.proxy.SuperMethod;

@Vetoed
public class DefaultRelationBuilder implements RelationBuilder {

	private final List<Relation> relations = new ArrayList<Relation>();
	private final Proxifier proxifier;
	private final Router router;

	public DefaultRelationBuilder(Router router, Proxifier proxifier) {
		this.router = router;
		this.proxifier = proxifier;
	}

	public void add(Relation relation) {
		relations.add(relation);
	}

	public WithName relation(String name) {
		return new WithNameImpl(name);
	}

	public List<Relation> getRelations() {
		return new ArrayList<Relation>(relations);
	}

	public <T> T relation(final Class<T> type) {
		return proxifier.proxify(type, new MethodInvocation<T>() {
			public Object intercept(T proxy, Method method, Object[] args, SuperMethod superMethod) {
				T instance = relation(method.getName()).uses(type);
				new Mirror().on(instance).invoke().method(method).withArgs(args);
				return null;
			}
		});
	}

	private class WithNameImpl implements WithName {

		private final String name;

		public WithNameImpl(String name) {
			this.name = name;
		}

		public void at(String uri) {
			relations.add(new UriBasedRelation(name, uri));
		}

		public <T> T uses(final Class<T> controller) {
			return proxifier.proxify(controller, new MethodInvocation<T>() {
				public Object intercept(T proxy, Method method, Object[] args, SuperMethod superMethod) {
					relations.add(new UriBasedRelation(name, router.urlFor(controller, method, args)));
					return null;
				}
			});
		}

	}

}
