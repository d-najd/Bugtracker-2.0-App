package io.dnajd.domain

import com.google.gson.GsonBuilder
import io.dnajd.util.BugtrackerDateFormat
import uy.kohesive.injekt.api.*

class DomainModule : InjektModule {
    companion object {
        private const val USE_MOCKS = true
    }
    
    override fun InjektRegistrar.registerInjectables() {
        addSingletonFactory {
            // OkHttpClient()
        }
        
        addSingletonFactory {
            GsonBuilder()
                .setDateFormat(BugtrackerDateFormat.defaultRequestDateFormat().toPattern())
                .create()
        }
        
        when (USE_MOCKS) {
            /*
            true -> {
                addSingletonFactory<AuthService> { AuthServiceMock }
                
                addSingletonFactory<AnalyticsUsageService> { AnalyticsUsageServiceMock }
            }
            false -> {
                addSingletonFactory<AuthService> { AuthServiceImpl }
                
                addSingletonFactory<AnalyticsUsageService> { AnalyticsUsageServiceImpl }
            }
             */
        }

        /*
        addFactory { LoginUser(get()) }
        addFactory { RegisterUser(get()) }
        
        addFactory { RequestAnalyticSessionInfo(get()) }
         */
    }
}
