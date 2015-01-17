package rest.resource;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.ChunkedOutput;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

@Path("/")
public class AsyncResource {
	@GET
	@Path("hello")
	@Produces(MediaType.APPLICATION_JSON)
	public void helloAsync(@Suspended final AsyncResponse asyncResponse){
		final StringBuffer sb = new StringBuffer();
		 
		  Observable.range(1, 100).flatMap(new Func1<Integer, Observable<String>>() {
		         public Observable<String> call(final Integer i) {
		        	 return Observable.create(new OnSubscribe<String>(){
						public void call(Subscriber<? super String> subscriber) {
							subscriber.onNext(i.toString());
							subscriber.onCompleted();
						}
		        		 
		        	 }).subscribeOn(Schedulers.io());
		         }}).toList().subscribe(list -> {
		        	for(String s : list){
		        		sb.append(s);
		        	}
		        	asyncResponse.resume(sb.toString());
		         });
//		  Observable<String> o1 = createObservable(1);
//		  Observable<String> o2 = createObservable(2);
//		  Observable.zip(o1, o2, (String s1, String s2) -> {return s1 +  s2;}).subscribe(new Action1<String>(){
//					@Override
//					public void call(String s) {
//						asyncResponse.resume(s);
//					}
//		         });
	}
	
	@GET
	@Path("chunk")
    public ChunkedOutput<String> getChunkedResponse() {
        final ChunkedOutput<String> output = new ChunkedOutput<String>(String.class);
// 
//        new Thread() {
//            public void run() {
//                try {
//                    String chunk;
// 
//                    while ((chunk = getNextString()) != null) {
//                        output.write(chunk);
//                        sleep(1000);
//                    }
//                } catch (IOException | InterruptedException e) {
//                    // IOException thrown when writing the
//                    // chunks of response: should be handled
//                } finally {
//                    try {
//						output.close();
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//                        // simplified: IOException thrown from
//                        // this close() should be handled here...
//                }
//            }
//        }.start();
        Observable.range(1, 100).flatMap(new Func1<Integer, Observable<String>>() {
	         public Observable<String> call(final Integer i) {
	        	 return Observable.create(new OnSubscribe<String>(){
					public void call(Subscriber<? super String> subscriber) {
						subscriber.onNext(i.toString());
						subscriber.onCompleted();
					}
	        		 
	        	 }).subscribeOn(Schedulers.io());
	         }}).toList().subscribe(list -> {
	        	for(String s : list){
	        		 try {
						output.write(s+"\r\n");
						Thread.sleep(100);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        	}
	        	try {
					output.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	         });
        // the output will be probably returned even before
        // a first chunk is written by the new thread
        return output;
    }
 
    private String getNextString() {
        return "hello";
    }
}
