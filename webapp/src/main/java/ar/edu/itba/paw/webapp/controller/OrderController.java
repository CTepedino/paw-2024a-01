package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.OrderService;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.exception.OrderNotFoundException;
import ar.edu.itba.paw.models.files.PaymentReceipt;
import ar.edu.itba.paw.models.orders.Order;
import ar.edu.itba.paw.models.orders.OrderStatus;
import ar.edu.itba.paw.webapp.dto.OrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import static ar.edu.itba.paw.webapp.controller.ControllerUtils.paginatedResponse;

@Path("orders")
@Component
public class OrderController {

    private final OrderService os;

    @Context
    private UriInfo uriInfo;

    @Autowired
    public OrderController(final OrderService os){
        this.os = os;
    }

    @GET
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response listOrders(
            @QueryParam("bookId") final Long bookId,
            @QueryParam("readerId") final Long readerId,
            @QueryParam("writerId") final Long writerId,
            @QueryParam("title") final String title,
            @QueryParam("status") final OrderStatus status,
            @QueryParam("page") @DefaultValue("1") final int page,
            @QueryParam("size") @DefaultValue("20") final int size
    ){
        final PaginatedContent<Order> orderPage = os.searchOrders(bookId, readerId, writerId, title, status, page, size);

        return paginatedResponse(
                Response.ok(new GenericEntity<>(orderPage){}), orderPage, uriInfo
        ).build();
    }

    @GET
    @Path("/{id}")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") final long id){
        final Order order = os.findById(id).orElseThrow(OrderNotFoundException::new);

        return Response.ok(OrderDTO.fromOrder(uriInfo, order)).build();
    }

    @GET
    @Path("/{id}/receipt")
    @Produces(value = {"image/jpeg", "application/pdf"})
    public Response getReceipt(@PathParam("id") final long id){
        final PaymentReceipt receipt = os.getReceipt(id);

        return Response
                .ok(receipt.getFile(), receipt.getType())
                .header("content-disposition", "attachment; filename=receipt" + receipt.getFileExtension())
                .build();
    }
}
