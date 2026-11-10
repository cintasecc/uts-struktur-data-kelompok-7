package id.putusan.dashboard.ui;

import id.putusan.dashboard.model.Putusan;import id.putusan.dashboard.service.PutusanService;
import javax.swing.*;import javax.swing.table.DefaultTableModel;import java.awt.*;

public class SearchPanel extends JPanel {
 public SearchPanel(PutusanService service){setLayout(new BorderLayout(0,16));setBackground(UiStyle.BG);setBorder(new javax.swing.border.EmptyBorder(24,28,24,28));
  JLabel title=new JLabel("Pencarian Putusan");title.setFont(new Font("SansSerif",Font.BOLD,25));JLabel sub=UiStyle.muted("Cari berdasarkan Case ID atau nomor perkara.");JPanel heading=new JPanel();heading.setOpaque(false);heading.setLayout(new BoxLayout(heading,BoxLayout.Y_AXIS));heading.add(title);heading.add(Box.createVerticalStrut(5));heading.add(sub);add(heading,BorderLayout.NORTH);
  JPanel body=new JPanel(new BorderLayout(0,12));body.setOpaque(false);JPanel search=UiStyle.card();search.setLayout(new BorderLayout(10,0));JTextField q=new JTextField();q.putClientProperty("JTextField.placeholderText","Contoh: P001 atau nomor perkara...");JButton go=UiStyle.primary("Cari putusan");search.add(q,BorderLayout.CENTER);search.add(go,BorderLayout.EAST);body.add(search,BorderLayout.NORTH);
  DefaultTableModel m=new DefaultTableModel(new String[]{"Case ID","Nomor Perkara","Pengadilan","Jenis Narkotika","Vonis (bln)"},0){public boolean isCellEditable(int r,int c){return false;}};JTable table=new JTable(m);UiStyle.styleTable(table);JPanel results=UiStyle.card();results.setLayout(new BorderLayout());results.add(new JScrollPane(table));body.add(results,BorderLayout.CENTER);add(body,BorderLayout.CENTER);
  Runnable run=()->{m.setRowCount(0);for(Putusan p:service.search(q.getText()))m.addRow(new Object[]{p.getCaseId(),p.getNomorPerkara(),p.getPengadilan(),p.getJenisNarkotika(),p.getVonisBulan()});};go.addActionListener(e->run.run());q.addActionListener(e->run.run());
 }
}
