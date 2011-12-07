package com.google.gwt.user.server.rpc;

import java.lang.reflect.Modifier;

import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.SerializationException;

/**
 * Uses reflection to find a class with .client. to .server. + "Impl"
 * 
 * @author Henry Chan - Dec 2011
 */
@SuppressWarnings("serial")
public class GwtServiceResolver extends RemoteServiceServlet {



	public String processCall(String payload) throws SerializationException {
		checkPermutationStrongName();
		try {

			RPCRequest rpcRequest = RPC.decodeRequest(payload);

			onAfterRequestDeserialized(rpcRequest);

			Class<?> invokeClass = rpcRequest.getMethod().getDeclaringClass();

			Object service = null;
			try {
				String classStr = invokeClass.getName();
				classStr = getResoloverClass(classStr);


				Class clazz = Class.forName(classStr);
				int modifiers = clazz.getModifiers();
				if (Modifier.isAbstract(modifiers)) {
					String[] payloadSplit = payload.split("\\|");
					classStr = payloadSplit[5]; // a bit of a hack
					classStr = getResoloverClass(classStr);
				}			
				service = Class.forName(classStr).newInstance();

				RemoteServiceServlet rss = (RemoteServiceServlet)service;
				rss.init(this.getServletConfig());
				rss.perThreadRequest = this.perThreadRequest;
				rss.perThreadResponse = this.perThreadResponse;

			} catch (Exception e) {
				log(
						"GWTDispatcher reflection exception",
						e);
				return RPC.encodeResponseForFailure(null, e);
			}

			return RPC.invokeAndEncodeResponse(service, rpcRequest.getMethod(),
					rpcRequest.getParameters(), rpcRequest.getSerializationPolicy(),
					rpcRequest.getFlags());
		} catch (IncompatibleRemoteServiceException ex) {
			log(
					"An IncompatibleRemoteServiceException was thrown while processing this call.",
					ex);
			return RPC.encodeResponseForFailure(null, ex);
		}
	}

	private String getResoloverClass(String oldClassStr) {
		String retval = oldClassStr;
		retval = retval.replace(".client.", ".server.");
		retval += "Impl";

		return retval;
	}
}
