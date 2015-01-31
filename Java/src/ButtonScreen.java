import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class ButtonScreen extends JFrame implements KeyListener {

	public ButtonScreen()
	{
		super("Dat");
		setBounds(20, 20, 100, 100);
		addKeyListener(this);
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		if(e.getKeyChar() == 'q') {
			System.out.println("DIE!!!");
			Subscriber.close();
		}
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
