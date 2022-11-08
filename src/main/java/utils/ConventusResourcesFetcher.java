package utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dtos.ConventusResourceDTO;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ConventusResourcesFetcher {

//TODO, så skal den returnerede streng bygges ind i URLen til et andet fetch, sammen med dagens dato og en uge frem, eller lignende.
    public static String getResource() throws IOException {
        String resourcesJSON = HttpUtils.fetchData("https://www.conventus.dk/publicBooking/api/resources?organization=13688");
        //SKal finde external, directories, resources, id
        //System.out.println("JSON Conventus: " + resourcesJSON);

        JsonObject json = JsonParser.parseString(resourcesJSON).getAsJsonObject();
        //System.out.println("parsed JSON: " + json.toString());

        StringBuilder resourceIds = new StringBuilder();

        JsonArray externals = json.get("external").getAsJsonArray();
        //System.out.println("get parsed as string: " + externals.toString());
        for (JsonElement external : externals) {
            if(external.getAsJsonObject().has("directories")) {
            JsonArray jArray = external.getAsJsonObject().get("directories").getAsJsonArray();
            //System.out.println("jArray: " + jArray);
                for (JsonElement jsonElement : jArray) {
                    JsonArray jArrayResources = jsonElement.getAsJsonObject().get("resources").getAsJsonArray();
          //          System.out.println("nested resources;: " + jArrayResources);
                    for (JsonElement jArrayResource : jArrayResources) {
                        String ids = jArrayResource.getAsJsonObject().get("id").getAsString();
                        resourceIds.append(ids);
                        resourceIds.append(";");
            //            System.out.println("IDs: " + ids);
                    }
                }
            } else if (external.getAsJsonObject().has("resources")) {
                JsonArray jArray = external.getAsJsonObject().get("resources").getAsJsonArray();
                System.out.println("jArrayResources: " + jArray);
                for (JsonElement jsonElement : jArray) {
                    String ids = jsonElement.getAsJsonObject().get("id").getAsString();
                    resourceIds.append(ids);
                    resourceIds.append(";");
                    System.out.println("IDs: " + ids);
                }
            }
        }

        String resourceIdsString = resourceIds.substring(0, resourceIds.length()-1);
        System.out.println(resourceIdsString);
        return resourceIdsString;
    }

    public void getBFFInfo() throws IOException {
        String resources = getResource();
        String today = LocalDate.now().toString();
        System.out.println("today: " + today);
        String inOneWeek = LocalDate.now().plusDays(7).toString();
        System.out.println("in a week: " + inOneWeek);
        String resourcesJSON = HttpUtils.fetchData(String.format("https://www.conventus.dk/publicBooking/api/bookings?organization=13688&from=%s&to=%s&resources=%s", today, inOneWeek, resources));
        //System.out.println("JSON Conventus BFF INFO: " + resourcesJSON);
        //organization.id eller name, og løbe igennem dem
        JsonArray json = JsonParser.parseString(resourcesJSON).getAsJsonArray();

        for (JsonElement jsonElement : json) {
            //System.out.println("jsomElement: " + jsonElement);
            String organizationId = jsonElement.getAsJsonObject().get("organization").getAsJsonObject().get("id").getAsString();
        //Get object where organization.id = 13688
            if (organizationId.equals("13688")) {

                if(jsonElement.getAsJsonObject().get("resource").getAsJsonObject().has("resourceGroup")) {
                    String resourceName = jsonElement.getAsJsonObject().get("resource").getAsJsonObject().get("resourceGroup").getAsJsonObject().get("title").getAsString() + " " + jsonElement.getAsJsonObject().get("resource").getAsJsonObject().get("name").getAsString();
                    System.out.println(resourceName);
                } else {
                    String resourceName = jsonElement.getAsJsonObject().get("resource").getAsJsonObject().get("name").getAsString();
                System.out.println(resourceName);
                }
                String timeStart = jsonElement.getAsJsonObject().get("start").getAsString();
                System.out.println(timeStart);
                String timeEnd = jsonElement.getAsJsonObject().get("end").getAsString();
                System.out.println(timeEnd);
            }

        }


        //JsonObject organization = json.get("organization").getAsJsonObject();

        //https://www.conventus.dk/publicBooking/api/bookings?organization=13688&from=2022-11-09&to=2022-11-16&resources=7070;7033;7208;7206;6925;27655

        // så skal jeg fiske resource.name resource.resourceGroup.title og start og end ud.

    }
}
