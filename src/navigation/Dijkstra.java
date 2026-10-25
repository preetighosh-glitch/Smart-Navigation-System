package navigation;

import java.util.*;

public class Dijkstra {


public static class RouteResult {

    private final List<String> path;
    private final double distance;

    public RouteResult(List<String> path, double distance) {
        this.path = new ArrayList<>(path);
        this.distance = distance;
    }

    public List<String> getPath() {
        return Collections.unmodifiableList(path);
    }

    public double getDistance() {
        return distance;
    }
}

public static RouteResult calculateShortestPath(
        CityMap cityMap, String start, String destination) {

    Map<String, Map<String, Double>> roads = cityMap.getRoads();

    if (start == null || destination == null
            || !roads.containsKey(start)
            || !roads.containsKey(destination)) {
        return null;
    }

    Map<String, Double> distances = new HashMap<>();
    Map<String, String> previous = new HashMap<>();

    for (String place : roads.keySet()) {
        distances.put(place, Double.MAX_VALUE);
    }

    PriorityQueue<String> queue = new PriorityQueue<>(
            Comparator.comparingDouble(
                    place -> distances.getOrDefault(
                            place, Double.MAX_VALUE)
            )
    );

    distances.put(start, 0.0);
    queue.add(start);

    while (!queue.isEmpty()) {

        String current = queue.poll();

        if (current.equals(destination)) {
            break;
        }

        double currentDistance = distances.get(current);

        for (Map.Entry<String, Double> road :
                roads.get(current).entrySet()) {

            String neighbor = road.getKey();
            double newDistance =
                    currentDistance + road.getValue();

            if (newDistance < distances.getOrDefault(
                    neighbor, Double.MAX_VALUE)) {

                distances.put(neighbor, newDistance);
                previous.put(neighbor, current);

                queue.remove(neighbor);
                queue.add(neighbor);
            }
        }
    }

    double totalDistance = distances.get(destination);

    if (totalDistance == Double.MAX_VALUE) {
        return null;
    }

    List<String> path = new ArrayList<>();
    String current = destination;

    while (current != null) {
        path.add(current);
        current = previous.get(current);
    }

    Collections.reverse(path);

    return new RouteResult(path, totalDistance);
}

public static void findShortestPath(
        CityMap cityMap, String start, String destination) {

    RouteResult result =
            calculateShortestPath(cityMap, start, destination);

    if (result == null) {
        System.out.println("No route found or invalid location.");
        return;
    }

    System.out.println("\n===== SHORTEST ROUTE =====");
    System.out.println("From: " + start);
    System.out.println("To: " + destination);
    System.out.println("Route: "
            + String.join(" -> ", result.getPath()));
    System.out.println("Total distance: "
            + result.getDistance() + " km");
}

public static void main(String[] args) {

    try {
        CityMap cityMap = new CityMap();

        findShortestPath(
                cityMap,
                "Kapaleeshwarar Temple",
                "Government Museum, Egmore"
        );

    } catch (Exception e) {
        System.out.println("Could not calculate the route.");
        System.out.println("Reason: " + e.getMessage());
    }
}


}
