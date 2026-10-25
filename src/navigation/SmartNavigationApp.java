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

    // CYBERPUNK THEME
    private final Color background = new Color(8, 6, 17);
    private final Color panel = new Color(20, 15, 35);
    private final Color purple = new Color(145, 75, 210);
    private final Color pink = new Color(255, 75, 190);
    private final Color lightPink = new Color(220, 170, 255);
    private final Color green = new Color(65, 255, 190);

    // AVAILABLE LOCATIONS
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

    // ROAD NETWORK
    private final Map<String, Map<String, Integer>> graph =
            new LinkedHashMap<>();

    public SmartNavigationApp() {
        createGraph();

        setTitle("Smart Navigation System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800, 600));
        setSize(1200, 750);
        setLocationRelativeTo(null);
        setResizable(true);

        createInterface();

        getContentPane().setBackground(background);
        setVisible(true);
    }

    // CREATE THE ROAD NETWORK
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

    // ADD A TWO-WAY ROAD
    private void addRoad(String first, String second, int km) {
        graph.get(first).put(second, km);
        graph.get(second).put(first, km);
    }

    // CREATE THE USER INTERFACE
    private void createInterface() {

        JPanel root = new JPanel(new BorderLayout(0, 25));
        root.setBackground(background);
        root.setBorder(new EmptyBorder(20, 20, 20, 20));

        // HEADER
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(background);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(
                new BoxLayout(titlePanel, BoxLayout.Y_AXIS)
        );
        titlePanel.setBackground(background);

        JLabel title = new JLabel("SMART NAVIGATION");
        title.setFont(new Font("Arial", Font.BOLD, 34));
        title.setForeground(lightPink);

        JLabel subtitle = new JLabel(
                "SYSTEM  |  Smarter. Faster. Better journeys."
        );
        subtitle.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitle.setForeground(new Color(185, 175, 205));

        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(8));
        titlePanel.add(subtitle);

        JLabel systemReady = new JLabel("● SYSTEM READY");
        systemReady.setFont(
                new Font("Monospaced", Font.BOLD, 13)
        );
        systemReady.setForeground(green);
        systemReady.setBorder(new CompoundBorder(
                new LineBorder(new Color(45, 120, 100), 1),
                new EmptyBorder(20, 16, 20, 16)
        ));

        header.add(titlePanel, BorderLayout.WEST);
        header.add(systemReady, BorderLayout.EAST);

        root.add(header, BorderLayout.NORTH);

        // CENTER AREA
        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(background);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 18, 0);

        // ROUTE PLANNER
        JPanel planner = createPanel("PLAN YOUR ROUTE");

        JPanel routeFields = new JPanel(
                new GridLayout(1, 2, 22, 0)
        );
        routeFields.setBackground(panel);

        // START LOCATION
        JPanel startPanel = new JPanel(
                new BorderLayout(0, 10)
        );
        startPanel.setBackground(panel);

        JLabel startLabel = new JLabel(
                "●  STARTING LOCATION"
        );
        startLabel.setFont(
                new Font("Arial", Font.BOLD, 13)
        );
        startLabel.setForeground(green);

        startBox = createLocationBox();

        startPanel.add(startLabel, BorderLayout.NORTH);
        startPanel.add(startBox, BorderLayout.CENTER);

        // DESTINATION
        JPanel destinationPanel = new JPanel(
                new BorderLayout(0, 10)
        );
        destinationPanel.setBackground(panel);

        JLabel destinationLabel = new JLabel(
                "●  DESTINATION"
        );
        destinationLabel.setFont(
                new Font("Arial", Font.BOLD, 13)
        );
        destinationLabel.setForeground(pink);

        destinationBox = createLocationBox();

        destinationPanel.add(
                destinationLabel,
                BorderLayout.NORTH
        );
        destinationPanel.add(
                destinationBox,
                BorderLayout.CENTER
        );

        routeFields.add(startPanel);
        routeFields.add(destinationPanel);

        // BUTTONS
        JButton findButton = new JButton(
                "FIND SHORTEST ROUTE"
        );
        styleButton(findButton, pink);

        JButton resetButton = new JButton("RESET");
        styleButton(resetButton, purple);

        JPanel buttons = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 12, 0)
        );
        buttons.setBackground(panel);
        buttons.add(findButton);
        buttons.add(resetButton);

        planner.add(routeFields);
        planner.add(Box.createVerticalStrut(22));
        planner.add(buttons);

        gbc.gridy = 0;
        gbc.weighty = 0;
        center.add(planner, gbc);

        // ROUTE INFORMATION
        JPanel information = createPanel("ROUTE INFORMATION");
        information.setLayout(
                new BorderLayout(0, 15)
        );

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setFont(
                new Font("Monospaced", Font.PLAIN, 14)
        );
        resultArea.setForeground(Color.WHITE);
        resultArea.setBackground(background);
        resultArea.setCaretColor(Color.WHITE);
        resultArea.setMargin(new Insets(20, 20, 20, 20));

        showWelcomeMessage();

        JScrollPane scroll = new JScrollPane(resultArea);
        scroll.setBorder(new LineBorder(lightPink, 1));
        scroll.getViewport().setBackground(background);

        information.add(scroll, BorderLayout.CENTER);

        // STATUS BAR
        JPanel bottomStatus = new JPanel(
                new GridLayout(1, 2)
        );
        bottomStatus.setBackground(panel);

        distanceLabel = new JLabel(
                "DISTANCE  |  NOT CALCULATED"
        );
        distanceLabel.setFont(
                new Font("Arial", Font.BOLD, 12)
        );
        distanceLabel.setForeground(lightPink);

        statusLabel = new JLabel("STATUS  |  READY");
        statusLabel.setFont(
                new Font("Arial", Font.BOLD, 12)
        );
        statusLabel.setForeground(green);
        statusLabel.setHorizontalAlignment(
                SwingConstants.RIGHT
        );

        bottomStatus.add(distanceLabel);
        bottomStatus.add(statusLabel);

        information.add(bottomStatus, BorderLayout.SOUTH);

        gbc.gridy = 1;
        gbc.weighty = 1;
        gbc.insets = new Insets(0, 0, 0, 0);

        center.add(information, gbc);

        root.add(center, BorderLayout.CENTER);

        // BUTTON ACTIONS
        findButton.addActionListener(
                e -> findShortestRoute()
        );

        resetButton.addActionListener(
                e -> resetForm()
        );

        getRootPane().setDefaultButton(findButton);

        setContentPane(root);
    }

    // REUSABLE PANEL
    private JPanel createPanel(String heading) {

        JPanel panelBox = new JPanel();

        panelBox.setLayout(
                new BoxLayout(panelBox, BoxLayout.Y_AXIS)
        );
        panelBox.setBackground(panel);

        panelBox.setBorder(new CompoundBorder(
                new LineBorder(purple, 1),
                new EmptyBorder(25, 25, 25, 25)
        ));

        JLabel label = new JLabel("▯  " + heading);
        label.setFont(
                new Font("Arial", Font.BOLD, 19)
        );
        label.setForeground(lightPink);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelBox.add(label);
        panelBox.add(Box.createVerticalStrut(25));

        return panelBox;
    }

    // LOCATION DROPDOWN
    private JComboBox<String> createLocationBox() {

        JComboBox<String> box =
                new JComboBox<>(locations);

        box.setFont(
                new Font("Arial", Font.PLAIN, 14)
        );
        box.setForeground(Color.BLACK);
        box.setBackground(Color.WHITE);
        box.setPreferredSize(new Dimension(300, 48));
        box.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 48)
        );

        box.setRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(
                    JList<?> list,
                    Object value,
                    int index,
                    boolean selected,
                    boolean focus) {

                JLabel label =
                        (JLabel) super.getListCellRendererComponent(
                                list,
                                value,
                                index,
                                selected,
                                focus
                        );

                label.setForeground(Color.BLACK);

                label.setBackground(
                        selected
                                ? new Color(255, 220, 240)
                                : Color.WHITE
                );

                return label;
            }
        });

        return box;
    }

    // BUTTON STYLING
    private void styleButton(
            JButton button,
            Color borderColor) {

        button.setFont(
                new Font("Arial", Font.BOLD, 12)
        );
        button.setForeground(Color.BLACK);
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorder(new CompoundBorder(
                new LineBorder(borderColor, 2),
                new EmptyBorder(12, 20, 12, 20)
        ));
        button.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );
    }

    // INITIAL MESSAGE
    private void showWelcomeMessage() {

        resultArea.setText(
                "Welcome to Smart Navigation System!\n\n"
                + "Select your starting location and destination.\n"
                + "Click FIND SHORTEST ROUTE to calculate the route.\n\n"
                + "Your route details will appear here."
        );

        resultArea.setCaretPosition(0);
    }

    // DIJKSTRA'S SHORTEST-PATH ALGORITHM
    private void findShortestRoute() {

        String start =
                (String) startBox.getSelectedItem();

        String destination =
                (String) destinationBox.getSelectedItem();

        if (start == null || destination == null) {

            resultArea.setText(
                    "Please select both locations."
            );

            statusLabel.setText(
                    "STATUS  |  INPUT REQUIRED"
            );

            return;
        }

        if (start.equals(destination)) {

            resultArea.setText(
                    "YOU ARE ALREADY AT YOUR DESTINATION\n\n"
                    + "Location: " + start + "\n"
                    + "Total distance: 0 km"
            );

            distanceLabel.setText("DISTANCE  |  0 KM");
            statusLabel.setText("STATUS  |  COMPLETE");

            return;
        }

        Map<String, Integer> distances =
                new HashMap<>();

        Map<String, String> previous =
                new HashMap<>();

        for (String location : locations) {
            distances.put(location, Integer.MAX_VALUE);
        }

        distances.put(start, 0);

        PriorityQueue<Node> queue =
                new PriorityQueue<>(
                        Comparator.comparingInt(
                                node -> node.distance
                        )
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
                int roadDistance = road.getValue();

                if (visited.contains(neighbour)) {
                    continue;
                }

                int newDistance =
                        distances.get(current) + roadDistance;

                if (newDistance < distances.get(neighbour)) {

                    distances.put(neighbour, newDistance);

                    previous.put(neighbour, current);

                    queue.add(
                            new Node(neighbour, newDistance)
                    );
                }
            }
        }

        if (distances.get(destination)
                == Integer.MAX_VALUE) {

            resultArea.setText(
                    "No route found between these locations."
            );

            distanceLabel.setText(
                    "DISTANCE  |  UNAVAILABLE"
            );

            statusLabel.setText(
                    "STATUS  |  NO ROUTE"
            );

            return;
        }

        // RECONSTRUCT THE SHORTEST ROUTE
        LinkedList<String> route = new LinkedList<>();

        String current = destination;

        while (current != null) {

            route.addFirst(current);
            current = previous.get(current);
        }

        // DISPLAY RESULTS
        StringBuilder output = new StringBuilder();

        output.append("✦ SHORTEST ROUTE FOUND\n");
        output.append(
                "========================================\n\n"
        );

        output.append("START       : ")
                .append(start)
                .append("\n");

        output.append("DESTINATION : ")
                .append(destination)
                .append("\n\n");

        output.append("YOUR ROUTE\n\n");

        for (int i = 0; i < route.size(); i++) {

            output.append(i + 1)
                    .append(". ")
                    .append(route.get(i))
                    .append("\n");

            if (i < route.size() - 1) {
                output.append("    |\n    v\n");
            }
        }

        output.append(
                "\n========================================\n"
        );

        output.append("TOTAL DISTANCE: ")
                .append(distances.get(destination))
                .append(" km\n\n");

        output.append(
                "Algorithm: Dijkstra's Shortest Path\n"
        );

        output.append(
                "Status: Route calculated successfully.\n\n"
        );

        output.append(
                "NOTE: Distances are sample values for "
                + "demonstration, not verified real-world "
                + "road distances."
        );

        resultArea.setText(output.toString());
        resultArea.setCaretPosition(0);

        distanceLabel.setText(
                "DISTANCE  |  "
                + distances.get(destination)
                + " KM"
        );

        statusLabel.setText(
                "STATUS  |  ROUTE FOUND"
        );
    }

    // RESET THE FORM
    private void resetForm() {

        startBox.setSelectedIndex(0);
        destinationBox.setSelectedIndex(0);

        showWelcomeMessage();

        distanceLabel.setText(
                "DISTANCE  |  NOT CALCULATED"
        );

        statusLabel.setText("STATUS  |  READY");
    }

    // PRIORITY QUEUE NODE
    private static class Node {

        private final String location;
        private final int distance;

        Node(String location, int distance) {
            this.location = location;
            this.distance = distance;
        }
    }

    // APPLICATION ENTRY POINT
    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                SmartNavigationApp::new
        );
    }
}