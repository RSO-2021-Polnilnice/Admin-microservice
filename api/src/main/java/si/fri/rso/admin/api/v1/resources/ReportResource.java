package si.fri.rso.admin.api.v1.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kumuluz.ee.cors.annotations.CrossOrigin;
import com.kumuluz.ee.discovery.annotations.DiscoverService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import si.fri.rso.admin.lib.Ocena;
import si.fri.rso.admin.lib.Report;
import si.fri.rso.admin.services.beans.ReportBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Path("/reports")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@CrossOrigin(supportedMethods = "GET, POST, PUT, HEAD, DELETE, OPTIONS")
public class ReportResource {


    // Dependency on polnilnice microservice
    @Inject
    @DiscoverService(value = "polnilnice-service", environment = "dev", version = "1.0.0")
    private Optional<String> polnilnice_host;

    @Inject
    private ReportBean reportBean;

    CloseableHttpClient httpClient = HttpClients.createDefault();
    ObjectMapper mapper = new ObjectMapper();

    @GET
    @Path("/discover")
    @Produces("application/json")
    public Response getDiscover() {
        return Response.status(Response.Status.OK).entity(polnilnice_host.get()).build();
    }

    @GET
    @Path("/test")
    @Produces("application/json")
    public Response getDiscover2() {
        String httpGetResponse = myHttpGet(polnilnice_host.get() + "/v1/polnilnice");
        System.out.println(httpGetResponse);
        return Response.status(Response.Status.OK).entity(polnilnice_host.get()).build();
    }

    /** GET all reports **/
    @GET
    @Produces("application/json")
    public Response get() {
        List<Report> reportList = reportBean.getAllReports();
        if (reportList == null || reportList.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No reports found.").build();
        }
        return Response.status(Response.Status.OK).entity(reportList).build();
    }

    @POST
    @Produces("application/json")
    public Response createReport(Report r) {
        System.out.println(r);
        if (r.getUserId() == null || r.getKomentar() == null || r.getOcenaId() == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Missing fields in body.").build();
        }
        long unixTime = System.currentTimeMillis() / 1000L;
        r.setTimestamp(unixTime);


        Report report = reportBean.createReport(r);
        if (report.getTimestamp() == 999999999999L) {
            return Response.status(Response.Status.CONFLICT).entity("Report for this comment already exists.").build();
        }

        return Response.status(Response.Status.CREATED).entity(r).build();

    }

    /** GET reports by user ID **/
    @GET
    @Path("/users/{userId}")
    @Produces("application/json")
    public Response get(@PathParam("userId") Integer userId) {
        List<Report> reportList = reportBean.getReportsByUserId(userId);
        if (reportList == null || reportList.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No reports found.").build();
        }
        return Response.status(Response.Status.OK).entity(reportList).build();
    }


    /** **/
    @DELETE
    @Path("/{id}")
    public Response deleteReport(@PathParam("id") Integer id) {

        boolean deleted = reportBean.deleteReport(id);

        if (deleted) {
            return Response.status(Response.Status.OK)
                    .entity("Report with id " + id + " successfully deleted.").build();
        }
        else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Report with id " + id + " was not found.").build();
        }
    }


    private String myHttpGet(String url) {
        HttpGet request = new HttpGet(url);
        CloseableHttpResponse response = null;

        try {
            response = httpClient.execute(request);
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            System.out.println("err: " + e.getMessage());
            return  e.getMessage();
        }
    }



}

