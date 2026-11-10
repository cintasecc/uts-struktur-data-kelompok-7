package id.putusan.dashboard.ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/** Shared visual language for the Swing interface. */
public final class UiStyle {
    static final Color NAVY = new Color(20, 35, 58);
    static final Color NAVY_LIGHT = new Color(35, 55, 82);
    static final Color TEAL = new Color(20, 145, 137);
    static final Color BG = new Color(244, 247, 251);
    static final Color TEXT = new Color(35, 48, 66);
    static final Color MUTED = new Color(111, 124, 143);
    static final Color BORDER = new Color(226, 232, 240);
    static final Color WHITE = Color.WHITE;
    private UiStyle() {}

    public static void install() {
        UIManager.put("Panel.background", BG);
        UIManager.put("OptionPane.background", WHITE);
        UIManager.put("OptionPane.messageForeground", TEXT);
        UIManager.put("Label.foreground", TEXT);
        UIManager.put("TextField.background", WHITE);
        UIManager.put("TextField.foreground", TEXT);
        UIManager.put("TextField.caretForeground", TEXT);
        UIManager.put("TextField.border", new CompoundBorder(new LineBorder(BORDER, 1, true), new EmptyBorder(7, 10, 7, 10)));
        UIManager.put("PasswordField.border", new CompoundBorder(new LineBorder(BORDER, 1, true), new EmptyBorder(7, 10, 7, 10)));
        UIManager.put("Button.background", WHITE);
        UIManager.put("Button.foreground", TEXT);
        UIManager.put("Button.border", new CompoundBorder(new LineBorder(BORDER, 1, true), new EmptyBorder(8, 14, 8, 14)));
        UIManager.put("Table.rowHeight", 34);
        UIManager.put("Table.gridColor", BORDER);
        UIManager.put("Table.selectionBackground", new Color(220, 242, 240));
        UIManager.put("Table.selectionForeground", TEXT);
        UIManager.put("TabbedPane.selected", WHITE);
    }

    static JPanel page(String title, String subtitle) {
        JPanel p = new JPanel(new BorderLayout(0, 18));
        p.setBackground(BG);
        p.setBorder(new EmptyBorder(24, 28, 24, 28));
        JPanel heading = new JPanel(); heading.setOpaque(false); heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        JLabel t = new JLabel(title); t.setFont(new Font("SansSerif", Font.BOLD, 25)); t.setForeground(TEXT);
        JLabel s = new JLabel(subtitle); s.setFont(new Font("SansSerif", Font.PLAIN, 13)); s.setForeground(MUTED);
        heading.add(t); heading.add(Box.createVerticalStrut(5)); heading.add(s);
        p.add(heading, BorderLayout.NORTH);
        return p;
    }

    static JPanel card() {
        JPanel p = new JPanel(); p.setBackground(WHITE);
        p.setBorder(new CompoundBorder(new LineBorder(BORDER, 1, true), new EmptyBorder(18, 18, 18, 18)));
        return p;
    }

    static JLabel muted(String text) { JLabel l = new JLabel(text); l.setForeground(MUTED); l.setFont(new Font("SansSerif", Font.PLAIN, 12)); return l; }
    static JButton primary(String text) { JButton b = new JButton(text); b.setBackground(TEAL); b.setForeground(WHITE); b.setFocusPainted(false); b.setBorder(new EmptyBorder(9, 16, 9, 16)); return b; }
    static void styleTable(JTable table) {
        table.setFont(new Font("SansSerif", Font.PLAIN, 12)); table.setForeground(TEXT); table.setBackground(WHITE);
        table.setFillsViewportHeight(true); table.setRowHeight(36); table.setShowVerticalLines(false); table.setGridColor(BORDER);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); table.setIntercellSpacing(new Dimension(0, 1));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12)); table.getTableHeader().setForeground(MUTED);
        table.getTableHeader().setBackground(new Color(248, 250, 252)); table.getTableHeader().setPreferredSize(new Dimension(0, 38));
    }
}
