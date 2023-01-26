package io.dnajd.domain.user_authority.model

@Suppress("EnumEntryName")
enum class UserAuthorityType {
	project_view,
	project_create,
	project_delete,
	project_edit,
	project_manage_users,
	project_manage_managers,
	project_owner,
}
