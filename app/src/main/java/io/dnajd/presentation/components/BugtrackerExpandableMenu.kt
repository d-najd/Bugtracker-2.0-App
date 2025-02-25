package io.dnajd.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.dnajd.bugtracker.R
import io.dnajd.domain.table_task.model.TableChildTask

/**
 * @param modifier modifier for the entire composable
 * @param expanded if true the content will be expanded and vice versa
 * @param displayMainDivider if true will display divider located below [menuContent]
 * @param displaySecondaryDivider if true will display divider which moves when the composable is
 * expanded and collapsed, located under [expandableContent]
 * @param mainDividerThickness thickness of the main divider
 * @param enter EnterTransition(s) used for the appearing animation, fading in while expanding
 *              vertically by default
 * @param exit ExitTransition(s) used for the disappearing animation, fading out while
 *             shrinking vertically by default
 * @param onClick optional clickable that gets triggered when the menu gets clicked, optional
 *                because the entire composable could be made clickable using [modifier]
 * @param menuContent menu that functions similarly to dropdown menu
 * @param expandableContent the content that will get expanded when [expanded] is true
 * @sample BugtrackerExpandableMenuPreview()
 */
@Composable
fun BugtrackerExpandableMenu(
	modifier: Modifier = Modifier,
	expanded: Boolean,
	displayMainDivider: Boolean = true,
	displaySecondaryDivider: Boolean = true,
	mainDividerThickness: Dp = 4.dp,
	enter: EnterTransition = fadeIn() + expandVertically(),
	exit: ExitTransition = fadeOut() + shrinkVertically(),
	onClick: (() -> Unit)? = null,
	menuContent: @Composable RowScope.() -> Unit,
	expandableContent: @Composable ColumnScope.() -> Unit,
) {
	Column(
		modifier = modifier,
		verticalArrangement = Arrangement.Center,
	) {
		BugtrackerMultipurposeMenu(
			dividerThickness = mainDividerThickness,
			includeDivider = displayMainDivider,
			onClick = onClick,
		) {
			menuContent()
		}

		AnimatedVisibility(
			visible = expanded,
			enter = enter,
			exit = exit,
		) {
			Column(
				verticalArrangement = Arrangement.Center,
			) {
				expandableContent()
			}
		}

		if (displaySecondaryDivider) {
			Divider(
				modifier = Modifier.padding(top = if (displayMainDivider) 14.dp else 0.dp)
			)
		}
	}
}

@Preview
@Composable
private fun BugtrackerExpandableMenuPreview() {
	var expanded by remember { mutableStateOf(false) }
	val childTasks = listOf(
		TableChildTask(
			id = 1,
			title = "Title 1",
			tableId = 1,
		),
		TableChildTask(
			id = 2,
			title = "Title 2",
			tableId = 2,
		)
	)

	BugtrackerCard {
		BugtrackerExpandableMenu(
			onClick = { expanded = !expanded },
			expanded = expanded,
			menuContent = {
				Row(
					verticalAlignment = Alignment.CenterVertically,
				) {
					Text(
						modifier = Modifier.padding(start = 3.5.dp),
						fontWeight = FontWeight.Bold,
						fontSize = 16.sp,
						text = stringResource(R.string.field_child_issues),
					)
					Row(
						modifier = Modifier.fillMaxWidth(),
						horizontalArrangement = Arrangement.End,
						verticalAlignment = Alignment.CenterVertically,
					) {
						Text(
							text = childTasks.size.toString(),
							color = MaterialTheme.colorScheme.onSurface.copy(0.5f),
						)

						Icon(
							modifier = Modifier.padding(start = 3.dp, end = 4.dp),
							tint = MaterialTheme.colorScheme.onSurface.copy(0.8f),
							imageVector = Icons.Default.ArrowDropDown,
							contentDescription = ""
						)
					}
				}
			},
			expandableContent = {
				for (childTask in childTasks) {
					BugtrackerIconPairField(
						modifier = Modifier.padding(top = 6.dp),
						iconContent = {
							Icon(
								imageVector = Icons.Default.TaskAlt,
								tint = MaterialTheme.colorScheme.primary,
								contentDescription = ""
							)
						},
						textContent = {
							Text(text = childTask.title)
							Row(
								verticalAlignment = Alignment.CenterVertically,
							) {
								Text(
									text = "${stringResource(R.string.field_task).uppercase()}-${childTask.id}",
									color = MaterialTheme.colorScheme.onSurface.copy(0.5f),
								)
								Text(
									text = " = ",
									color = colorResource(R.color.coral),
									fontFamily = FontFamily.SansSerif,
									fontSize = 26.sp,
								)
								Text(
									text = "INSERT TABLE NAME",
									color = MaterialTheme.colorScheme.onSurface.copy(0.8f),
								)
							}
						}
					)
				}
			}
		)
	}
}