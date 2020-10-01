package com.huawei.hms.adapter.analytics.utils

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.huawei.hms.analytics.type.HAEventType.*
import com.huawei.hms.analytics.type.HAParamType.*

const val emptyParameter = "EMPTY"

val gmsToHmsEventMap = mapOf(
    FirebaseAnalytics.Event.ADD_PAYMENT_INFO to CREATEPAYMENTINFO,
    FirebaseAnalytics.Event.ADD_TO_CART to ADDPRODUCT2CART,
    FirebaseAnalytics.Event.ADD_TO_WISHLIST to ADDPRODUCT2WISHLIST,
    FirebaseAnalytics.Event.PURCHASE to COMPLETEPURCHASE,

    FirebaseAnalytics.Event.APP_OPEN to FirebaseAnalytics.Event.APP_OPEN,

    FirebaseAnalytics.Event.LEVEL_START to STARTLEVEL,
    FirebaseAnalytics.Event.LEVEL_END to COMPLETELEVEL,
    FirebaseAnalytics.Event.LEVEL_UP to UPGRADELEVEL,
    FirebaseAnalytics.Event.UNLOCK_ACHIEVEMENT to OBTAINACHIEVEMENT,
    FirebaseAnalytics.Event.POST_SCORE to SUBMITSCORE,

    FirebaseAnalytics.Event.EARN_VIRTUAL_CURRENCY to WINVIRTUALCOIN,
    FirebaseAnalytics.Event.SPEND_VIRTUAL_CURRENCY to CONSUMEVIRTUALCOIN,

    FirebaseAnalytics.Event.LOGIN to SIGNIN,
    FirebaseAnalytics.Event.SIGN_UP to REGISTERACCOUNT,
    FirebaseAnalytics.Event.SHARE to SHARECONTENT,
    FirebaseAnalytics.Event.JOIN_GROUP to JOINUSERGROUP,

    FirebaseAnalytics.Event.BEGIN_CHECKOUT to STARTCHECKOUT,
    FirebaseAnalytics.Event.CAMPAIGN_DETAILS to VIEWCAMPAIGN,
    FirebaseAnalytics.Event.GENERATE_LEAD to OBTAINLEADS,
    FirebaseAnalytics.Event.REMOVE_FROM_CART to DELPRODUCTFROMCART,

    FirebaseAnalytics.Event.TUTORIAL_BEGIN to STARTTUTORIAL,
    FirebaseAnalytics.Event.TUTORIAL_COMPLETE to COMPLETETUTORIAL,

    FirebaseAnalytics.Event.VIEW_ITEM to VIEWPRODUCT,
    FirebaseAnalytics.Event.VIEW_ITEM_LIST to VIEWPRODUCTLIST,
    FirebaseAnalytics.Event.VIEW_SEARCH_RESULTS to VIEWSEARCHRESULT,

    FirebaseAnalytics.Event.SEARCH to SEARCH
)

val hmsToGmsParametersMap = mapOf(
    /** CREATEPAYMENTINFO Section**/
    FirebaseAnalytics.Param.PAYMENT_TYPE to PAYTYPE,

    /** ADDPRODUCT2CART, ADDPRODUCT2WISHLIST **/
    FirebaseAnalytics.Param.CURRENCY to CURRNAME,
    FirebaseAnalytics.Param.VALUE to PRICE,
    FirebaseAnalytics.Param.ITEM_NAME to PRODUCTNAME,

    /* STARTLEVEL, COMPLETELEVEL Section**/
    FirebaseAnalytics.Param.LEVEL_NAME to LEVELNAME,

    /* COMPLETELEVEL Section**/
    FirebaseAnalytics.Param.SUCCESS to RESULT,

    /* UPGRADELEVEL Section**/
    FirebaseAnalytics.Param.LEVEL to LEVELID,
    FirebaseAnalytics.Param.CHARACTER to ROLENAME,

    /* OBTAINACHIEVEMENT Section**/
    FirebaseAnalytics.Param.ACHIEVEMENT_ID to ACHIEVEMENTID,
    FirebaseAnalytics.Param.ACHIEVEMENT_ID to ACHIEVEMENTID,

    /* SUBMITSCORE Section**/
    FirebaseAnalytics.Param.SCORE to SCORE,

    /* WINVIRTUALCOIN, CONSUMEVIRTUALCOIN Section**/
    FirebaseAnalytics.Param.VIRTUAL_CURRENCY_NAME to VIRTUALCURRNAME,

    /* SHARE Section**/
    FirebaseAnalytics.Param.CONTENT_TYPE to CONTENTTYPE,
    FirebaseAnalytics.Param.ITEM_ID to PRODUCTID,

    /* JOINUSERGROUP Section**/
    FirebaseAnalytics.Param.GROUP_ID to USERGROUPID,

    /* VIEWCAMPAIGN Section**/
    FirebaseAnalytics.Param.MEDIUM to MEDIUM,
    FirebaseAnalytics.Param.SOURCE to SOURCE,
    FirebaseAnalytics.Param.CAMPAIGN to PROMOTIONNAME,
    FirebaseAnalytics.Param.CONTENT to CONTENT,
    FirebaseAnalytics.Param.TERM to KEYWORDS,
    FirebaseAnalytics.Param.ACLID to CLICKID,
    FirebaseAnalytics.Param.CP1 to EXTENDPARAM,

    /* VIEWSEARCHRESULT Section**/
    FirebaseAnalytics.Param.SEARCH_TERM to SEARCHKEYWORDS,

    /* COMMON Section**/
    FirebaseAnalytics.Param.PRICE to PRICE,
    FirebaseAnalytics.Param.QUANTITY to QUANTITY
)

fun transformGmsToHmsParameters(hmsEventName: String, gmsParamName: String): String? {
    when (gmsParamName) {
        FirebaseAnalytics.Param.VALUE -> {
            when (hmsEventName) {
                ADDPRODUCT2CART, ADDPRODUCT2WISHLIST -> return PRICE
                WINVIRTUALCOIN, CONSUMEVIRTUALCOIN, COMPLETEPURCHASE -> return REVENUE
            }
        }
        FirebaseAnalytics.Param.METHOD ->
            when (hmsEventName) {
                SIGNIN -> return CHANNEL
                REGISTERACCOUNT -> return REGISTMETHOD
            }
    }
    return hmsToGmsParametersMap[gmsParamName]
}

fun addMandatoryParams(hmsEventName: String, bundle: Bundle) {
    when (hmsEventName) {
        SIGNIN, REGISTERACCOUNT -> {
            if (!bundle.containsKey(OCCURREDTIME)) {
                bundle.putLong(OCCURREDTIME, System.currentTimeMillis())
            }
        }
        ADDPRODUCT2CART, ADDPRODUCT2WISHLIST -> {
            if (!bundle.containsKey(PRODUCTID)) {
                bundle.putString(PRODUCTID, emptyParameter)
            }
            if (!bundle.containsKey(PRODUCTNAME)) {
                bundle.putString(PRODUCTNAME, emptyParameter)
            }
        }
        VIEWPRODUCTLIST -> {
            if (!bundle.containsKey(CATEGORY)) {
                bundle.putString(CATEGORY, emptyParameter)
            }
        }
    }
}

fun processParameter(bundle: Bundle, newBundle: Bundle, key: String?, newKey: String?) {
    when (val value = bundle.get(key)) {
        is Byte -> newBundle.putByte(newKey, value)
        is Char -> newBundle.putChar(newKey, value)
        is Short -> newBundle.putShort(newKey, value)
        is Int -> newBundle.putInt(newKey, value)
        is Long -> newBundle.putLong(newKey, value)
        is Float -> newBundle.putFloat(newKey, value)
        is Double -> newBundle.putDouble(newKey, value)
        is String -> newBundle.putString(newKey, value)
        is Boolean -> newBundle.putBoolean(newKey, value)

        is ByteArray -> newBundle.putByteArray(newKey, value)
        is CharArray -> newBundle.putCharArray(newKey, value)
        is ShortArray -> newBundle.putShortArray(newKey, value)
        is IntArray -> newBundle.putIntArray(newKey, value)
        is LongArray -> newBundle.putLongArray(newKey, value)
        is FloatArray -> newBundle.putFloatArray(newKey, value)
        is DoubleArray -> newBundle.putDoubleArray(newKey, value)
        is BooleanArray -> newBundle.putBooleanArray(newKey, value)
    }
}