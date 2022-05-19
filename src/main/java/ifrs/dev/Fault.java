package ifrs.dev;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.jboss.resteasy.specimpl.ResponseBuilderImpl;

@Path("/fault")
public class Fault {

@GET
@Path("/{name}")
@Produces(MediaType.TEXT_PLAIN)
@Retry(maxRetries = 3, delay = 2000)
@Fallback(fallbackMethod = "recover")
@Timeout(3000)
public String getName(@PathParam("name") String name) {

    System.out.println("passei aqui");
    if (name.equalsIgnoreCase("error")) {
        System.out.println("entrei no if");
        ResponseBuilderImpl builder = new ResponseBuilderImpl();
        builder.status(Response.Status.INTERNAL_SERVER_ERROR);
        builder.entity("The requested was an error");
        System.out.println("deu erro, broa");
        Response response = builder.build();
        throw new WebApplicationException(response);
    }

    return name;
}

public String recover( String name) {

    System.out.println("Recuperado");

    return "Recovered";
}

@GET
@Path("/bulkhead/{name}")
@Produces(MediaType.TEXT_PLAIN)
@Bulkhead(value = 1, waitingTaskQueue = 5)
public String bulkhead(@PathParam("name") String name) {
    return name;
}
    
}
