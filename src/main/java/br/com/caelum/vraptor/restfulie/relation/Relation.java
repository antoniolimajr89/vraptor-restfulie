package br.com.caelum.vraptor.restfulie.relation;

import java.lang.reflect.Method;

public interface Relation {

	String getUri();

	String getName();

	/**
	 * Whether this relation uses this specific method in order to get executed.
	 */
	boolean matches(Method method);

}
