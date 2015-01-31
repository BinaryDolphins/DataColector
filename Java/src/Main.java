import javax.swing.SwingUtilities;


public class Main {
	public static void main(String[] args) 
    {
    	StockManager manager = new StockManager();
    	SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				ButtonScreen screen = new ButtonScreen();
				screen.setVisible(true);
			}});
    	
    	manager.timeStepThing();
    }
}
