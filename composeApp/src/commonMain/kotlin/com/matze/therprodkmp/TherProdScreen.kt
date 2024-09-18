package com.matze.therprodkmp

import WorkdayTreatmentScreen
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.matze.therprodkmp.ui.WorkdayDateScreen
import com.matze.therprodkmp.ui.WorkdayDetailsScreen
import com.matze.therprodkmp.ui.WorkdayListScreen
import com.matze.therprodkmp.ui.WorkdayMeetingScreen
import com.matze.therprodkmp.ui.WorkdaySummaryScreen
import com.matze.therprodkmp.ui.WorkdayViewModel
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import therprodkmp.composeapp.generated.resources.Res
import therprodkmp.composeapp.generated.resources.add_meeting
import therprodkmp.composeapp.generated.resources.add_treatment
import therprodkmp.composeapp.generated.resources.app_name
import therprodkmp.composeapp.generated.resources.back_button
import therprodkmp.composeapp.generated.resources.clock_in_out
import therprodkmp.composeapp.generated.resources.details
import therprodkmp.composeapp.generated.resources.summary

/**
 * enum values that represent the screens in the app
 */
enum class TherProdScreen(val title: StringResource) {
    Start(title = Res.string.app_name),
    Session(title = Res.string.clock_in_out),
    Meeting(title = Res.string.add_meeting),
    Treatment(title = Res.string.add_treatment),
    Summary(title = Res.string.summary),
    Details(title = Res.string.details)
}

@ExperimentalMaterial3Api
@Composable
fun TherProdAppBar(
    currentScreen: TherProdScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    deleteWorkdayItem: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(
                    onClick =
                    navigateUp
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(Res.string.back_button)
                    )
                }
            }
        },
        actions = {
            // Conditionally add a right-aligned icon button for detail screen only
            if (currentScreen.title == TherProdScreen.Details.title) {
                IconButton(onClick = deleteWorkdayItem) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Options"
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TherProdApp(
    viewModel: WorkdayViewModel = viewModel { WorkdayViewModel() },
    navController: NavHostController = rememberNavController()
) {
// Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = TherProdScreen.valueOf(
        backStackEntry?.destination?.route ?: TherProdScreen.Start.name
    )

    Scaffold(
        topBar = {
            TherProdAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                deleteWorkdayItem = {
                    viewModel.deleteWorkdayById(viewModel.getWorkdayId())
                    navController.navigate(TherProdScreen.Start.name)
                }

            )
        }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = TherProdScreen.Start.name,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(route = TherProdScreen.Start.name) {
                WorkdayListScreen(
                    viewModel,
                    onNextButtonClicked = {
                        navController.navigate(TherProdScreen.Session.name)
                    },
                    onCardClicked = {
                        viewModel.setWorkdayId(it)
                        navController.navigate(TherProdScreen.Details.name)
                    }
                )
            }
            composable(route = TherProdScreen.Session.name) {
                WorkdayDateScreen(
                    onNextButtonClicked = { date, timesheetList ->
                        viewModel.setDate(date)
                        viewModel.setTimesheets(timesheetList)
                        navController.navigate(TherProdScreen.Meeting.name)
                    },
                    onCancelButtonClicked = {
                        cancelOrderAndNavigateToStart(viewModel, navController)
                    },
                    modifier = Modifier.fillMaxHeight()
                )
            }
            composable(route = TherProdScreen.Meeting.name) {
                WorkdayMeetingScreen(
                    onNextButtonClicked = {
                        viewModel.setMeetings(it)
                        navController.navigate(TherProdScreen.Treatment.name)
                    },
                    onCancelButtonClicked = {
                        cancelOrderAndNavigateToStart(viewModel, navController)
                    }
                )
            }
            composable(route = TherProdScreen.Treatment.name) {
                WorkdayTreatmentScreen(
                    onNextButtonClicked = {
                        viewModel.setTreatments(it)
                        navController.navigate(TherProdScreen.Summary.name)
                    },
                    onCancelButtonClicked = {
                        cancelOrderAndNavigateToStart(viewModel, navController)
                    }
                )
            }
            composable(route = TherProdScreen.Summary.name) {
                WorkdaySummaryScreen(
                    workdayUiState = uiState,
                    onFinishButtonClicked = {
                        viewModel.addWorkday()
                        navController.navigate(TherProdScreen.Start.name)
                        {
                            popUpTo(TherProdScreen.Start.name)
                            launchSingleTop = true
                        }
                    },
                    onCancelButtonClicked = {
                        cancelOrderAndNavigateToStart(viewModel, navController)
                    }
                )
            }
            composable(route = TherProdScreen.Details.name) {
                WorkdayDetailsScreen(
                    viewModel
                )
            }
        }
    }
}

/**
 * Resets the [OrderUiState] and pops up to [TherProdScreen.Start]
 */
private fun cancelOrderAndNavigateToStart(
    viewModel: WorkdayViewModel,
    navController: NavHostController
) {
    viewModel.resetWorkday()
    navController.popBackStack(TherProdScreen.Start.name, inclusive = false)
}