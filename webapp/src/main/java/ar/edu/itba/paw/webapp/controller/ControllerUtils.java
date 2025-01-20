package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.PaginatedContent;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.math.BigDecimal;

public class ControllerUtils {
    private ControllerUtils(){}

    public static Response.ResponseBuilder paginatedResponse(Response.ResponseBuilder responseBuilder, PaginatedContent<?> paginatedContent, UriInfo uriInfo){
        responseBuilder.link(uriInfo.getRequestUriBuilder().replaceQueryParam("page", 1).build(), "first");
        responseBuilder.link(uriInfo.getRequestUriBuilder().replaceQueryParam("page", paginatedContent.getPageCount()).build(), "last");
        if (paginatedContent.hasMorePages()){
            responseBuilder.link(uriInfo.getRequestUriBuilder().replaceQueryParam("page", paginatedContent.getPageNumber()+1).build(), "next");
        }
        if (paginatedContent.getPageNumber() > 1){
            responseBuilder.link(uriInfo.getRequestUriBuilder().replaceQueryParam("page", paginatedContent.getPageNumber()-1).build(), "prev");
        }

        return responseBuilder;
    }

    public static BigDecimal bigDecimalQueryParam(String param){
        if (param == null){
            return null;
        }
        try {
            return new BigDecimal(param);
        } catch (NumberFormatException e){
            return null;
        }
    }
}
