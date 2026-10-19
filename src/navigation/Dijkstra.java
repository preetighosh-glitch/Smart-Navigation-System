package navigation;

import java.util.*;

public class Dijkstra {

    public static void findShortestPath(
            CityMap cityMap, String start, String destination) {

        Map<String, Map<String, Double>> roads = cityMap.getRoads();

        Map<String, Double> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();

        PriorityQueue<String> queue = new PriorityQueue<>(
        Comparator.comparingDouble(
                place -> distances.getOrDefault(place, Double.MAX_VALUE)
        )
);

        for (String place : cityMap.getLocations()) {
            distances.put(place, Double.MAX_VALUE);
        }

        if (!distances.containsKey(start)
                || !distances.containsKey(destination)) {
            System.out.println("Invalid starting point or destination.");
            return;
        }

        distances.put(start, 0.0);
        queue.add(start);

        while (!queue.isEmpty()) {
            String current = queue.poll();

            if (current.equals(destination)) {
                break;
            }

            for (Map.Entry<String, Double> road
                    : roads.get(current).entrySet()) {

                String neighbor = road.getKey();
                double roadDistance = road.getValue();

                double newDistance =
                        distances.get(current) + roadDistance;

                if (newDistance < distances.get(neighbor)) {
                    distances.put(neighbor, newDistance);
                    previous.put(neighbor, current);

                    queue.remove(neighbor);
                    queue.add(neighbor);
                }
            }
        }

        if (distances.get(destination) == Double.MAX_VALUE) {
            System.out.println("No route found.");
            return;
        }

        List<String> path = new ArrayList<>();
        String current = destination;

        while (current != null) {
            path.add(current);
            current = previous.get(current);
        }

        Collections.reverse(path);

        System.out.println("\n===== SHORTEST ROUTE =====");
        System.out.println("From: " + start);
        System.out.println("To: " + destination);
        System.out.println("Route: " + String.join(" -> ", path));
        System.out.println("Total distance: "
                + distances.get(destination) + " km");
    }

    public static void main(String[] args) {
        CityMap cityMap = new CityMap();

        findShortestPath(
                cityMap,
                "Kapaleeshwarar Temple",
                "Government Museum, Egmore"
        );
    }
}
