package navigation;

import java.util.*;

/**
 * Builds the local map data sent to assets/chennai_map.html.
 * No network requests or third-party libraries are required.
 */
public final class MapBridge {
    private MapBridge() {}

    public static String buildMapDataJson(
            CityMap cityMap,
            Map<String, double[]> coordinates) {

        List<String> locations = new ArrayList<>(cityMap.getLocations());
        locations.sort(String.CASE_INSENSITIVE_ORDER);
        Map<String, Map<String, Double>> graph = cityMap.getRoads();

        StringBuilder out = new StringBuilder("{\"locations\":[");
        for (int i = 0; i < locations.size(); i++) {
            if (i > 0) out.append(',');
            out.append(quote(locations.get(i)));
        }
        out.append("],\"coordinates\":{");
        boolean firstCoord = true;
        for (String name : locations) {
            double[] c = coordinates.get(name);
            if (c == null || c.length < 2) continue;
            if (!firstCoord) out.append(',');
            firstCoord = false;
            out.append(quote(name)).append(":[")
               .append(c[0]).append(',').append(c[1]).append(']');
        }
        out.append("},\"roads\":[");
        boolean firstRoad = true;
        Set<String> seen = new HashSet<>();
        for (Map.Entry<String, Map<String, Double>> from : graph.entrySet()) {
            for (Map.Entry<String, Double> to : from.getValue().entrySet()) {
                String a = from.getKey(), b = to.getKey();
                String key = a.compareTo(b) < 0 ? a + "|" + b : b + "|" + a;
                if (!seen.add(key)) continue;
                if (!firstRoad) out.append(',');
                firstRoad = false;
                out.append("{\"from\":").append(quote(a))
                   .append(",\"to\":").append(quote(b))
                   .append(",\"distance\":").append(to.getValue() == null ? 0 : to.getValue())
                   .append('}');
            }
        }
        return out.append("]}").toString();
    }

    public static String buildRouteJson(List<String> path, double distanceKm) {
        StringBuilder out = new StringBuilder("{\"path\":[");
        for (int i = 0; i < path.size(); i++) {
            if (i > 0) out.append(',');
            out.append(quote(path.get(i)));
        }
        return out.append("],\"distance\":").append(distanceKm).append('}').toString();
    }

    private static String quote(String value) {
        if (value == null) return "\"\"";
        StringBuilder out = new StringBuilder("\"");
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            switch (c) {
                case '"': out.append("\\\""); break;
                case '\\': out.append("\\\\"); break;
                case '\n': out.append("\\n"); break;
                case '\r': out.append("\\r"); break;
                case '\t': out.append("\\t"); break;
                default:
                    if (c < 0x20) out.append(String.format("\\u%04x", (int)c));
                    else out.append(c);
            }
        }
        return out.append('"').toString();
    }
}
