/* 
 * Copyright (c) 2011 Raunak Gupta
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package com.marketplace;

/**
 * <code>Constants</code> contain a list of URL. 
 * These can be replaced by SQL database details should one wishes to use SQL.
 * These constants currently refer to URI for a Rails 3 App. 
 * 
 * @author raunak
 * @version 1.0
 */
public class Constants {

	public static String url = "http://0.0.0.0:3000";
	
	public static String jsonExtension = ".json";
	
	public static String visuals = "/visuals";

	public static final String appsUrl = url + "/apps";
	
	public static final String appsUrlJson = appsUrl + jsonExtension;
	
	public static final String appExistUrl = appsUrl + "/exists" + jsonExtension;
	
	public static final String appNextIdUrl = appsUrl + "/ids" + jsonExtension; 
	
	public static final String appPermissionUrl = url + "/app_permissions/update_permissions" + jsonExtension;
	
	public static final String appTargetUrl = url + "/app_targets" + jsonExtension;
	
	public static final String appTargetExistUrl = url + "/app_targets/exists" + jsonExtension;
	
	public static final String appCommentUrl = url + "/comments";
	
	public static final String appCommentUrlJson = url + "/comments" + jsonExtension;
	
	public static final String appCommentExistUrl = appCommentUrl + "/exists" + jsonExtension;
	
	public static final String partVisualExistUrl =  visuals + "/exists" + jsonExtension;
	
	public static final String newPermissionUrlJson = url + "/permissions" + jsonExtension;
}
