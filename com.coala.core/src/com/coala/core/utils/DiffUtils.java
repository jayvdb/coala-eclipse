package com.coala.core.utils;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

public class DiffUtils {

  /**
   * Cleans the given `diff` to contain only the changes. This is the required format
   * for the `diff_match_patch` library.
   */
  public static String cleanDiff(String diff) {
    return diff.substring(diff.indexOf("@@"));
  }

  /**
   * Apply the given `diff` to the `IFile`.
   * Returns true if the `diff` was applied successfully, false otherwise.
   */
  public static boolean applyDiffToFile(IFile file, String diff) {
    diff_match_patch dmp = new diff_match_patch();
    try {
      InputStream in = file.getContents();
      String contents = IOUtils.toString(in, "UTF-8");
      LinkedList<diff_match_patch.Patch> patch = (LinkedList<diff_match_patch.Patch>) dmp
          .patch_fromText(diff);
      Object[] res = dmp.patch_apply(patch, contents);
      in = IOUtils.toInputStream((String) res[0], "UTF-8");
      file.setContents(in, IFile.KEEP_HISTORY, null);
      return true;
    } catch (CoreException ex) {
      System.out.println("Opening InputStream failed.");
      ex.printStackTrace();
    } catch (IOException ex) {
      System.out.println("InputStream to String conversion failed.");
      ex.printStackTrace();
    }
    return false;
  }
}
