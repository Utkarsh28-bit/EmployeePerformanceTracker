package com.example.ept.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ept.viewmodel.AnalyticsViewModel // Added Import

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    viewModel: AnalyticsViewModel, // Added Parameter
    onBack: () -> Unit             // Added Parameter
) {
    val indigoPrimary = Color(0xFF3949AB)
    val greenAccent = Color(0xFF43A047)
    val lightGrayBg = Color(0xFFFAFAFA)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Analytics & Insights", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = { // Added Functional Back Button
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = indigoPrimary)
            )
        },
        containerColor = lightGrayBg
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            item {
                Text("Performance Overview", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = indigoPrimary)
                Text("Department and employee metrics", color = Color.Gray, fontSize = 14.sp)
            }

            // PIE CHART: Department Performance
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Department Performance Breakdown", fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(bottom = 16.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceAround,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Native Compose Pie Chart
                            Canvas(modifier = Modifier.size(120.dp)) {
                                drawArc(color = indigoPrimary, startAngle = 0f, sweepAngle = 140f, useCenter = true)
                                drawArc(color = greenAccent, startAngle = 140f, sweepAngle = 100f, useCenter = true)
                                drawArc(color = Color(0xFFFFC107), startAngle = 240f, sweepAngle = 80f, useCenter = true)
                                drawArc(color = Color(0xFFF44336), startAngle = 320f, sweepAngle = 40f, useCenter = true)
                            }

                            // Legend
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                LegendItem(color = indigoPrimary, text = "IT (38%)")
                                LegendItem(color = greenAccent, text = "HR (28%)")
                                LegendItem(color = Color(0xFFFFC107), text = "Design (22%)")
                                LegendItem(color = Color(0xFFF44336), text = "Sales (12%)")
                            }
                        }
                    }
                }
            }

            // BAR CHART: Average Rating by Employee
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Top 5 Performers (Avg Rating)", fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(bottom = 24.dp))

                        // Native Compose Bar Chart
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            BarItem(heightWeight = 0.96f, label = "Rahul", rating = "4.8", color = greenAccent)
                            BarItem(heightWeight = 0.90f, label = "Amit", rating = "4.5", color = indigoPrimary)
                            BarItem(heightWeight = 0.84f, label = "Neha", rating = "4.2", color = indigoPrimary)
                            BarItem(heightWeight = 0.76f, label = "Priya", rating = "3.8", color = Color(0xFFFFC107))
                            BarItem(heightWeight = 0.60f, label = "Karan", rating = "3.0", color = Color(0xFFF44336))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LegendItem(color: Color, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(12.dp).background(color, shape = RoundedCornerShape(2.dp)))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, fontSize = 12.sp, color = Color.DarkGray)
    }
}

@Composable
fun RowScope.BarItem(heightWeight: Float, label: String, rating: String, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier.weight(1f).fillMaxHeight()
    ) {
        Text(text = rating, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = color, modifier = Modifier.padding(bottom = 4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .fillMaxHeight(heightWeight)
                .background(color, shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
    }
}