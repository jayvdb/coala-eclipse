package com.coala.core.ui.editors;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.IFormPage;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.wst.sse.ui.StructuredTextEditor;

public class _coafileEditor extends FormEditor {

  private StructuredTextEditor fSourceEditor;
  private int                  fSourceEditorIndex;
  private boolean              fSourceDirty = false;

  @Override
  public void init(final IEditorSite site, final IEditorInput input) throws PartInitException {
    super.init(site, input);
  }

  @Override
  protected void addPages() {
    fSourceEditor = new StructuredTextEditor();
    fSourceEditor.setEditorPart(this);

    try {
      // add form pages
      addPage(new _coafileEditorForm(this, "coafile_editor", "coafile"));

      // add source page
      fSourceEditorIndex = addPage(fSourceEditor, getEditorInput());
      setPageText(fSourceEditorIndex, "Source");
    } catch (final PartInitException ex) {
      ex.printStackTrace();
    }

    // add listener for changes of the document source
    getDocument().addDocumentListener(new IDocumentListener() {

      @Override
      public void documentAboutToBeChanged(final DocumentEvent event) {
        // nothing to do
      }

      @Override
      public void documentChanged(final DocumentEvent event) {
        fSourceDirty = true;
      }
    });
  }

  private IDocument getDocument() {
    final IDocumentProvider provider = fSourceEditor.getDocumentProvider();
    return provider.getDocument(getEditorInput());
  }

  @Override
  public void doSaveAs() {
    // not allowed
  }

  @Override
  public boolean isSaveAsAllowed() {
    return false;
  }

  @Override
  public void doSave(IProgressMonitor monitor) {
    if (getActivePage() != fSourceEditorIndex)
      updateSourceFromModel();

    fSourceEditor.doSave(monitor);
  }

  @Override
  protected void pageChange(final int newPageIndex) {
    // check for update from the source code
    if ((getActivePage() == fSourceEditorIndex) && (fSourceDirty))
      updateModelFromSource();

    // check for updates to be propagated to the source code
    if (newPageIndex == fSourceEditorIndex)
      updateSourceFromModel();

    // switch page
    super.pageChange(newPageIndex);

    // update page if needed
    final IFormPage page = getActivePageInstance();
    if (page != null) {
      // TODO update form page with new model data
      page.setFocus();
    }
  }

  private void updateModelFromSource() {
    // TODO update source code for source viewer using new model data
    String coafile = getContent();
    
    fSourceDirty = false;
  }

  private void updateSourceFromModel() {
    // TODO update source page from model
    // getDocument().set("new source code");
    fSourceDirty = false;
  }

  private IFile getFile() {
    final IEditorInput input = getEditorInput();
    if (input instanceof FileEditorInput)
      return ((FileEditorInput) input).getFile();

    return null;
  }

  private String getContent() {
    return getDocument().get();
  }

}
