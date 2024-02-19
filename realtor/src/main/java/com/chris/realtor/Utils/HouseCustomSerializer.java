package com.chris.realtor.Utils;
import com.chris.realtor.Models.House;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.ArrayList;

public class HouseCustomSerializer extends JsonSerializer<House> {

    @Override
    public void serialize(House house, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("seller", house.getUsername());
        jsonGenerator.writeStringField("listing", house.getListing());
        jsonGenerator.writeStringField("address", house.populateAddressLine());
        jsonGenerator.writeNumberField("price", house.getPrice());
        jsonGenerator.writeNumberField("beds", house.getBeds());
        jsonGenerator.writeNumberField("baths", house.getBaths());
        jsonGenerator.writeNumberField("size", house.getSize());
        // Serialize the list of image object keys
        if(house.getObjectKey() != null){
            ArrayList<String> imageObjectKeys = house.getObjectKey();
            jsonGenerator.writeArrayFieldStart("objectKeys");
            for (String objectKey : imageObjectKeys) {
                jsonGenerator.writeString(objectKey);
            }
            jsonGenerator.writeEndArray();
        }
        jsonGenerator.writeEndObject();
    }
}