Vraptor-Restfulie-Server
========================

A Restfulie server plugin for vraptor 4

Installing
========================

If you need use client restfulie also, add the dependency

```xml
<dependency>
	<groupId>br.com.caelum</groupId>
	<artifactId>restfulie</artifactId>
	<version>1.0.1</version>
</dependency>
```


Configuring
========================

Add in your web.xml

```xml
<context-param>
  <param-name>br.com.caelum.vraptor.packages</param-name>
  <param-value>br.com.caelum.vraptor.restfulie</param-value>
</context-param>
```
