package com.coala.plugin.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;
import org.json.JSONArray;
import org.json.JSONObject;

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
	 */
	public Plugin() {
	}

	public Object execute(ExecutionEvent event) throws ExecutionException {
		IFile file = (IFile) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart()
				.getSite().getPage().getActiveEditor().getEditorInput().getAdapter(IFile.class);
		try {
			new RemoveMarkers().execute(event);
			String json = getcoalaJSON(file, "CheckstyleBear");
			processJSON(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void processJSON(String json) throws IOException {
		JSONObject obj = new JSONObject(json);
		JSONArray arr = obj.getJSONObject("results").getJSONArray("default");
		for (int i = 0; i < arr.length(); i++) {
			String message = arr.getJSONObject(i).getString("message");
			String origin = arr.getJSONObject(i).getString("origin");
			int severity = arr.getJSONObject(i).getInt("severity");
			JSONArray arr2 = arr.getJSONObject(i).getJSONArray("affected_code");
			for (int j = 0; j < arr2.length(); j++) {
				int end_line = arr2.getJSONObject(j).getJSONObject("end").getInt("line");
				createCoolMarker(end_line, 3 - severity, "( " + origin + " ): " + message);
			}
		}
	}

	public IMarker createCoolMarker(int line_num, int flag, String message) {
		// :: param : flag = 1 for error
		// flag = 2 for warning
		IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		IFileEditorInput input = (IFileEditorInput) editor.getEditorInput();
		IFile file = input.getFile();
		IResource resource = (IResource) file;
		try {

			IMarker marker = resource.createMarker("com.coala.plugin.coolproblem");
			// marker.setAttribute("coolFactor", "ULTRA");
			marker.setAttribute(IMarker.LINE_NUMBER, line_num);
			if (flag == 1)
				marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
			else if (flag == 2)
				marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
			marker.setAttribute(IMarker.MESSAGE, message);
			marker.setAttribute(IMarker.TEXT, "Awesome Marker");

			return marker;
		} catch (CoreException e) {
			return null;
		}
	}

	public static String getcoalaJSON(IFile file, String bear) throws IOException {
		String path = file.getRawLocation().toOSString();
		String cmd = "coala-json -f " + path + " -b " + bear;
		System.out.println(cmd);
		Process proc = Runtime.getRuntime().exec(cmd);
		InputStream is = proc.getInputStream();
		Scanner s = new Scanner(is);
		s.useDelimiter("\\A");
		String val = "";
		if (s.hasNext()) {
			val = s.next();
		} else {
			val = "";
		}
		s.close();
		return val;
	}
}
