package com.falcontech.controller;

import com.falcontech.model.CreditCard;
import com.falcontech.model.CreditCardResponse;
import com.falcontech.services.CreditcardService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Optional;

@Path("/creditcard")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CreditCardController {
    @Inject
    CreditcardService creditcardService;

    @GET
    @Path("/{hash}")
    public Optional<CreditCard> getAddress(@PathParam("hash") String hash) {
        return creditcardService.getByHash(hash);
    }

    @POST
    public Response add(CreditCard creditCard) {
        CreditCardResponse response = creditcardService.persist(creditCard);
        return Response.status(201).entity(response).build();
    }

}
