import java.util.logging.*;
import javax.swing.*;

public class EightPuzzle {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
			Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
		}

		GUI gui = new GUI();
		gui.setVisible(true);
	}
}