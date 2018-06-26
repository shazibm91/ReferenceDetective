import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;

public class windowfour {

	protected Shell shell;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			windowfour window = new windowfour();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(450, 255);
		shell.setText("Reference Detective");
		
		Label lblPleaseCheckThe = new Label(shell, SWT.NONE);
		lblPleaseCheckThe.setAlignment(SWT.CENTER);
		lblPleaseCheckThe.setBounds(49, 45, 324, 78);
		lblPleaseCheckThe.setText("Please check the desktop's \"Results\" folder for analysis report. \r\nThank you");
		
		Button btnGotIt = new Button(shell, SWT.NONE);
		btnGotIt.setBounds(176, 129, 75, 25);
		btnGotIt.setText("Got it !");
		
		btnGotIt.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent arg0) {
	    		shell.close();
	    	}
	    });

	}
}
