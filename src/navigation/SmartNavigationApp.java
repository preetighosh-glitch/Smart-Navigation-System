package navigation;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class SmartNavigationApp extends JFrame {

    private JTextField sourceField;
    private JTextField destinationField;
    private JTextArea routeArea;
    private JLabel distanceLabel;
    private JLabel statusLabel;

    private final Color BG = new Color(8, 10, 28);
    private final Color PANEL = new Color(15, 19, 48);
    private final Color PURPLE = new Color(160, 70, 255);
    private final Color CYAN = new Color(0, 225, 255);
    private final Color PINK = new Color(255, 55, 220);
    private final Color TEXT = new Color(235, 235, 255);
    private final Color MUTED = new Color(160, 170, 210);

    public SmartNavigationApp() {

        setTitle("Smart Navigation System");
        setSize(1100, 720);
        setMinimumSize(new Dimension(850, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);

        root.add(createSidebar(), BorderLayout.WEST);
        root.add(createMainContent(), BorderLayout.CENTER);

        setContentPane(root);
    }

    private JPanel createSidebar() {

        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(210, 0));
        sidebar.setBackground(new Color(10, 12, 34));
        sidebar.setBorder(new MatteBorder(0, 0, 0, 1, PURPLE));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        JLabel logo = new JLabel(" ◈ ");
        logo.setFont(new Font("Arial", Font.BOLD, 48));
        logo.setForeground(CYAN);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel brand = new JLabel(
                "<html><center>SMART<br>NAVIGATION<br>"
                + "<font size='4'>SYSTEM</font></center></html>"
        );
        brand.setFont(new Font("Arial", Font.BOLD, 23));
        brand.setForeground(PURPLE);
        brand.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebar.add(Box.createVerticalStrut(30));
        sidebar.add(logo);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(brand);
        sidebar.add(Box.createVerticalStrut(45));

        sidebar.add(createNavButton("⌂   HOME", true));
        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(createNavButton("◎   NAVIGATION", false));
        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(createNavButton("◷   HISTORY", false));
        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(createNavButton("ⓘ   ABOUT", false));

        sidebar.add(Box.createVerticalGlue());

        JLabel footer = new JLabel(
                "<html><center>BETTER ROUTES<br>"
                + "BRIGHTER JOURNEYS<br><br>"
                + "✦ MADE FOR YOUR NEXT ADVENTURE</center></html>"
        );
        footer.setFont(new Font("Arial", Font.PLAIN, 11));
        footer.setForeground(CYAN);
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebar.add(footer);
        sidebar.add(Box.createVerticalStrut(25));

        return sidebar;
    }

    private JButton createNavButton(String text, boolean active) {

        JButton button = new JButton(text);

        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setForeground(active ? Color.WHITE : MUTED);
        button.setBackground(active ? new Color(78, 35, 155) : BG);
        button.setBorder(new EmptyBorder(15, 18, 15, 10));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(195, 48));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setForeground(CYAN);
            }

            public void mouseExited(MouseEvent e) {
                button.setForeground(active ? Color.WHITE : MUTED);
            }
        });

        button.addActionListener(e -> {
            if (text.contains("ABOUT")) {
                JOptionPane.showMessageDialog(
                        this,
                        "Smart Navigation System\n"
                        + "A Java-based navigation project.\n\n"
                        + "Route calculation requires backend integration.",
                        "About",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else if (text.contains("HISTORY")) {
                JOptionPane.showMessageDialog(
                        this,
                        "Route history will be available in a future version.",
                        "History",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else if (text.contains("NAVIGATION")) {
                sourceField.requestFocusInWindow();
            }
        });

        return button;
    }

    private JPanel createMainContent() {

        JPanel content = new JPanel(new BorderLayout(18, 18));
        content.setBackground(BG);
        content.setBorder(new EmptyBorder(25, 25, 25, 25));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG);

        JLabel title = new JLabel("PLAN YOUR ROUTE");
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setForeground(CYAN);

        JLabel subtitle = new JLabel(
                "Smarter. Faster. Together.  ✦"
        );
        subtitle.setFont(new Font("Arial", Font.PLAIN, 15));
        subtitle.setForeground(MUTED);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(BG);
        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(8));
        titlePanel.add(subtitle);

        JLabel ready = new JLabel("  ● SYSTEM READY  ");
        ready.setFont(new Font("Arial", Font.BOLD, 12));
        ready.setForeground(CYAN);
        ready.setBorder(new CompoundBorder(
                new LineBorder(new Color(65, 85, 180), 1, true),
                new EmptyBorder(10, 10, 10, 10)
        ));

        header.add(titlePanel, BorderLayout.WEST);
        header.add(ready, BorderLayout.EAST);

        JPanel center = new JPanel();
        center.setBackground(BG);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        center.add(createRoutePanel());
        center.add(Box.createVerticalStrut(18));
        center.add(createResultsPanel());
        center.add(Box.createVerticalStrut(18));
        center.add(createFeaturePanel());

        JScrollPane scroll = new JScrollPane(center);
        scroll.setBorder(null);
        scroll.setBackground(BG);
        scroll.getViewport().setBackground(BG);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        content.add(header, BorderLayout.NORTH);
        content.add(scroll, BorderLayout.CENTER);

        return content;
    }

    private JPanel createRoutePanel() {

        JPanel panel = new JPanel(new BorderLayout(12, 15));
        panel.setBackground(PANEL);
        panel.setBorder(new CompoundBorder(
                new LineBorder(PURPLE, 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel heading = new JLabel("◈  ROUTE DETAILS");
        heading.setFont(new Font("Arial", Font.BOLD, 18));
        heading.setForeground(PINK);

        JPanel fields = new JPanel(new GridLayout(1, 2, 15, 10));
        fields.setBackground(PANEL);

        sourceField = createInputField("e.g. Sathyabama University");
        destinationField = createInputField("e.g. Marina Beach");

        fields.add(createFieldPanel("STARTING LOCATION", sourceField, CYAN));
        fields.add(createFieldPanel("DESTINATION", destinationField, PINK));

        JButton findButton = createActionButton("➤   FIND ROUTE", PURPLE);
        JButton resetButton = createActionButton("↻   RESET", PANEL);

        JPanel actions = new JPanel(new GridLayout(1, 2, 15, 10));
        actions.setBackground(PANEL);
        actions.add(findButton);
        actions.add(resetButton);

        findButton.addActionListener(e -> findRoute());
        resetButton.addActionListener(e -> resetFields());

        JPanel body = new JPanel(new BorderLayout(15, 18));
        body.setBackground(PANEL);
        body.add(fields, BorderLayout.CENTER);
        body.add(actions, BorderLayout.SOUTH);

        panel.add(heading, BorderLayout.NORTH);
        panel.add(body, BorderLayout.CENTER);

        return panel;
    }

    private JTextField createInputField(String placeholder) {

        JTextField field = new JTextField();
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setForeground(TEXT);
        field.setCaretColor(CYAN);
        field.setBackground(BG);
        field.setBorder(new CompoundBorder(
                new LineBorder(new Color(75, 85, 190), 1, true),
                new EmptyBorder(12, 12, 12, 12)
        ));
        field.setToolTipText(placeholder);

        return field;
    }

    private JPanel createFieldPanel(
            String labelText, JTextField field, Color accent) {

        JPanel panel = new JPanel(new BorderLayout(5, 10));
        panel.setBackground(PANEL);

        JLabel label = new JLabel("●  " + labelText);
        label.setFont(new Font("Arial", Font.BOLD, 11));
        label.setForeground(accent);

        panel.add(label, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);

        return panel;
    }

    private JButton createActionButton(String text, Color accent) {

        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(accent);
        button.setBorder(new CompoundBorder(
                new LineBorder(accent, 1, true),
                new EmptyBorder(12, 15, 12, 15)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setForeground(CYAN);
            }

            public void mouseExited(MouseEvent e) {
                button.setForeground(Color.WHITE);
            }
        });

        return button;
    }

    private JPanel createResultsPanel() {

        JPanel panel = new JPanel(new BorderLayout(10, 12));
        panel.setBackground(PANEL);
        panel.setBorder(new CompoundBorder(
                new LineBorder(new Color(70, 55, 170), 1, true),
                new EmptyBorder(18, 18, 18, 18)
        ));

        JLabel heading = new JLabel("◈  ROUTE INFORMATION");
        heading.setFont(new Font("Arial", Font.BOLD, 17));
        heading.setForeground(CYAN);

        routeArea = new JTextArea(
                "Your journey starts here.\n\n"
                + "Enter your starting location and destination,\n"
                + "then click FIND ROUTE to continue."
        );

        routeArea.setEditable(false);
        routeArea.setLineWrap(true);
        routeArea.setWrapStyleWord(true);
        routeArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        routeArea.setForeground(TEXT);
        routeArea.setBackground(BG);
        routeArea.setCaretColor(CYAN);
        routeArea.setBorder(new EmptyBorder(15, 15, 15, 15));

        JScrollPane scroll = new JScrollPane(routeArea);
        scroll.setBorder(new LineBorder(new Color(60, 70, 150), 1, true));
        scroll.setPreferredSize(new Dimension(400, 155));

        distanceLabel = new JLabel("DISTANCE  |  NOT CALCULATED");
        distanceLabel.setForeground(PINK);
        distanceLabel.setFont(new Font("Arial", Font.BOLD, 12));

        statusLabel = new JLabel("STATUS  |  READY");
        statusLabel.setForeground(CYAN);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 12));

        JPanel stats = new JPanel(new GridLayout(1, 2, 12, 0));
        stats.setBackground(PANEL);
        stats.add(createStatCard("◈", distanceLabel, PURPLE));
        stats.add(createStatCard("⌁", statusLabel, CYAN));

        panel.add(heading, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(stats, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createStatCard(
            String icon, JLabel label, Color accent) {

        JPanel card = new JPanel(new BorderLayout(10, 5));
        card.setBackground(BG);
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(55, 65, 140), 1, true),
                new EmptyBorder(12, 12, 12, 12)
        ));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Arial", Font.BOLD, 22));
        iconLabel.setForeground(accent);

        card.add(iconLabel, BorderLayout.WEST);
        card.add(label, BorderLayout.CENTER);

        return card;
    }

    private JPanel createFeaturePanel() {

        JPanel panel = new JPanel(new GridLayout(1, 3, 12, 0));
        panel.setBackground(BG);

        panel.add(createFeatureCard(
                "⚡ FAST & EASY",
                "Plan your journey",
                CYAN
        ));

        panel.add(createFeatureCard(
                "◈ SMART ROUTING",
                "Backend integration pending",
                PURPLE
        ));

        panel.add(createFeatureCard(
                "✦ COMING SOON",
                "Live route information",
                PINK
        ));

        return panel;
    }

    private JPanel createFeatureCard(
            String heading, String description, Color accent) {

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(PANEL);
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(65, 60, 140), 1, true),
                new EmptyBorder(14, 12, 14, 12)
        ));

        JLabel title = new JLabel(heading);
        title.setFont(new Font("Arial", Font.BOLD, 12));
        title.setForeground(accent);

        JLabel desc = new JLabel(
                "<html><body style='width:130px'>"
                + description + "</body></html>"
        );
        desc.setFont(new Font("Arial", Font.PLAIN, 11));
        desc.setForeground(MUTED);

        card.add(title);
        card.add(Box.createVerticalStrut(8));
        card.add(desc);

        return card;
    }

    private void findRoute() {

        String source = sourceField.getText().trim();
        String destination = destinationField.getText().trim();

        if (source.isEmpty() || destination.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter both locations.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE
            );

            statusLabel.setText("STATUS  |  INPUT REQUIRED");
            return;
        }

        if (source.equalsIgnoreCase(destination)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Starting location and destination must differ.",
                    "Invalid Route",
                    JOptionPane.WARNING_MESSAGE
            );

            statusLabel.setText("STATUS  |  INVALID INPUT");
            return;
        }

        routeArea.setText(
                "SOURCE      : " + source + "\n\n"
                + "DESTINATION : " + destination + "\n\n"
                + "REQUEST     : ACCEPTED\n\n"
                + "The interface has received your request.\n"
                + "Connect the navigation backend to calculate\n"
                + "the actual route and distance."
        );

        distanceLabel.setText("DISTANCE  |  AWAITING BACKEND");
        statusLabel.setText("STATUS  |  INPUT VALIDATED");
    }

    private void resetFields() {

        sourceField.setText("");
        destinationField.setText("");

        routeArea.setText(
                "Your journey starts here.\n\n"
                + "Enter your starting location and destination,\n"
                + "then click FIND ROUTE to continue."
        );

        distanceLabel.setText("DISTANCE  |  NOT CALCULATED");
        statusLabel.setText("STATUS  |  READY");
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName()
                );
            } catch (Exception ignored) {
                // Keep the default Swing look and feel.
            }

            SmartNavigationApp app = new SmartNavigationApp();
            app.setVisible(true);
        });
    }
}