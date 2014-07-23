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
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.controller.HttpMethod;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.events.RequestStarted;
import br.com.caelum.vraptor.http.route.Route;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.restfulie.Restfulie;
import br.com.caelum.vraptor.restfulie.hypermedia.HypermediaController;
import br.com.caelum.vraptor.restfulie.hypermedia.Transition;
import br.com.caelum.vraptor.restfulie.relation.Relation;
import br.com.caelum.vraptor.restfulie.relation.RelationBuilder;
import br.com.caelum.vraptor.view.Status;

@Intercepts
@RequestScoped
public class ControllerInterceptor<T extends HypermediaController>
		implements Interceptor {

	private final ControllerControl<T> control;
	private final List<Class<?>> controllers;
	private final Status status;
	private final Restfulie restfulie;
	private final Route routes;
	private final RequestStarted info;
	private final ParameterizedTypeSearcher searcher = new ParameterizedTypeSearcher();

	/**
	 * @deprecated CDI eyes only
	 */
	protected ControllerInterceptor() {
		this(null, null, null, null, null);
	}

	@Inject
	public ControllerInterceptor(ControllerControl<T> control,
			Restfulie restfulie, Status status, RequestStarted info,
			Route routes) {
		this.control = control;
		this.restfulie = restfulie;
		this.status = status;
		this.info = info;
		this.routes = routes;
		this.controllers = Arrays.asList(control.getControllers());
	}

	public boolean accepts(ControllerMethod method) {
		return controllers.contains(method.getController().getType())
				&& method.getMethod().isAnnotationPresent(Transition.class);
	}

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
		T controller = retrieveResource(found);
		if (controller == null) {
			status.notFound();
			return false;
		}
		if (allows(controller, method.getMethod())) {
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
		T controller = control.retrieve(id);
		return controller;
	}

	private boolean allows(T controller, Method method) {
		RelationBuilder builder = restfulie.newRelationBuilder();
		controller.configureRelations(builder);

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
