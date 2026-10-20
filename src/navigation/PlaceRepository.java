
package navigation;

import java.sql.*;
import java.util.*;

public class PlaceRepository {

    private final String url;
    private final String username;
    private final String password;

    public PlaceRepository() {
        url = System.getenv("DB_URL");
        username = System.getenv("DB_USER");
        password = System.getenv("DB_PASSWORD");

        if (url == null || username == null || password == null) {
            throw new IllegalStateException(
                "Set DB_URL, DB_USER and DB_PASSWORD."
            );
        }
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public Map<String, Map<String, Double>> loadRoads()
            throws SQLException {

        Map<String, Map<String, Double>> roads = new HashMap<>();

        String placesSql = "SELECT name FROM places";

        String roadsSql = """
            SELECT p1.name AS from_name,
                   p2.name AS to_name,
                   r.distance_km,
                   r.is_bidirectional
            FROM roads r
            JOIN places p1 ON r.from_place_id = p1.id
            JOIN places p2 ON r.to_place_id = p2.id
            """;

        try (Connection connection = connect()) {

            // Add every place, including places with no roads.
            try (PreparedStatement statement =
                     connection.prepareStatement(placesSql);
                 ResultSet result = statement.executeQuery()) {

                while (result.next()) {
                    roads.put(result.getString("name"),
                              new HashMap<>());
                }
            }

            // Add road connections to the graph.
            try (PreparedStatement statement =
                     connection.prepareStatement(roadsSql);
                 ResultSet result = statement.executeQuery()) {

                while (result.next()) {
                    String from = result.getString("from_name");
                    String to = result.getString("to_name");
                    double distance = result.getDouble("distance_km");
                    boolean bidirectional =
                            result.getBoolean("is_bidirectional");

                    roads.get(from).put(to, distance);

                    if (bidirectional) {
                        roads.get(to).put(from, distance);
                    }
                }
            }
        }

        return roads;
    }
}
