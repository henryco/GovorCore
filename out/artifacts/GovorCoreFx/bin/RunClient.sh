#!/bin/bash
#
#	linux chat client start script
#	@author HenryCo 
#	github { https://github.com/henryco }
#	contact {hd2files@google_mail}
#
#	START ARGS:
#	[loc_path] [mode] [ip] [port]
#	[loc_path] [mode]
#	[loc_path]
#	[*void*]
#

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
"$DIR"/./GovorCore.jar "$DIR"/ client # --> client args here <-- #
