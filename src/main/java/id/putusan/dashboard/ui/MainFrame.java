package id.putusan.dashboard.ui;

import id.putusan.dashboard.repository.PutusanRepository;
import id.putusan.dashboard.service.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/** Main application shell with persistent sidebar navigation. */
public class MainFrame extends JFrame {
    private final CardLayout cards = new CardLayout();
    private final JPanel content = new JPanel(cards);
    private final Map<String, JButton> navButtons = new LinkedHashMap<>();
    private AuditService auditService;

    public MainFrame(String user, String role) {
        setTitle("Putusan Dashboard | Legal Case Management");
        setDefaultCloseOperation(EXIT_ON_CLOSE); setMinimumSize(new Dimension(1050, 680)); setSize(1280, 800); setLocationRelativeTo(null);

        PutusanRepository repo = new PutusanRepository(); AuditService audit = new AuditService();
        this.auditService = audit;
        PutusanService service = new PutusanService(repo, audit);
        try { repo.load(Path.of("data", "putusan-50.csv")); }
        catch (Exception ex) { JOptionPane.showMessageDialog(this, "Gagal memuat dataset: " + ex.getMessage(), "Peringatan", JOptionPane.WARNING_MESSAGE); }
        audit.record(user, "LOGIN", "Role " + role);

        content.setBackground(UiStyle.BG);
        addPage("Overview", new DashboardPanel(repo, audit));
        addPage("Data Putusan", new DataPanel(repo, service, user, role));
        addPage("Pencarian", new SearchPanel(service));
        addPage("Filter Data", new FilterPanel(service));
        addPage("Statistik", new StatistikPanel(repo));
        addPage("Audit Log", new AuditPanel(audit));
        addPage("Import PDF", new ImportPanel());
        addPage("Ekspor Data", new ExportPanel(repo));

        JPanel shell = new JPanel(new BorderLayout()); shell.add(sidebar(user, role), BorderLayout.WEST);
        JPanel right = new JPanel(new BorderLayout()); right.setBackground(UiStyle.BG); right.add(topbar(user, role), BorderLayout.NORTH); right.add(content, BorderLayout.CENTER);
        shell.add(right, BorderLayout.CENTER); setContentPane(shell); showPage("Overview");
    }

    private void addPage(String name, JPanel panel) { content.add(panel, name); }

    private JPanel sidebar(String user, String role) {
        JPanel side = new JPanel(new BorderLayout(0, 20)); side.setBackground(UiStyle.NAVY); side.setPreferredSize(new Dimension(238, 0)); side.setBorder(new EmptyBorder(24, 16, 20, 16));
        JPanel brand = new JPanel(); brand.setOpaque(false); brand.setLayout(new BoxLayout(brand, BoxLayout.Y_AXIS));
        JLabel mark = new JLabel("PD"); mark.setOpaque(true); mark.setBackground(UiStyle.TEAL); mark.setForeground(Color.WHITE); mark.setFont(new Font("SansSerif", Font.BOLD, 19)); mark.setHorizontalAlignment(SwingConstants.CENTER); mark.setPreferredSize(new Dimension(42,42)); mark.setMaximumSize(new Dimension(42,42));
        JLabel name = new JLabel("PUTUSAN"); name.setForeground(Color.WHITE); name.setFont(new Font("SansSerif", Font.BOLD, 18));
        JLabel sub = new JLabel("LEGAL CASE DASHBOARD"); sub.setForeground(new Color(166,181,201)); sub.setFont(new Font("SansSerif", Font.PLAIN, 9));
        brand.add(mark); brand.add(Box.createVerticalStrut(13)); brand.add(name); brand.add(Box.createVerticalStrut(3)); brand.add(sub);
        JPanel menu = new JPanel(); menu.setOpaque(false); menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        JLabel section = new JLabel("WORKSPACE"); section.setForeground(new Color(137,155,179)); section.setFont(new Font("SansSerif", Font.BOLD, 10)); section.setBorder(new EmptyBorder(12, 10, 10, 0)); menu.add(section);
        String[][] items = {{"Overview","▦"},{"Data Putusan","▤"},{"Pencarian","⌕"},{"Filter Data","☷"},{"Statistik","▥"},{"Audit Log","◷"},{"Import PDF","⇧"},{"Ekspor Data","⇩"}};
        for (String[] item : items) {
            JButton b = new JButton(item[1] + "    " + item[0]); b.setHorizontalAlignment(SwingConstants.LEFT); b.setForeground(new Color(207,218,232)); b.setBackground(UiStyle.NAVY); b.setBorder(new EmptyBorder(11, 12, 11, 8)); b.setFocusPainted(false); b.setFont(new Font("SansSerif", Font.PLAIN, 13)); b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 43)); b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            b.addActionListener(e -> showPage(item[0])); navButtons.put(item[0], b); menu.add(b); menu.add(Box.createVerticalStrut(4));
        }
        JPanel bottom = new JPanel(new BorderLayout(0,5)); bottom.setOpaque(false); bottom.setBorder(new EmptyBorder(12, 10, 0, 5));
        JLabel line = new JLabel("────────────────────"); line.setForeground(new Color(76,94,119));
        JLabel app = new JLabel("Java Swing  •  v1.0"); app.setForeground(new Color(137,155,179)); app.setFont(new Font("SansSerif", Font.PLAIN, 11)); bottom.add(line, BorderLayout.NORTH); bottom.add(app, BorderLayout.SOUTH);
        side.add(brand, BorderLayout.NORTH); side.add(menu, BorderLayout.CENTER); side.add(bottom, BorderLayout.SOUTH); return side;
    }

    private JPanel topbar(String user, String role) {
        JPanel p = new JPanel(new BorderLayout()); p.setBackground(Color.WHITE); p.setBorder(new EmptyBorder(14, 28, 14, 28)); p.setBorder(BorderFactory.createMatteBorder(0,0,1,0,UiStyle.BORDER));
        JLabel crumb = new JLabel("Workspace  /  Overview"); crumb.setForeground(UiStyle.MUTED); crumb.setFont(new Font("SansSerif", Font.PLAIN, 12));
        JPanel profile = new JPanel(new FlowLayout(FlowLayout.RIGHT, 9, 0)); profile.setOpaque(false);
        JLabel avatar = new JLabel(user.isBlank()?"U":user.substring(0,1).toUpperCase()); avatar.setOpaque(true); avatar.setBackground(new Color(220,242,240)); avatar.setForeground(UiStyle.TEAL); avatar.setHorizontalAlignment(SwingConstants.CENTER); avatar.setFont(new Font("SansSerif", Font.BOLD, 13)); avatar.setPreferredSize(new Dimension(32,32));
        JPanel who = new JPanel(); who.setOpaque(false); who.setLayout(new BoxLayout(who,BoxLayout.Y_AXIS)); JLabel u = new JLabel(user); u.setFont(new Font("SansSerif",Font.BOLD,12)); JLabel r = new JLabel(role); r.setForeground(UiStyle.MUTED); r.setFont(new Font("SansSerif",Font.PLAIN,10)); who.add(u); who.add(r);
        profile.add(avatar); profile.add(who);
        JButton logout = new JButton("Keluar");
        logout.setFocusPainted(false);
        logout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logout.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this,
                "Yakin ingin keluar dari dashboard?", "Konfirmasi Logout",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (choice == JOptionPane.YES_OPTION) {
                auditService.record(user, "LOGOUT", "Role " + role);
                try {
                    Path logFile = Path.of("logs", "audit-log.txt");
                    Files.createDirectories(logFile.getParent());
                    String safeUser = user.replace("\n", " ").replace("\r", " ");
                    Files.writeString(logFile, LocalDateTime.now() + " | " + safeUser
                                    + " | LOGOUT | Role " + role + System.lineSeparator(),
                            StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Sesi ditutup, tetapi catatan logout gagal disimpan: " + ex.getMessage(),
                            "Peringatan audit", JOptionPane.WARNING_MESSAGE);
                }
                dispose();
                SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
            }
        });
        profile.add(logout);
        p.add(crumb,BorderLayout.WEST); p.add(profile,BorderLayout.EAST); return p;
    }

    private void showPage(String name) {
        cards.show(content, name);
        for (Map.Entry<String,JButton> entry : navButtons.entrySet()) {
            boolean active = entry.getKey().equals(name); JButton b = entry.getValue();
            b.setBackground(active ? UiStyle.NAVY_LIGHT : UiStyle.NAVY); b.setForeground(active ? Color.WHITE : new Color(207,218,232));
        }
    }
}
