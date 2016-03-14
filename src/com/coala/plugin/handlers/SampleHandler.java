package com.coala.plugin.handlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class SampleHandler extends AbstractHandler {
	/**
	 * The constructor.
	 * @throws IOException 
	 */
	
	String output_json = null;
	public SampleHandler() {
		
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {

		try {
			cleanup();
			run_coala();
			process_json();
		} catch (CoreException | IOException | InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
						
		return null;
	}
	
	public void process_json() throws IOException{
		
		String content = new Scanner(new File(get_json_dir())).useDelimiter("\\Z").next();
		JSONObject obj = new JSONObject(content);
		JSONArray arr = obj.getJSONObject("results").getJSONArray("default");
		for (int i = 0; i < arr.length(); i++)
		{
			
			String message = arr.getJSONObject(i).getString("message");
			String origin = arr.getJSONObject(i).getString("origin");
			int severity = arr.getJSONObject(i).getInt("severity");
			//System.out.println(message);
			JSONArray arr2 = arr.getJSONObject(i).getJSONArray("affected_code"); 
		    for (int j = 0; j < arr2.length(); j++)
			{
			    int end_line = arr2.getJSONObject(j).getJSONObject("end").getInt("line");
			    System.out.println(end_line);
			    createCoolMarker(end_line , 3-severity ,"( "+ origin + " ): "+message);
			}
		}
	
	}
	
	public IMarker createCoolMarker(int line_num, int flag , String message) {
		// :: param : flag = 1 for error 
		// 		      flag = 2 for warning
		IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
	    IFileEditorInput input = (IFileEditorInput)editor.getEditorInput() ;
	    IFile file = input.getFile();
	    IResource resource = (IResource) file;
		   try {
			 
			   
		      IMarker marker = resource.createMarker("com.coala.plugin.coolproblem");
		     // marker.setAttribute("coolFactor", "ULTRA");
		      marker.setAttribute(IMarker.LINE_NUMBER, line_num);
		      if(flag==1)
		    	  marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
		      else if(flag ==2)
		    	  marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
		      marker.setAttribute(IMarker.MESSAGE, message);
		      marker.setAttribute(IMarker.TEXT, "Awesome Marker");
		      
		      return marker;
		   } catch (CoreException e) {
		      // You need to handle the cases where attribute value is rejected
			   return null;
		   }
		}
	
	public String get_current_file_path() throws FileNotFoundException{
		IWorkbenchPart workbenchPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart(); 
		IFile file = (IFile) workbenchPart.getSite().getPage().getActiveEditor().getEditorInput().getAdapter(IFile.class);
		if (file == null) throw new FileNotFoundException();
		String path = file.getRawLocation().toOSString();
		String p = file.getFullPath().toOSString();
		String Dir = System.getProperty("user.home");

		return path;
	}
	
	public void cleanup() throws CoreException{
		IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
	    IFileEditorInput input = (IFileEditorInput)editor.getEditorInput() ;
	    IFile file = input.getFile();
		IMarker[] problems = null;
		IResource resource = (IResource) file;
		int depth = IResource.DEPTH_INFINITE;
		try {
		   problems = resource.findMarkers(IMarker.PROBLEM, true, depth);
		   
		} catch (CoreException e) {
		   // something went wrong
		}
		for(IMarker m : problems){
			if(m.getType().equals("com.coala.plugin.coolproblem"))
				m.delete();
		}
	}
	
	public void run_coala() throws IOException, InterruptedException{
		String Dir = System.getProperty("user.home");
		String path = get_current_file_path();
		String command = "coala-json -f "  + path + " -o " +  get_json_dir()  + " -d Bears -b CheckstyleBear ";
		System.out.println(command);
		Process process1 = Runtime.getRuntime().exec(command);
		process1.waitFor();
	}
	
	public String get_json_dir(){
		String Dir = System.getProperty("user.home");
		Dir = Dir.concat("/out.json");
		return Dir;
		
	}
	
}
