package com.huawei.hms.adapter.analytics.utils

import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.FirebaseAnalytics.Event.*
import com.huawei.hms.adapter.analytics.TAG
import com.huawei.hms.analytics.type.HAEventType
import com.huawei.hms.analytics.type.HAEventType.*
import com.huawei.hms.analytics.type.HAParamType.*

val GMS2HMSEvents = mapOf(
    ADD_PAYMENT_INFO to CREATEPAYMENTINFO,
    ADD_TO_CART to ADDPRODUCT2CART,
    ADD_TO_WISHLIST to ADDPRODUCT2WISHLIST,

    APP_OPEN to APP_OPEN,

    LEVEL_START to STARTLEVEL,
    LEVEL_END to COMPLETELEVEL,
    LEVEL_UP to UPGRADELEVEL,
    UNLOCK_ACHIEVEMENT to OBTAINACHIEVEMENT,
    POST_SCORE to SUBMITSCORE,

    EARN_VIRTUAL_CURRENCY to WINVIRTUALCOIN,
    SPEND_VIRTUAL_CURRENCY to CONSUMEVIRTUALCOIN,

    LOGIN to SIGNIN,
    SIGN_UP to REGISTERACCOUNT,
    SHARE to SHARECONTENT,
    JOIN_GROUP to JOINUSERGROUP,

    BEGIN_CHECKOUT to STARTCHECKOUT,
    CAMPAIGN_DETAILS to VIEWCAMPAIGN,
    GENERATE_LEAD to OBTAINLEADS,
    REMOVE_FROM_CART to DELPRODUCTFROMCART,

    TUTORIAL_BEGIN to STARTTUTORIAL,
    TUTORIAL_COMPLETE to COMPLETETUTORIAL,

    VIEW_ITEM to VIEWPRODUCT,
    VIEW_ITEM_LIST to VIEWPRODUCTLIST,
    VIEW_SEARCH_RESULTS to VIEWSEARCHRESULT,

    FirebaseAnalytics.Event.SEARCH to HAEventType.SEARCH
)

val GMS2HMSParams = mapOf(
//    CREATEPAYMENTINFO
    FirebaseAnalytics.Param.PAYMENT_TYPE to PAYTYPE,

//    ADDPRODUCT2CART, ADDPRODUCT2WISHLIST
    FirebaseAnalytics.Param.CURRENCY to CURRNAME,
    FirebaseAnalytics.Param.VALUE to PRICE,

//    STARTLEVEL, COMPLETELEVEL
    FirebaseAnalytics.Param.LEVEL_NAME to LEVELNAME,

//    COMPLETELEVEL
    FirebaseAnalytics.Param.SUCCESS to RESULT,

//    UPGRADELEVEL
    FirebaseAnalytics.Param.LEVEL to LEVELID,
    FirebaseAnalytics.Param.CHARACTER to ROLENAME,

//    OBTAINACHIEVEMENT
    FirebaseAnalytics.Param.ACHIEVEMENT_ID to ACHIEVEMENTID,
    FirebaseAnalytics.Param.ACHIEVEMENT_ID to ACHIEVEMENTID,

//    SUBMITSCORE
    FirebaseAnalytics.Param.SCORE to SCORE,

//    WINVIRTUALCOIN, CONSUMEVIRTUALCOIN
    FirebaseAnalytics.Param.VIRTUAL_CURRENCY_NAME to VIRTUALCURRNAME,

//   SHARE
    FirebaseAnalytics.Param.CONTENT_TYPE to CONTENTTYPE,
    FirebaseAnalytics.Param.ITEM_ID to PRODUCTID,

//    JOINUSERGROUP
    FirebaseAnalytics.Param.GROUP_ID to USERGROUPID,

    //VIEWCAMPAIGN
    FirebaseAnalytics.Param.MEDIUM to MEDIUM,
    FirebaseAnalytics.Param.SOURCE to SOURCE,
    FirebaseAnalytics.Param.CAMPAIGN to PROMOTIONNAME,
    FirebaseAnalytics.Param.CONTENT to CONTENT,
    FirebaseAnalytics.Param.TERM to KEYWORDS,
    FirebaseAnalytics.Param.ACLID to CLICKID,
    FirebaseAnalytics.Param.CP1 to EXTENDPARAM,

//    VIEWSEARCHRESULT
    FirebaseAnalytics.Param.SEARCH_TERM to SEARCHKEYWORDS
)

fun mapEvent(name: String, bundle: Bundle?): Pair<String, Bundle?> {
    val mappedHMSEvent = GMS2HMSEvents[name]
    if (mappedHMSEvent != null) {
        val newBundle = Bundle()
        bundle?.keySet()?.forEach { key ->
            val hmsParamName = substitute(mappedHMSEvent, key)
            if (hmsParamName != null) {
                processParameter(bundle, newBundle, key, hmsParamName)
            } else {
                Log.e(TAG, "can't map $key parameter to HMS analogue")
            }
        }

        addMandatoryParams(mappedHMSEvent, newBundle)

        return Pair(mappedHMSEvent, newBundle)
    } else {
        return Pair(name, bundle)
    }
}

private fun substitute(hmsEventName: String, gmsParamName: String): String? {
    when (gmsParamName) {
        FirebaseAnalytics.Param.VALUE -> {
            when (hmsEventName) {
                ADDPRODUCT2CART, ADDPRODUCT2WISHLIST -> return PRICE
                WINVIRTUALCOIN, CONSUMEVIRTUALCOIN -> return REVENUE
            }
        }
        FirebaseAnalytics.Param.METHOD ->
            when (hmsEventName) {
                SIGNIN -> return CHANNEL
                REGISTERACCOUNT -> return REGISTMETHOD
            }
    }
    return GMS2HMSParams[gmsParamName]
}

private fun addMandatoryParams(hmsEventName: String, bundle: Bundle) {
    when (hmsEventName) {
        SIGNIN, REGISTERACCOUNT -> {
            if (!bundle.containsKey(OCCURREDTIME)) {
                bundle.putLong(OCCURREDTIME, System.currentTimeMillis())
            }
        }
        ADDPRODUCT2CART, ADDPRODUCT2WISHLIST -> {
            if (!bundle.containsKey(PRODUCTID)) {
                bundle.putString(PRODUCTID, "Stub product id")
            }
            if (!bundle.containsKey(PRODUCTNAME)) {
                bundle.putString(PRODUCTID, "Stub product name")
            }
        }
        VIEWPRODUCTLIST -> {
            if (!bundle.containsKey(CATEGORY)) {
                bundle.putString(CATEGORY, "Stub category")
            }
        }

    }
}

private fun processParameter(bundle: Bundle, newBundle: Bundle, key: String?, newKey: String?) {
    when (val value = bundle.get(key)) {
        is Int -> newBundle.putInt(newKey, value)
        is Long -> newBundle.putLong(newKey, value)
        is String -> newBundle.putString(newKey, value)
        is Byte -> newBundle.putByte(newKey, value)
        is Char -> newBundle.putChar(newKey, value)
        is Boolean -> newBundle.putBoolean(newKey, value)
    }
}

val HmsEventsList = setOf(
    CREATEPAYMENTINFO,
    ADDPRODUCT2CART,
    ADDPRODUCT2WISHLIST,
    STARTAPP,
    STARTCHECKOUT,
    VIEWCAMPAIGN,
    VIEWCHECKOUTSTEP,
    WINVIRTUALCOIN,
    COMPLETEPURCHASE,
    OBTAINLEADS,
    JOINUSERGROUP,
    COMPLETELEVEL,
    STARTLEVEL,
    UPGRADELEVEL,
    SIGNIN,
    SIGNOUT,
    SUBMITSCORE,
    CREATEORDER,
    REFUNDORDER,
    DELPRODUCTFROMCART,
    HAEventType.SEARCH,
    VIEWCONTENT,
    UPDATECHECKOUTOPTION,
    SHARECONTENT,
    REGISTERACCOUNT,
    CONSUMEVIRTUALCOIN,
    STARTTUTORIAL,
    COMPLETETUTORIAL,
    OBTAINACHIEVEMENT,
    VIEWPRODUCT,
    VIEWPRODUCTLIST,
    VIEWSEARCHRESULT,
    UPDATEMEMBERSHIPLEVEL,
    FILTRATEPRODUCT,
    VIEWCATEGORY,
    UPDATEORDER,
    CANCELORDER,
    COMPLETEORDER,
    CANCELCHECKOUT,
    OBTAINVOUCHER,
    CONTACTCUSTOMSERVICE,
    RATE,
    INVITE
)