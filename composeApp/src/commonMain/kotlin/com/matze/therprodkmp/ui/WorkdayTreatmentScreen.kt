import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType

import com.matze.therprodkmp.data.model.Treatment
import com.matze.therprodkmp.ui.SelectQuantityButton
import com.matze.therprodkmp.ui.WorkdayFilledCard
import org.jetbrains.compose.resources.stringResource
import therprodkmp.composeapp.generated.resources.Res
import therprodkmp.composeapp.generated.resources.cancel
import therprodkmp.composeapp.generated.resources.new_session
import therprodkmp.composeapp.generated.resources.next

@Composable
fun WorkdayTreatmentScreen(
    onSelectionChanged: (List<Int>) -> Unit = {},
    onCancelButtonClicked: () -> Unit = {},
    onNextButtonClicked: (List<Treatment>) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var treatments by remember { mutableStateOf<List<Int?>>(listOf()) }

    var itemsList by remember { mutableStateOf(listOf<Treatment>()) }
    var showDialog by remember { mutableStateOf(false) }
    var textFieldValue by remember { mutableStateOf(TextFieldValue("")) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                // Top row with one button
                Button(
                    onClick = { showDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add Treatment")
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Bottom row with two buttons side by side
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedButton(
                        onClick = onCancelButtonClicked,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(Res.string.cancel))
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = { onNextButtonClicked(itemsList) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(Res.string.next))
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                itemsIndexed(itemsList) { index, item ->
                    Text(
                        text = "Treatment ${index + 1}: " + item.timeInMins + " mins",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                }
            }
        }

        if (showDialog) {
            Dialog(onDismissRequest = { showDialog = false }) {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    shadowElevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Treatment Time in Mins")

                        Spacer(modifier = Modifier.height(8.dp))

                        Box(
                            modifier = Modifier
                                .border(1.dp, Color.Gray, MaterialTheme.shapes.small)
                                .padding(8.dp)
                                .fillMaxWidth()
                        ) {
                            BasicTextField(
                                value = textFieldValue,
                                onValueChange = { textFieldValue = it },
                                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                singleLine = true,
                                decorationBox = { innerTextField ->
                                    if (textFieldValue.text.isEmpty()) {
                                        Text("Enter text here...", color = Color.Gray)
                                    }
                                    innerTextField()  // The actual text input
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(onClick = {
                                showDialog = false
                            }) {
                                Text("Cancel")
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Button(onClick = {
                                if (textFieldValue.text.isNotBlank()) {
                                    itemsList = itemsList + Treatment(0, 0, textFieldValue.text.toInt())
                                    textFieldValue = TextFieldValue("")
                                    showDialog = false
                                }
                            }) {
                                Text("OK")
                            }
                        }
                    }
                }
            }
        }
    }
}