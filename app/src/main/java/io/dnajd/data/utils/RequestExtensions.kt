package io.dnajd.data.utils

import io.dnajd.bugtracker.R
import io.dnajd.bugtracker.util.view.ContextHolder
import retrofit2.Call
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get
import java.io.IOException

inline fun <reified T> Call<T>.processRequest(): T? {
    if(T::class.java == Void::class.java) throw IllegalStateException("Use Call<T>.processVoidRequest for void requests")
    try{
        val response = execute()
        if(response.isSuccessful && response.code() == 200){
            return response.body()
        } else if (response.code() == 401){
            Injekt.get<ContextHolder>().composeToast(R.string.error_auth)
        } else if (response.code() == 409) {
            Injekt.get<ContextHolder>().composeToast(R.string.error_duplicate_entry)
        } else {
            val contextHolder = Injekt.get<ContextHolder>()
            contextHolder.composeToast("${contextHolder.getString(R.string.error_unhandled)} code ${response.code()}")
        }

    } catch (e: Exception){
        e.printStackTrace()
        when(e){
            is IOException -> Injekt.get<ContextHolder>().composeToast(R.string.error_network)
            else -> throw e
        }
    }
    return null
}

/**
 * @return true for successful requests
 */
fun <T> Call<T>.processVoidRequest(): Boolean {
    try{
        val response = execute()
        if(response.isSuccessful){
            return true
        } else if (response.code() == 401){
            Injekt.get<ContextHolder>().composeToast(R.string.error_auth)
        } else if (response.code() == 409) {
            Injekt.get<ContextHolder>().composeToast(R.string.error_duplicate_entry)
        } else {
            val contextHolder = Injekt.get<ContextHolder>()
            contextHolder.composeToast("${contextHolder.getString(R.string.error_unhandled)} code ${response.code()}")
        }

    } catch (e: Exception){
        e.printStackTrace()
        when(e){
            is IOException -> Injekt.get<ContextHolder>().composeToast(R.string.error_network)
            else -> throw e
        }
    }
    return false
}