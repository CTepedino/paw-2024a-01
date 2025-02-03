package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.OrderService;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.exception.OrderNotFoundException;
import ar.edu.itba.paw.models.files.PaymentReceipt;
import ar.edu.itba.paw.models.orders.Order;
import ar.edu.itba.paw.models.orders.OrderStatus;
import ar.edu.itba.paw.webapp.contentType.VndMediaTypes;
import ar.edu.itba.paw.webapp.dto.input.OrderCreateDTO;
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
    @Produces(value = {VndMediaTypes.ORDER})
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
    @Consumes(value = {VndMediaTypes.ORDER})
    public Response createOrder(
            @Valid OrderCreateDTO orderDTO
    ){
        Long orderId = os.create(orderDTO.getBookId(), orderDTO.getUserId());

        if(orderId != null){
            return Response
                    .created(uriInfo.getAbsolutePathBuilder().path(orderId.toString()).build())
                    .build();
        } else {
            return Response.status(Response.Status.CONFLICT).build();
        }
    }

    @GET
    @Path("{id}")
    @Produces(value = {VndMediaTypes.ORDER})
    public Response getById(@PathParam("id") final long id){
        final Order order = os.findById(id).orElseThrow(OrderNotFoundException::new);

        return Response.ok(OrderDTO.fromOrder(uriInfo, order)).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(value = {VndMediaTypes.ORDER})
    public Response updateOrder(
            @PathParam("id") final long id,
            @Valid final OrderEditDTO orderDTO
    ){
        os.updateOrderWriterSide(id, orderDTO.getRejectionReason());
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/receipt")
    @Produces(value = {"image/jpeg", "application/pdf"})
    public Response getReceipt(@PathParam("id") final long id){
        final PaymentReceipt receipt = os.getReceipt(id);
        if (receipt == null){
            return Response.noContent().build();
        }

        return Response
                .ok(receipt.getFile(), receipt.getType())
                .build();
    }

    @PUT
    @Path("{id}/receipt")
    @Consumes(value = MediaType.MULTIPART_FORM_DATA)
    public Response setReceipt(
            @PathParam("id") final long orderId,
            @ImageOrPdf @FormDataParam("receipt") FormDataBodyPart receipt
    ){
        os.updateOrderBuyerSide(orderId, receipt.getEntityAs(byte[].class), receipt.getMediaType().toString());

        return Response.noContent().build();
    }
}
