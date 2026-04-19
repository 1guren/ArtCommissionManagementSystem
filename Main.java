package finals;

import javax.swing.JFrame;

public class Main extends JFrame {

    public static void main(String[] args) {
        SystemManager.getInstance();
        
        new LoginForm().setVisible(true);
    }

}