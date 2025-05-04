package com.example.a75hard.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.a75hard.R
import com.example.a75hard.helpers.WeightHelper
import com.example.a75hard.helpers.WeightHelper.getAllWeights
import com.example.a75hard.viewmodels.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeightTrackerScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Weight Tracker",
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Image(
                painter = painterResource(R.drawable.plant_background),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(-1f),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
                    .verticalScroll(scrollState)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.onPrimary),
            ) {
                val context = LocalContext.current

                val weightEntries by produceState(initialValue = emptyList<Pair<Int, String>>(), context) {
                    value = getAllWeights(context)
                }

//                val chartPoints = weightEntries.mapNotNull { (day, weightStr) ->
//                    weightStr.toFloatOrNull()?.let { weight ->
//                        day to weight
//                    }
//                }

                Column(modifier = Modifier.padding(16.dp)) {
                    Row {
                        Text("", modifier = Modifier.weight(1f))
                        Text("Weight", modifier = Modifier.weight(1f))
                    }

                    HorizontalDivider()

                    weightEntries.forEach { (day, weight) ->
                        Row(modifier = Modifier.padding(vertical = 4.dp)) {
                            Text("Day $day", modifier = Modifier.weight(1f))
                            Text("${weight}kg", modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}