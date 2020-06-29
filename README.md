Analytics Adapter Library Features

The library is one point for Google + Huawei Analytics
To use both GA and HA you have to 
	a. Apply appropriate plugins (google-services and agconnect)
	b. Use analytics library as a dependency
	c. Use existing methods inside it for 

For example to use google + huawei analytics in one project without Analutics Adapter you have to do the following:
1. Detect if Huawei Services available
2. Detect if Google Services available
3. Instantiate Google Analytics 
4. Instantiate Huawei Analytics
5. Use different methods for Google and Huawei events with

In case of Analytics Adapter usage you only have to do only two strings of code: 
ComposedAnalytics.init(this@MainActivity)
ComposedAnalytics.onEvent("custom_event", bundle)

Library automatically detects avalable analytics API on device, initializes it and send events and profile to both GA and HA

Methods signatures:
fun init(context: Context, APIs: Set<String> = getSupportedAPIs())

fun getSupportedAPIs(): Set<String>
Implemented analytics integrations static list (FirebaseAnalytics and HuaweiAnalytics by now)

fun getDeviceAPIs(): Set<String>
List of on-device (emulator) services available for interaction (depends on GMS/HMS presents on device)

fun onEvent(eventName: String, bundle: Bundle?, APIs: Set<String> = getDeviceAPIs())
Custom/Build-in event sending to available analytics. 
Third parameter is optional, in case developer doesn't use it, it filled with available integration ids.


fun setUserProperty(name: String, value: String, APIs: Set<String> = getDeviceAPIs())
UserProfile/UserProperty setting. 
Third parameter is optional, in case developer doesn't use it, it filled with available integration ids.