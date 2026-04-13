package com.example.ept.ui.screens
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ept.data.entity.Employee
import com.example.ept.data.entity.Performance
import com.example.ept.data.entity.Task
import com.example.ept.ui.components.AvatarInitials
import com.example.ept.ui.components.SimpleBarChart
import com.example.ept.ui.components.StarRating
import com.example.ept.ui.theme.*
import com.example.ept.viewmodel.EmployeeViewModel
import com.example.ept.viewmodel.PerformanceViewModel
import com.example.ept.viewmodel.TaskViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeAnalyticsScreen(
    employeeId: Long,
    employeeViewModel: EmployeeViewModel,
    performanceViewModel: PerformanceViewModel,
    taskViewModel: TaskViewModel,
    onBack: () -> Unit
) {
    var employee by remember { mutableStateOf<Employee?>(null) }
    LaunchedEffect(employeeId) { employee = employeeViewModel.getById(employeeId) }
    val performances by performanceViewModel
        .getPerformanceForEmployee(employeeId)
        .collectAsStateWithLifecycle(emptyList())
    val tasks by taskViewModel
        .getTasksForEmployee(employeeId)
        .collectAsStateWithLifecycle(emptyList())
    // Computed analytics
    val avgRating = if (performances.isNotEmpty())
        performances.map { it.overallRating }.average().toFloat() else 0f
    val avgQuality = if (performances.isNotEmpty())
        performances.map { it.qualityScore }.average().toFloat() else 0f
    val avgTimeliness = if (performances.isNotEmpty())
        performances.map { it.timelinessScore }.average().toFloat() else 0f
    val avgAttendance = if (performances.isNotEmpty())
        performances.map { it.attendanceScore }.average().toFloat() else 0f
    val avgCommunication = if (performances.isNotEmpty())
        performances.map { it.communicationScore }.average().toFloat() else 0f
    val avgInnovation = if (performances.isNotEmpty())
        performances.map { it.innovationScore }.average().toFloat() else 0f
    val totalTasks = tasks.size
    val completedTasks = tasks.count { it.status == "Completed" || it.status == "Reviewed" }
    val pendingTasks = tasks.count { it.status == "Pending" }
    val inProgressTasks = tasks.count { it.status == "In Progress" }
    val completionRate = if (totalTasks > 0) completedTasks * 100 / totalTasks else 0
    val ratingLabel = when {
        avgRating >= 4.5f -> "Excellent 🌟"
        avgRating >= 3.5f -> "Good 👍"
        avgRating >= 2.5f -> "Average 📈"
        avgRating > 0f    -> "Needs Improvement 💪"
        else              -> "No Data"
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Analytics Report", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Indigo600,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ── Employee Hero Card ───────────────────────────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.horizontalGradient(listOf(Indigo700, Indigo500))
                        )
                        .padding(20.dp)
                ) {
                    employee?.let { emp ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            AvatarInitials(
                                name = emp.name,
                                size = 64.dp,
                                backgroundColor = Color.White.copy(alpha = 0.2f),
                                textColor = Color.White
                            )
                            Column(Modifier.weight(1f)) {
                                Text(
                                    emp.name,
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    "${emp.role} • ${emp.department}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White.copy(alpha = 0.85f)
                                )
                                Spacer(Modifier.height(8.dp))
                                Surface(
                                    color = Color.White.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(50)
                                ) {
                                    Text(
                                        ratingLabel,
                                        color = Color.White,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    } ?: CircularProgressIndicator(color = Color.White)
                }
            }
            // ── Overall Rating Card ──────────────────────────────────────
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(
                                "Overall Rating",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Based on ${performances.size} review(s)",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(Modifier.height(8.dp))
                            StarRating(
                                rating = avgRating,
                                starSize = 24.dp,
                                activeColor = StatusPending
                            )
                        }
                        // Big rating circle
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(RoundedCornerShape(50))
                                .background(
                                    if (avgRating > 0) Indigo600 else MaterialTheme.colorScheme.surfaceVariant
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = if (avgRating > 0) "%.1f".format(avgRating) else "—",
                                    color = Color.White,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 22.sp
                                )
                                Text(
                                    text = "/ 5",
                                    color = Color.White.copy(alpha = 0.7f),
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }
            // ── Task Summary ─────────────────────────────────────────────
            item {
                Text(
                    "📋 Task Summary",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TaskStatCard("Total", "$totalTasks", Indigo600, Modifier.weight(1f))
                    TaskStatCard("Done", "$completedTasks", Green600, Modifier.weight(1f))
                    TaskStatCard("Pending", "$pendingTasks", StatusPending, Modifier.weight(1f))
                    TaskStatCard("Active", "$inProgressTasks", StatusInProgress, Modifier.weight(1f))
                }
                Spacer(Modifier.height(12.dp))
                // Completion rate progress bar
                Card(
                    shape = RoundedCornerShape(14.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Task Completion Rate",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                "$completionRate%",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Green600
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(completionRate / 100f)
                                    .clip(RoundedCornerShape(5.dp))
                                    .background(
                                        Brush.horizontalGradient(listOf(Green600, Green500))
                                    )
                            )
                        }
                    }
                }
            }
            // ── Metric Breakdown ─────────────────────────────────────────
            item {
                Text(
                    "📊 Performance Breakdown",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(10.dp))
                Card(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        if (performances.isEmpty()) {
                            Text(
                                "No performance reviews yet.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } else {
                            SimpleBarChart(
                                data = listOf(
                                    "Quality"       to avgQuality,
                                    "Timeliness"    to avgTimeliness,
                                    "Attendance"    to avgAttendance,
                                    "Communication" to avgCommunication,
                                    "Innovation"    to avgInnovation
                                ),
                                maxValue = 5f,
                                modifier = Modifier.fillMaxWidth(),
                                barColor = Indigo600
                            )
                        }
                    }
                }
            }
            // ── Performance Trend (reviews timeline) ─────────────────────
            if (performances.size >= 2) {
                item {
                    Text(
                        "📈 Performance Trend",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(10.dp))
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            performances.reversed().forEachIndexed { index, perf ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Text(
                                        "Review ${index + 1}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.width(60.dp)
                                    )
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(8.dp)
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(MaterialTheme.colorScheme.surfaceVariant)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxHeight()
                                                .fillMaxWidth(perf.overallRating / 5f)
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(
                                                    when {
                                                        perf.overallRating >= 4f -> Green600
                                                        perf.overallRating >= 3f -> StatusPending
                                                        else -> Red500
                                                    }
                                                )
                                        )
                                    }
                                    Text(
                                        "%.1f ★".format(perf.overallRating),
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Indigo600,
                                        modifier = Modifier.width(44.dp)
                                    )
                                }
                                Text(
                                    perf.date,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(start = 72.dp)
                                )
                            }
                        }
                    }
                }
            }
            // ── Recent Reviews ───────────────────────────────────────────
            if (performances.isNotEmpty()) {
                item {
                    Text(
                        "📝 Latest Review",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(10.dp))
                    val latest = performances.first()
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(2.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Indigo600.copy(alpha = 0.05f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    latest.date,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                StarRating(latest.overallRating, starSize = 16.dp)
                            }
                            HorizontalDivider()
                            ReviewMetricRow("Quality of Work",    latest.qualityScore)
                            ReviewMetricRow("Timeliness",         latest.timelinessScore)
                            ReviewMetricRow("Attendance",         latest.attendanceScore)
                            ReviewMetricRow("Communication",      latest.communicationScore)
                            ReviewMetricRow("Innovation",         latest.innovationScore)
                            if (latest.remarks.isNotBlank()) {
                                HorizontalDivider()
                                Text(
                                    "💬 ${latest.remarks}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}
@Composable
private fun TaskStatCard(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = color
            )
            Text(
                label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
@Composable
private fun ReviewMetricRow(label: String, score: Float) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            label,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.width(120.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(score / 5f)
                    .clip(RoundedCornerShape(3.dp))
                    .background(Indigo600)
            )
        }
        Text(
            "%.1f".format(score),
            style = MaterialTheme.typography.labelSmall,
            color = Indigo600,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(28.dp)
        )
    }
}
