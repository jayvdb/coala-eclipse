package com.coala.core.tests;

import static org.junit.Assert.assertEquals;

import com.coala.core.utils.ExternalUtils;

import org.apache.commons.exec.ExecuteException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

public class RuncoafileTest {

  private IProject      project;
  private IFile         file;

  /**
   * Setup before test.
   * @throws Exception exception
   */
  @Before
  public void setUp() throws Exception {
    String data = "package test_proj;\n\npublic class newClass {\n\n}";
    String coafileData = "[Default]\nfiles = *.java\n\nbears = CheckstyleBear";
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
    IFile coafile = project.getFile(".coafile");
    if (!coafile.exists()) {
      byte[] bytes = coafileData.getBytes();
      InputStream source = new ByteArrayInputStream(bytes);
      coafile.create(source, IResource.NONE, null);
    }
  }

  @Test
  public void test() throws ExecuteException, IOException, CoreException {
    IPath path = project.getLocation();
    String folder = path.toOSString();
    ExternalUtils.runcoafile(folder, project);
    sleep(15);
    IMarker[] markers = file.findMarkers("com.coala.core.coolproblem", true,
        IResource.DEPTH_INFINITE);
    assertEquals(markers.length, 3);
  }

  /**
   * Tear-down after test.
   * @throws CoreException exception
   */
  @After
  public void tearDown() throws CoreException {
    if (project.isOpen()) {
      project.close(null);
    }
    project.delete(true, true, null);
    ResourcesPlugin.getWorkspace().save(true, null);
  }
  
  private void sleep(int seconds) {
    try {
      TimeUnit.SECONDS.sleep(seconds);
    } catch (InterruptedException ex) {
      ex.printStackTrace();
    }
  }

}
