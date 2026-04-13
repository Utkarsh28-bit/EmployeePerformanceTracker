package com.example.ept.ui.navigation
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ept.ui.screens.*
import com.example.ept.viewmodel.*
object Routes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val DASHBOARD = "dashboard"
    const val EMPLOYEE_LIST = "employee_list"
    const val ADD_EMPLOYEE = "add_employee"
    const val EDIT_EMPLOYEE = "edit_employee/{employeeId}"
    const val EMPLOYEE_DETAIL = "employee_detail/{employeeId}"
    const val ADD_TASK = "add_task/{employeeId}"
    const val EDIT_TASK = "edit_task/{taskId}/{employeeId}"
    const val PERFORMANCE_EVAL = "performance_eval/{employeeId}"
    const val EMPLOYEE_ANALYTICS = "employee_analytics/{employeeId}"
    const val ANALYTICS = "analytics"
    const val REPORTS = "reports"
}
@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val employeeViewModel: EmployeeViewModel = viewModel(factory = EmployeeViewModel.Factory)
    val taskViewModel: TaskViewModel = viewModel(factory = TaskViewModel.Factory)
    val performanceViewModel: PerformanceViewModel = viewModel(factory = PerformanceViewModel.Factory)
    val analyticsViewModel: AnalyticsViewModel = viewModel(factory = AnalyticsViewModel.Factory)
    NavHost(navController = navController, startDestination = Routes.SPLASH) {
        composable(Routes.SPLASH) {
            SplashScreen(onNavigationDone = {
                navController.navigate(Routes.LOGIN) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                }
            })
        }
        composable(Routes.LOGIN) {
            LoginScreen(onLoginSuccess = {
                navController.navigate(Routes.DASHBOARD) {
                    popUpTo(Routes.LOGIN) { inclusive = true }
                }
            })
        }
        composable(Routes.DASHBOARD) {
            DashboardScreen(
                employeeViewModel = employeeViewModel,
                taskViewModel = taskViewModel,
                performanceViewModel = performanceViewModel,
                onNavigateToEmployees = { navController.navigate(Routes.EMPLOYEE_LIST) },
                onNavigateToAnalytics = { navController.navigate(Routes.ANALYTICS) },
                onNavigateToReports = { navController.navigate(Routes.REPORTS) },
                onNavigateToEvaluation = { navController.navigate(Routes.EMPLOYEE_LIST) },
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.DASHBOARD) { inclusive = true }
                    }
                },
                onEmployeeClick = { id ->
                    navController.navigate("employee_detail/$id")
                }
            )
        }
        composable(Routes.EMPLOYEE_LIST) {
            EmployeeListScreen(
                viewModel = employeeViewModel,
                onAddEmployee = { navController.navigate(Routes.ADD_EMPLOYEE) },
                onEmployeeClick = { id -> navController.navigate("employee_detail/$id") },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.ADD_EMPLOYEE) {
            AddEditEmployeeScreen(
                employeeId = null,
                viewModel = employeeViewModel,
                onSaved = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Routes.EDIT_EMPLOYEE,
            arguments = listOf(navArgument("employeeId") { type = NavType.LongType })
        ) { backStack ->
            val empId = backStack.arguments?.getLong("employeeId")
            AddEditEmployeeScreen(
                employeeId = empId,
                viewModel = employeeViewModel,
                onSaved = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Routes.EMPLOYEE_DETAIL,
            arguments = listOf(navArgument("employeeId") { type = NavType.LongType })
        ) { backStack ->
            val empId = backStack.arguments?.getLong("employeeId") ?: return@composable
            EmployeeDetailScreen(
                employeeId = empId,
                employeeViewModel = employeeViewModel,
                taskViewModel = taskViewModel,
                performanceViewModel = performanceViewModel,
                onAddTask = { navController.navigate("add_task/$empId") },
                onAddPerformance = { navController.navigate("performance_eval/$empId") },
                onEditEmployee = { navController.navigate("edit_employee/$empId") },
                onViewAnalytics = { navController.navigate("employee_analytics/$empId") },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Routes.ADD_TASK,
            arguments = listOf(navArgument("employeeId") { type = NavType.LongType })
        ) { backStack ->
            val empId = backStack.arguments?.getLong("employeeId") ?: return@composable
            AddEditTaskScreen(
                taskId = null,
                employeeId = empId,
                viewModel = taskViewModel,
                onSaved = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Routes.PERFORMANCE_EVAL,
            arguments = listOf(navArgument("employeeId") { type = NavType.LongType })
        ) { backStack ->
            val empId = backStack.arguments?.getLong("employeeId") ?: return@composable
            PerformanceEvaluationScreen(
                employeeId = empId,
                employeeViewModel = employeeViewModel,
                performanceViewModel = performanceViewModel,
                onSaved = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Routes.EMPLOYEE_ANALYTICS,
            arguments = listOf(navArgument("employeeId") { type = NavType.LongType })
        ) { backStack ->
            val empId = backStack.arguments?.getLong("employeeId") ?: return@composable
            EmployeeAnalyticsScreen(
                employeeId = empId,
                employeeViewModel = employeeViewModel,
                performanceViewModel = performanceViewModel,
                taskViewModel = taskViewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.ANALYTICS) {
            AnalyticsScreen(
                viewModel = analyticsViewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.REPORTS) {
            ReportsScreen(
                employeeViewModel = employeeViewModel,
                performanceViewModel = performanceViewModel,
                taskViewModel = taskViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}