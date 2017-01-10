#!/bin/bash
#
#	linux chat server start script
#	@author HenryCo 
#	github { https://github.com/henryco }
#	contact {hd2files@google_mail}
#
#	START ARGS:
#	[loc_path] [mode] [db_type] [db_path]
#	[loc_path] [mode]
#	[loc_path]
#	[*void*]


DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
"$DIR"/./GovorCore.jar "$DIR"/ server-gui # --> server args here <-- #
