package id.putusan.dashboard.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/** Demo login screen. Authentication remains local/demo as documented. */
public class LoginFrame extends JFrame {
    public LoginFrame() {
        setTitle("Putusan Dashboard | Sign in"); setSize(850, 540); setMinimumSize(new Dimension(760, 480)); setLocationRelativeTo(null); setDefaultCloseOperation(EXIT_ON_CLOSE);
        JPanel root = new JPanel(new GridLayout(1,2)); root.setBackground(Color.WHITE);
        JPanel hero = new JPanel(); hero.setBackground(UiStyle.NAVY); hero.setBorder(new EmptyBorder(55,42,45,38)); hero.setLayout(new BoxLayout(hero,BoxLayout.Y_AXIS));
        JLabel logo = new JLabel("PD  /  LEGAL TECHNOLOGY"); logo.setForeground(new Color(105,214,202)); logo.setFont(new Font("SansSerif",Font.BOLD,12));
        JLabel headline = new JLabel("PUTUSAN<br>DASHBOARD"); headline.setForeground(Color.WHITE); headline.setFont(new Font("SansSerif",Font.BOLD,31));
        JLabel desc = new JLabel("<html>Kelola data putusan perkara narkotika<br>dalam satu ruang kerja yang terstruktur,<br>terukur, dan mudah ditelusuri.</html>"); desc.setForeground(new Color(190,203,220)); desc.setFont(new Font("SansSerif",Font.PLAIN,14));
        hero.add(logo); hero.add(Box.createVerticalStrut(65)); hero.add(headline); hero.add(Box.createVerticalStrut(18)); hero.add(desc); hero.add(Box.createVerticalGlue()); JLabel foot = new JLabel("CUSTOM DATA STRUCTURE  •  JAVA SWING"); foot.setForeground(new Color(137,155,179)); foot.setFont(new Font("SansSerif",Font.PLAIN,10)); hero.add(foot);

        JPanel form = new JPanel(); form.setBackground(Color.WHITE); form.setBorder(new EmptyBorder(55,48,45,48)); form.setLayout(new BoxLayout(form,BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Selamat datang"); title.setFont(new Font("SansSerif",Font.BOLD,25)); title.setForeground(UiStyle.TEXT);
        JLabel subtitle = new JLabel("Masuk untuk melanjutkan ke dashboard."); subtitle.setForeground(UiStyle.MUTED); subtitle.setFont(new Font("SansSerif",Font.PLAIN,12));
        JTextField user = new JTextField("admin"); JPasswordField pass = new JPasswordField(); JComboBox<String> role = new JComboBox<>(new String[]{"Admin","Operator","Viewer"});
        form.add(title); form.add(Box.createVerticalStrut(7)); form.add(subtitle); form.add(Box.createVerticalStrut(35));
        form.add(fieldLabel("USERNAME")); form.add(Box.createVerticalStrut(7)); form.add(user); user.setMaximumSize(new Dimension(Integer.MAX_VALUE,40));
        form.add(Box.createVerticalStrut(18)); form.add(fieldLabel("PASSWORD")); form.add(Box.createVerticalStrut(7)); form.add(pass); pass.setMaximumSize(new Dimension(Integer.MAX_VALUE,40));
        form.add(Box.createVerticalStrut(18)); form.add(fieldLabel("ROLE DEMO")); form.add(Box.createVerticalStrut(7)); form.add(role); role.setMaximumSize(new Dimension(Integer.MAX_VALUE,40));
        form.add(Box.createVerticalStrut(26)); JButton login = UiStyle.primary("Masuk ke Dashboard  →"); login.setAlignmentX(Component.LEFT_ALIGNMENT); login.addActionListener(e->{if(user.getText().isBlank()){JOptionPane.showMessageDialog(this,"Username wajib diisi.");return;}new MainFrame(user.getText().trim(),role.getSelectedItem().toString()).setVisible(true);dispose();}); form.add(login);
        form.add(Box.createVerticalStrut(16)); JLabel note = new JLabel("Mode demo: password belum diverifikasi."); note.setForeground(UiStyle.MUTED); note.setFont(new Font("SansSerif",Font.PLAIN,10)); form.add(note);
        root.add(hero); root.add(form); setContentPane(root);
    }
    private JLabel fieldLabel(String s){JLabel l=new JLabel(s);l.setForeground(UiStyle.MUTED);l.setFont(new Font("SansSerif",Font.BOLD,10));return l;}
}
