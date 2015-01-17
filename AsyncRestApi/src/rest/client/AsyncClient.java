package rest.client;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ChunkedInput;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;

public class AsyncClient {
	public static void main(String[] args){
		JerseyClient client = JerseyClientBuilder.createClient();
//		final AsyncInvoker asyncInvoker = client.target("http://localhost:8080/AsyncRestApi/chunk").request().async();
//		final Future<Response> responseFuture = asyncInvoker.get();
//		System.out.println("Request is being processed asynchronously.");
//		try {
//			final Response response = responseFuture.get();
//			
//		    // get() waits for the response to be ready
//		System.out.println("Response received.");
//
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ExecutionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		final Response response = client.target("http://localhost:8080/AsyncRestApi/chunk").request().get();
		final ChunkedInput<String> chunkedInput =
		        response.readEntity(new GenericType<ChunkedInput<String>>() {});
		String chunk;
		while ((chunk = chunkedInput.read()) != null) {
		    System.out.println("Next chunk received: " + chunk);
		}
	}
}
