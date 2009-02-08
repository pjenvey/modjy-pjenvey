package com.xhaus.modjy;

import java.io.File;

import org.python.core.PyDictionary;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.core.PyTuple;

public class ModjyTestAppInvocation extends ModjyTestBase
{

	protected void appInvocationTestSetUp()
    	throws Exception
	{
		baseSetUp();
		setAppDir("");
		setAppFile("");
		setAppName("");
		setAppImportable("");
	}

	String[] importablepathElements = new String[] {
		"mock_framework",
		"web",
		"handlers",
		"wsgi_handlers",
	};

	public void testRelativeDirectory ( )
		throws Exception
	{
		baseSetUp();
		setRealPath("/test_apps_dir", "test_apps_dir");
		setAppDir("$/test_apps_dir");
		setAppFile("simple_app.py");
		createServlet();
	 	doGet();
		String result = getOutput();
		assertEquals("Hello World!", result);
	}

	public String setupAppImport ( String appCallableName )
    	throws Exception
	{
		StringBuffer filePathBuffer = new StringBuffer();
		StringBuffer importPath = new StringBuffer();
		for (int ix = 0 ; ix < importablepathElements.length ; ix++)
			{
			String resourcePath;
			String filePath;
			filePathBuffer.append(importablepathElements[ix]);
			resourcePath = "/WEB-INF/"+LIB_PYTHON_DIR+"/"+filePathBuffer.toString();
			filePath = LIB_PYTHON_TEST_PATH+"/"+filePathBuffer.toString();
			setRealPath(resourcePath, filePath);
			filePathBuffer.append("/");
			resourcePath = "/WEB-INF/"+LIB_PYTHON_DIR+"/"+filePathBuffer.toString()+"__init__.py";
			filePath = LIB_PYTHON_TEST_PATH+"/"+filePathBuffer.toString()+"__init__.py";
			setRealPath(resourcePath, filePath);
			importPath.append(importablepathElements[ix]);
			importPath.append(".");
			}
		importPath.append(appCallableName);
		return importPath.toString();
	}

	public  void testAppImportCallable ( )
    	throws Exception
	{
		appInvocationTestSetUp();
		String importableName = setupAppImport("WSGIHandlerFunction");
		setAppImportable(importableName);
		createServlet();
    	doGet();
    	String result = getOutput();
    	assertEquals("WSGIHandlerFunction called.", result);
	}

	public  void testAppImportBadInstantiable ( )
    	throws Exception
	{
		appInvocationTestSetUp();
		String importableName = setupAppImport("WSGIHandlerClass");
		setAppImportable(importableName);
		createServlet();
    	doGet();
		assertEquals("Status code != 500: ServerError, =='"+getStatus()+"'", 500, getStatus());
	}

	public  void testAppImportInstantiable ( )
    	throws Exception
	{
		appInvocationTestSetUp();
		String importableName = setupAppImport("WSGIHandlerClass()");
		setAppImportable(importableName);
		createServlet();
    	doGet();
    	String result = getOutput();
    	assertEquals("__call__ counter = 0", result);
	}

	public  void testAppImportInstantiableCached ( )
    	throws Exception
	{
		appInvocationTestSetUp();
		setInitParameter("cache_callables", "1");
		String importableName = setupAppImport("WSGIHandlerClass()");
		setAppImportable(importableName);
		createServlet();
    	doGet();
    	String result = getOutput();
    	assertEquals("__call__ counter = 0", result);
		clearOutput();
    	doGet();
    	result = getOutput();
    	assertEquals("__call__ counter = 1", result);
	}

	public  void testAppImportInstantiableNotCached ( )
    	throws Exception
	{
		appInvocationTestSetUp();
		setInitParameter("cache_callables", "0");
		String importableName = setupAppImport("WSGIHandlerClass()");
		setAppImportable(importableName);
		createServlet();
    	doGet();
    	String result = getOutput();
    	assertEquals("__call__ counter = 0", result);
		clearOutput();
    	doGet();
    	result = getOutput();
    	assertEquals("__call__ counter = 0", result);
	}

	public  void testAppImportInstantiableMethod ( )
    	throws Exception
	{
		appInvocationTestSetUp();
		String importableName = setupAppImport("WSGIHandlerClass().handler_fn");
		setAppImportable(importableName);
		createServlet();
    	doGet();
    	String result = getOutput();
    	assertEquals("handler_fn counter = 0", result);
	}

	public  void testAppImportInstantiableBadMethod ( )
    	throws Exception
	{
		appInvocationTestSetUp();
		String importableName = setupAppImport("WSGIHandlerClass().handler_fn.fn_attr");
		setAppImportable(importableName);
		createServlet();
    	doGet();
		assertEquals("Status code != 500: ServerError, =='"+getStatus()+"'", 500, getStatus());
	}

	public  void testAppImportInstantiableMethodCached ( )
    	throws Exception
	{
		appInvocationTestSetUp();
		setInitParameter("cache_callables", "1");
		String importableName = setupAppImport("WSGIHandlerClass().handler_fn");
		setAppImportable(importableName);
		createServlet();
    	doGet();
    	String result = getOutput();
    	assertEquals("handler_fn counter = 0", result);
		clearOutput();
    	doGet();
    	result = getOutput();
    	assertEquals("handler_fn counter = 1", result);
	}

	public  void testAppImportInstantiableMethodNotCached ( )
    	throws Exception
	{
		appInvocationTestSetUp();
		setInitParameter("cache_callables", "0");
		String importableName = setupAppImport("WSGIHandlerClass().handler_fn");
		setAppImportable(importableName);
		createServlet();
    	doGet();
    	String result = getOutput();
    	assertEquals("handler_fn counter = 0", result);
		clearOutput();
    	doGet();
    	result = getOutput();
    	assertEquals("handler_fn counter = 0", result);
	}

	public  void testBadAppImport()
		throws Exception
	{
		appInvocationTestSetUp();
		setAppImportable("never.existed");
		createServlet();
    	doGet();
		assertEquals("Status code != 500: ServerError, =='"+getStatus()+"'", 500, getStatus());
	}

}
