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
import com.example.ept.data.entity.Employee
import com.example.ept.ui.theme.Indigo600
import com.example.ept.viewmodel.EmployeeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditEmployeeScreen(
    employeeId: Long?,
    viewModel: EmployeeViewModel,
    onSaved: () -> Unit,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }
    var department by remember { mutableStateOf("") }
    var joiningDate by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var existingEmployee by remember { mutableStateOf<Employee?>(null) }

    LaunchedEffect(employeeId) {
        if (employeeId != null) {
            val emp = viewModel.getById(employeeId)
            emp?.let {
                existingEmployee = it
                name = it.name
                role = it.role
                department = it.department
                joiningDate = it.joiningDate
                email = it.email
                contact = it.contact
            }
        }
    }

    val isEdit = employeeId != null
    val title = if (isEdit) "Edit Employee" else "Add Employee"

    fun isValid() = name.isNotBlank() && role.isNotBlank() && department.isNotBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title, fontWeight = FontWeight.Bold) },
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
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text("Personal Information", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

            EPTTextField(value = name, onValueChange = { name = it }, label = "Full Name *", icon = Icons.Default.Person)

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                EPTTextField(
                    value = role,
                    onValueChange = { role = it },
                    label = "Role / Job Title *",
                    icon = Icons.Default.Work,
                    modifier = Modifier.weight(1f)
                )
                EPTTextField(
                    value = department,
                    onValueChange = { department = it },
                    label = "Department *",
                    icon = Icons.Default.Business,
                    modifier = Modifier.weight(1f)
                )
            }

            HorizontalDivider()

            Text("Contact Details", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

            EPTTextField(value = email, onValueChange = { email = it }, label = "Email Address", icon = Icons.Default.Email)
            EPTTextField(value = contact, onValueChange = { contact = it }, label = "Phone Number", icon = Icons.Default.Phone)
            EPTTextField(value = joiningDate, onValueChange = { joiningDate = it }, label = "Joining Date (DD/MM/YYYY)", icon = Icons.Default.CalendarToday)

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    if (isValid()) {
                        val emp = Employee(
                            id = existingEmployee?.id ?: 0L,
                            name = name.trim(),
                            role = role.trim(),
                            department = department.trim(),
                            joiningDate = joiningDate.trim(),
                            email = email.trim(),
                            contact = contact.trim()
                        )
                        if (isEdit) viewModel.update(emp) else viewModel.save(emp)
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
                Icon(if (isEdit) Icons.Default.Save else Icons.Default.PersonAdd, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(if (isEdit) "Save Changes" else "Add Employee", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Composable
private fun EPTTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(icon, contentDescription = null, tint = Indigo600) },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        singleLine = true
    )
}
