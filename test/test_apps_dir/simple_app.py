# -*- coding: windows-1252 -*-

def simple_app(environ, start_response):
	writer = start_response("200 OK", [])
	writer("Hello World!")
	return []
