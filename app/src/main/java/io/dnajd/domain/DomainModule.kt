package io.dnajd.domain

import com.google.gson.GsonBuilder
import com.skydoves.retrofit.adapters.result.ResultCallAdapterFactory
import io.dnajd.data.google_auth.GoogleAuthApiServiceImpl
import io.dnajd.data.jwt_auth.JwtRefreshAuthApiServiceImpl
import io.dnajd.data.project.api.ProjectApiServiceImpl
import io.dnajd.data.project.api.ProjectApiServiceMock
import io.dnajd.data.project_table.api.ProjectTableApiServiceImpl
import io.dnajd.data.project_table.api.ProjectTableApiServiceMock
import io.dnajd.data.table_task.api.TableTaskApiServiceImpl
import io.dnajd.data.table_task.api.TableTaskApiServiceMock
import io.dnajd.data.user_authority.api.UserAuthorityApiServiceImpl
import io.dnajd.data.user_authority.api.UserAuthorityApiServiceMock
import io.dnajd.data.utils.JwtAuthenticator
import io.dnajd.domain.google_auth.service.GoogleAuthApiService
import io.dnajd.domain.jwt_auth.service.JwtRefreshAuthApiService
import io.dnajd.domain.project.service.ProjectApiService
import io.dnajd.domain.project_table.service.ProjectTableApiService
import io.dnajd.domain.table_task.service.TableTaskApiService
import io.dnajd.domain.user_authority.service.UserAuthorityApiService
import io.dnajd.domain.utils.MutableListTypeAdapterFactory
import io.dnajd.util.BugtrackerDateFormat
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.InjektModule
import uy.kohesive.injekt.api.InjektRegistrar
import uy.kohesive.injekt.api.addSingletonFactory
import uy.kohesive.injekt.api.get

class DomainModule : InjektModule {
	companion object {
		private const val USE_MOCKS = false
	}

	override fun InjektRegistrar.registerInjectables() {
		val loggingInterceptor = HttpLoggingInterceptor()
		loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
		addSingletonFactory {
			OkHttpClient
				.Builder()
				.addInterceptor(loggingInterceptor)
				.addInterceptor(JwtAuthenticator)
				.build()
		}

		addSingletonFactory {
			GsonBuilder()
				.setDateFormat(
					BugtrackerDateFormat
						.defaultRequestDateFormat()
						.toPattern(),
				)
				.registerTypeAdapterFactory(MutableListTypeAdapterFactory())
				.create()
		}

		addSingletonFactory {
			Retrofit
				.Builder()
				.addConverterFactory(GsonConverterFactory.create(Injekt.get()))
				.addCallAdapterFactory(ResultCallAdapterFactory.create())
				.client(Injekt.get<OkHttpClient>())
		}

		addSingletonFactory {
			Injekt
				.get<Retrofit.Builder>()
				.build()
		}

		when (USE_MOCKS) {
			true -> {
				addSingletonFactory<GoogleAuthApiService> { GoogleAuthApiServiceImpl }
				addSingletonFactory<JwtRefreshAuthApiService> { JwtRefreshAuthApiServiceImpl }
				addSingletonFactory<ProjectApiService> { ProjectApiServiceMock }
				addSingletonFactory<ProjectTableApiService> { ProjectTableApiServiceMock }
				addSingletonFactory<TableTaskApiService> { TableTaskApiServiceMock }
				addSingletonFactory<UserAuthorityApiService> { UserAuthorityApiServiceMock }
			}

			false -> {
				addSingletonFactory<GoogleAuthApiService> { GoogleAuthApiServiceImpl }
				addSingletonFactory<JwtRefreshAuthApiService> { JwtRefreshAuthApiServiceImpl }
				addSingletonFactory<ProjectApiService> { ProjectApiServiceImpl }
				addSingletonFactory<ProjectTableApiService> { ProjectTableApiServiceImpl }
				addSingletonFactory<TableTaskApiService> { TableTaskApiServiceImpl }
				addSingletonFactory<UserAuthorityApiService> { UserAuthorityApiServiceImpl }
			}
		}
	}
}
