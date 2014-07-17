package br.com.caelum.vraptor.restfulie.serialization;

import java.io.Writer;

import javax.enterprise.inject.Vetoed;

import br.com.caelum.vraptor.serialization.Serializer;
import br.com.caelum.vraptor.serialization.SerializerBuilder;
import br.com.caelum.vraptor.serialization.xstream.XStreamSerializer;

import com.thoughtworks.xstream.XStream;

@Vetoed
public class LinksSerializer implements SerializerBuilder {

	private final XStreamSerializer serializer;

	public LinksSerializer(XStream xstream, Writer writer) {
		this.serializer = new XStreamSerializer(xstream, writer);
	}

	public Serializer exclude(String... arg0) {
		return serializer.exclude(arg0);
	}

	public Serializer excludeAll() {
		return serializer.excludeAll();
	}

	public <T> Serializer from(T object) {
		return serializer.from(object);
	}

	public Serializer include(String... arg0) {
		return serializer.include(arg0);
	}

	public void serialize() {
		serializer.serialize();
	}

	public <T> Serializer from(T arg0, String arg1) {
		return serializer.from(arg0, arg1);
	}

	public Serializer recursive() {
		return serializer.recursive();
	}

}
