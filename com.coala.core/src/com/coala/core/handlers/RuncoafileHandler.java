package com.coala.core.handlers;

import com.coala.core.utils.ExternalUtils;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * The PluginHandler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class RuncoafileHandler extends AbstractHandler {

  /**
   * The constructor.
   * 
   * @throws IOException
   *           exception
   */
  public RuncoafileHandler() {
  }

  /**
   * Execute analysis.
   */
  public Object execute(ExecutionEvent event) throws ExecutionException {
    String folder = null;
    IProject project = null;
    IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
    IFile openFile = (IFile) window.getActivePage().getActivePart().getSite().getPage()
        .getActiveEditor().getEditorInput().getAdapter(IFile.class);
    project = openFile.getProject();
    if (project != null) {
      IPath path = project.getLocation();
      folder = path.toOSString();
    }
    new RemoveMarkers().execute(event);
    try {
      if (folder != null && project.getFile(".coafile").exists()) {
        ExternalUtils.runcoafile(folder, project);
      } else {
        boolean ret = MessageDialog.openQuestion(null, "coafile was not found in this project",
            "coala requires a configuration file to run the analysis.\nDo you want to create one?");
        if (ret) {
          IFile coafile = project.getFile(".coafile");
          String data = "# Learn about writing coafile at http://coala.readthedocs.io/en/latest/Users/coafile.html";
          byte[] bytes = data.getBytes();
          InputStream source = new ByteArrayInputStream(bytes);
          coafile.create(source, IResource.NONE, null);
          IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
              .getActivePage();
          IDE.openEditor(page, coafile, true);
        }
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    } catch (PartInitException ex) {
      ex.printStackTrace();
    } catch (CoreException ex) {
      ex.printStackTrace();
    }
    return null;
  }

}
