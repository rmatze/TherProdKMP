package com.matze.therprodkmp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.matze.therprodkmp.data.model.WorkdayResponse
import com.matze.therprodkmp.util.roundToDecimals
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import therprodkmp.composeapp.generated.resources.Res
import therprodkmp.composeapp.generated.resources.ic_event_note_black_24dp
import therprodkmp.composeapp.generated.resources.new_session

@Composable
fun WorkdayListScreen(
    viewModel: WorkdayViewModel,
    onNextButtonClicked: (Int) -> Unit,
    onCardClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val workdays1 = viewModel.workdays.collectAsState()

//    LaunchedEffect(Unit) {
//        viewModel.fetchUsers()
//    }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(workdays1.value) {
                WorkdayFilledCard(it, onCardClicked)
            }

        }
        SelectQuantityButton(
            labelResource = Res.string.new_session,
            onClick = { onNextButtonClicked(1) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
    }
}

/**
 * Customizable button composable that displays the [labelResourceId]
 * and triggers [onClick] lambda when this composable is clicked
 */
@Composable
fun SelectQuantityButton(
    labelResource: StringResource,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .widthIn(min = 250.dp)
    ) {
        Text(stringResource(labelResource))
    }
}

@Composable
fun WorkdayFilledCard(workday: WorkdayResponse, onCardClicked: (Int) -> Unit) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        onClick = {
            onCardClicked(workday.id)
        },
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text(
                    text = workday.date,
                    modifier = Modifier
                        .padding(top = 16.dp, start = 16.dp),
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                )
                val timeSheetList = workday.timesheets.toList()
                var timeWorked = 0
                timeSheetList.forEach {
                    timeWorked += it.minsClockedIn ?: 0
                }
                var treatmentTime = 0
                workday.treatments.forEach {
                    treatmentTime += it.timeInMins
                }
                var productivity = 0.0
                if (timeWorked > 0) {
                    productivity = (treatmentTime / timeWorked.toDouble()) * 100
                }
                Text(
                    text = "Productivity: ${productivity.toFloat().roundToDecimals(2)}%",
                    modifier = Modifier
                        .padding(bottom = 16.dp, start = 16.dp),
                    fontWeight = FontWeight.Light,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                )
            }
            if (workday.meetings.isNotEmpty()) {
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(resource = Res.drawable.ic_event_note_black_24dp),
                    contentDescription = "meeting icon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(alignment = Alignment.CenterVertically)
                )
            }
        }
    }
}
