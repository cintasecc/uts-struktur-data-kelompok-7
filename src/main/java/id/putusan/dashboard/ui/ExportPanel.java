package id.putusan.dashboard.ui;

import id.putusan.dashboard.repository.PutusanRepository;import id.putusan.dashboard.service.ExportService;import id.putusan.dashboard.service.StatistikService;
import javax.swing.*;import javax.swing.border.EmptyBorder;import java.awt.*;import java.nio.file.Path;
public class ExportPanel extends JPanel {
 public ExportPanel(PutusanRepository repo){setLayout(new BorderLayout(0,16));setBackground(UiStyle.BG);setBorder(new EmptyBorder(24,28,24,28));JLabel title=new JLabel("Ekspor Data");title.setFont(new Font("SansSerif",Font.BOLD,25));JLabel sub=UiStyle.muted("Unduh dataset atau ringkasan statistik untuk kebutuhan pelaporan.");JPanel h=new JPanel();h.setOpaque(false);h.setLayout(new BoxLayout(h,BoxLayout.Y_AXIS));h.add(title);h.add(Box.createVerticalStrut(5));h.add(sub);add(h,BorderLayout.NORTH);
  JPanel cards=new JPanel(new GridLayout(1,2,14,14));cards.setOpaque(false);ExportService export=new ExportService(repo);
  cards.add(exportCard("CSV","Dataset putusan","Ekspor seluruh data putusan ke format CSV agar dapat dibuka di spreadsheet.","Ekspor CSV",()->save(p->export.csv(p),"putusan-export.csv")));
  cards.add(exportCard("TXT","Ringkasan statistik","Ekspor ringkasan jumlah putusan dan statistik vonis ke dokumen teks.","Ekspor ringkasan",()->save(p->export.txt(p,new StatistikService().summary(repo.all())),"laporan-statistik.txt")));
  add(cards,BorderLayout.CENTER);
 }
 private JPanel exportCard(String icon,String title,String desc,String button,Runnable action){JPanel p=UiStyle.card();p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));JLabel badge=new JLabel(icon);badge.setOpaque(true);badge.setBackground(new Color(220,242,240));badge.setForeground(UiStyle.TEAL);badge.setFont(new Font("SansSerif",Font.BOLD,13));badge.setBorder(new EmptyBorder(9,12,9,12));badge.setAlignmentX(Component.LEFT_ALIGNMENT);JLabel t=new JLabel(title);t.setFont(new Font("SansSerif",Font.BOLD,17));JLabel d=new JLabel("<html><div style='width:250px'>"+desc+"</div></html>");d.setForeground(UiStyle.MUTED);d.setFont(new Font("SansSerif",Font.PLAIN,12));JButton b=UiStyle.primary(button);b.setAlignmentX(Component.LEFT_ALIGNMENT);b.addActionListener(e->action.run());p.add(badge);p.add(Box.createVerticalStrut(18));p.add(t);p.add(Box.createVerticalStrut(8));p.add(d);p.add(Box.createVerticalGlue());p.add(b);return p;}
 private void save(IOAction action,String name){JFileChooser c=new JFileChooser();c.setSelectedFile(new java.io.File(name));if(c.showSaveDialog(this)==JFileChooser.APPROVE_OPTION)try{action.run(c.getSelectedFile().toPath());JOptionPane.showMessageDialog(this,"Ekspor berhasil disimpan.");}catch(Exception e){JOptionPane.showMessageDialog(this,"Ekspor gagal: "+e.getMessage());}}
 @FunctionalInterface private interface IOAction{void run(Path p)throws Exception;}
}
