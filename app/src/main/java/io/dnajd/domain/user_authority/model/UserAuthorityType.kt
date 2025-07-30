package io.dnajd.domain.user_authority.model

import androidx.annotation.StringRes
import io.dnajd.bugtracker.R

@Suppress("EnumEntryName") enum class UserAuthorityType(
	@StringRes val titleResId: Int,
	@StringRes val descriptionResId: Int,
) {
	project_view(
		R.string.authority_project_view,
		R.string.info_authority_project_view,
	),
	project_create(
		R.string.authority_project_create,
		R.string.info_authority_project_create,
	),
	project_edit(
		R.string.authority_project_edit,
		R.string.info_authority_project_edit,
	),
	project_delete(
		R.string.authority_project_delete,
		R.string.info_authority_project_delete,
	),
	project_manage(
		R.string.authority_manage_users,
		R.string.info_authority_manage_users,
	),
	project_owner(
		R.string.authority_owner,
		R.string.info_authority_manage_owner,
	),
}
