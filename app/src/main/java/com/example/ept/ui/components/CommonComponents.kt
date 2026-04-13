package com.example.ept.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ept.ui.theme.*

@Composable
fun AvatarInitials(
    name: String,
    size: Dp = 44.dp,
    backgroundColor: Color = Indigo600,
    textColor: Color = Color.White
) {
    val initials = name.split(" ")
        .take(2)
        .mapNotNull { it.firstOrNull()?.uppercaseChar() }
        .joinToString("")

    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials.ifEmpty { "?" },
            color = textColor,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            fontSize = (size.value * 0.35f).sp
        )
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    color: Color = Indigo600,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.ExtraBold,
                color = color
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun StarRating(
    rating: Float,
    maxStars: Int = 5,
    starSize: Dp = 18.dp,
    activeColor: Color = StatusPending
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        repeat(maxStars) { index ->
            Icon(
                imageVector = if (index < rating.toInt()) Icons.Filled.Star else Icons.Outlined.StarOutline,
                contentDescription = null,
                tint = if (index < rating.toInt()) activeColor else MaterialTheme.colorScheme.outlineVariant,
                modifier = Modifier.size(starSize)
            )
        }
        Spacer(Modifier.width(4.dp))
        Text(
            text = "%.1f".format(rating),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun StatusChip(status: String) {
    val (bg, fg) = when (status) {
        "Pending"     -> StatusPending.copy(alpha = 0.15f)   to StatusPending
        "In Progress" -> StatusInProgress.copy(alpha = 0.15f) to StatusInProgress
        "Completed"   -> StatusCompleted.copy(alpha = 0.15f)  to StatusCompleted
        "Reviewed"    -> StatusReviewed.copy(alpha = 0.15f)   to StatusReviewed
        else          -> Color.LightGray.copy(alpha = 0.3f)   to Color.Gray
    }
    Surface(
        color = bg,
        shape = RoundedCornerShape(50.dp)
    ) {
        Text(
            text = status,
            color = fg,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun PriorityChip(priority: String) {
    val (bg, fg) = when (priority) {
        "High"   -> PriorityHigh.copy(alpha = 0.15f)   to PriorityHigh
        "Medium" -> PriorityMedium.copy(alpha = 0.15f) to PriorityMedium
        "Low"    -> PriorityLow.copy(alpha = 0.15f)    to PriorityLow
        else     -> Color.LightGray.copy(alpha = 0.3f) to Color.Gray
    }
    Surface(color = bg, shape = RoundedCornerShape(50.dp)) {
        Text(
            text = priority,
            color = fg,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun SectionHeader(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = modifier
    )
}

// Simple Bar Chart using Canvas-based boxes
@Composable
fun SimpleBarChart(
    data: List<Pair<String, Float>>,    // label to value
    maxValue: Float = 5f,
    modifier: Modifier = Modifier,
    barColor: Color = Indigo600
) {
    if (data.isEmpty()) return
    Column(modifier = modifier) {
        data.forEach { (label, value) ->
            val fraction = (value / maxValue).coerceIn(0f, 1f)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(
                    text = label.take(12),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.width(90.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(18.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(fraction)
                            .clip(RoundedCornerShape(4.dp))
                            .background(barColor)
                    )
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "%.1f".format(value),
                    style = MaterialTheme.typography.labelSmall,
                    color = barColor,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
