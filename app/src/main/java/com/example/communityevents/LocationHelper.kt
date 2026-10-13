
package com.example.communityevents

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.CancellationSignal
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import java.io.IOException
import java.util.Locale

fun hasLocationPermission(context: Context): Boolean =
    isGranted(context, Manifest.permission.ACCESS_COARSE_LOCATION) ||
            isGranted(context, Manifest.permission.ACCESS_FINE_LOCATION)

private fun isGranted(context: Context, permission: String) =
    ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

fun fetchCurrentPlace(context: Context, onResult: (String?) -> Unit) {
    val manager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
    if (manager == null || !hasLocationPermission(context)) {
        onResult(null)
        return
    }

    val providers = buildList {
        if (manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            add(LocationManager.NETWORK_PROVIDER)
        }
        if (isGranted(context, Manifest.permission.ACCESS_FINE_LOCATION) &&
            manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        ) {
            add(LocationManager.GPS_PROVIDER)
        }
    }

    tryProviders(context, manager, providers, onResult)
}

@SuppressLint("MissingPermission")
private fun tryProviders(
    context: Context,
    manager: LocationManager,
    providers: List<String>,
    onResult: (String?) -> Unit
) {
    if (providers.isEmpty()) {
        onResult(null)
        return
    }

    val provider = providers.first()
    try {
        LocationManagerCompat.getCurrentLocation(
            manager,
            provider,
            null as CancellationSignal?,
            ContextCompat.getMainExecutor(context)
        ) { location ->
            val found: Location? = location ?: try {
                manager.getLastKnownLocation(provider)
            } catch (_: SecurityException) {
                null
            }

            if (found != null) {
                resolvePlace(context, found, onResult)
            } else {
                tryProviders(context, manager, providers.drop(1), onResult)
            }
        }
    } catch (_: SecurityException) {
        onResult(null)
    }
}

private fun resolvePlace(context: Context, location: Location, onResult: (String?) -> Unit) {
    val mainHandler = Handler(Looper.getMainLooper())
    if (!Geocoder.isPresent()) {
        onResult(null)
        return
    }

    val geocoder = Geocoder(context, Locale.getDefault())

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        geocoder.getFromLocation(
            location.latitude,
            location.longitude,
            1,
            object : Geocoder.GeocodeListener {
                override fun onGeocode(addresses: MutableList<Address>) {
                    val name = placeName(addresses.firstOrNull())
                    mainHandler.post { onResult(name) }
                }

                override fun onError(errorMessage: String?) {
                    mainHandler.post { onResult(null) }
                }
            }
        )
    } else {
        Thread {
            val addresses = try {
                @Suppress("DEPRECATION")
                geocoder.getFromLocation(location.latitude, location.longitude, 1)
            } catch (_: IOException) {
                null
            }
            val name = placeName(addresses?.firstOrNull())
            mainHandler.post { onResult(name) }
        }.start()
    }
}

private fun placeName(address: Address?): String? =
    listOf(address?.subLocality, address?.locality, address?.subAdminArea, address?.adminArea)
        .firstOrNull { !it.isNullOrBlank() }
