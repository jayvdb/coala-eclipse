package com.coala.core.ui.editors;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class _coafileEditorForm extends FormPage {

  /**
   * Create the form page.
   */
  public _coafileEditorForm(String id, String title) {
    super(id, title);
  }

  /**
   * Create the form page.
   */
  public _coafileEditorForm(FormEditor editor, String id, String title) {
    super(editor, id, title);
  }

  /**
   * Create contents of the form.
   */
  @Override
  protected void createFormContent(IManagedForm managedForm) {
    FormToolkit toolkit = managedForm.getToolkit();
    ScrolledForm form = managedForm.getForm();
    form.setText("coafile editor");
    Composite body = form.getBody();
    toolkit.decorateFormHeading(form.getForm());
    toolkit.paintBordersFor(body);
    
    Section sctnSection = managedForm.getToolkit().createSection(managedForm.getForm().getBody(), Section.TWISTIE | Section.TITLE_BAR);
    sctnSection.setBounds(10, 10, 574, 150);
    managedForm.getToolkit().paintBordersFor(sctnSection);
    sctnSection.setText("Sections");
    sctnSection.setExpanded(true);
    
    List listSections = new List(sctnSection, SWT.BORDER);
    listSections.setItems(new String[] {"Default"});
    managedForm.getToolkit().adapt(listSections, true, true);
    sctnSection.setClient(listSections);
    
    Button btnAddSection = new Button(sctnSection, SWT.NONE);
    managedForm.getToolkit().adapt(btnAddSection, true, true);
    sctnSection.setTextClient(btnAddSection);
    btnAddSection.setText("New Section");
    
    Section sctnBears = managedForm.getToolkit().createSection(managedForm.getForm().getBody(), Section.TWISTIE | Section.TITLE_BAR);
    sctnBears.setBounds(10, 166, 284, 150);
    managedForm.getToolkit().paintBordersFor(sctnBears);
    sctnBears.setText("Bears");
    sctnBears.setExpanded(true);
    
    Button btnAddBear = new Button(sctnBears, SWT.NONE);
    managedForm.getToolkit().adapt(btnAddBear, true, true);
    sctnBears.setTextClient(btnAddBear);
    btnAddBear.setText("Add Bear");
    
    List listBears = new List(sctnBears, SWT.BORDER);
    managedForm.getToolkit().adapt(listBears, true, true);
    sctnBears.setClient(listBears);
    
    Section sctnNewSection = managedForm.getToolkit().createSection(managedForm.getForm().getBody(), Section.TWISTIE | Section.TITLE_BAR);
    sctnNewSection.setBounds(303, 166, 284, 150);
    managedForm.getToolkit().paintBordersFor(sctnNewSection);
    sctnNewSection.setText("Bear Settings");
    sctnNewSection.setExpanded(true);
    
    List listSettings = new List(sctnNewSection, SWT.BORDER);
    listSettings.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseDoubleClick(MouseEvent event) {
        InputDialog dlg = new InputDialog(null, listSettings.getSelection()[0], "", null, null);
        dlg.open();
      }
    });
    managedForm.getToolkit().adapt(listSettings, true, true);
    sctnNewSection.setClient(listSettings);
    
  }
}
