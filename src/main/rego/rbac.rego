package app.rbac

import future.keywords.contains
import future.keywords.if
import future.keywords.in

# By default, deny requests.
default allow := false

# Allow admins to do anything.
allow if user_is_admin

# Allow the action if the user is granted permission to perform the action.
allow if {
	# Find grants for the user.
	some grant in user_is_granted

	# Check if the grant permits the action.
	input.action == grant.action
	input.type == grant.type
}

# user_is_admin is true if "admin" is among the user's roles as per data.user_roles
user_is_admin if "admin" in data.user_roles[input.user]

# user_is_granted is a set of grants for the user identified in the request.
# The `grant` will be contained if the set `user_is_granted` for every...
user_is_granted contains grant if {
	# `role` assigned an element of the user_roles for this user...
	some role in data.user_roles[input.user]

	# `grant` assigned a single grant from the grants list for 'role'...
	some grant in data.role_grants[role]
}