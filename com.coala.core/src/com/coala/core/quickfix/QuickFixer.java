package com.coala.core.quickfix;

import com.coala.core.utils.DiffUtils;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolutionGenerator;

/**
 * This class is responsible for providing marker resolution.
 */
public class QuickFixer implements IMarkerResolutionGenerator {
  @Override
  public IMarkerResolution[] getResolutions(IMarker mk) {
    try {
      Object problem = mk.getAttribute(IMarker.MESSAGE);
      String diff = mk.getAttribute("diff", null);
      if (diff != null) {
        diff = DiffUtils.cleanDiff(diff);
        return new IMarkerResolution[] { new QuickFix("Apply patch for " + problem, diff) };
      }
      return new IMarkerResolution[] {};
    } catch (CoreException ex) {
      return new IMarkerResolution[0];
    }
  }

  private class QuickFix implements IMarkerResolution {
    String label;
    String diff;

    QuickFix(String label, String diff) {
      this.label = label;
      this.diff = diff;
    }

    public String getLabel() {
      return label;
    }

    public void run(IMarker marker) {
      try {
        String location = (String) marker.getAttribute("file");
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        IFile file = (IFile) root.findMember(location.substring(1));
        if (DiffUtils.applyDiffToFile(file, diff)) {
          marker.delete();
        }
      } catch (CoreException ex) {
        System.out.println("Marker resolution failed.");
        ex.printStackTrace();
      }
    }
  }

}
