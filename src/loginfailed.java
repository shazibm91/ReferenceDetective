import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.wb.swt.SWTResourceManager;


public class loginfailed {

	protected Shell shell;
	
	/**
	 * Launch the application.
	 * @param args
	 * @wbp.parser.entryPoint
	 */
	public static void main() {
		try {
			loginfailed window = new loginfailed();
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
		shell.setSize(375, 250);
		shell.setText("Login failed");
		shell.setLayout(new FormLayout());
		
		Label lblLoginWasUnsuccessful = new Label(shell, SWT.NONE);
		lblLoginWasUnsuccessful.setAlignment(SWT.CENTER);
		lblLoginWasUnsuccessful.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
		FormData fd_lblLoginWasUnsuccessful = new FormData();
		fd_lblLoginWasUnsuccessful.top = new FormAttachment(0, 36);
		fd_lblLoginWasUnsuccessful.left = new FormAttachment(0, 10);
		fd_lblLoginWasUnsuccessful.right = new FormAttachment(0, 349);
		fd_lblLoginWasUnsuccessful.bottom = new FormAttachment(100, -74);
		lblLoginWasUnsuccessful.setLayoutData(fd_lblLoginWasUnsuccessful);
		lblLoginWasUnsuccessful.setText("Login was unsuccessful.\r\n\r\nHint: Please remember to use the security token with your password and enter the correct login URL.");
		
		Button btnOk = new Button(shell, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				shell.close();
				String[] args = {};
				try {
					Main.main(args);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		FormData fd_btnOk = new FormData();
		fd_btnOk.top = new FormAttachment(0, 144);
		fd_btnOk.right = new FormAttachment(100, -109);
		fd_btnOk.left = new FormAttachment(0, 110);
		btnOk.setLayoutData(fd_btnOk);
		btnOk.setText("Ok");
		

	}
}