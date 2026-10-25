
package navigation;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SmartNavigationApp extends JFrame {

    private JComboBox<String> startBox;
    private JComboBox<String> destinationBox;
    private JTextArea resultArea;
    private JLabel distanceLabel;
    private JLabel statusLabel;
    private JLabel systemLabel;
    private MapPanel mapPanel;
    private JButton findButton;
    private JButton resetButton;

    private CityMap cityMap;
    private boolean backendReady = false;

    private List<String> currentRoute = new ArrayList<>();

    private final Color background = new Color(8, 6, 17);
    private final Color panel = new Color(20, 15, 35);
    private final Color purple = new Color(145, 75, 210);
    private final Color pink = new Color(255, 75, 190);
    private final Color lightPink = new Color(220, 170, 255);
    private final Color green = new Color(65, 255, 190);
    private final Color muted = new Color(185, 175, 205);

    public SmartNavigationApp() {
        createInterface();

        setTitle("Smart Navigation System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900, 700));
        setSize(1250, 850);
        setLocationRelativeTo(null);

        setVisible(true);

        loadBackend();
    }

    // =====================================================
    // LOAD ACTUAL BACKEND
    // =====================================================

    private void loadBackend() {

        backendReady = false;

        findButton.setEnabled(false);
        resetButton.setEnabled(false);

        systemLabel.setText("● CONNECTING TO DATABASE");
        systemLabel.setForeground(pink);

        statusLabel.setText("STATUS | CONNECTING");

        resultArea.setText(
                "CONNECTING TO THE NAVIGATION DATABASE...\n\n"
                + "Loading locations and road connections from MySQL.\n"
                + "Please wait."
        );

        SwingWorker<CityMap, Void> worker =
                new SwingWorker<CityMap, Void>() {

            @Override
            protected CityMap doInBackground() {
                return new CityMap();
            }

            @Override
            protected void done() {

                try {
                    cityMap = get();

                    List<String> locations =
                            new ArrayList<>(cityMap.getLocations());

                    Collections.sort(locations);

                    if (locations.isEmpty()) {
                        throw new IllegalStateException(
                                "The database contains no locations."
                        );
                    }

                    DefaultComboBoxModel<String> model1 =
                            new DefaultComboBoxModel<>(
                                    locations.toArray(new String[0])
                            );

                    DefaultComboBoxModel<String> model2 =
                            new DefaultComboBoxModel<>(
                                    locations.toArray(new String[0])
                            );

                    startBox.setModel(model1);
                    destinationBox.setModel(model2);

                    if (locations.size() > 1) {
                        destinationBox.setSelectedIndex(1);
                    }

                    backendReady = true;

                    systemLabel.setText("● DATABASE CONNECTED");
                    systemLabel.setForeground(green);

                    findButton.setEnabled(true);
                    resetButton.setEnabled(true);

                    statusLabel.setText("STATUS | READY");

                    resultArea.setText(
                            "SMART NAVIGATION SYSTEM\n"
                            + "================================\n\n"
                            + "Database connection successful!\n\n"
                            + "Locations loaded: " + locations.size()
                            + "\n\n"
                            + "Select your starting location and "
                            + "destination.\n\n"
                            + "Click FIND SHORTEST ROUTE to calculate "
                            + "the shortest route using your backend."
                    );

                    mapPanel.repaint();

                } catch (InterruptedException ex) {

                    Thread.currentThread().interrupt();
                    showBackendError(
                            "Database loading was interrupted."
                    );

                } catch (ExecutionException ex) {

                    Throwable cause = ex.getCause();

                    String message = cause == null
                            ? ex.getMessage()
                            : cause.getMessage();

                    showBackendError(message);

                } catch (Exception ex) {

                    showBackendError(ex.getMessage());
                }
            }
        };

        worker.execute();
    }

    private void showBackendError(String message) {

        backendReady = false;

        systemLabel.setText("● DATABASE ERROR");
        systemLabel.setForeground(pink);

        statusLabel.setText("STATUS | CONNECTION FAILED");

        findButton.setEnabled(false);

        resultArea.setText(
                "COULD NOT LOAD THE NAVIGATION BACKEND\n"
                + "====================================\n\n"
                + "Reason:\n"
                + (message == null ? "Unknown error." : message)
                + "\n\nCheck the following:\n"
                + "1. MySQL is running.\n"
                + "2. DB_URL is configured.\n"
                + "3. DB_USER is configured.\n"
                + "4. DB_PASSWORD is configured.\n"
                + "5. The MySQL JDBC driver is available.\n"
                + "6. The places and roads tables exist.\n\n"
                + "Fix the configuration and restart the app."
        );

        JOptionPane.showMessageDialog(
                this,
                "Could not load the database.\n\n"
                + (message == null ? "Unknown error." : message),
                "Backend Connection Error",
                JOptionPane.ERROR_MESSAGE
        );

        mapPanel.repaint();
    }

    // =====================================================
    // INTERFACE
    // =====================================================

    private void createInterface() {

        JPanel root = new JPanel(new BorderLayout(15, 15));
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
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setForeground(lightPink);

        JLabel subtitle = new JLabel(
                "SYSTEM | Smarter. Faster. Better journeys."
        );
        subtitle.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitle.setForeground(muted);

        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(6));
        titlePanel.add(subtitle);

        systemLabel = new JLabel("● CONNECTING TO DATABASE");
        systemLabel.setFont(
                new Font("Monospaced", Font.BOLD, 12)
        );
        systemLabel.setForeground(pink);

        systemLabel.setBorder(new CompoundBorder(
                new LineBorder(new Color(45, 120, 100), 1),
                new EmptyBorder(12, 14, 12, 14)
        ));

        header.add(titlePanel, BorderLayout.WEST);
        header.add(systemLabel, BorderLayout.EAST);

        root.add(header, BorderLayout.NORTH);

        // MAIN CONTENT

        JPanel content = new JPanel(
                new GridLayout(1, 2, 15, 0)
        );
        content.setBackground(background);

        // LEFT SIDE

        JPanel left = new JPanel(
                new BorderLayout(0, 15)
        );
        left.setBackground(background);

        // ROUTE PLANNER

        JPanel planner = createPanel("PLAN YOUR ROUTE");

        planner.setLayout(
                new BoxLayout(planner, BoxLayout.Y_AXIS)
        );

        JLabel startLabel = new JLabel("STARTING LOCATION");
        styleLabel(startLabel, green);

        startBox = createLocationBox();

        JLabel destinationLabel = new JLabel("DESTINATION");
        styleLabel(destinationLabel, pink);

        destinationBox = createLocationBox();

        findButton = new JButton("FIND SHORTEST ROUTE");
        styleButton(findButton, pink);

        resetButton = new JButton("RESET");
        styleButton(resetButton, purple);

        JPanel buttons = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 8, 5)
        );
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

        // ROUTE INFORMATION

        JPanel information = createPanel("ROUTE INFORMATION");

        information.setLayout(
                new BorderLayout(0, 10)
        );

        resultArea = new JTextArea();

        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setFont(
                new Font("Monospaced", Font.PLAIN, 13)
        );
        resultArea.setForeground(Color.WHITE);
        resultArea.setBackground(background);
        resultArea.setMargin(new Insets(12, 12, 12, 12));

        JScrollPane scroll = new JScrollPane(resultArea);

        scroll.setBorder(new LineBorder(lightPink, 1));
        scroll.getViewport().setBackground(background);

        information.add(scroll, BorderLayout.CENTER);

        JPanel statusContainer = new JPanel(
                new GridLayout(2, 1, 0, 5)
        );

        statusContainer.setBackground(panel);
        statusContainer.setBorder(
                new EmptyBorder(8, 8, 8, 8)
        );

        distanceLabel = new JLabel(
                "DISTANCE | NOT CALCULATED"
        );
        styleLabel(distanceLabel, lightPink);

        statusLabel = new JLabel("STATUS | CONNECTING");
        styleLabel(statusLabel, green);

        statusContainer.add(distanceLabel);
        statusContainer.add(statusLabel);

        information.add(statusContainer, BorderLayout.SOUTH);

        left.add(information, BorderLayout.CENTER);

        // RIGHT SIDE: SCHEMATIC ROAD NETWORK

        JPanel mapContainer = createPanel(
                "CHENNAI ROAD NETWORK"
        );

        mapContainer.setLayout(
                new BorderLayout(0, 8)
        );

        mapPanel = new MapPanel();

        mapContainer.add(mapPanel, BorderLayout.CENTER);

        JLabel mapNote = new JLabel(
                "SCHEMATIC NETWORK | Route based on database roads"
        );

        mapNote.setForeground(lightPink);
        mapNote.setFont(
                new Font("Arial", Font.PLAIN, 11)
        );

        mapNote.setBorder(
                new EmptyBorder(8, 2, 2, 2)
        );

        mapContainer.add(mapNote, BorderLayout.SOUTH);

        content.add(left);
        content.add(mapContainer);

        root.add(content, BorderLayout.CENTER);

        findButton.addActionListener(
                e -> findShortestRoute()
        );

        resetButton.addActionListener(
                e -> resetForm()
        );

        setContentPane(root);

        showWelcomeMessage();
    }

    private JPanel createPanel(String heading) {

        JPanel panelBox = new JPanel();

        panelBox.setLayout(
                new BorderLayout(0, 12)
        );

        panelBox.setBackground(panel);

        panelBox.setBorder(new CompoundBorder(
                new LineBorder(purple, 1),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel label = new JLabel(heading);

        label.setFont(
                new Font("Arial", Font.BOLD, 17)
        );

        label.setForeground(lightPink);

        panelBox.add(label, BorderLayout.NORTH);

        return panelBox;
    }

    private void styleLabel(JLabel label, Color color) {

        label.setFont(
                new Font("Arial", Font.BOLD, 12)
        );

        label.setForeground(color);
    }

    private JComboBox<String> createLocationBox() {

        JComboBox<String> box = new JComboBox<>();

        box.setFont(
                new Font("Arial", Font.PLAIN, 13)
        );

        box.setPreferredSize(
                new Dimension(260, 38)
        );

        box.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 38)
        );

        box.setBackground(Color.WHITE);
        box.setForeground(Color.BLACK);

        return box;
    }

    private void styleButton(
            JButton button,
            Color borderColor) {

        button.setFont(
                new Font("Arial", Font.BOLD, 11)
        );

        button.setForeground(Color.BLACK);
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setOpaque(true);

        button.setBorder(new CompoundBorder(
                new LineBorder(borderColor, 2),
                new EmptyBorder(10, 12, 10, 12)
        ));

        button.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );
    }

    private void showWelcomeMessage() {

        resultArea.setText(
                "WELCOME TO SMART NAVIGATION!\n\n"
                + "Connecting to your navigation backend...\n\n"
                + "Locations and road distances will be loaded "
                + "from MySQL.\n\n"
                + "The shortest route will be calculated using "
                + "your backend's Dijkstra algorithm."
        );

        resultArea.setCaretPosition(0);
    }

    // =====================================================
    // SHORTEST ROUTE USING FRIEND'S DIJKSTRA CLASS
    // =====================================================

    private void findShortestRoute() {

        if (!backendReady || cityMap == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "The backend is not connected yet.",
                    "Backend Unavailable",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String start =
                (String) startBox.getSelectedItem();

        String destination =
                (String) destinationBox.getSelectedItem();

        if (start == null || destination == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select both locations.",
                    "Missing Location",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        try {

            Dijkstra.RouteResult result =
                    Dijkstra.calculateShortestPath(
                            cityMap,
                            start,
                            destination
                    );

            if (result == null) {

                currentRoute.clear();

                resultArea.setText(
                        "NO ROUTE FOUND\n"
                        + "==============================\n\n"
                        + "From: " + start + "\n"
                        + "To: " + destination + "\n\n"
                        + "There is no connected route between "
                        + "these locations in the loaded road network."
                );

                distanceLabel.setText(
                        "DISTANCE | UNAVAILABLE"
                );

                statusLabel.setText(
                        "STATUS | NO ROUTE"
                );

                mapPanel.repaint();

                return;
            }

            currentRoute = new ArrayList<>(
                    result.getPath()
            );

            StringBuilder output = new StringBuilder();

            output.append("SHORTEST ROUTE FOUND\n");
            output.append(
                    "================================\n\n"
            );

            output.append("START: ")
                    .append(start)
                    .append("\n\n");

            List<String> route = result.getPath();

            for (int i = 0; i < route.size(); i++) {

                output.append(i + 1)
                        .append(". ")
                        .append(route.get(i))
                        .append("\n");

                if (i < route.size() - 1) {
                    output.append("   |\n   v\n");
                }
            }

            output.append("\nDESTINATION: ")
                    .append(destination)
                    .append("\n\n");

            output.append("TOTAL DISTANCE: ")
                    .append(
                            String.format(
                                    Locale.US,
                                    "%.2f",
                                    result.getDistance()
                            )
                    )
                    .append(" km\n\n");

            output.append(
                    "Algorithm: Backend Dijkstra\n"
            );

            output.append(
                    "Source: MySQL road network\n\n"
            );

            output.append(
                    "Distance reflects the weights stored "
                    + "in the database."
            );

            resultArea.setText(output.toString());
            resultArea.setCaretPosition(0);

            distanceLabel.setText(
                    "DISTANCE | "
                    + String.format(
                            Locale.US,
                            "%.2f",
                            result.getDistance()
                    )
                    + " KM"
            );

            statusLabel.setText(
                    "STATUS | ROUTE FOUND"
            );

            mapPanel.repaint();

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not calculate the route:\n"
                    + ex.getMessage(),
                    "Route Calculation Error",
                    JOptionPane.ERROR_MESSAGE
            );

            statusLabel.setText(
                    "STATUS | ROUTE ERROR"
            );
        }
    }

    // =====================================================
    // RESET
    // =====================================================

    private void resetForm() {

        if (startBox.getItemCount() > 0) {
            startBox.setSelectedIndex(0);
        }

        if (destinationBox.getItemCount() > 0) {

            destinationBox.setSelectedIndex(
                    destinationBox.getItemCount() > 1 ? 1 : 0
            );
        }

        currentRoute.clear();

        showWelcomeMessage();

        distanceLabel.setText(
                "DISTANCE | NOT CALCULATED"
        );

        statusLabel.setText(
                backendReady
                        ? "STATUS | READY"
                        : "STATUS | DATABASE ERROR"
        );

        mapPanel.repaint();
    }

    // =====================================================
    // SCHEMATIC ROAD NETWORK
    // =====================================================

    private class MapPanel extends JPanel {

        MapPanel() {

            setBackground(
                    new Color(12, 18, 32)
            );

            setPreferredSize(
                    new Dimension(480, 520)
            );
        }

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 =
                    (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int width = getWidth();
            int height = getHeight();

            drawGrid(g2, width, height);

            if (!backendReady || cityMap == null) {

                g2.setColor(Color.WHITE);

                g2.setFont(
                        new Font("Arial", Font.BOLD, 14)
                );

                g2.drawString(
                        "Waiting for backend...",
                        20,
                        35
                );

                g2.dispose();

                return;
            }

            List<String> locations =
                    new ArrayList<>(
                            cityMap.getLocations()
                    );

            Collections.sort(locations);

            if (locations.isEmpty()) {

                g2.setColor(Color.WHITE);

                g2.drawString(
                        "No locations available",
                        20,
                        35
                );

                g2.dispose();

                return;
            }

            Map<String, Point> points =
                    calculatePoints(
                            locations,
                            width,
                            height
                    );

            drawRoads(g2, points);
            drawRoute(g2, points);
            drawLocations(g2, points, locations);

            g2.dispose();
        }

        private void drawGrid(
                Graphics2D g2,
                int width,
                int height) {

            g2.setColor(
                    new Color(25, 34, 52)
            );

            for (int x = 0; x < width; x += 35) {
                g2.drawLine(x, 0, x, height);
            }

            for (int y = 0; y < height; y += 35) {
                g2.drawLine(0, y, width, y);
            }
        }

        private Map<String, Point> calculatePoints(
                List<String> locations,
                int width,
                int height) {

            Map<String, Point> points =
                    new LinkedHashMap<>();

            int count = locations.size();

            int centerX = width / 2;
            int centerY = height / 2;

            int radiusX = Math.max(
                    35,
                    width / 2 - 75
            );

            int radiusY = Math.max(
                    35,
                    height / 2 - 75
            );

            for (int i = 0; i < count; i++) {

                double angle =
                        -Math.PI / 2
                        + (2.0 * Math.PI * i / count);

                int x = centerX
                        + (int) (radiusX * Math.cos(angle));

                int y = centerY
                        + (int) (radiusY * Math.sin(angle));

                points.put(
                        locations.get(i),
                        new Point(x, y)
                );
            }

            return points;
        }

        private void drawRoads(
                Graphics2D g2,
                Map<String, Point> points) {

            Map<String, Map<String, Double>> roads =
                    cityMap.getRoads();

            g2.setStroke(
                    new BasicStroke(1.5f)
            );

            g2.setColor(
                    new Color(95, 75, 125)
            );

            for (Map.Entry<String, Map<String, Double>> entry
                    : roads.entrySet()) {

                String from = entry.getKey();

                Point p1 = points.get(from);

                if (p1 == null) {
                    continue;
                }

                for (String to : entry.getValue().keySet()) {

                    Point p2 = points.get(to);

                    if (p2 == null) {
                        continue;
                    }

                    // Draw each undirected connection once.
                    // Directed connections are also represented
                    // as a schematic line.
                    if (from.compareTo(to) < 0) {
                        g2.drawLine(
                                p1.x,
                                p1.y,
                                p2.x,
                                p2.y
                        );
                    }
                }
            }
        }

        private void drawRoute(
                Graphics2D g2,
                Map<String, Point> points) {

            if (currentRoute.size() < 2) {
                return;
            }

            g2.setColor(green);

            g2.setStroke(
                    new BasicStroke(
                            4.5f,
                            BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND
                    )
            );

            for (int i = 0;
                    i < currentRoute.size() - 1;
                    i++) {

                Point p1 = points.get(
                        currentRoute.get(i)
                );

                Point p2 = points.get(
                        currentRoute.get(i + 1)
                );

                if (p1 != null && p2 != null) {

                    g2.drawLine(
                            p1.x,
                            p1.y,
                            p2.x,
                            p2.y
                    );
                }
            }
        }

        private void drawLocations(
                Graphics2D g2,
                Map<String, Point> points,
                List<String> locations) {

            for (String location : locations) {

                Point point = points.get(location);

                if (point == null) {
                    continue;
                }

                boolean selected =
                        currentRoute.contains(location);

                g2.setColor(
                        selected ? green : pink
                );

                g2.fillOval(
                        point.x - 6,
                        point.y - 6,
                        12,
                        12
                );

                g2.setColor(Color.WHITE);

                g2.setFont(
                        new Font("Arial", Font.PLAIN, 10)
                );

                String label = location;

                int maximumLabelLength = 22;

                if (label.length() > maximumLabelLength) {
                    label = label.substring(
                            0,
                            maximumLabelLength - 3
                    ) + "...";
                }

                int labelX = point.x + 9;

                if (labelX + 130 > getWidth()) {
                    labelX = point.x - 125;
                }

                int labelY = point.y - 8;

                g2.drawString(
                        label,
                        labelX,
                        labelY
                );
            }
        }
    }

    // =====================================================
    // MAIN METHOD
    // =====================================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                SmartNavigationApp::new
        );
    }
}
