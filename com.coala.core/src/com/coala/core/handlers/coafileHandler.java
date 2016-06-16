package com.coala.core.handlers;

import com.coala.core.utils.ExternalUtils;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.ui.PlatformUI;

import java.io.IOException;

/**
 * The PluginHandler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class Plugin extends AbstractHandler {

  /**
   * The constructor.
   * 
   * @throws IOException
   *           exception
   */
  public Plugin() {
  }

  /**
   * Execute analysis.
   */
  public Object execute(ExecutionEvent event) throws ExecutionException {
    IFile file = (IFile) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
        .getActivePart().getSite().getPage().getActiveEditor().getEditorInput()
        .getAdapter(IFile.class);
    new RemoveMarkers().execute(event);
    try {
      ExternalUtils.runBearOnFile(file, "CheckstyleBear");
    } catch (IOException | InterruptedException ex) {
      ex.printStackTrace();
    }
    return null;
  }

}
