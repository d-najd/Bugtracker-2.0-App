package io.dnajd.domain.base

import android.annotation.SuppressLint
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession

/**
 * Sometimes telebit's certificates don't seem to pass, this is a workaround, only our api and auth providers
 * are used so this should be fine
 *
 * https://stackoverflow.com/questions/31917988/okhttp-javax-net-ssl-sslpeerunverifiedexception-hostname-domain-com-not-verifie
 */
object HostnameVerifierAlwaysTrue : HostnameVerifier {
	@SuppressLint("BadHostnameVerifier")
	override fun verify(hostname: String?, session: SSLSession?): Boolean {
		return true
	}
}

/*
okHttpClient.setHostnameVerifier(new HostnameVerifier() {
	@Override
	public boolean verify(String hostname, SSLSession session) {
		return true;
	}
});
 */
