package id.putusan.dashboard.ui;

import id.putusan.dashboard.model.Putusan;
import id.putusan.dashboard.repository.PutusanRepository;
import id.putusan.dashboard.service.PutusanService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.time.LocalDate;

/** Searchable data table with role-aware add/delete actions. */
public class DataPanel extends JPanel {
    private final PutusanRepository repo; private final PutusanService service; private final String user,role;
    private final DefaultTableModel model; private final JTable table; private final JTextField search=new JTextField();
    public DataPanel(PutusanRepository r,PutusanService s,String u,String role){
        repo=r;service=s;user=u;this.role=role;setLayout(new BorderLayout());setBackground(UiStyle.BG);setBorder(new javax.swing.border.EmptyBorder(24,28,24,28));
        JPanel heading=new JPanel();heading.setOpaque(false);heading.setLayout(new BoxLayout(heading,BoxLayout.Y_AXIS));JLabel title=new JLabel("Data Putusan");title.setFont(new Font("SansSerif",Font.BOLD,25));JLabel sub=UiStyle.muted("Kelola dan telusuri data perkara yang tersimpan dalam dataset.");heading.add(title);heading.add(Box.createVerticalStrut(5));heading.add(sub);heading.add(Box.createVerticalStrut(18));
        model=new DefaultTableModel(new String[]{"Case ID","Nomor Perkara","Tanggal","Jenis Narkotika","Berat (g)","Pasal Terbukti","Vonis (bln)"},0){public boolean isCellEditable(int r,int c){return false;}};
        table=new JTable(model);UiStyle.styleTable(table);TableRowSorter<DefaultTableModel> sorter=new TableRowSorter<>(model);table.setRowSorter(sorter);
        JPanel toolbar=UiStyle.card();toolbar.setLayout(new BorderLayout(10,0));search.setPreferredSize(new Dimension(260,38));search.putClientProperty("JTextField.placeholderText","Cari data pada tabel...");JLabel count=new JLabel(" ");count.setForeground(UiStyle.MUTED);JPanel actions=new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0));actions.setOpaque(false);JButton add=UiStyle.primary("＋ Tambah putusan"),del=new JButton("Hapus terpilih"),reload=new JButton("Muat ulang");actions.add(reload);actions.add(del);actions.add(add);toolbar.add(search,BorderLayout.WEST);toolbar.add(count,BorderLayout.CENTER);toolbar.add(actions,BorderLayout.EAST);
        JPanel center=new JPanel(new BorderLayout(0,12));center.setOpaque(false);center.add(toolbar,BorderLayout.NORTH);JPanel tableCard=UiStyle.card();tableCard.setLayout(new BorderLayout());tableCard.add(new JScrollPane(table),BorderLayout.CENTER);center.add(tableCard,BorderLayout.CENTER);
        add(heading,BorderLayout.NORTH);add(center,BorderLayout.CENTER);refresh();count.setText(repo.all().size()+" putusan");
        search.getDocument().addDocumentListener(new javax.swing.event.DocumentListener(){private void apply(){sorter.setRowFilter(search.getText().isBlank()?null:RowFilter.regexFilter("(?i)"+java.util.regex.Pattern.quote(search.getText())));}public void insertUpdate(javax.swing.event.DocumentEvent e){apply();}public void removeUpdate(javax.swing.event.DocumentEvent e){apply();}public void changedUpdate(javax.swing.event.DocumentEvent e){apply();}});
        reload.addActionListener(e->{refresh();count.setText(repo.all().size()+" putusan");});
        del.addActionListener(e->{if(!role.equals("Admin")){msg("Hanya Admin yang dapat menghapus data.");return;}int view=table.getSelectedRow();if(view<0){msg("Pilih baris yang akan dihapus.");return;}int row=table.convertRowIndexToModel(view);int ok=JOptionPane.showConfirmDialog(this,"Hapus putusan "+model.getValueAt(row,0)+"?","Konfirmasi",JOptionPane.YES_NO_OPTION);if(ok==JOptionPane.YES_OPTION){service.delete(row,user);refresh();count.setText(repo.all().size()+" putusan");}});
        add.addActionListener(e->addDialog());
    }
    private void refresh(){model.setRowCount(0);for(int i=0;i<repo.all().size();i++){Putusan p=repo.all().get(i);model.addRow(new Object[]{p.getCaseId(),p.getNomorPerkara(),p.getTanggalPutusan(),p.getJenisNarkotika(),p.getBeratBarangBuktiGram(),p.getPasalTerbukti(),p.getVonisBulan()});}}
    private void addDialog(){if(role.equals("Viewer")){msg("Viewer tidak dapat menambah data.");return;}JTextField id=new JTextField(),no=new JTextField(),jenis=new JTextField(),pengadilan=new JTextField("Pengadilan Negeri Pekanbaru"),tanggal=new JTextField(LocalDate.now().toString()),vonis=new JTextField();Object[] f={"Case ID",id,"Nomor perkara",no,"Pengadilan",pengadilan,"Tanggal (YYYY-MM-DD)",tanggal,"Jenis narkotika",jenis,"Vonis bulan (boleh kosong)",vonis};if(JOptionPane.showConfirmDialog(this,f,"Tambah putusan",JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION)try{service.add(new Putusan(id.getText(),no.getText(),pengadilan.getText(),LocalDate.parse(tanggal.getText()),null,jenis.getText(),null,null,null,null,vonis.getText().isBlank()?null:Integer.valueOf(vonis.getText()),null,null,null,null,null),user);refresh();}catch(Exception ex){msg("Data tidak tersimpan: "+ex.getMessage());}}
    private void msg(String s){JOptionPane.showMessageDialog(this,s);}
}
