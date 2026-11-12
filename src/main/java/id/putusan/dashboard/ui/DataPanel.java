
package id.putusan.dashboard.ui;

import id.putusan.dashboard.model.Putusan;
import id.putusan.dashboard.repository.PutusanRepository;
import id.putusan.dashboard.service.PutusanService;
import id.putusan.dashboard.util.PdfExtractor;
import id.putusan.dashboard.util.PutusanPdfParser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.nio.file.Path;
import java.time.LocalDate;

public class DataPanel extends JPanel {

    private static final Path DATA_FILE =
            Path.of("data", "putusan-50.csv");

    private final PutusanRepository repo;
    private final PutusanService service;
    private final String user;

    private final DefaultTableModel model;
    private final JTable table;
    private final JTextField search = new JTextField();
    private final JLabel count = new JLabel(" ");

    public DataPanel(
            PutusanRepository repository,
            PutusanService putusanService,
            String username,
            String role) {

        repo = repository;
        service = putusanService;
        user = username;

        setLayout(new BorderLayout());
        setBackground(UiStyle.BG);
        setBorder(new EmptyBorder(24, 28, 24, 28));

        JPanel heading = new JPanel();
        heading.setOpaque(false);
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Data Putusan");
        title.setFont(new Font("SansSerif", Font.BOLD, 25));

        JLabel subtitle = UiStyle.muted(
                "Kelola dan telusuri data perkara yang tersimpan dalam dataset."
        );

        heading.add(title);
        heading.add(Box.createVerticalStrut(5));
        heading.add(subtitle);
        heading.add(Box.createVerticalStrut(18));

        model = new DefaultTableModel(
                new String[]{
                        "Case ID",
                        "Nomor Perkara",
                        "Tanggal",
                        "Jenis Narkotika",
                        "Berat (g)",
                        "Pasal Terbukti",
                        "Vonis (bln)"
                }, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        UiStyle.styleTable(table);

        TableRowSorter<DefaultTableModel> sorter =
                new TableRowSorter<>(model);

        table.setRowSorter(sorter);

        JPanel toolbar = UiStyle.card();
        toolbar.setLayout(new BorderLayout(10, 0));

        search.setPreferredSize(new Dimension(260, 38));
        search.putClientProperty(
                "JTextField.placeholderText",
                "Cari data pada tabel..."
        );

        count.setForeground(UiStyle.MUTED);

        JPanel actions = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 8, 0)
        );

        actions.setOpaque(false);

        JButton reload = new JButton("Muat ulang");
        JButton delete = new JButton("Hapus terpilih");
        JButton edit = new JButton("Edit terpilih");
        JButton add = UiStyle.primary("+ Tambah putusan (PDF)");

        actions.add(reload);
        actions.add(delete);
        actions.add(edit);
        actions.add(add);

        toolbar.add(search, BorderLayout.WEST);
        toolbar.add(count, BorderLayout.CENTER);
        toolbar.add(actions, BorderLayout.EAST);

        JPanel center = new JPanel(new BorderLayout(0, 12));
        center.setOpaque(false);
        center.add(toolbar, BorderLayout.NORTH);

        JPanel tableCard = UiStyle.card();
        tableCard.setLayout(new BorderLayout());
        tableCard.add(new JScrollPane(table), BorderLayout.CENTER);

        center.add(tableCard, BorderLayout.CENTER);

        add(heading, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);

        refresh();

        search.getDocument().addDocumentListener(
                new javax.swing.event.DocumentListener() {

            private void apply() {
                String query = search.getText();

                sorter.setRowFilter(
                        query.isBlank()
                                ? null
                                : RowFilter.regexFilter(
                                        "(?i)" + java.util.regex.Pattern.quote(query)
                                )
                );
            }

            public void insertUpdate(
                    javax.swing.event.DocumentEvent e) {
                apply();
            }

            public void removeUpdate(
                    javax.swing.event.DocumentEvent e) {
                apply();
            }

            public void changedUpdate(
                    javax.swing.event.DocumentEvent e) {
                apply();
            }
        });

        reload.addActionListener(e -> reloadData());

        // CREATE: wajib melalui PDF.
        add.addActionListener(e -> importPdf(add));

        // UPDATE: koreksi data yang sudah tersimpan.
        edit.addActionListener(e -> {
            int index = selectedModelIndex();

            if (index >= 0) {
                showEditor(repo.all().get(index), index, false);
            }
        });

        // DELETE.
        delete.addActionListener(e -> {
            int index = selectedModelIndex();

            if (index < 0) {
                return;
            }

            String id = repo.all().get(index).getCaseId();

            int answer = JOptionPane.showConfirmDialog(
                    this,
                    "Hapus putusan " + id + "?",
                    "Konfirmasi",
                    JOptionPane.YES_NO_OPTION
            );

            if (answer == JOptionPane.YES_OPTION) {
                try {
                    service.delete(index, user);
                    persistAndRefresh("Data berhasil dihapus.");
                } catch (Exception ex) {
                    showError(ex);
                }
            }
        });
    }

    /**
     * Memilih PDF, mengekstrak teks, dan melakukan parsing.
     */
    private void importPdf(JButton addButton) {

        JFileChooser chooser = new JFileChooser();

        chooser.setDialogTitle("Pilih PDF Putusan");

        chooser.setFileFilter(
                new javax.swing.filechooser.FileNameExtensionFilter(
                        "Dokumen PDF (*.pdf)",
                        "pdf"
                )
        );

        if (chooser.showOpenDialog(this)
                != JFileChooser.APPROVE_OPTION) {
            return;
        }

        Path pdfPath = chooser.getSelectedFile().toPath();

        addButton.setEnabled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        SwingWorker<Putusan, Void> worker =
                new SwingWorker<>() {

            @Override
            protected Putusan doInBackground() throws Exception {

                PdfExtractor extractor = new PdfExtractor();

                PdfExtractor.PageExtraction extraction =
                        extractor.extractWithPages(pdfPath);

                if (!extraction.hasText()) {
                    throw new IllegalArgumentException(
                            "PDF tidak memiliki text layer. "
                            + "Dokumen hasil scan memerlukan OCR."
                    );
                }

                String fileName = pdfPath.getFileName().toString();

                String stem = fileName.replaceFirst(
                        "(?i)\\.pdf$",
                        ""
                );

                // ID dibuat dari nama file agar konsisten.
                String safeId = stem
                        .replaceAll("[^a-zA-Z0-9_-]", "_");

                if (safeId.isBlank()) {
                    throw new IllegalArgumentException(
                            "Nama file tidak dapat digunakan sebagai Case ID."
                    );
                }

                String caseId = "PDF-" + safeId;

                // Kode anonim, bukan nama terdakwa.
                String terdakwaKode = "T-" + safeId;

                PutusanPdfParser parser =
                        new PutusanPdfParser();

                return parser.parse(
                        extraction,
                        caseId,
                        terdakwaKode
                );
            }

            @Override
            protected void done() {

                addButton.setEnabled(true);
                setCursor(Cursor.getDefaultCursor());

                try {
                    Putusan parsed = get();

                    JOptionPane.showMessageDialog(
                            DataPanel.this,
                            "PDF berhasil diekstrak.\n"
                            + "Silakan periksa hasil parsing "
                            + "sebelum menyimpan.",
                            "Ekstraksi berhasil",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    // Form ini adalah verifikasi hasil PDF,
                    // bukan form pembuatan data manual.
                    showEditor(parsed, -1, true);

                } catch (Exception ex) {

                    Throwable cause = ex.getCause();

                    String message = cause != null
                            ? cause.getMessage()
                            : ex.getMessage();

                    JOptionPane.showMessageDialog(
                            DataPanel.this,
                            "Import PDF gagal:\n" + message,
                            "Kesalahan ekstraksi",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        };

        worker.execute();
    }

    private int selectedModelIndex() {

        int viewIndex = table.getSelectedRow();

        if (viewIndex < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Pilih satu baris terlebih dahulu."
            );

            return -1;
        }

        return table.convertRowIndexToModel(viewIndex);
    }

    private void refresh() {

        model.setRowCount(0);

        for (int i = 0; i < repo.all().size(); i++) {

            Putusan p = repo.all().get(i);

            model.addRow(new Object[]{
                    p.getCaseId(),
                    p.getNomorPerkara(),
                    p.getTanggalPutusan(),
                    p.getJenisNarkotika(),
                    p.getBeratBarangBuktiGram(),
                    p.getPasalTerbukti(),
                    p.getVonisBulan()
            });
        }

        count.setText(repo.all().size() + " putusan");
    }

    private void reloadData() {

        try {
            repo.load(DATA_FILE);
            refresh();

        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void persistAndRefresh(String successMessage) {

        try {
            repo.save(DATA_FILE);
            refresh();

            JOptionPane.showMessageDialog(
                    this,
                    successMessage
            );

        } catch (Exception ex) {

            refresh();

            JOptionPane.showMessageDialog(
                    this,
                    "Perubahan ada di memori, tetapi gagal "
                    + "disimpan ke CSV: " + ex.getMessage(),
                    "Peringatan penyimpanan",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    /**
     * Form verifikasi hasil PDF atau edit data yang sudah tersimpan.
     */
    private void showEditor(
            Putusan existing,
            int index,
            boolean adding) {

        String[] labels = {
                "Case ID",
                "Nomor Perkara",
                "Pengadilan",
                "Tanggal Putusan (YYYY-MM-DD)",
                "Kode Terdakwa",
                "Jenis Narkotika",
                "Berat Barang Bukti (gram)",
                "Pasal Dakwaan",
                "Pasal Terbukti",
                "Tuntutan (bulan)",
                "Vonis (bulan)",
                "Denda (rupiah)",
                "Barang Bukti",
                "Keadaan Memberatkan",
                "Keadaan Meringankan",
                "Source Pages"
        };

        String[] values = new String[]{
                text(existing.getCaseId()),
                text(existing.getNomorPerkara()),
                text(existing.getPengadilan()),
                text(existing.getTanggalPutusan()),
                text(existing.getTerdakwaKode()),
                text(existing.getJenisNarkotika()),
                text(existing.getBeratBarangBuktiGram()),
                text(existing.getPasalDakwaan()),
                text(existing.getPasalTerbukti()),
                text(existing.getTuntutanBulan()),
                text(existing.getVonisBulan()),
                text(existing.getDendaRupiah()),
                text(existing.getBarangBukti()),
                text(existing.getKeadaanMemberatkan()),
                text(existing.getKeadaanMeringankan()),
                text(existing.getSourcePages())
        };

        JTextField[] fields = new JTextField[labels.length];

        JPanel form = new JPanel(
                new GridLayout(labels.length, 2, 8, 7)
        );

        for (int i = 0; i < labels.length; i++) {

            fields[i] = new JTextField(
                    values[i] == null ? "" : values[i],
                    24
            );

            form.add(new JLabel(labels[i]));
            form.add(fields[i]);
        }

        JScrollPane scroll = new JScrollPane(form);
        scroll.setPreferredSize(new Dimension(720, 520));

        int result = JOptionPane.showConfirmDialog(
                this,
                scroll,
                adding
                        ? "Verifikasi Hasil Ekstraksi PDF"
                        : "Edit Putusan",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        try {

            Putusan putusan = new Putusan(
                    required(fields[0]),
                    required(fields[1]),
                    optional(fields[2]),
                    date(optional(fields[3])),
                    optional(fields[4]),
                    optional(fields[5]),
                    decimal(fields[6]),
                    optional(fields[7]),
                    optional(fields[8]),
                    integer(fields[9]),
                    integer(fields[10]),
                    longNumber(fields[11]),
                    optional(fields[12]),
                    optional(fields[13]),
                    optional(fields[14]),
                    optional(fields[15])
            );

            if (adding) {
                service.add(putusan, user);
            } else {
                service.update(index, putusan, user);
            }

            persistAndRefresh(
                    adding
                            ? "Putusan hasil PDF berhasil ditambahkan."
                            : "Putusan berhasil diperbarui."
            );

        } catch (Exception ex) {
            showError(ex);
        }
    }

    private static String text(Object value) {
        return value == null ? "" : value.toString();
    }

    private static String optional(JTextField field) {
        return field.getText().isBlank()
                ? null
                : field.getText().trim();
    }

    private static String required(JTextField field) {

        String value = optional(field);

        if (value == null) {
            throw new IllegalArgumentException(
                    "Case ID dan nomor perkara wajib diisi."
            );
        }

        return value;
    }

    private static LocalDate date(String value) {
        return value == null || value.isBlank()
                ? null
                : LocalDate.parse(value.trim());
    }

    private static Double decimal(JTextField field) {

        String value = optional(field);

        return value == null
                ? null
                : Double.valueOf(value.replace(',', '.'));
    }

    private static Integer integer(JTextField field) {

        String value = optional(field);

        return value == null
                ? null
                : Integer.valueOf(value);
    }

    private static Long longNumber(JTextField field) {

        String value = optional(field);

        return value == null
                ? null
                : Long.valueOf(value);
    }

    private void showError(Exception ex) {

        JOptionPane.showMessageDialog(
                this,
                "Operasi gagal: " + ex.getMessage(),
                "Kesalahan",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
