package com.huawei.hms.adapter.analytics.utils

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.huawei.hms.analytics.type.HAEventType
import com.huawei.hms.analytics.type.HAParamType

const val emptyParameter = "EMPTY"

val eventsMap = mapOf(
    FirebaseAnalytics.Event.ADD_PAYMENT_INFO to HAEventType.CREATEPAYMENTINFO,
    FirebaseAnalytics.Event.ADD_TO_CART to HAEventType.ADDPRODUCT2CART,
    FirebaseAnalytics.Event.ADD_TO_WISHLIST to HAEventType.ADDPRODUCT2WISHLIST,
    FirebaseAnalytics.Event.PURCHASE to HAEventType.COMPLETEPURCHASE,

    FirebaseAnalytics.Event.APP_OPEN to FirebaseAnalytics.Event.APP_OPEN,

    FirebaseAnalytics.Event.LEVEL_START to HAEventType.STARTLEVEL,
    FirebaseAnalytics.Event.LEVEL_END to HAEventType.COMPLETELEVEL,
    FirebaseAnalytics.Event.LEVEL_UP to HAEventType.UPGRADELEVEL,
    FirebaseAnalytics.Event.UNLOCK_ACHIEVEMENT to HAEventType.OBTAINACHIEVEMENT,
    FirebaseAnalytics.Event.POST_SCORE to HAEventType.SUBMITSCORE,

    FirebaseAnalytics.Event.EARN_VIRTUAL_CURRENCY to HAEventType.WINVIRTUALCOIN,
    FirebaseAnalytics.Event.SPEND_VIRTUAL_CURRENCY to HAEventType.CONSUMEVIRTUALCOIN,

    FirebaseAnalytics.Event.LOGIN to HAEventType.SIGNIN,
    FirebaseAnalytics.Event.SIGN_UP to HAEventType.REGISTERACCOUNT,
    FirebaseAnalytics.Event.SHARE to HAEventType.SHARECONTENT,
    FirebaseAnalytics.Event.JOIN_GROUP to HAEventType.JOINUSERGROUP,

    FirebaseAnalytics.Event.BEGIN_CHECKOUT to HAEventType.STARTCHECKOUT,
    FirebaseAnalytics.Event.CAMPAIGN_DETAILS to HAEventType.VIEWCAMPAIGN,
    FirebaseAnalytics.Event.GENERATE_LEAD to HAEventType.OBTAINLEADS,
    FirebaseAnalytics.Event.REMOVE_FROM_CART to HAEventType.DELPRODUCTFROMCART,

    FirebaseAnalytics.Event.TUTORIAL_BEGIN to HAEventType.STARTTUTORIAL,
    FirebaseAnalytics.Event.TUTORIAL_COMPLETE to HAEventType.COMPLETETUTORIAL,

    FirebaseAnalytics.Event.VIEW_ITEM to HAEventType.VIEWPRODUCT,
    FirebaseAnalytics.Event.VIEW_ITEM_LIST to HAEventType.VIEWPRODUCTLIST,
    FirebaseAnalytics.Event.VIEW_SEARCH_RESULTS to HAEventType.VIEWSEARCHRESULT,

    FirebaseAnalytics.Event.SEARCH to HAEventType.SEARCH
)

val paramsMap = mapOf(
    //CREATEPAYMENTINFO
    FirebaseAnalytics.Param.PAYMENT_TYPE to HAParamType.PAYTYPE,

    //ADDPRODUCT2CART, ADDPRODUCT2WISHLIST
    FirebaseAnalytics.Param.CURRENCY to HAParamType.CURRNAME,
    FirebaseAnalytics.Param.VALUE to HAParamType.PRICE,
    FirebaseAnalytics.Param.ITEM_NAME to HAParamType.PRODUCTNAME,

    //STARTLEVEL, COMPLETELEVEL
    FirebaseAnalytics.Param.LEVEL_NAME to HAParamType.LEVELNAME,

    //COMPLETELEVEL
    FirebaseAnalytics.Param.SUCCESS to HAParamType.RESULT,

    //UPGRADELEVEL
    FirebaseAnalytics.Param.LEVEL to HAParamType.LEVELID,
    FirebaseAnalytics.Param.CHARACTER to HAParamType.ROLENAME,

    //OBTAINACHIEVEMENT
    FirebaseAnalytics.Param.ACHIEVEMENT_ID to HAParamType.ACHIEVEMENTID,
    FirebaseAnalytics.Param.ACHIEVEMENT_ID to HAParamType.ACHIEVEMENTID,

    //SUBMITSCORE
    FirebaseAnalytics.Param.SCORE to HAParamType.SCORE,

    //WINVIRTUALCOIN, CONSUMEVIRTUALCOIN
    FirebaseAnalytics.Param.VIRTUAL_CURRENCY_NAME to HAParamType.VIRTUALCURRNAME,

    //SHARE
    FirebaseAnalytics.Param.CONTENT_TYPE to HAParamType.CONTENTTYPE,
    FirebaseAnalytics.Param.ITEM_ID to HAParamType.PRODUCTID,

    //JOINUSERGROUP
    FirebaseAnalytics.Param.GROUP_ID to HAParamType.USERGROUPID,

    //VIEWCAMPAIGN
    FirebaseAnalytics.Param.MEDIUM to HAParamType.MEDIUM,
    FirebaseAnalytics.Param.SOURCE to HAParamType.SOURCE,
    FirebaseAnalytics.Param.CAMPAIGN to HAParamType.PROMOTIONNAME,
    FirebaseAnalytics.Param.CONTENT to HAParamType.CONTENT,
    FirebaseAnalytics.Param.TERM to HAParamType.KEYWORDS,
    FirebaseAnalytics.Param.ACLID to HAParamType.CLICKID,
    FirebaseAnalytics.Param.CP1 to HAParamType.EXTENDPARAM,

    //VIEWSEARCHRESULT
    FirebaseAnalytics.Param.SEARCH_TERM to HAParamType.SEARCHKEYWORDS,

    //Common parameters
    FirebaseAnalytics.Param.PRICE to HAParamType.PRICE,
    FirebaseAnalytics.Param.QUANTITY to HAParamType.QUANTITY
)

fun substitute(hmsEventName: String, gmsParamName: String): String? {
    when (gmsParamName) {
        FirebaseAnalytics.Param.VALUE -> {
            when (hmsEventName) {
                HAEventType.ADDPRODUCT2CART, HAEventType.ADDPRODUCT2WISHLIST -> return HAParamType.PRICE
                HAEventType.WINVIRTUALCOIN, HAEventType.CONSUMEVIRTUALCOIN, HAEventType.COMPLETEPURCHASE -> return HAParamType.REVENUE
            }
        }
        FirebaseAnalytics.Param.METHOD ->
            when (hmsEventName) {
                HAEventType.SIGNIN -> return HAParamType.CHANNEL
                HAEventType.REGISTERACCOUNT -> return HAParamType.REGISTMETHOD
            }
    }
    return paramsMap[gmsParamName]
}

fun addMandatoryParams(hmsEventName: String, bundle: Bundle) {
    when (hmsEventName) {
        HAEventType.SIGNIN, HAEventType.REGISTERACCOUNT -> {
            if (!bundle.containsKey(HAParamType.OCCURREDTIME)) {
                bundle.putLong(HAParamType.OCCURREDTIME, System.currentTimeMillis())
            }
        }
        HAEventType.ADDPRODUCT2CART, HAEventType.ADDPRODUCT2WISHLIST -> {
            if (!bundle.containsKey(HAParamType.PRODUCTID)) {
                bundle.putString(HAParamType.PRODUCTID, emptyParameter)
            }
            if (!bundle.containsKey(HAParamType.PRODUCTNAME)) {
                bundle.putString(HAParamType.PRODUCTNAME, emptyParameter)
            }
        }
        HAEventType.VIEWPRODUCTLIST -> {
            if (!bundle.containsKey(HAParamType.CATEGORY)) {
                bundle.putString(HAParamType.CATEGORY, emptyParameter)
            }
        }

    }
}

fun processParameter(bundle: Bundle, newBundle: Bundle, key: String?, newKey: String?) {
    when (val value = bundle.get(key)) {
        is Int -> newBundle.putInt(newKey, value)
        is Long -> newBundle.putLong(newKey, value)
        is String -> newBundle.putString(newKey, value)
        is Byte -> newBundle.putByte(newKey, value)
        is Char -> newBundle.putChar(newKey, value)
        is Boolean -> newBundle.putBoolean(newKey, value)
    }
}