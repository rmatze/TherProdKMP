package com.matze

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.matze.therprodkmp.TherProdApp
import com.matze.therprodkmp.model.WorkdayResponse
import com.matze.therprodkmp.ui.theme.TherProdTheme
import com.matze.therprodkmp.util.roundToDecimals
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import therprodkmp.composeapp.generated.resources.Res
import therprodkmp.composeapp.generated.resources.ic_event_note_black_24dp

@Composable
@Preview
fun App() {
    TherProdTheme {
        TherProdApp()
    }
}

//@Composable
//@Preview
//fun App(root: RootComponent) {
//    MaterialTheme {
//
//        val childStack by root.childStack.subscribeAsState()
//        Children(
//            stack = childStack,
//            animation = stackAnimation(slide())
//        ) { child ->
//            when(val instance = child.instance) {
//                is RootComponent.Child.ScreenA -> ScreenA(instance.component)
//                is RootComponent.Child.ScreenB -> ScreenB(instance.component.text, instance.component)
//            }
//        }
//
////        val objects = remember {
////            listOf(
////                WorkdayResponse(0, "8/20/24", listOf(), listOf(), listOf()),
////                WorkdayResponse(1, "8/21/24", listOf(), listOf(), listOf()),
////                WorkdayResponse(2, "8/22/24", listOf(), listOf(), listOf()),
////            )
////        }
////
////        LazyColumn(
////            modifier = Modifier.fillMaxSize(),
////            contentPadding = PaddingValues(8.dp)
////        ) {
////
////
////
////            items(objects) { obj ->
////                ElevatedItemCard(
////                    obj = obj,
//////                    onClick = { onObjectClick(obj.id) },
////                )
////            }
////        }
//    }
//}

@Composable
fun ElevatedItemCard(
    obj: WorkdayResponse,
//    onClick: () -> Unit,
//    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .background(Color.White)
//            .clickable { onClick() }
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text(
                    text = obj.date,
                    modifier = Modifier
                        .padding(top = 16.dp, start = 16.dp),
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                )
                val timeSheetList = obj.timesheetResponse.toList()
                var timeWorked = 0
                timeSheetList.forEach {
                    timeWorked += it.minsClockedIn ?: 0
                }
                var treatmentTime = 0
                obj.treatmentResponse.forEach {
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
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(resource = Res.drawable.ic_event_note_black_24dp),
                contentDescription = "meeting icon",
//                tint = Teal200,
                modifier = Modifier
                    .padding(16.dp)
                    .align(alignment = Alignment.CenterVertically)
            )
        }
    }
}
