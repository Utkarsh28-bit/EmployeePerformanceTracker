package com.example.ept.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ept.data.entity.Task
import com.example.ept.ui.theme.Indigo600
import com.example.ept.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskScreen(
    taskId: Long?,
    employeeId: Long,
    viewModel: TaskViewModel,
    onSaved: () -> Unit,
    onBack: () -> Unit
) {
    var description by remember { mutableStateOf("") }
    var deadline by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf("Medium") }
    var priorityExpanded by remember { mutableStateOf(false) }

    val priorities = listOf("Low", "Medium", "High")

    fun isValid() = description.isNotBlank() && deadline.isNotBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Assign Task", fontWeight = FontWeight.Bold) },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Task Details", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Task Description *") },
                leadingIcon = { Icon(Icons.Default.Assignment, contentDescription = null, tint = Indigo600) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                minLines = 3,
                maxLines = 5
            )

            OutlinedTextField(
                value = deadline,
                onValueChange = { deadline = it },
                label = { Text("Deadline (DD/MM/YYYY) *") },
                leadingIcon = { Icon(Icons.Default.Event, contentDescription = null, tint = Indigo600) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            // Priority Dropdown
            ExposedDropdownMenuBox(
                expanded = priorityExpanded,
                onExpandedChange = { priorityExpanded = it }
            ) {
                OutlinedTextField(
                    value = priority,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Priority") },
                    leadingIcon = { Icon(Icons.Default.Flag, contentDescription = null, tint = Indigo600) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = priorityExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = priorityExpanded,
                    onDismissRequest = { priorityExpanded = false }
                ) {
                    priorities.forEach { p ->
                        DropdownMenuItem(
                            text = { Text(p) },
                            onClick = { priority = p; priorityExpanded = false }
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    if (isValid()) {
                        val today = java.time.LocalDate.now().toString()
                        val task = Task(
                            employeeId = employeeId,
                            description = description.trim(),
                            deadline = deadline.trim(),
                            priority = priority,
                            status = "Pending",
                            assignedDate = today
                        )
                        viewModel.insert(task)
                        onSaved()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = isValid(),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Indigo600)
            ) {
                Icon(Icons.Default.AssignmentTurnedIn, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Assign Task", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}
