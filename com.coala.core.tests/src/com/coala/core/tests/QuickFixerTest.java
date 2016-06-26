package com.coala.core.tests;

import static org.junit.Assert.assertEquals;

import com.coala.core.quickfix.QuickFixer;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IMarkerResolution;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class QuickFixerTest {

  private IProject project;
  private IFile    file;
  private IMarker  marker;

  /**
   * Setup before test.
   */
  @Before
  public void setUp() throws Exception {
    String data = "import os\npass";
    final String diff = "--- \n+++ \n@@ -1,2 +0,0 @@\n-import os\n-pass\n";
    IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
    project = root.getProject(TestUtils.generateRandomStr(5));
    if (!project.exists()) {
      project.create(null);
    }
    if (!project.isOpen()) {
      project.open(null);
    }
    file = project.getFile("test.java");
    if (!file.exists()) {
      byte[] bytes = data.getBytes();
      InputStream source = new ByteArrayInputStream(bytes);
      file.create(source, IResource.NONE, null);
    }
    marker = file.createMarker("com.coala.core.problem");
    marker.setAttribute(IMarker.MESSAGE, "");
    marker.setAttribute("file", file.toString());
    marker.setAttribute("diff", diff);
  }

  @Test
  public void test() throws IOException, CoreException {
    QuickFixer qf = new QuickFixer();
    IMarkerResolution[] mr = qf.getResolutions(marker);
    mr[0].run(marker);
    assertEquals("", IOUtils.toString(file.getContents()));
  }

  /**
   * Tear-down after test.
   */
  @After
  public void tearDown() throws Exception {
    if (project.isOpen()) {
      project.close(null);
    }
    project.delete(true, true, null);
    ResourcesPlugin.getWorkspace().save(true, null);
  }

}
