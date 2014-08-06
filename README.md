VRaptor-Restfulie
========================

A Restfulie plugin for VRaptor 4

Installing
========================

If you need to use a client restfulie, you just need to add the dependency:

```xml
<dependency>
	<groupId>br.com.caelum.vraptor</groupId>
	<artifactId>vraptor-restfulie</artifactId>
	<version>4.0.0</version> <!-- or last version -->
</dependency>
```
Hypermedia
========================

Create a class that implements `HyperpermediaController` like this:

```java
public class Order implements HypermediaController {

  private Long id;
  private String name;
  private String description;
  private Double price;

  //getters and setters

  public void configureRelations(RelationBuilder builder) {
    //with an easier to read DSL we can easily configure our links
    builder.relation("self").uses(OrdersController.class).show(id);
    builder.relation("payment").uses(OrdersController.class).payment(id);
  }
}
```
Through the builder object we are able to configure which hypermedia links will be returned with our resource

