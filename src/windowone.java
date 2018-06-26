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


public class windowone {

	protected Shell shell;
	private Text usernametext;
	private Text passwordtext;
	public static String username;
	public static String password;
	public static String URL;
	private Label lblUsername;
	private Text URLtext;
	public static boolean successwindowone;
	
	/**
	 * Launch the application.
	 * @param args
	 * @wbp.parser.entryPoint
	 */
	public static void main() {
		try {
			windowone window = new windowone();
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
		successwindowone=false;
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
		shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		shell.setSize(450, 300);
		shell.setText("Reference Detective");
		shell.setLayout(new FormLayout());
		
		usernametext = new Text(shell, SWT.BORDER);
		usernametext.setFont(SWTResourceManager.getFont("Segoe UI", 8, SWT.NORMAL));
		FormData fd_usernametext = new FormData();
		fd_usernametext.right = new FormAttachment(100, -53);
		usernametext.setLayoutData(fd_usernametext);

		
		Label lblPassword = new Label(shell, SWT.NONE);
		lblPassword.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		FormData fd_lblPassword = new FormData();
		fd_lblPassword.left = new FormAttachment(0, 10);
		lblPassword.setLayoutData(fd_lblPassword);
		lblPassword.setText("Password:");
		
		passwordtext = new Text(shell, SWT.PASSWORD |  SWT.BORDER);
		fd_usernametext.left = new FormAttachment(passwordtext, 0, SWT.LEFT);
		passwordtext.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
		FormData fd_passwordtext = new FormData();
		fd_passwordtext.right = new FormAttachment(100, -53);
		fd_passwordtext.left = new FormAttachment(lblPassword, 34);
		fd_passwordtext.top = new FormAttachment(lblPassword, 1, SWT.TOP);
		passwordtext.setLayoutData(fd_passwordtext);

		
		Label lblLoginUrl = new Label(shell, SWT.NONE);
		fd_lblPassword.bottom = new FormAttachment(100, -179);
		lblLoginUrl.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		FormData fd_lblLoginUrl = new FormData();
		fd_lblLoginUrl.top = new FormAttachment(lblPassword, 17);
		fd_lblLoginUrl.left = new FormAttachment(lblPassword, 0, SWT.LEFT);
		lblLoginUrl.setLayoutData(fd_lblLoginUrl);
		lblLoginUrl.setText("Login URL:");
		
		Button btnLogin = new Button(shell, SWT.NONE);
		btnLogin.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				assignments();

				shell.close();
			}
		});
		FormData fd_btnLogin = new FormData();
		fd_btnLogin.left = new FormAttachment(0, 181);
		fd_btnLogin.right = new FormAttachment(100, -175);
		fd_btnLogin.bottom = new FormAttachment(100, -56);
		btnLogin.setLayoutData(fd_btnLogin);
		btnLogin.setText("Login");
		
		lblUsername = new Label(shell, SWT.NONE);
		fd_usernametext.top = new FormAttachment(lblUsername, 1, SWT.TOP);
		lblUsername.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		FormData fd_lblUsername = new FormData();
		fd_lblUsername.bottom = new FormAttachment(lblPassword, -15);
		fd_lblUsername.left = new FormAttachment(lblPassword, 0, SWT.LEFT);
		lblUsername.setLayoutData(fd_lblUsername);
		lblUsername.setText("Username:");
		
		URLtext = new Text(shell, SWT.BORDER);
		URLtext.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
		FormData fd_text_1 = new FormData();
		fd_text_1.right = new FormAttachment(100, -53);
		fd_text_1.left = new FormAttachment(lblLoginUrl, 29);
		fd_text_1.top = new FormAttachment(lblLoginUrl, 1, SWT.TOP);
		URLtext.setLayoutData(fd_text_1);
		URLtext.setText("https://test.salesforce.com/services/Soap/u/34.0");
		

	}

	public void assignments() {
		username=usernametext.getText();
		password=passwordtext.getText();
		URL=URLtext.getText();
		successwindowone=true;
	}
}