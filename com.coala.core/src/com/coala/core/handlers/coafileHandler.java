package com.coala.core.handlers;

import com.coala.core.utils.ExternalUtils;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import java.io.IOException;

/**
 * The PluginHandler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class coafileHandler extends AbstractHandler {

  /**
   * The constructor.
   * 
   * @throws IOException
   *           exception
   */
  public coafileHandler() {
  }

  /**
   * Execute analysis.
   */
  public Object execute(ExecutionEvent event) throws ExecutionException {
    String folder = null;
    IProject project = null;
    IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
    if (window != null) {
      IStructuredSelection selection = (IStructuredSelection) window.getSelectionService()
          .getSelection();
      Object firstElement = selection.getFirstElement();
      if (firstElement instanceof IAdaptable) {
        project = (IProject) ((IAdaptable) firstElement).getAdapter(IProject.class);
        if (project == null) {
          System.out.println("Project returned null.");
        } else {
          IPath path = project.getLocation();
          folder = path.toOSString();
        }
      }
    }
    new RemoveMarkers().execute(event);
    try {
      if (folder != null && project != null) {
        ExternalUtils.runcoafile(folder, project);
      } else {
        System.out.println("Couldn't run analysis using .coafile");
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return null;
  }

}
