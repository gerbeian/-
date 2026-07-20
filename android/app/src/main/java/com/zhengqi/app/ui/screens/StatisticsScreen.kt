package com.zhengqi.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zhengqi.app.ui.components.MonthlyLineChart
import com.zhengqi.app.ui.components.WeeklyBarChart
import com.zhengqi.app.ui.components.YearlyHeatmap
import com.zhengqi.app.ui.theme.HairlineSoft
import com.zhengqi.app.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    onBack: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    val weeklyData by viewModel.weeklyData.collectAsState()
    val monthlyData by viewModel.monthlyData.collectAsState()
    val yearlyData by viewModel.yearlyData.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.refreshStats()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "统计",
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回", tint = MaterialTheme.colorScheme.onBackground)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 17.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Weekly chart
            Text(
                text = "周频率",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(12.dp))
            WeeklyBarChart(
                data = weeklyData,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .border(1.dp, HairlineSoft, MaterialTheme.shapes.large)
                    .padding(12.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Monthly chart
            Text(
                text = "月视图",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(12.dp))
            MonthlyLineChart(
                data = monthlyData,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .border(1.dp, HairlineSoft, MaterialTheme.shapes.large)
                    .padding(12.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Yearly heatmap
            YearlyHeatmap(
                data = yearlyData,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .border(1.dp, HairlineSoft, MaterialTheme.shapes.large)
                    .padding(17.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
