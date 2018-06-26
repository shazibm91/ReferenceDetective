import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.wb.swt.SWTResourceManager;


public class windowtwo {
	static boolean windowtwosuccess;
	protected Shell shell;
	public static String username;
	public static String password;
	public static String URL;
	public static boolean successwindowtwo;
	static List<String> selectedobjslist = new ArrayList<String>(); 	
	/**
	 * Launch the application.
	 * @param args
	 * @return 
	 * @wbp.parser.entryPoint
	 */
	public static void main(List<String> objslist) {
		try {
			windowtwo window = new windowtwo();
			window.open(objslist);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open(List<String> objslist) {
		Display display = Display.getDefault();
		successwindowtwo=false;
		createContents(objslist);
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
	protected void createContents(final List<String> objslist) {

		shell = new Shell();
		shell.setSize(539, 457);
		shell.setText("Reference Detective");
		shell.setLayout(new FormLayout());
		
		final Table table = new Table(shell, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI
		        | SWT.H_SCROLL);
		FormData fd_table = new FormData();
		fd_table.right = new FormAttachment(100, -41);
		fd_table.top = new FormAttachment(0, 10);
		fd_table.bottom = new FormAttachment(100, -10);
		table.setLayoutData(fd_table);
		    for (String s:objslist) {
		      TableItem item = new TableItem(table, SWT.NONE);
		      item.setText(s);
		    }
		    table.setSize(100, 100);
		    table.addListener(SWT.DefaultSelection, new Listener() {
		        public void handleEvent(Event e) {
		          String string = "";
		          TableItem[] selection = table.getSelection();
		          for (int i = 0; i < selection.length; i++)
		            string += selection[i] + " ";
		          System.out.println("DefaultSelection={" + string + "}");
		        }
		      });
	    
	    Label lblPleaseSelectThe = new Label(shell, SWT.NONE);
	    fd_table.left = new FormAttachment(lblPleaseSelectThe, 20);


	    lblPleaseSelectThe.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
	    lblPleaseSelectThe.setAlignment(SWT.CENTER);
	    FormData fd_lblPleaseSelectThe = new FormData();
	    fd_lblPleaseSelectThe.top = new FormAttachment(0, 68);
	    fd_lblPleaseSelectThe.left = new FormAttachment(0, 10);
	    fd_lblPleaseSelectThe.right = new FormAttachment(0, 201);
	    lblPleaseSelectThe.setLayoutData(fd_lblPleaseSelectThe);
	    lblPleaseSelectThe.setText("Please select the objects for a reference check on their fields. Each selected object will be downloaded as a separate csv.");
	    
	    Button btnuncheckall = new Button(shell, SWT.NONE);
	    btnuncheckall.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent arg0) {
	    		table.deselectAll();
	    	}
	    });
	    FormData fd_btnuncheckall = new FormData();
	    fd_btnuncheckall.right = new FormAttachment(table, -76);
	    btnuncheckall.setLayoutData(fd_btnuncheckall);
	    btnuncheckall.setText("Uncheck all");
	    
	    Button btncheckall = new Button(shell, SWT.NONE);
	    fd_btnuncheckall.top = new FormAttachment(btncheckall, 6);
	    fd_btnuncheckall.left = new FormAttachment(btncheckall, 0, SWT.LEFT);
	    btncheckall.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent arg0) {
	    		table.selectAll();	    		
	    	}
	    });
	    btncheckall.setText("Check all");
	    FormData fd_btncheckall = new FormData();
	    fd_btncheckall.bottom = new FormAttachment(100, -147);
	    fd_btncheckall.right = new FormAttachment(table, -76);
	    fd_btncheckall.left = new FormAttachment(0, 58);
	    btncheckall.setLayoutData(fd_btncheckall);
	    
	    Button btnNext = new Button(shell, SWT.NONE);
	    btnNext.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent arg0) {
	    		int[] indices=table.getSelectionIndices();
	    		for (int i:indices ){
	    		
	    		selectedobjslist.add(objslist.get(i));
	    		}
	    		
	    		windowtwosuccess = true;
	    		shell.close();	    		
	    	}


	    });
	    FormData fd_btnNext = new FormData();
	    fd_btnNext.right = new FormAttachment(table, -76);
	    fd_btnNext.left = new FormAttachment(0, 58);
	    fd_btnNext.top = new FormAttachment(lblPleaseSelectThe, 35);
	    btnNext.setLayoutData(fd_btnNext);
	    btnNext.setText("Download");
	    
	    Label lblHintUseControl = new Label(shell, SWT.NONE);
	    fd_lblPleaseSelectThe.bottom = new FormAttachment(100, -280);
	    lblHintUseControl.setFont(SWTResourceManager.getFont("Segoe UI", 8, SWT.ITALIC));
	    FormData fd_lblHintUseControl = new FormData();
	    fd_lblHintUseControl.bottom = new FormAttachment(table, 0, SWT.BOTTOM);
	    fd_lblHintUseControl.left = new FormAttachment(0, 31);
	    fd_lblHintUseControl.top = new FormAttachment(0, 353);
	    fd_lblHintUseControl.right = new FormAttachment(0, 182);
	    lblHintUseControl.setLayoutData(fd_lblHintUseControl);
	    lblHintUseControl.setText("Hint: Use control or shift keys\r\n for multi select.");

	}

		public static List<String> returnselectedobjslist() {
			
			return selectedobjslist;
			
			
		}
	}
