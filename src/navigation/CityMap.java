package navigation;

import java.util.*;

public class CityMap {

    private final Map<String, Map<String, Double>> roads;

    public CityMap() {
        try {
            PlaceRepository repository = new PlaceRepository();
            roads = repository.loadRoads();
        } catch (java.sql.SQLException e) {
            throw new IllegalStateException(
                "Could not load the city map from MySQL.", e
            );
        }
    }

    private void addRoad(String place1, String place2, double distance) {
        roads.putIfAbsent(place1, new HashMap<>());
        roads.putIfAbsent(place2, new HashMap<>());

        roads.get(place1).put(place2, distance);
        roads.get(place2).put(place1, distance);
    }

    public Map<String, Map<String, Double>> getRoads() {
        return roads;
    }
    public Set<String> getLocations() {
    return roads.keySet();
}

    public void displayMap() {
        System.out.println("===== CHENNAI TOURIST MAP =====");

        for (String place : roads.keySet()) {
            System.out.println(place + " -> " + roads.get(place));
        }
    }

    public static void main(String[] args) {
        CityMap map = new CityMap();
        map.displayMap();
    }
}