package com.falcontech.services;

import static com.mongodb.client.model.Filters.eq;

import com.falcontech.model.CreditCard;
import com.falcontech.model.CreditCardResponse;
import com.google.common.hash.Hashing;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import org.bson.Document;
import org.bson.conversions.Bson;

@ApplicationScoped
public class CreditcardService {
  @Inject MongoClient mongoClient;

  public CreditCardResponse persist(CreditCard creditcard) {
    String hash = setHash(creditcard);
    if (!verifyIfExists(hash)) {
      Document document =
              new Document()
                      .append("number", creditcard.number())
                      .append("expiration", creditcard.expiration())
                      .append("cvv", creditcard.cvv())
                      .append("owner", creditcard.owner())
                      .append("hash", setHash(creditcard));
      getCollection().insertOne(document);
    }
    return new CreditCardResponse(hash);
  }

  public Optional<CreditCard> getByHash(String hash) {
    Bson filer = eq("hash", hash);
    return Optional.ofNullable(
        getCollection()
            .find(filer)
            .map(
                document -> new CreditCard(
                    document.getString("number"),
                    document.getString("expiration"),
                    document.getInteger("cvv"),
                    document.getString("owner"),
                    document.getString("hash")))
            .first());
  }

  public boolean verifyIfExists(String hash) {
    Bson filer = eq("hash", hash);
    return getCollection().find(filer).iterator().hasNext();
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
