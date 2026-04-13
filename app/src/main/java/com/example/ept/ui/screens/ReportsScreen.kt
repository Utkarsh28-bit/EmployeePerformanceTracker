package com.example.ept.ui.screens

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ept.ui.theme.*
import com.example.ept.viewmodel.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(
    employeeViewModel: EmployeeViewModel,
    performanceViewModel: PerformanceViewModel,
    taskViewModel: TaskViewModel,
    onBack: () -> Unit
) {
    val employees by employeeViewModel.allEmployees.collectAsStateWithLifecycle()
    val departments by employeeViewModel.departments.collectAsStateWithLifecycle()
    val performances by performanceViewModel.averageRatings.collectAsStateWithLifecycle()
    val tasks by taskViewModel.allTasks.collectAsStateWithLifecycle()
    var selectedDept by remember { mutableStateOf("All") }
    var selectedCategory by remember { mutableStateOf("All") }
    val categories = listOf("All", "Top Performers (≥4)", "Needs Improvement (<3)")

    // Filtered data
    val deptEmployees = if (selectedDept == "All") employees else employees.filter { it.department == selectedDept }
    val filteredReport = deptEmployees.map { emp ->
        val avgRating = performances.firstOrNull { it.employeeId == emp.id }?.avgRating ?: 0f
        val empTasks = tasks.filter { it.employeeId == emp.id }
        Triple(emp, avgRating, empTasks)
    }.filter { (_, rating, _) ->
        when (selectedCategory) {
            "Top Performers (≥4)" -> rating >= 4f
            "Needs Improvement (<3)" -> rating in 0.01f..2.99f
            else -> true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reports", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Indigo600,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {

            // Filters
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Filter by Department", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val allDepts = listOf("All") + departments
                    items(allDepts) { dept ->
                        FilterChip(
                            selected = selectedDept == dept,
                            onClick = { selectedDept = dept },
                            label = { Text(dept) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Indigo600,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                }

                Text("Filter by Category", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(categories) { cat ->
                        FilterChip(
                            selected = selectedCategory == cat,
                            onClick = { selectedCategory = cat },
                            label = { Text(cat) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Green600,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                }
            }

            HorizontalDivider()

            // Summary Row
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Showing ${filteredReport.size} employees",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )
                Icon(Icons.Default.FilterList, contentDescription = null, tint = Indigo600, modifier = Modifier.size(20.dp))
            }

            if (filteredReport.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No data matches the selected filters.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredReport) { (emp, avgRating, empTasks) ->
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(2.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                // Header
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    com.example.ept.ui.components.AvatarInitials(name = emp.name, size = 40.dp)
                                    Column(Modifier.weight(1f)) {
                                        Text(emp.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                                        Text("${emp.role} • ${emp.department}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    if (avgRating > 0) {
                                        com.example.ept.ui.components.StarRating(rating = avgRating, starSize = 14.dp)
                                    } else {
                                        Text("No reviews", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outlineVariant)
                                    }
                                }
                                HorizontalDivider()
                                // Task summary
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                    ReportStat("Total Tasks", "${empTasks.size}")
                                    ReportStat("Completed", "${empTasks.count { it.status == "Completed" }}")
                                    ReportStat("Pending", "${empTasks.count { it.status == "Pending" }}")
                                    ReportStat("Avg Rating", if (avgRating > 0) "%.1f".format(avgRating) else "—")
                                }
                                // Joined
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Icon(Icons.Default.CalendarToday, contentDescription = null, modifier = Modifier.size(12.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text("Joined: ${emp.joiningDate.ifBlank { "N/A" }}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ReportStat(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Indigo600)
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
