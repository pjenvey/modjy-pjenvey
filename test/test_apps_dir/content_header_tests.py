# -*- coding: windows-1252 -*-

"""
	A variety of app callables used to test content related headers.
"""

def test_set_content_length(environ, start_response):
    writer = start_response("200 OK", [('content-length', '12')])
    writer('Hello World!')
    return []

def test_set_bad_content_length(environ, start_response):
    writer = start_response("200 OK", [('content-length', 'abcd')])
    writer('Hello World!')
    return []

def test_inferred_content_length(environ, start_response):
    writer = start_response("200 OK", [])
    return [environ["QUERY_STRING"]]

