package com.falcontech.services;

import com.falcontech.model.CreditCard;
import com.falcontech.model.CreditCardResponse;
import com.google.common.hash.Hashing;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.InsertOneResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

@ApplicationScoped
public class CreditcardService {
  @Inject MongoClient mongoClient;

  public CreditCardResponse persist(CreditCard creditcard) {
    Document document =
        new Document()
            .append("number", creditcard.number())
            .append("expiration", creditcard.expiration())
            .append("cvv", creditcard.cvv())
            .append("owner", creditcard.owner())
            .append("hash", setHash(creditcard));
    InsertOneResult result = getCollection().insertOne(document);
    return new CreditCardResponse(result.getInsertedId().toString());
  }

  public Optional<CreditCard> getByID(String id) {
    Bson filer = eq("_id", new ObjectId(id));
    return Optional.ofNullable(
        getCollection()
            .find(filer)
            .map(
                document -> new CreditCard(
                    document.getInteger("number"),
                    document.getString("expiration"),
                    document.getInteger("cvv"),
                    document.getString("owner"),
                    document.getString("hash")))
            .first());
  }

  private String setHash(CreditCard creditCard) {
    String hashValues =
        (new StringBuffer())
            .append(creditCard.number())
            .append(creditCard.expiration())
            .append(creditCard.cvv())
            .append(creditCard.owner())
            .toString();
    return Hashing.sha256().hashString(hashValues, StandardCharsets.UTF_8).toString();
  }

  private MongoCollection<Document> getCollection() {
    return mongoClient.getDatabase("CCServiceDB").getCollection("CreditCards");
  }
}
