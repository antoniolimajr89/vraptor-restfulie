package br.com.caelum.vraptor.restfulie.relation;

import java.lang.reflect.Method;

import javax.enterprise.inject.Vetoed;

public class UriBasedRelation implements Relation {

	private final String name;
	private final String uri;

	public UriBasedRelation(String name, String uri) {
		this.name = name;
		this.uri = uri;
	}

	public String getName() {
		return name;
	}

	public String getUri() {
		return uri;
	}

	public boolean matches(Method method) {
		return false;
	}

}
