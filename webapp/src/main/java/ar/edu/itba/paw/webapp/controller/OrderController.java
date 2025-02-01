package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.OrderService;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.exception.OrderNotFoundException;
import ar.edu.itba.paw.models.files.PaymentReceipt;
import ar.edu.itba.paw.models.orders.Order;
import ar.edu.itba.paw.models.orders.OrderStatus;
import ar.edu.itba.paw.webapp.dto.input.OrderEditDTO;
import ar.edu.itba.paw.webapp.dto.input.validations.ImageOrPdf;
import ar.edu.itba.paw.webapp.dto.output.OrderDTO;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import java.util.List;

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
            @QueryParam("book_id") final Long bookId,
            @QueryParam("buyer_id") final Long readerId,
            @QueryParam("seller_id") final Long writerId,
            @QueryParam("title") final String title,
            @QueryParam("status") final OrderStatus status,
            @QueryParam("page") @DefaultValue("1") final int page,
            @QueryParam("size") @DefaultValue("20") final int size
    ){
        final PaginatedContent<Order> orderPage = os.searchOrders(bookId, writerId, readerId, title, status, page, size);
        List<OrderDTO> orders = orderPage.getPage().stream().map(OrderDTO.mapper(uriInfo)).toList();

        return paginatedResponse(
                Response.ok(new GenericEntity<>(orders){}), orderPage, uriInfo
        ).build();
    }

    @POST
    @Consumes(value = MediaType.APPLICATION_JSON)
    public Response createOrder(
            @FormDataParam("bookId") long bookId,
            @FormDataParam("userId") long userId //TODO: remove userId and get it from jwt
    ){
        Long orderId = os.create(bookId, userId);

        if(orderId != null){
            return Response
                    .created(uriInfo.getAbsolutePathBuilder().path(orderId.toString()).build())
                    .build();
        } else {
            return Response.status(Response.Status.CONFLICT).build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") final long id){
        final Order order = os.findById(id).orElseThrow(OrderNotFoundException::new);

        return Response.ok(OrderDTO.fromOrder(uriInfo, order)).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(value = MediaType.APPLICATION_JSON)
    public Response updateOrder(
            @PathParam("id") final long id,
            @Valid final OrderEditDTO orderDTO
    ){
        os.updateOrderWriterSide(id, orderDTO.getRejectionReason());
        return Response.ok().build();
    }

    @GET
    @Path("/{id}/receipt")
    @Produces(value = {"image/jpeg", "application/pdf"})
    public Response getReceipt(@PathParam("id") final long id){
        final PaymentReceipt receipt = os.getReceipt(id);

        return Response
                .ok(receipt.getFile(), receipt.getType())
                //.header("content-disposition", "attachment; filename=receipt" + receipt.getFileExtension())
                .build();
    }

    @PUT
    @Path("{id}/receipt")
    @Consumes(value = MediaType.APPLICATION_JSON)
    public Response setReceipt(
            @PathParam("id") final long orderId,
            @ImageOrPdf @FormDataParam("receipt") FormDataBodyPart receipt
    ){
        os.updateOrderBuyerSide(orderId, receipt.getEntityAs(byte[].class), receipt.getMediaType().toString());

        return Response.ok().build();
    }
}
