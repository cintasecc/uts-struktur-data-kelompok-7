package id.putusan.dashboard.ui;

import id.putusan.dashboard.model.Putusan;
import id.putusan.dashboard.repository.PutusanRepository;
import id.putusan.dashboard.service.AuditService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class DashboardPanel extends JPanel {
    public DashboardPanel(PutusanRepository repo, AuditService audit) {
        setLayout(new BorderLayout()); setBackground(UiStyle.BG); setBorder(new EmptyBorder(26,28,26,28));
        int total=repo.all().size(), known=0; double totalWeight=0; Map<String,Integer> types=new LinkedHashMap<>();
        for(int i=0;i<total;i++){Putusan p=repo.all().get(i);if(p.getVonisBulan()!=null)known++;if(p.getBeratBarangBuktiGram()!=null)totalWeight+=p.getBeratBarangBuktiGram();String type=p.getJenisNarkotika();if(type!=null&&!type.isBlank())types.put(type,types.getOrDefault(type,0)+1);}
        JPanel body=new JPanel();body.setOpaque(false);body.setLayout(new BoxLayout(body,BoxLayout.Y_AXIS));
        JLabel title=new JLabel("Ringkasan perkara");title.setFont(new Font("SansSerif",Font.BOLD,25));title.setForeground(UiStyle.TEXT);
        JLabel sub=UiStyle.muted("Pantau dataset dan aktivitas pengelolaan putusan dalam satu tampilan.");
        body.add(title);body.add(Box.createVerticalStrut(5));body.add(sub);body.add(Box.createVerticalStrut(22));
        JPanel metrics=new JPanel(new GridLayout(1,4,14,14));metrics.setOpaque(false);
        metrics.add(metric("TOTAL PUTUSAN",String.valueOf(total),"Data dalam dataset",new Color(20,145,137)));
        metrics.add(metric("VONIS TERSEDIA",String.valueOf(known),"Memiliki nilai vonis",new Color(72,113,190)));
        metrics.add(metric("JENIS NARKOTIKA",String.valueOf(types.size()),"Kategori teridentifikasi",new Color(166,119,55)));
        metrics.add(metric("TOTAL BERAT",String.format(java.util.Locale.ROOT,"%.2f g",totalWeight),"Akumulasi data tersedia",new Color(126,93,170)));
        body.add(metrics);body.add(Box.createVerticalStrut(18));
        JPanel lower=new JPanel(new GridLayout(1,2,14,14));lower.setOpaque(false);
        JPanel typeCard=UiStyle.card();typeCard.setLayout(new BorderLayout(0,12));JLabel tt=new JLabel("Komposisi jenis narkotika");tt.setFont(new Font("SansSerif",Font.BOLD,14));typeCard.add(tt,BorderLayout.NORTH);
        JPanel bars=new JPanel();bars.setOpaque(false);bars.setLayout(new BoxLayout(bars,BoxLayout.Y_AXIS));
        if(types.isEmpty())bars.add(UiStyle.muted("Belum ada kategori yang tersedia."));
        for(Map.Entry<String,Integer> e:types.entrySet()){JPanel line=new JPanel(new BorderLayout(8,5));line.setOpaque(false);JLabel label=new JLabel(e.getKey());label.setFont(new Font("SansSerif",Font.PLAIN,12));JLabel value=new JLabel(e.getValue()+" data");value.setForeground(UiStyle.MUTED);value.setFont(new Font("SansSerif",Font.PLAIN,11));JPanel labels=new JPanel(new BorderLayout());labels.setOpaque(false);labels.add(label,BorderLayout.WEST);labels.add(value,BorderLayout.EAST);JProgressBar bar=new JProgressBar(0,total);bar.setValue(e.getValue());bar.setStringPainted(false);bar.setForeground(UiStyle.TEAL);bar.setBackground(new Color(235,240,245));bar.setBorderPainted(false);line.add(labels,BorderLayout.NORTH);line.add(bar,BorderLayout.SOUTH);bars.add(line);bars.add(Box.createVerticalStrut(12));}
        typeCard.add(bars,BorderLayout.CENTER);
        JPanel activity=UiStyle.card();activity.setLayout(new BoxLayout(activity,BoxLayout.Y_AXIS));JLabel at=new JLabel("Aktivitas terbaru");at.setFont(new Font("SansSerif",Font.BOLD,14));activity.add(at);activity.add(Box.createVerticalStrut(15));JLabel count=new JLabel(audit.size()+" aktivitas tercatat");count.setFont(new Font("SansSerif",Font.BOLD,22));count.setForeground(UiStyle.TEAL);activity.add(count);activity.add(Box.createVerticalStrut(6));activity.add(UiStyle.muted("Audit disimpan menggunakan custom Stack."));activity.add(Box.createVerticalStrut(18));JLabel ds=new JLabel("STRUKTUR DATA UTAMA");ds.setForeground(UiStyle.MUTED);ds.setFont(new Font("SansSerif",Font.BOLD,10));activity.add(ds);JLabel linked=new JLabel("Custom Singly Linked List");linked.setFont(new Font("SansSerif",Font.BOLD,13));activity.add(Box.createVerticalStrut(5));activity.add(linked);
        lower.add(typeCard);lower.add(activity);body.add(lower);add(body,BorderLayout.NORTH);
    }
    private JPanel metric(String label,String value,String caption,Color accent){JPanel p=UiStyle.card();p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));JLabel l=new JLabel(label);l.setForeground(UiStyle.MUTED);l.setFont(new Font("SansSerif",Font.BOLD,10));JLabel v=new JLabel(value);v.setForeground(accent);v.setFont(new Font("SansSerif",Font.BOLD,27));JLabel c=UiStyle.muted(caption);p.add(l);p.add(Box.createVerticalStrut(13));p.add(v);p.add(Box.createVerticalStrut(5));p.add(c);return p;}
}
