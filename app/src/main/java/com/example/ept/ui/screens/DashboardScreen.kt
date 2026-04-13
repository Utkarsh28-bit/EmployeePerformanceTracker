package com.example.ept.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ept.data.entity.Employee
import com.example.ept.ui.components.*
import com.example.ept.ui.theme.*
import com.example.ept.viewmodel.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    employeeViewModel: EmployeeViewModel,
    taskViewModel: TaskViewModel,
    performanceViewModel: PerformanceViewModel,
    onNavigateToEmployees: () -> Unit,
    onNavigateToAnalytics: () -> Unit,
    onNavigateToReports: () -> Unit,
    onNavigateToEvaluation: () -> Unit,
    onLogout: () -> Unit,
    onEmployeeClick: (Long) -> Unit
) {
    val employees by employeeViewModel.allEmployees.collectAsStateWithLifecycle()
    val employeeCount by employeeViewModel.employeeCount.collectAsStateWithLifecycle()
    val pendingTasks by taskViewModel.pendingTaskCount.collectAsStateWithLifecycle()
    val totalReviews by performanceViewModel.totalReviewCount.collectAsStateWithLifecycle()
    val topPerformers by performanceViewModel.topPerformers.collectAsStateWithLifecycle()

    var showLogoutDialog by remember { mutableStateOf(false) }

    // Logout confirmation dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            icon = { Icon(Icons.Default.ExitToApp, contentDescription = null, tint = Red600) },
            title = { Text("Logout / Exit", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to logout and return to the login screen?") },
            confirmButton = {
                Button(
                    onClick = { showLogoutDialog = false; onLogout() },
                    colors = ButtonDefaults.buttonColors(containerColor = Red600)
                ) { Text("Logout") }
            },
            dismissButton = {
                OutlinedButton(onClick = { showLogoutDialog = false }) { Text("Cancel") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("EPTracker", fontWeight = FontWeight.Bold)
                        Text(
                            "Admin Dashboard",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Indigo600,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    IconButton(onClick = onNavigateToAnalytics) {
                        Icon(Icons.Default.Analytics, contentDescription = "Analytics")
                    }
                    IconButton(onClick = { showLogoutDialog = true }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Logout")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
                    label = { Text("Dashboard") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToEmployees,
                    icon = { Icon(Icons.Default.People, contentDescription = "Employees") },
                    label = { Text("Employees") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToAnalytics,
                    icon = { Icon(Icons.Default.BarChart, contentDescription = "Analytics") },
                    label = { Text("Analytics") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToReports,
                    icon = { Icon(Icons.Default.Description, contentDescription = "Reports") },
                    label = { Text("Reports") }
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Hero Banner
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(Brush.horizontalGradient(listOf(Indigo700, Indigo500)))
                        .padding(20.dp)
                ) {
                    Column {
                        Text(
                            "Welcome back, Admin 👋",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "Here's your team performance overview",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f)
                        )
                    }
                }
            }

            // Stats Row
            item {
                SectionHeader("Overview")
                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        title = "Employees",
                        value = "$employeeCount",
                        color = Indigo600,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Pending Tasks",
                        value = "$pendingTasks",
                        color = StatusPending,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Reviews",
                        value = "$totalReviews",
                        color = Green600,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Quick Actions
            item {
                SectionHeader("Quick Actions")
                Spacer(Modifier.height(12.dp))
                // Row 1
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickActionCard(
                        icon = Icons.Default.PersonAdd,
                        label = "Add Employee",
                        color = Indigo600,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToEmployees
                    )
                    QuickActionCard(
                        icon = Icons.Default.BarChart,
                        label = "Analytics",
                        color = Green600,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToAnalytics
                    )
                    QuickActionCard(
                        icon = Icons.Default.Download,
                        label = "Reports",
                        color = StatusInProgress,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToReports
                    )
                }
                Spacer(Modifier.height(10.dp))
                // Row 2
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickActionCard(
                        icon = Icons.Default.Star,
                        label = "Evaluation",
                        color = StatusReviewed,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToEvaluation
                    )
                    QuickActionCard(
                        icon = Icons.Default.Description,
                        label = "View Reports",
                        color = StatusPending,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToReports
                    )
                    QuickActionCard(
                        icon = Icons.Default.ExitToApp,
                        label = "Logout",
                        color = Red600,
                        modifier = Modifier.weight(1f),
                        onClick = { showLogoutDialog = true }
                    )
                }
            }

            // Top Performers
            item {
                SectionHeader("🏆 Top Performers")
                Spacer(Modifier.height(12.dp))
                if (topPerformers.isEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Text(
                            "No performance reviews yet. Add evaluations to see top performers!",
                            modifier = Modifier.padding(20.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        topPerformers.forEachIndexed { index, avgRating ->
                            val emp = employees.firstOrNull { it.id == avgRating.employeeId }
                            emp?.let {
                                TopPerformerCard(
                                    rank = index + 1,
                                    employee = it,
                                    rating = avgRating.avgRating,
                                    onClick = { onEmployeeClick(it.id) }
                                )
                            }
                        }
                    }
                }
            }

            // Recent Employees
            item {
                SectionHeader("Recent Employees")
                Spacer(Modifier.height(12.dp))
                if (employees.isEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.PersonAdd,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "No employees yet",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                "Tap 'Employees' to get started",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(employees.take(5)) { emp ->
                            EmployeeMiniCard(emp = emp, onClick = { onEmployeeClick(emp.id) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickActionCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    color: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(icon, contentDescription = label, tint = color, modifier = Modifier.size(28.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = color
            )
        }
    }
}

@Composable
private fun TopPerformerCard(
    rank: Int,
    employee: Employee,
    rating: Float,
    onClick: () -> Unit
) {
    val medal = when (rank) { 1 -> "🥇"; 2 -> "🥈"; else -> "🥉" }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(medal, style = MaterialTheme.typography.headlineMedium)
            AvatarInitials(name = employee.name)
            Column(Modifier.weight(1f)) {
                Text(employee.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(
                    "${employee.role} • ${employee.department}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            StarRating(rating = rating, starSize = 16.dp)
        }
    }
}

@Composable
private fun EmployeeMiniCard(emp: Employee, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AvatarInitials(name = emp.name, size = 52.dp)
            Text(
                text = emp.name.split(" ").firstOrNull() ?: emp.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = emp.department,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
