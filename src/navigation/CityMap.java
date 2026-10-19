package navigation;

import java.util.*;

public class CityMap {

    private final Map<String, Map<String, Double>> roads = new HashMap<>();

    public CityMap() {
        addRoad("Marina Beach", "Fort St. George", 2.7);
        addRoad("Marina Beach", "Kapaleeshwarar Temple", 2.1);
        addRoad("Kapaleeshwarar Temple", "Santhome Basilica", 1.1);
        addRoad("Santhome Basilica", "Elliot's Beach", 6.5);
        addRoad("Fort St. George", "Chennai Central Railway Station", 5.6);
        addRoad("Fort St. George", "Government Museum, Egmore", 6.0);
        addRoad("Government Museum, Egmore", "Valluvar Kottam", 5.0);
        addRoad("Valluvar Kottam", "Kapaleeshwarar Temple", 6.3);
        addRoad("Elliot's Beach", "Valluvar Kottam", 11.5);
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