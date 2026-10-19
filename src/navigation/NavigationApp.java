package navigation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.*;

public class NavigationApp {

    private static final Color BACKGROUND = new Color(241, 245, 249);
private static final Color BLUE = new Color(20, 33, 61);
private static final Color PURPLE = new Color(59, 130, 246);
private static final Color CORAL = new Color(37, 99, 235);
private static final Color DARK = new Color(30, 41, 59);
private static final Color TEAL = new Color(37, 99, 235);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(NavigationApp::createUI);
    }

    private static void createUI() {

        JFrame frame = new JFrame("Smart City Navigation System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(720, 650);
        frame.setMinimumSize(new Dimension(620, 600));
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(BACKGROUND);
        frame.setLayout(new BorderLayout(0, 0));

        // HEADER
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(BLUE);
        header.setBorder(new EmptyBorder(25, 30, 25, 30));

        JLabel icon = new JLabel("✦  SMART CITY");
        icon.setFont(new Font("SansSerif", Font.BOLD, 14));
        icon.setForeground(new Color(200, 215, 255));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("City Navigation");
        title.setFont(new Font("SansSerif", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Find your shortest route, effortlessly.");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 15));
        subtitle.setForeground(new Color(225, 232, 255));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(icon);
        header.add(Box.createVerticalStrut(8));
        header.add(title);
        header.add(Box.createVerticalStrut(6));
        header.add(subtitle);

        frame.add(header, BorderLayout.NORTH);

        // MAIN CONTENT
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(BACKGROUND);
        content.setBorder(new EmptyBorder(25, 45, 25, 45));

        JLabel sectionTitle = new JLabel("PLAN YOUR JOURNEY");
        sectionTitle.setFont(new Font("SansSerif", Font.BOLD, 13));
        sectionTitle.setForeground(PURPLE);
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        content.add(sectionTitle);
        content.add(Box.createVerticalStrut(15));

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(222, 229, 241)),
                new EmptyBorder(22, 24, 22, 24)
        ));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        String[] places = {
            "Marina Beach",
            "Kapaleeshwarar Temple",
            "Santhome Basilica",
            "Elliot's Beach",
            "Fort St. George",
            "Chennai Central Railway Station",
            "Government Museum, Egmore",
            "Valluvar Kottam"
        };

        JComboBox<String> startLocation = new JComboBox<>(places);
        JComboBox<String> destination = new JComboBox<>(places);

        styleComboBox(startLocation);
        styleComboBox(destination);

        JLabel fromLabel = new JLabel("📍  STARTING LOCATION");
        styleLabel(fromLabel);

        JLabel toLabel = new JLabel("🏁  DESTINATION");
        styleLabel(toLabel);

        card.add(fromLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(startLocation);
        card.add(Box.createVerticalStrut(20));
        card.add(toLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(destination);

        content.add(card);
        content.add(Box.createVerticalStrut(18));

        JButton routeButton = new JButton("Find Shortest Route   →");
        routeButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        routeButton.setForeground(Color.WHITE);
        routeButton.setBackground(CORAL);
        routeButton.setFocusPainted(false);
        routeButton.setBorderPainted(false);
        routeButton.setOpaque(true);
        routeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        routeButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        routeButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        routeButton.setPreferredSize(new Dimension(400, 48));

        content.add(routeButton);
        content.add(Box.createVerticalStrut(22));

        JLabel resultHeading = new JLabel("YOUR ROUTE");
        resultHeading.setFont(new Font("SansSerif", Font.BOLD, 13));
        resultHeading.setForeground(TEAL);
        resultHeading.setAlignmentX(Component.LEFT_ALIGNMENT);

        content.add(resultHeading);
        content.add(Box.createVerticalStrut(10));

        JLabel resultLabel = new JLabel(
                "<html><div style='width:430px;'>"
                + "Choose your starting point and destination, "
                + "then click the button to calculate your route."
                + "</div></html>"
        );
        resultLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        resultLabel.setForeground(DARK);
        resultLabel.setVerticalAlignment(SwingConstants.TOP);

        JScrollPane resultScroll = new JScrollPane(resultLabel);
        resultScroll.setBorder(BorderFactory.createEmptyBorder());
        resultScroll.setBackground(new Color(230, 248, 245));
        resultScroll.getViewport().setBackground(new Color(230, 248, 245));
        resultScroll.setPreferredSize(new Dimension(500, 110));
        resultScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        resultScroll.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel resultCard = new JPanel(new BorderLayout());
        resultCard.setBackground(new Color(230, 248, 245));
        resultCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 5, 0, 0, TEAL),
                new EmptyBorder(14, 14, 14, 14)
        ));
        resultCard.add(resultScroll, BorderLayout.CENTER);
        resultCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        resultCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));

        content.add(resultCard);

        // ROUTE CALCULATION
        routeButton.addActionListener(e -> {

            String start = (String) startLocation.getSelectedItem();
            String end = (String) destination.getSelectedItem();

            if (start.equals(end)) {
                resultLabel.setText(
                        "<html><div style='width:430px;'>"
                        + "<b>Please select two different locations.</b>"
                        + "</div></html>"
                );
                return;
            }

            CityMap cityMap = new CityMap();
            Map<String, Map<String, Double>> roads = cityMap.getRoads();

            Map<String, Double> distances = new HashMap<>();
            Map<String, String> previous = new HashMap<>();
            PriorityQueue<String> queue = new PriorityQueue<>(
                    Comparator.comparingDouble(
                            place -> distances.getOrDefault(place, Double.MAX_VALUE)
                    )
            );

            for (String place : roads.keySet()) {
                distances.put(place, Double.MAX_VALUE);
            }

            distances.put(start, 0.0);
            queue.add(start);

            while (!queue.isEmpty()) {

                String current = queue.poll();

                if (current.equals(end)) {
                    break;
                }

                for (Map.Entry<String, Double> road :
                        roads.getOrDefault(current, Collections.emptyMap()).entrySet()) {

                    String neighbor = road.getKey();
                    double newDistance = distances.get(current) + road.getValue();

                    if (newDistance < distances.getOrDefault(
                            neighbor, Double.MAX_VALUE)) {

                        distances.put(neighbor, newDistance);
                        previous.put(neighbor, current);

                        queue.remove(neighbor);
                        queue.add(neighbor);
                    }
                }
            }

            if (distances.getOrDefault(end, Double.MAX_VALUE) == Double.MAX_VALUE) {
                resultLabel.setText(
                        "<html><div style='width:430px;'>"
                        + "<b>No route found between these locations.</b>"
                        + "</div></html>"
                );
                return;
            }

            java.util.List<String> path = new ArrayList<>();
            String current = end;

            while (current != null) {
                path.add(current);
                current = previous.get(current);
            }

            Collections.reverse(path);

            String route = String.join(" &rarr; ", path);

            resultLabel.setText(
                    "<html><div style='width:430px;'>"
                    + "<b style='color:#00897B;'>Shortest Route</b><br><br>"
                    + route
                    + "<br><br><b>Total Distance: "
                    + String.format("%.2f", distances.get(end)) + " km</b>"
                    + "</div></html>"
            );
        });

        frame.add(content, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private static void styleLabel(JLabel label) {
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        label.setForeground(DARK);
    }

    private static void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(new Font("SansSerif", Font.PLAIN, 14));
        comboBox.setBackground(Color.WHITE);
        comboBox.setForeground(DARK);
        comboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        comboBox.setPreferredSize(new Dimension(400, 42));
    }
}