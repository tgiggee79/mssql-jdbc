//---------------------------------------------------------------------------------------------------------------------------------
// File: KeyVaultCredential.java
//
//
// Microsoft JDBC Driver for SQL Server
// Copyright(c) Microsoft Corporation
// All rights reserved.
// MIT License
// Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files(the "Software"), 
//  to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
//  and / or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions :
// The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
// THE SOFTWARE IS PROVIDED *AS IS*, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
//  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
//  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS 
//  IN THE SOFTWARE.
//---------------------------------------------------------------------------------------------------------------------------------
 

package com.microsoft.sqlserver.jdbc;

import java.util.Map;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import com.microsoft.azure.keyvault.authentication.KeyVaultCredentials;
import com.microsoft.windowsazure.core.pipeline.filter.ServiceRequestContext;

/**
 * 
 * An implementation of ServiceClientCredentials that supports automatic bearer token refresh.
 *
 */
class KeyVaultCredential extends KeyVaultCredentials{

	//this is the only supported access token type
	//https://msdn.microsoft.com/en-us/library/azure/dn645538.aspx
	private final String accessTokenType = "Bearer";

	SQLServerKeyVaultAuthenticationCallback authenticationCallback = null;
	String clientId = null;
	String clientKey = null;
	String accessToken = null;

	KeyVaultCredential(SQLServerKeyVaultAuthenticationCallback authenticationCallback) {
		this.authenticationCallback = authenticationCallback;
	}

	/**
	 * Authenticates the service request
	 * @param request the ServiceRequestContext
	 * @param challenge used to get the accessToken
	 * @return BasicHeader
	 */
	@Override
	public Header doAuthenticate(ServiceRequestContext request, Map<String, String> challenge) {
		assert null != challenge;
		
		String authorization = challenge.get("authorization");
		String resource = challenge.get("resource");

		accessToken = authenticationCallback.getAccessToken(authorization, resource, "");
		return new BasicHeader("Authorization", accessTokenType + " " + accessToken);
	}
	
	void setAccessToken(String accessToken){
		this.accessToken= accessToken;
	}

}
