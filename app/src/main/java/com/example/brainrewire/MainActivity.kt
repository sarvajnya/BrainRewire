package com.example.brainrewire

import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    private lateinit var devicePolicyManager: DevicePolicyManager
    private lateinit var compName: ComponentName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        devicePolicyManager = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        compName = ComponentName(this, BrainRewireDeviceAdminReceiver::class.java)

        setContent {
            var isAdmin by remember { mutableStateOf(isDeviceAdmin()) }
            Scaffold(
                topBar = { TopAppBar(title = { Text("BrainRewire") }) }
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                ) {
                    Text("Uninstall Prevention", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(24.dp))
                    Switch(
                        checked = isAdmin,
                        onCheckedChange = { checked ->
                            if (checked) {
                                val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN).apply {
                                    putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName)
                                    putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Enable to prevent uninstalling BrainRewire.")
                                }
                                startActivityForResult(intent, 1)
                            } else {
                                devicePolicyManager.removeActiveAdmin(compName)
                                isAdmin = false
                            }
                        }
                    )
                    Text(if (isAdmin) "Protection Enabled" else "Protection Disabled")
                }
            }
        }
    }

    private fun isDeviceAdmin(): Boolean {
        return devicePolicyManager.isAdminActive(compName)
    }

    override fun onResume() {
        super.onResume()
        // Update UI in case device admin status changed
        setContent { // Rebuild Compose UI
            var isAdmin by remember { mutableStateOf(isDeviceAdmin()) }
            Scaffold(
                topBar = { TopAppBar(title = { Text("BrainRewire") }) }
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                ) {
                    Text("Uninstall Prevention", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(24.dp))
                    Switch(
                        checked = isAdmin,
                        onCheckedChange = { checked ->
                            if (checked) {
                                val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN).apply {
                                    putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName)
                                    putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Enable to prevent uninstalling BrainRewire.")
                                }
                                startActivityForResult(intent, 1)
                            } else {
                                devicePolicyManager.removeActiveAdmin(compName)
                                isAdmin = false
                            }
                        }
                    )
                    Text(if (isAdmin) "Protection Enabled" else "Protection Disabled")
                }
            }
        }
    }
}
