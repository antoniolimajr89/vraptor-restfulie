package br.com.caelum.vraptor.restfulie.controller;

public interface ControllerControl<T> {

	/**
	 * Returns a list of controllers to be intercepted.
	 */
	Class<?>[] getControllers();

	/**
	 * Given its id (retrieved from the request parameter %id%), returns an
	 * element from the database or null if its not found.
	 */
	T retrieve(String id);

}
