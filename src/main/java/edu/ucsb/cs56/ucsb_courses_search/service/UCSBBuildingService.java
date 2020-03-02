package edu.ucsb.cs56.ucsb_courses_search.service;

import edu.ucsb.cs56.ucsb_courses_search.model.UCSBBuilding;
import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.core.type.TypeReference;
import java.io.InputStream;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UCSBBuildingService {

    private static UCSBBuildingService instance = null;

    private HashMap<String, UCSBBuilding> buildingMap;

    private UCSBBuildingService() {

        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream is = TypeReference.class.getResourceAsStream("classpath:ucsb_buildings.json");

            UCSBBuilding[] buildings = mapper.readValue(is, UCSBBuilding[].class);

            for (int i = 0; i < buildings.length; i++) {
                this.buildingMap.put(buildings[i].getName(), buildings[i]);
            }

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    public static UCSBBuildingService getInstance() {
        if (instance == null) {
            instance = new UCSBBuildingService();
        }
        return instance;
    }

    public static UCSBBuilding getBuilding(String buildingCode) {
        UCSBBuildingService service = UCSBBuildingService.getInstance();

        return service.buildingMap.get(buildingCode);
    }

    public static String getLink(String buildingCode){
        UCSBBuildingService service = UCSBBuildingService.getInstance();
        UCSBBuilding building = service.buildingMap.get(buildingCode);
        String lat = Double.toString(building.getLatitude());
        String lon = Double.toString(building.getLongitude());
        String result = "https://maps.openrouteservice.org/directions?n1=" + lat + "&n2="
            + lon + "&n3=13&b=0&k1=en-US&k2=km";
        return result;
    }
}