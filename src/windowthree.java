import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Label;

import javax.swing.JOptionPane;



public class windowthree extends Shell {
	static boolean successwindowthree;

	public static void main(Thread t1) {
		try {
			successwindowthree= false;
			Display display = Display.getDefault();
			windowthree shell = new windowthree(display);
			shell.open();
			shell.layout();
			while (!shell.isDisposed() && t1.isAlive()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
			
//			JOptionPane.showMessageDialog(null,"Please check the tool's directory for your downloaded reports. Thanks","Information",JOptionPane.INFORMATION_MESSAGE);
//			MessageDialog.openInformation(shell, "Information", "Please check the tool's directory for your downloaded reports. Thanks");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		successwindowthree=true;
	}

	/**
	 * Create the shell.
	 * @param display
	 */
	public windowthree(Display display) {
		super(display, SWT.SHELL_TRIM);
		
		ProgressBar progressBar = new ProgressBar(this, SWT.INDETERMINATE);
		progressBar.setBounds(40, 112, 351, 34);
		
		Label lblPleaseWaitWhile = new Label(this, SWT.NONE);
		lblPleaseWaitWhile.setBounds(40, 36, 384, 70);
		lblPleaseWaitWhile.setText("Please wait while tool prepares and downloads the requested\r\nanalysis.");
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("Reference Detective");
		setSize(450, 300);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
