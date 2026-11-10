package id.putusan.dashboard.ui;

import id.putusan.dashboard.service.AuditService;import javax.swing.*;import javax.swing.border.EmptyBorder;import java.awt.*;
public class AuditPanel extends JPanel {
 public AuditPanel(AuditService audit){setLayout(new BorderLayout(0,16));setBackground(UiStyle.BG);setBorder(new EmptyBorder(24,28,24,28));JLabel title=new JLabel("Audit Log");title.setFont(new Font("SansSerif",Font.BOLD,25));JLabel sub=UiStyle.muted("Jejak aktivitas aplikasi, ditampilkan dari aktivitas terbaru.");JPanel h=new JPanel();h.setOpaque(false);h.setLayout(new BoxLayout(h,BoxLayout.Y_AXIS));h.add(title);h.add(Box.createVerticalStrut(5));h.add(sub);add(h,BorderLayout.NORTH);
  JPanel card=UiStyle.card();card.setLayout(new BorderLayout(0,10));JPanel meta=new JPanel(new BorderLayout());meta.setOpaque(false);JLabel count=new JLabel(audit.size()+" aktivitas tercatat");count.setForeground(UiStyle.TEAL);count.setFont(new Font("SansSerif",Font.BOLD,12));JButton refresh=new JButton("Perbarui");meta.add(count,BorderLayout.WEST);meta.add(refresh,BorderLayout.EAST);JTextArea area=new JTextArea(audit.history());area.setEditable(false);area.setFont(new Font(Font.MONOSPACED,Font.PLAIN,12));area.setForeground(UiStyle.TEXT);area.setLineWrap(true);area.setWrapStyleWord(true);area.setBorder(new EmptyBorder(12,12,12,12));card.add(meta,BorderLayout.NORTH);card.add(new JScrollPane(area),BorderLayout.CENTER);refresh.addActionListener(e->{area.setText(audit.history());count.setText(audit.size()+" aktivitas tercatat");});add(card,BorderLayout.CENTER);
 }
}
