package com.example.ept.ui.screens
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ept.data.entity.Employee
import com.example.ept.data.entity.Performance
import com.example.ept.data.entity.Task
import com.example.ept.ui.components.*
import com.example.ept.ui.theme.*
import com.example.ept.viewmodel.*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeDetailScreen(
    employeeId: Long,
    employeeViewModel: EmployeeViewModel,
    taskViewModel: TaskViewModel,
    performanceViewModel: PerformanceViewModel,
    onAddTask: () -> Unit,
    onAddPerformance: () -> Unit,
    onEditEmployee: () -> Unit,
    onViewAnalytics: () -> Unit,
    onBack: () -> Unit
) {
    var employee by remember { mutableStateOf<Employee?>(null) }
    LaunchedEffect(employeeId) { employee = employeeViewModel.getById(employeeId) }
    val tasks by taskViewModel.getTasksForEmployee(employeeId).collectAsStateWithLifecycle(emptyList())
    val performances by performanceViewModel.getPerformanceForEmployee(employeeId).collectAsStateWithLifecycle(emptyList())
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Profile", "Tasks", "Performance")
    employee?.let { emp ->
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(emp.name, fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        IconButton(onClick = onViewAnalytics) {
                            Icon(Icons.Default.Analytics, contentDescription = "Analytics Report")
                        }
                        IconButton(onClick = onEditEmployee) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Indigo600,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            },
            floatingActionButton = {
                when (selectedTab) {
                    1 -> FloatingActionButton(
                        onClick = onAddTask,
                        containerColor = Indigo600
                    ) { Icon(Icons.Default.Add, contentDescription = "Add Task", tint = MaterialTheme.colorScheme.onPrimary) }
                    2 -> FloatingActionButton(
                        onClick = onAddPerformance,
                        containerColor = Green600
                    ) { Icon(Icons.Default.Star, contentDescription = "Add Review", tint = MaterialTheme.colorScheme.onPrimary) }
                    else -> {}
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // Employee summary header
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    AvatarInitials(name = emp.name, size = 64.dp)
                    Column(Modifier.weight(1f)) {
                        Text(emp.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                        Text(emp.role, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.height(4.dp))
                        Surface(color = Indigo600.copy(alpha = 0.1f), shape = RoundedCornerShape(50)) {
                            Text(
                                emp.department,
                                style = MaterialTheme.typography.labelSmall,
                                color = Indigo600,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
                // Quick stats
                val avgRating = if (performances.isNotEmpty())
                    performances.map { it.overallRating }.average().toFloat() else 0f
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard("Tasks", "${tasks.size}", Indigo600, Modifier.weight(1f))
                    StatCard("Reviews", "${performances.size}", Green600, Modifier.weight(1f))
                    StatCard("Avg Rating", if (avgRating > 0) "%.1f".format(avgRating) else "—", StatusPending, Modifier.weight(1f))
                }
                Spacer(Modifier.height(12.dp))
                // Tab Row
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = Indigo600
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(title, fontWeight = FontWeight.Medium) }
                        )
                    }
                }
                when (selectedTab) {
                    0 -> ProfileTab(emp)
                    1 -> TasksTab(tasks, taskViewModel)
                    2 -> PerformanceTab(performances, performanceViewModel)
                }
            }
        }
    } ?: Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}
@Composable
private fun ProfileTab(emp: Employee) {
    LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item { ProfileInfoRow(Icons.Default.Email, "Email", emp.email.ifBlank { "Not provided" }) }
        item { ProfileInfoRow(Icons.Default.Phone, "Contact", emp.contact.ifBlank { "Not provided" }) }
        item { ProfileInfoRow(Icons.Default.CalendarToday, "Joining Date", emp.joiningDate.ifBlank { "Not provided" }) }
        item { ProfileInfoRow(Icons.Default.Business, "Department", emp.department) }
        item { ProfileInfoRow(Icons.Default.Work, "Role", emp.role) }
    }
}
@Composable
private fun ProfileInfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(icon, contentDescription = null, tint = Indigo600, modifier = Modifier.size(22.dp))
            Column {
                Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
            }
        }
    }
}
@Composable
private fun TasksTab(tasks: List<Task>, viewModel: TaskViewModel) {
    if (tasks.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No tasks assigned yet. Tap + to add.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    } else {
        LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(tasks, key = { it.id }) { task ->
                TaskCard(task, onStatusChange = { newStatus -> viewModel.updateStatus(task, newStatus) })
            }
        }
    }
}
@Composable
private fun TaskCard(task: Task, onStatusChange: (String) -> Unit) {
    var statusExpanded by remember { mutableStateOf(false) }
    val statuses = listOf("Pending", "In Progress", "Completed", "Reviewed")
    Card(
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Text(task.description, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
                Spacer(Modifier.width(8.dp))
                PriorityChip(task.priority)
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.Default.Event, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(task.deadline, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Box {
                    StatusChip(task.status)
                    // Tap chip to change status
                    Surface(
                        modifier = Modifier.matchParentSize().clickable { statusExpanded = true },
                        color = androidx.compose.ui.graphics.Color.Transparent
                    ) {}
                    DropdownMenu(expanded = statusExpanded, onDismissRequest = { statusExpanded = false }) {
                        statuses.forEach { s -> DropdownMenuItem(text = { Text(s) }, onClick = { onStatusChange(s); statusExpanded = false }) }
                    }
                }
            }
        }
    }
}
@Composable
private fun PerformanceTab(performances: List<Performance>, viewModel: PerformanceViewModel) {
    if (performances.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No reviews yet. Tap ★ to add an evaluation.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    } else {
        LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(performances, key = { it.id }) { perf ->
                PerformanceCard(perf, onDelete = { viewModel.delete(perf) })
            }
        }
    }
}
@Composable
private fun PerformanceCard(perf: Performance, onDelete: () -> Unit) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    Card(
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(perf.date, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f))
                StarRating(perf.overallRating)
                IconButton(onClick = { showDeleteDialog = true }, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = Red500, modifier = Modifier.size(18.dp))
                }
            }
            HorizontalDivider()
            ScoreRow("Quality", perf.qualityScore)
            ScoreRow("Timeliness", perf.timelinessScore)
            ScoreRow("Attendance", perf.attendanceScore)
            ScoreRow("Communication", perf.communicationScore)
            ScoreRow("Innovation", perf.innovationScore)
            if (perf.remarks.isNotBlank()) {
                HorizontalDivider()
                Text("💬 ${perf.remarks}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Review?") },
            text = { Text("This action cannot be undone.") },
            confirmButton = { TextButton(onClick = { onDelete(); showDeleteDialog = false }) { Text("Delete", color = Red500) } },
            dismissButton = { TextButton(onClick = { showDeleteDialog = false }) { Text("Cancel") } }
        )
    }
}
@Composable
private fun ScoreRow(label: String, score: Float) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(label, style = MaterialTheme.typography.bodySmall, modifier = Modifier.width(110.dp), color = MaterialTheme.colorScheme.onSurfaceVariant)
        Box(
            modifier = Modifier
                .weight(1f)
                .height(6.dp)
                .padding(horizontal = 4.dp)
        ) {
            Surface(Modifier.fillMaxSize(), shape = RoundedCornerShape(3.dp), color = MaterialTheme.colorScheme.surfaceVariant) {}
            Surface(
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(score / 5f),
                shape = RoundedCornerShape(3.dp),
                color = Indigo600
            ) {}
        }
        Text("%.1f".format(score), style = MaterialTheme.typography.labelSmall, color = Indigo600, fontWeight = FontWeight.Bold, modifier = Modifier.width(30.dp))
    }
}
