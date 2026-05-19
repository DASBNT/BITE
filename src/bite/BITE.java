package bite;

import javax.swing.SwingUtilities;

public class BITE {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                RestauranteUI ui = new RestauranteUI(new Restaurante());
                ui.setVisible(true);
            }
        });
    }
}
