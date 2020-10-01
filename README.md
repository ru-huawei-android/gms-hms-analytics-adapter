Analytics Adapter Library Features

The library is one entry point for Firebase + Huawei Analytics interation.
It reduces amount of code for multiple analytics libraries integration. 

For example to use Firebase and Huawei analytics in one project without Analytics Adapter you have to do the following:
1. Detect if Huawei Services available
2. Detect if Google Services available
3. Check availability and instantiate Google Analytics
4. Check availability and instantiate Huawei Analytics
5. Use different methods for Google and Huawei events and user properties

In case of Analytics Adapter usage you only have to do only two strings of code: 
*val composedAnalytics = ComposedAnalytics(this@MainActivity)
composedAnalytics.onEvent("Cool event", bundle)*

**You could also send Firebase dependent built-in events, the library automatically translate it to appropriate HA analogue**
e.g. FirebaseAnalytics.Event.ADD_PAYMENT_INFO becomes HAEventType.CREATEPAYMENTINFO

**Library automatically detects available analytics API on device, initializes it and send events and profile to both GA and HA**

Signatures:

*fun getIntegrations(): Set<String>*

Available analytics integrations list detected on ComposedAnalytics object initialization

*fun onEvent(eventName: String, bundle: Bundle?)*

Custom/Build-in event sending to available analytics. 
Third parameter is optional, in case developer doesn't use it, it filled with available integration ids.

*fun setUserProperty(name: String, value: String)*

UserProfile/UserProperty setting. 
Third parameter is optional, in case developer doesn't use it, it filled with available integration ids.