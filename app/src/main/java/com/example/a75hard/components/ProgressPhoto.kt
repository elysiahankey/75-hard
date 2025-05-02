package com.example.a75hard.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.a75hard.DayViewModel
import com.example.a75hard.R
import com.example.a75hard.helpers.ProgressPhotoHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

@Composable
fun ProgressPhoto(dayNumber: String, viewModel: DayViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val savedPath by ProgressPhotoHelper.getPhotoState(context, dayNumber)
        .collectAsState(initial = "")

    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(savedPath) {
        bitmap = if (savedPath.isNotEmpty()) {
            BitmapFactory.decodeFile(savedPath)
        } else null
    }

    val pickImageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                coroutineScope.launch {
                    val filename = "progress_photo_${dayNumber}_${System.currentTimeMillis()}.jpg"
                    val newPath = saveImageToInternalStorage(context, it, filename)

                    ProgressPhotoHelper.savePhotoState(context, dayNumber, newPath)
                    viewModel.setPhotoUploaded(true)
                }
            }
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.day_screen_progress_photo_title),
            style = MaterialTheme.typography.titleLarge
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            bitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Selected image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { pickImageLauncher.launch("image/*") },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (bitmap != null)
                        stringResource(R.string.progress_photo_button_upload_another)
                    else
                        stringResource(R.string.progress_photo_button_label)
                )
            }

            if (bitmap != null) {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            val file = File(savedPath)
                            if (file.exists()) file.delete()

                            ProgressPhotoHelper.savePhotoState(context, dayNumber, "")
                            viewModel.setPhotoUploaded(false)
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.progress_photo_delete_photo))
                }
            }
        }
    }
}

suspend fun saveImageToInternalStorage(context: Context, uri: Uri, filename: String): String {
    return withContext(Dispatchers.IO) {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(context.filesDir, filename)
        inputStream?.use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }
        file.absolutePath
    }
}