package br.com.caelum.vraptor.restfulie.controller;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.controller.HttpMethod;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.events.RequestStarted;
import br.com.caelum.vraptor.http.route.Route;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.restfulie.Restfulie;
import br.com.caelum.vraptor.restfulie.hypermedia.HypermediaResource;
import br.com.caelum.vraptor.restfulie.hypermedia.Transition;
import br.com.caelum.vraptor.restfulie.relation.Relation;
import br.com.caelum.vraptor.restfulie.relation.RelationBuilder;
import br.com.caelum.vraptor.view.Status;

@RequestScoped
public class ResourceControllerInterceptor<T extends HypermediaResource>
		implements Interceptor {

	private ResourceControl<T> control;
	private List<Class<?>> controllers;
	private Status status;
	private Restfulie restfulie;
	private Route routes;
	private RequestStarted info;
	private ParameterizedTypeSearcher searcher = new ParameterizedTypeSearcher();

	/**
	 * @deprecated CDI eyes only
	 */
	public ResourceControllerInterceptor() {
	}

	@Inject
	public ResourceControllerInterceptor(ResourceControl<T> control,
			Restfulie restfulie, Status status, RequestStarted info,
			Route routes) {
		this.control = control;
		this.restfulie = restfulie;
		this.status = status;
		this.info = info;
		this.routes = routes;
		this.controllers = Arrays.asList(control.getControllers());
	}

	@Override
	public boolean accepts(ControllerMethod method) {
		return controllers.contains(method.getController().getType())
				&& method.getMethod().isAnnotationPresent(Transition.class);
	}

	@Override
	public void intercept(InterceptorStack stack, ControllerMethod method,
			Object instance) throws InterceptionException {
		ParameterizedType type = searcher.search(control.getClass());
		if (analyzeImplementation(method, type)) {
			stack.next(method, instance);
		}
	}

	private boolean analyzeImplementation(ControllerMethod method,
			ParameterizedType parameterized) {
		Type parameterType = parameterized.getActualTypeArguments()[0];
		Class<?> found = (Class<?>) parameterType;
		T resource = retrieveResource(found);
		if (resource == null) {
			status.notFound();
			return false;
		}
		if (allows(resource, method.getMethod())) {
			return true;
		}
		status.methodNotAllowed(allowedMethods());
		return false;
	}

	private EnumSet<HttpMethod> allowedMethods() {
		EnumSet<HttpMethod> allowed = routes.allowedMethods();
		allowed.remove(HttpMethod.of(info.getRequest()));
		return allowed;
	}

	private T retrieveResource(Class<?> found) {
		String parameterName = lowerFirstChar(found.getSimpleName()) + ".id";
		String id = info.getRequest().getParameter(parameterName);
		T resource = control.retrieve(id);
		return resource;
	}

	private boolean allows(T resource, Method method) {
		RelationBuilder builder = restfulie.newRelationBuilder();
		resource.configureRelations(builder);

		for (Relation relation : builder.getRelations()) {
			if (relation.matches(method)) {
				return true;
			}
		}
		return false;
	}

	private String lowerFirstChar(String simpleName) {
		if (simpleName.length() == 1) {
			return simpleName.toLowerCase();
		}
		return Character.toLowerCase(simpleName.charAt(0))
				+ simpleName.substring(1);
	}
}
