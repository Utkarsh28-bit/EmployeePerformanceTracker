package com.example.ept.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ept.data.entity.Employee
import com.example.ept.data.entity.Performance
import com.example.ept.ui.components.AvatarInitials
import com.example.ept.ui.components.StarRating
import com.example.ept.ui.theme.*
import com.example.ept.viewmodel.EmployeeViewModel
import com.example.ept.viewmodel.PerformanceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerformanceEvaluationScreen(
    employeeId: Long,
    employeeViewModel: EmployeeViewModel,
    performanceViewModel: PerformanceViewModel,
    onSaved: () -> Unit,
    onBack: () -> Unit
) {
    var employee by remember { mutableStateOf<Employee?>(null) }
    LaunchedEffect(employeeId) { employee = employeeViewModel.getById(employeeId) }

    var qualityScore by remember { mutableFloatStateOf(3f) }
    var timelinessScore by remember { mutableFloatStateOf(3f) }
    var attendanceScore by remember { mutableFloatStateOf(3f) }
    var communicationScore by remember { mutableFloatStateOf(3f) }
    var innovationScore by remember { mutableFloatStateOf(3f) }
    var overallRating by remember { mutableFloatStateOf(3f) }
    var remarks by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Performance Evaluation", fontWeight = FontWeight.Bold) },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Employee header
            employee?.let { emp ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Indigo600.copy(alpha = 0.08f))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        AvatarInitials(name = emp.name)
                        Column {
                            Text(emp.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text("${emp.role} • ${emp.department}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }

            Text("Performance Metrics (1 – 5)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

            MetricSlider("Quality of Work", qualityScore, Icons.Default.WorkspacePremium) { qualityScore = it }
            MetricSlider("Timeliness", timelinessScore, Icons.Default.Schedule) { timelinessScore = it }
            MetricSlider("Attendance", attendanceScore, Icons.Default.CheckCircle) { attendanceScore = it }
            MetricSlider("Communication", communicationScore, Icons.Default.Forum) { communicationScore = it }
            MetricSlider("Innovation / Initiative", innovationScore, Icons.Default.Lightbulb) { innovationScore = it }

            HorizontalDivider()

            // Overall star rating
            Text("Overall Rating", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(5) { index ->
                    val filled = index < overallRating.toInt()
                    IconButton(onClick = { overallRating = (index + 1).toFloat() }) {
                        Icon(
                            if (filled) Icons.Default.Star else Icons.Default.StarOutline,
                            contentDescription = null,
                            tint = if (filled) StatusPending else MaterialTheme.colorScheme.outlineVariant,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
                Text(
                    "%.0f / 5".format(overallRating),
                    style = MaterialTheme.typography.titleMedium,
                    color = StatusPending,
                    fontWeight = FontWeight.Bold
                )
            }

            // Remarks
            OutlinedTextField(
                value = remarks,
                onValueChange = { remarks = it },
                label = { Text("Additional Comments (optional)") },
                leadingIcon = { Icon(Icons.Default.Comment, contentDescription = null, tint = Indigo600) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                minLines = 3,
                maxLines = 5
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    val today = java.time.LocalDate.now().toString()
                    val performance = Performance(
                        employeeId = employeeId,
                        date = today,
                        qualityScore = qualityScore,
                        timelinessScore = timelinessScore,
                        attendanceScore = attendanceScore,
                        communicationScore = communicationScore,
                        innovationScore = innovationScore,
                        overallRating = overallRating,
                        remarks = remarks.trim()
                    )
                    performanceViewModel.insert(performance)
                    onSaved()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Green600)
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Save Evaluation", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Composable
private fun MetricSlider(
    label: String,
    value: Float,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onValueChange: (Float) -> Unit
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(icon, contentDescription = null, tint = Indigo600, modifier = Modifier.size(18.dp))
            Text(label, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
            Surface(color = Indigo600, shape = RoundedCornerShape(8.dp)) {
                Text(
                    "%.0f".format(value),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 1f..5f,
            steps = 3,
            colors = SliderDefaults.colors(
                thumbColor = Indigo600,
                activeTrackColor = Indigo600,
                inactiveTrackColor = Indigo100
            )
        )
    }
}
