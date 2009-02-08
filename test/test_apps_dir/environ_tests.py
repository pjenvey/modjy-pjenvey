# -*- coding: windows-1252 -*-

"""
	A variety of app callables used to test the WSGI environment.
"""

def test_echo_wsgi_env(environ, start_response):
	writer = start_response("200 OK", [])
	output_dict = {}
	for k in environ["QUERY_STRING"].split(';'):
		output_dict[k] = environ[k]
	return [repr(output_dict)]

def test_env_is_dict(environ, start_response):
	writer = start_response("200 OK", [])
	if type(environ) is type({}):
		writer("true")
	else:
		writer("false")
	return []

def test_env_is_mutable(environ, start_response):
	writer = start_response("200 OK", [])
	try:
		environ['some_key'] = 'some value'
		writer("true")
	except:
		writer("false")
	return []

def test_env_contains_request_method(environ, start_response):
	writer = start_response("200 OK", [])
	try:
		writer(environ['REQUEST_METHOD'])
	except KeyError, k:
		writer(str(k))
	return []

def test_env_script_name_path_info(environ, start_response):
	writer = start_response("200 OK", [])
	writer("%s:::%s" % (environ['SCRIPT_NAME'], environ['PATH_INFO']))
	return []

def test_env_query_string(environ, start_response):
	writer = start_response("200 OK", [])
	writer(environ['QUERY_STRING'])
	return []

required_cgi_vars = [
    'REQUEST_METHOD',
    'SCRIPT_NAME',
    'PATH_INFO',
    'QUERY_STRING',
    'CONTENT_TYPE',
    'CONTENT_LENGTH',
    'SERVER_NAME',
    'SERVER_PORT',
    'SERVER_PROTOCOL',
]

other_cgi_vars = [
]

def test_cgi_vars_are_str(environ, start_response):
    start_response("200 OK", [])
    for rcv in required_cgi_vars:
        if environ.has_key(rcv) and not isinstance(environ[rcv], str):
            return ["fail: '%s(%s)' is not 'str'" % (rcv, str(type(environ[rcv])))]
    for ocv in other_cgi_vars:
        if environ.has_key(ocv) and not isinstance(environ[ocv], str):
            return ["fail: '%s(%s)' is not 'str'" % (ocv, str(type(environ[ocv])))]
    for k in environ.keys():
        if k.startswith('HTTP_') and not isinstance(environ[k], str):
            return ["fail: '%s(%s)' is not 'str'" % (k, str(type(environ[k])))]
    return ["pass"]
