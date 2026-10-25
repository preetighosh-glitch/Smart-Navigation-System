
package navigation;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.*;

public class SmartNavigationApp extends JFrame {

    private JComboBox<String> startBox;
    private JComboBox<String> destinationBox;
    private JTextArea resultArea;
    private JLabel distanceLabel;
    private JLabel statusLabel;
    private MapPanel mapPanel;

    private final Color background = new Color(8, 6, 17);
    private final Color panel = new Color(20, 15, 35);
    private final Color purple = new Color(145, 75, 210);
    private final Color pink = new Color(255, 75, 190);
    private final Color lightPink = new Color(220, 170, 255);
    private final Color green = new Color(65, 255, 190);

    private final String[] locations = {
        "Sathyabama University",
        "Adyar",
        "Marina Beach",
        "Chennai Central",
        "T Nagar",
        "Guindy",
        "Tambaram",
        "Mahabalipuram"
    };

    private final Map<String, Map<String, Integer>> graph =
            new LinkedHashMap<>();

    private java.util.List<String> currentRoute = new ArrayList<>();

    public SmartNavigationApp() {
        createGraph();
        createInterface();

        setTitle("Smart Navigation System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900, 700));
        setSize(1250, 850);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void createGraph() {
        for (String location : locations) {
            graph.put(location, new LinkedHashMap<>());
        }

        addRoad("Sathyabama University", "Adyar", 12);
        addRoad("Sathyabama University", "Mahabalipuram", 35);
        addRoad("Adyar", "Marina Beach", 10);
        addRoad("Adyar", "Guindy", 8);
        addRoad("Marina Beach", "Chennai Central", 5);
        addRoad("Marina Beach", "T Nagar", 7);
        addRoad("Chennai Central", "T Nagar", 9);
        addRoad("T Nagar", "Guindy", 6);
        addRoad("Guindy", "Tambaram", 18);
        addRoad("Mahabalipuram", "Tambaram", 32);
        addRoad("Adyar", "T Nagar", 8);
    }

    private void addRoad(String first, String second, int km) {
        graph.get(first).put(second, km);
        graph.get(second).put(first, km);
    }

    private void createInterface() {
        JPanel root = new JPanel(new BorderLayout(15, 15));
        root.setBackground(background);
        root.setBorder(new EmptyBorder(20, 20, 20, 20));

        // HEADER
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(background);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(background);

        JLabel title = new JLabel("SMART NAVIGATION");
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setForeground(lightPink);

        JLabel subtitle = new JLabel(
                "SYSTEM | Smarter. Faster. Better journeys."
        );
        subtitle.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitle.setForeground(new Color(185, 175, 205));

        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(6));
        titlePanel.add(subtitle);

        JLabel ready = new JLabel("● SYSTEM READY");
        ready.setFont(new Font("Monospaced", Font.BOLD, 13));
        ready.setForeground(green);
        ready.setBorder(new CompoundBorder(
                new LineBorder(new Color(45, 120, 100), 1),
                new EmptyBorder(12, 14, 12, 14)
        ));

        header.add(titlePanel, BorderLayout.WEST);
        header.add(ready, BorderLayout.EAST);
        root.add(header, BorderLayout.NORTH);

        // MAIN CONTENT
        JPanel content = new JPanel(new GridLayout(1, 2, 15, 0));
        content.setBackground(background);

        // LEFT SIDE: ROUTE PLANNER AND RESULTS
        JPanel left = new JPanel(new BorderLayout(0, 15));
        left.setBackground(background);

        JPanel planner = createPanel("PLAN YOUR ROUTE");
        planner.setLayout(new BoxLayout(planner, BoxLayout.Y_AXIS));

        JLabel startLabel = new JLabel("STARTING LOCATION");
        styleLabel(startLabel, green);

        startBox = createLocationBox();

        JLabel destinationLabel = new JLabel("DESTINATION");
        styleLabel(destinationLabel, pink);

        destinationBox = createLocationBox();

        JButton findButton = new JButton("FIND SHORTEST ROUTE");
        styleButton(findButton, pink);

        JButton resetButton = new JButton("RESET");
        styleButton(resetButton, purple);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        buttons.setBackground(panel);
        buttons.add(findButton);
        buttons.add(resetButton);

        planner.add(startLabel);
        planner.add(Box.createVerticalStrut(7));
        planner.add(startBox);
        planner.add(Box.createVerticalStrut(15));
        planner.add(destinationLabel);
        planner.add(Box.createVerticalStrut(7));
        planner.add(destinationBox);
        planner.add(Box.createVerticalStrut(15));
        planner.add(buttons);

        left.add(planner, BorderLayout.NORTH);

        JPanel information = createPanel("ROUTE INFORMATION");
        information.setLayout(new BorderLayout(0, 10));

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        resultArea.setForeground(Color.WHITE);
        resultArea.setBackground(background);
        resultArea.setMargin(new Insets(12, 12, 12, 12));

        JScrollPane scroll = new JScrollPane(resultArea);
        scroll.setBorder(new LineBorder(lightPink, 1));
        scroll.getViewport().setBackground(background);

        information.add(scroll, BorderLayout.CENTER);

        GridLayout statusPanel = new GridLayout(2, 1, 0, 5);
JPanel statusContainer = new JPanel(statusPanel);
        statusContainer.setBackground(panel);
        statusContainer.setBorder(new EmptyBorder(8, 8, 8, 8));

        distanceLabel = new JLabel("DISTANCE | NOT CALCULATED");
        styleLabel(distanceLabel, lightPink);

        statusLabel = new JLabel("STATUS | READY");
        styleLabel(statusLabel, green);

        statusContainer.add(distanceLabel);
        statusContainer.add(statusLabel);

        information.add(statusContainer, BorderLayout.SOUTH);
        left.add(information, BorderLayout.CENTER);

        // RIGHT SIDE: MAP
        JPanel mapContainer = createPanel("CHENNAI OFFLINE MAP");
        mapContainer.setLayout(new BorderLayout());
        mapPanel = new MapPanel();
        mapContainer.add(mapPanel, BorderLayout.CENTER);

        JLabel mapNote = new JLabel(
                "SCHEMATIC MAP | Illustrative roads and locations"
        );
        mapNote.setForeground(lightPink);
        mapNote.setFont(new Font("Arial", Font.PLAIN, 11));
        mapNote.setBorder(new EmptyBorder(8, 2, 2, 2));
        mapContainer.add(mapNote, BorderLayout.SOUTH);

        content.add(left);
        content.add(mapContainer);
        root.add(content, BorderLayout.CENTER);

        findButton.addActionListener(e -> findShortestRoute());
        resetButton.addActionListener(e -> resetForm());

        setContentPane(root);
        showWelcomeMessage();
    }

    private JPanel createPanel(String heading) {
        JPanel panelBox = new JPanel();
        panelBox.setLayout(new BorderLayout(0, 12));
        panelBox.setBackground(panel);
        panelBox.setBorder(new CompoundBorder(
                new LineBorder(purple, 1),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel label = new JLabel(heading);
        label.setFont(new Font("Arial", Font.BOLD, 17));
        label.setForeground(lightPink);

        panelBox.add(label, BorderLayout.NORTH);
        return panelBox;
    }

    private void styleLabel(JLabel label, Color color) {
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(color);
    }

    private JComboBox<String> createLocationBox() {
        JComboBox<String> box = new JComboBox<>(locations);
        box.setFont(new Font("Arial", Font.PLAIN, 13));
        box.setPreferredSize(new Dimension(260, 38));
        box.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        return box;
    }

    private void styleButton(JButton button, Color borderColor) {
        button.setFont(new Font("Arial", Font.BOLD, 11));
        button.setForeground(Color.BLACK);
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorder(new CompoundBorder(
                new LineBorder(borderColor, 2),
                new EmptyBorder(10, 12, 10, 12)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void showWelcomeMessage() {
        resultArea.setText(
                "WELCOME TO SMART NAVIGATION!\n\n"
                + "1. Select your starting location.\n"
                + "2. Select your destination.\n"
                + "3. Click FIND SHORTEST ROUTE.\n\n"
                + "The shortest route will be highlighted on the map.\n\n"
                + "NOTE: Road distances are illustrative values."
        );
        resultArea.setCaretPosition(0);
    }

    private void findShortestRoute() {
        String start = (String) startBox.getSelectedItem();
        String destination = (String) destinationBox.getSelectedItem();

        if (start == null || destination == null) {
            return;
        }

        if (start.equals(destination)) {
            currentRoute = new ArrayList<>();
            currentRoute.add(start);
            resultArea.setText(
                    "YOU ARE ALREADY AT YOUR DESTINATION\n\n"
                    + "Location: " + start + "\n"
                    + "Total distance: 0 km"
            );
            distanceLabel.setText("DISTANCE | 0 KM");
            statusLabel.setText("STATUS | COMPLETE");
            mapPanel.repaint();
            return;
        }

        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();

        for (String location : locations) {
            distances.put(location, Integer.MAX_VALUE);
        }
        distances.put(start, 0);

        PriorityQueue<Node> queue = new PriorityQueue<>(
                Comparator.comparingInt(node -> node.distance)
        );
        Set<String> visited = new HashSet<>();
        queue.add(new Node(start, 0));

        while (!queue.isEmpty()) {
            Node node = queue.poll();
            String current = node.location;

            if (!visited.add(current)) {
                continue;
            }
            if (current.equals(destination)) {
                break;
            }

            for (Map.Entry<String, Integer> road :
                    graph.get(current).entrySet()) {
                String neighbour = road.getKey();
                int newDistance = distances.get(current) + road.getValue();

                if (!visited.contains(neighbour)
                        && newDistance < distances.get(neighbour)) {
                    distances.put(neighbour, newDistance);
                    previous.put(neighbour, current);
                    queue.add(new Node(neighbour, newDistance));
                }
            }
        }

        if (distances.get(destination) == Integer.MAX_VALUE) {
            resultArea.setText("No route found.");
            distanceLabel.setText("DISTANCE | UNAVAILABLE");
            statusLabel.setText("STATUS | NO ROUTE");
            currentRoute.clear();
            mapPanel.repaint();
            return;
        }

        LinkedList<String> route = new LinkedList<>();
        String current = destination;

        while (current != null) {
            route.addFirst(current);
            current = previous.get(current);
        }

        currentRoute = new ArrayList<>(route);

        StringBuilder output = new StringBuilder();
        output.append("SHORTEST ROUTE FOUND\n");
        output.append("==============================\n\n");

        for (int i = 0; i < route.size(); i++) {
            output.append(i + 1).append(". ")
                    .append(route.get(i)).append("\n");
            if (i < route.size() - 1) {
                output.append("   |\n   v\n");
            }
        }

        output.append("\nTOTAL DISTANCE: ")
                .append(distances.get(destination)).append(" km\n\n");
        output.append("Algorithm: Dijkstra's shortest path\n");
        output.append("NOTE: Sample distances; not verified road distances.");

        resultArea.setText(output.toString());
        resultArea.setCaretPosition(0);
        distanceLabel.setText(
                "DISTANCE | " + distances.get(destination) + " KM"
        );
        statusLabel.setText("STATUS | ROUTE FOUND");

        mapPanel.repaint();
    }

    private void resetForm() {
        startBox.setSelectedIndex(0);
        destinationBox.setSelectedIndex(0);
        currentRoute.clear();
        showWelcomeMessage();
        distanceLabel.setText("DISTANCE | NOT CALCULATED");
        statusLabel.setText("STATUS | READY");
        mapPanel.repaint();
    }

    private class MapPanel extends JPanel {

        private final Map<String, Point> points = new LinkedHashMap<>();

        MapPanel() {
            setBackground(new Color(12, 18, 32));
            setPreferredSize(new Dimension(480, 520));

            points.put("Sathyabama University", new Point(65, 410));
            points.put("Adyar", new Point(160, 300));
            points.put("Marina Beach", new Point(285, 170));
            points.put("Chennai Central", new Point(355, 85));
            points.put("T Nagar", new Point(260, 260));
            points.put("Guindy", new Point(180, 365));
            points.put("Tambaram", new Point(100, 465));
            points.put("Mahabalipuram", new Point(80, 500));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int width = getWidth();
            int height = getHeight();

            // Background grid
            g2.setColor(new Color(25, 34, 52));
            for (int x = 0; x < width; x += 35) {
                g2.drawLine(x, 0, x, height);
            }
            for (int y = 0; y < height; y += 35) {
                g2.drawLine(0, y, width, y);
            }

            // Decorative roads
            g2.setStroke(new BasicStroke(5));
            g2.setColor(new Color(55, 67, 88));

            for (Map.Entry<String, Map<String, Integer>> entry : graph.entrySet()) {
                Point p1 = getScaledPoint(entry.getKey());
                for (String neighbour : entry.getValue().keySet()) {
                    if (entry.getKey().compareTo(neighbour) < 0) {
                        Point p2 = getScaledPoint(neighbour);
                        g2.drawLine(p1.x, p1.y, p2.x, p2.y);
                    }
                }
            }

            // Highlight calculated route
            if (currentRoute.size() > 1) {
                g2.setColor(green);
                g2.setStroke(new BasicStroke(5));

                for (int i = 0; i < currentRoute.size() - 1; i++) {
                    Point p1 = getScaledPoint(currentRoute.get(i));
                    Point p2 = getScaledPoint(currentRoute.get(i + 1));
                    g2.drawLine(p1.x, p1.y, p2.x, p2.y);
                }
            }

            // Location markers
            for (String location : locations) {
                Point p = getScaledPoint(location);
                boolean selected = currentRoute.contains(location);

                g2.setColor(selected ? green : pink);
                g2.fillOval(p.x - 6, p.y - 6, 12, 12);

                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 10));
                g2.drawString(shortName(location), p.x + 8, p.y - 8);
            }

            g2.setColor(lightPink);
            g2.setFont(new Font("Arial", Font.BOLD, 16));
            g2.drawString("CHENNAI", 18, 25);

            g2.dispose();
        }

        private Point getScaledPoint(String location) {
            Point original = points.get(location);

            int margin = 25;
            int maxX = 420;
            int maxY = 500;

            int availableWidth = Math.max(1, getWidth() - margin * 2);
            int availableHeight = Math.max(1, getHeight() - margin * 2);

            int x = margin + original.x * availableWidth / maxX;
            int y = margin + original.y * availableHeight / maxY;

            return new Point(x, y);
        }

        private String shortName(String location) {
            switch (location) {
                case "Sathyabama University": return "Sathyabama";
                case "Chennai Central": return "Central";
                case "Mahabalipuram": return "Mahabalipuram";
                default: return location;
            }
        }
    }

    private static class Node {
        private final String location;
        private final int distance;

        Node(String location, int distance) {
            this.location = location;
            this.distance = distance;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SmartNavigationApp::new);
    }
}

