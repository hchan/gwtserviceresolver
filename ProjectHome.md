The GWTServiceResolver
helps resolve gwt rpc web services with minimal installation and configuration.
Its very lightweight (one Java file), a few lines in web.xml and
one line in each service to point to the gwtserviceresolver


If you have a sample.client.XXXService,

then by this project's naming convention,
it will resolve the Service Impl as

sample.server.XXXServiceImpl

This is done via reflection in the
```
com.google.gwt.user.server.rpc.GwtServiceResolver
```
class.  No need for Guice and maintenance of Guice Service classes.
Also no need for further entries in web.xml.
Redirect all your gwt rpc services to the GWTServiceResolover by giving it a .gwtrpc
endpoint

It is suggested that you mark (annotate) your service endpoint
with `@RemoteServiceRelativePath` with the argument `"ServiceResolver.gwtrpc"`
note that only the '.gwtrpc' is used in web.xml
i.e.
```

@RemoteServiceRelativePath("ServiceResolver.gwtrpc")
public interface GreetingService extends RemoteService {
	String greetServer(String name) throws IllegalArgumentException;
}
```
and web.xml must capture this via:
```
<web-app>
        <servlet>
		<servlet-name>GwtServiceResolver</servlet-name>
		<servlet-class>com.google.gwt.user.server.rpc.GwtServiceResolver</servlet-class>
	</servlet>

	<servlet-mapping>
		<servlet-name>GwtServiceResolver</servlet-name>
		<url-pattern>*.gwtrpc</url-pattern>
	</servlet-mapping>
</web-app>
```