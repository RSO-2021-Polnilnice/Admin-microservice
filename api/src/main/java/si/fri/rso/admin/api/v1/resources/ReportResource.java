package si.fri.rso.admin.api.v1.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kumuluz.ee.cors.annotations.CrossOrigin;
import com.kumuluz.ee.discovery.annotations.DiscoverService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
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

    @Operation(description = "Get charging stations microservice IP as per service discovery network..", summary = "Get charging stations microservice IP")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Charging stations microservice IP."),
    })
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

    @Operation(description = "Get list of all reports.", summary = "List of all reports")
    @APIResponses({
        @APIResponse(responseCode = "404",
                description = "No reports found."
        ),
        @APIResponse(responseCode = "200",
                description = "List of all reports.",
                content = @Content(
                        schema = @Schema(implementation = Report.class))
        )
    })
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

    @Operation(description = "Create a new report.", summary = "Create report")
    @APIResponses({
        @APIResponse(responseCode = "400",
                description = "Missing fields in body.."
        ),
        @APIResponse(responseCode = "403", description = "Report for this comment already exists."),
        @APIResponse(responseCode = "201",
                description = "Report created.",
                content = @Content(
                        schema = @Schema(implementation = Report.class))
        )
    })
    @POST
    @Produces("application/json")
    public Response createReport(
            @RequestBody(
            description = "DTO object with report data.",
            required = true, content = @Content(
            schema = @Schema(implementation = Report.class))) Report r) {
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
    @Operation(description = "Get list of reports for user with provided ID.", summary = "Get list of reports for user")
    @APIResponses({
        @APIResponse(responseCode = "404",
                description = "Reports for this user not found"
        ),
        @APIResponse(responseCode = "200",
                description = "List of reports for user with provided ID.",
                content = @Content(
                        schema = @Schema(implementation = Report.class))
        )
    })
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

    @Operation(description = "Deletes the report with provided ID.", summary = "Delete report")
    @APIResponses({
            @APIResponse(responseCode = "404",
                    description = "Report with provided ID not found"
            ),
            @APIResponse(responseCode = "200", description = "Report deleted")
    })
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

