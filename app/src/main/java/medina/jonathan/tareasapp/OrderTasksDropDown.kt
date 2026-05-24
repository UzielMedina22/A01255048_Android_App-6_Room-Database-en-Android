package medina.jonathan.tareasapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun OrderTasksDropDown(modifier: Modifier = Modifier, selectSort: (TasksOrder) -> Unit) {
    var isExpanded by remember { mutableStateOf(false) }

    Box(modifier = modifier.padding(8.dp)) {
        IconButton(onClick = { isExpanded = !isExpanded }) {
            Icon(
                painter = painterResource(R.drawable.round_more_vert_32),
                contentDescription = stringResource(R.string.show_search_filters)
            )
        }
        DropdownMenu(
            expanded = isExpanded, onDismissRequest = { isExpanded = false}
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.show_by_recent)) },
                onClick = { selectSort(TasksOrder.RECENT) }
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.show_by_oldest)) },
                onClick = { selectSort(TasksOrder.OLDEST) }
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.show_by_a_z)) },
                onClick = { selectSort(TasksOrder.A_Z) }
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.show_by_z_a)) },
                onClick = { selectSort(TasksOrder.Z_A) }
            )
        }
    }
}