package id.putusan.dashboard.ui;

import id.putusan.dashboard.model.Putusan;import id.putusan.dashboard.service.PutusanService;
import javax.swing.*;import javax.swing.table.DefaultTableModel;import java.awt.*;

public class FilterPanel extends JPanel {
 public FilterPanel(PutusanService service){setLayout(new BorderLayout(0,16));setBackground(UiStyle.BG);setBorder(new javax.swing.border.EmptyBorder(24,28,24,28));
  JLabel title=new JLabel("Filter Data Putusan");title.setFont(new Font("SansSerif",Font.BOLD,25));JLabel sub=UiStyle.muted("Persempit data berdasarkan jenis narkotika, berat barang bukti, dan pasal.");JPanel heading=new JPanel();heading.setOpaque(false);heading.setLayout(new BoxLayout(heading,BoxLayout.Y_AXIS));heading.add(title);heading.add(Box.createVerticalStrut(5));heading.add(sub);add(heading,BorderLayout.NORTH);
  JPanel body=new JPanel(new BorderLayout(0,12));body.setOpaque(false);JPanel form=UiStyle.card();form.setLayout(new GridLayout(2,4,12,8));JTextField jenis=new JTextField(),min=new JTextField(),max=new JTextField(),pasal=new JTextField();form.add(field("Jenis narkotika",jenis));form.add(field("Berat minimum (g)",min));form.add(field("Berat maksimum (g)",max));form.add(field("Pasal terbukti",pasal));JButton apply=UiStyle.primary("Terapkan filter");JButton clear=new JButton("Reset");JPanel buttons=new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0));buttons.setOpaque(false);buttons.add(clear);buttons.add(apply);form.add(new JLabel());form.add(new JLabel());form.add(new JLabel());form.add(buttons);body.add(form,BorderLayout.NORTH);
  DefaultTableModel m=new DefaultTableModel(new String[]{"Case ID","Jenis","Berat (g)","Pasal Terbukti","Vonis (bln)"},0){public boolean isCellEditable(int r,int c){return false;}};JTable table=new JTable(m);UiStyle.styleTable(table);JPanel results=UiStyle.card();results.setLayout(new BorderLayout());results.add(new JScrollPane(table));body.add(results,BorderLayout.CENTER);add(body,BorderLayout.CENTER);
  apply.addActionListener(e->{try{m.setRowCount(0);for(Putusan p:service.filter(jenis.getText(),number(min),number(max),pasal.getText(),null,null))m.addRow(new Object[]{p.getCaseId(),p.getJenisNarkotika(),p.getBeratBarangBuktiGram(),p.getPasalTerbukti(),p.getVonisBulan()});}catch(NumberFormatException ex){JOptionPane.showMessageDialog(this,"Berat minimum/maksimum harus berupa angka.");}});
  clear.addActionListener(e->{jenis.setText("");min.setText("");max.setText("");pasal.setText("");m.setRowCount(0);});
 }
 private JPanel field(String label,JTextField input){JPanel p=new JPanel(new BorderLayout(0,6));p.setOpaque(false);JLabel l=new JLabel(label);l.setForeground(UiStyle.MUTED);l.setFont(new Font("SansSerif",Font.BOLD,11));p.add(l,BorderLayout.NORTH);p.add(input,BorderLayout.CENTER);return p;}
 private Double number(JTextField f){return f.getText().isBlank()?null:Double.valueOf(f.getText().trim());}
}
