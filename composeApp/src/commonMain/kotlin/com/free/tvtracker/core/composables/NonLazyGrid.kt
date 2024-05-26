package com.free.tvtracker.core.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NonLazyGrid(
    columns: Int,
    itemCount: Int,
    modifier: Modifier = Modifier,
    content: @Composable (Int) -> Unit
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        var rows = (itemCount / columns)
        if (itemCount.mod(columns) > 0) {
            rows += 1
        }

        for (rowId in 0 until rows) {
            val firstIndex = rowId * columns

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.height(IntrinsicSize.Min)) {
                for (columnId in 0 until columns) {
                    val index = firstIndex + columnId
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .height(IntrinsicSize.Min)
                            .weight(1f)
                    ) {
                        if (index < itemCount) {
                            content(index)
                        }
                    }
                }
            }
        }
    }
}

fun LazyListScope.gridInLazyColumn(
    columns: Int,
    itemCount: Int,
    rowModifier: Modifier = Modifier,
    content: @Composable (Int) -> Unit
) {
    var rows = (itemCount / columns)
    if (itemCount.mod(columns) > 0) {
        rows += 1
    }

    for (rowId in 0 until rows) {
        val firstIndex = rowId * columns
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = rowModifier.height(IntrinsicSize.Min)) {
                for (columnId in 0 until columns) {
                    val index = firstIndex + columnId
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .height(IntrinsicSize.Min)
                            .weight(1f)
                    ) {
                        if (index < itemCount) {
                            content(index)
                        }
                    }
                }
            }
            if (rowId + 1 < rows) {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }

}
