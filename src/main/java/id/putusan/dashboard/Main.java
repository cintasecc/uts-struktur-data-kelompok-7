package id.putusan.dashboard;

import id.putusan.dashboard.ui.LoginFrame;
import javax.swing.SwingUtilities;
import id.putusan.dashboard.ui.UiStyle;

/** Entry point aplikasi PUTUSAN DASHBOARD. */
public final class Main {
    private Main() {}
    public static void main(String[] args) {
        UiStyle.install();
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
