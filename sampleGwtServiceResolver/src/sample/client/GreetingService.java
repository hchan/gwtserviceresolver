package sample.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("ServiceResolver.gwtrpc")
public interface GreetingService extends RemoteService {
	String greetServer(String name) throws IllegalArgumentException;
}
