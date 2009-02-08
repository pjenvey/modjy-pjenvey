package com.xhaus.modjy;

import java.io.File;

import org.python.core.PyDictionary;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.core.PyTuple;

public class ModjyTestWebInf extends ModjyTestBase
{

	final static String callables_dir = "test_apps";

	protected void webInfTestSetUp()
    	throws Exception
	{
		baseSetUp();
		setAppFile("web_inf_tests.py");
        setRealPath("/WEB-INF/lib-python/some_libs", "lib_python_folder/test-lib");
        setRealPath("/WEB-INF/lib-python/some_libs/__init__.py", "lib_python_folder/test_lib/__init__.py");
        setRealPath("/WEB-INF/lib-python/some_libs/some_libs.py", "lib_python_folder/test_lib/some_libs.py");
	}

	public void testLibPythonVisible ( )
    	throws Exception
	{
		// Check that a lib directory in lib-python is visible.
		webInfTestSetUp();
		setAppName("test_import_from_lib_python");
		createServlet();
    	doGet();
    	String result = getOutput();
    	assertEquals("Factorial 10 is 3628800", result);
	}

	public void testImportFromZipInLibPython ( )
    	throws Exception
	{
		// Check that a lib directory in lib-python is visible.
		webInfTestSetUp();
        setRealPath("/WEB-INF/lib-python/test_modules.zip", "lib_python_folder/test_modules.zip");
        setRealPath("/WEB-INF/lib-python/add_zips.pth", "lib_python_folder/add_zips.pth");
		setAppName("test_import_from_zip_file");
		createServlet();
    	doGet();
    	String result = getOutput();
    	assertEquals("This is a library function", result);
	}

}
