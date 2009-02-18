# -*- coding: windows-1252 -*-

"""
	A variety of app callables used to test WEB-INF interactions.
"""

def test_import_from_lib_python(environ, start_response):
    from test_lib import some_libs
    writer = start_response("200 OK", [])
    return ["Factorial 10 is %d" % some_libs.factorial(10)]

def test_import_from_zip_file(environ, start_response):
    from module_in_zipfile import lib_function
    writer = start_response("200 OK", [])
    return [lib_function()]

def test_execed_import_in_pth(environ, start_response):
    writer = start_response("200 OK", [])
    import sys
    if sys.modules.has_key('math'):
        return ["pass"]
    else:
        return ["fail"]
