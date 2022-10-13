package http.authz
import future.keywords

import input.authorities
import input.username
import input.method
import input.path

default allow := false

allow if {
#    print(input)
    "ROLE_JOURNALIST" in authorities
}

