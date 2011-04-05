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
package com.marketplace.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;

import com.gc.android.market.api.model.Market;
import com.gc.android.market.api.model.Market.AppsResponse;
import com.gc.android.market.api.model.Market.CommentsResponse;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.marketplace.Constants;
import com.marketplace.Utils;
import com.marketplace.exceptions.ConnectivityException;

/**
 * <code>Sender</code> saves details (app info, comments, images) to the
 * database. The class is designed to save these details to a Rails app, but can
 * be modified to save details to a SQL database.
 * 
 * @author raunak
 * @version 1.0
 */
public class Sender {

	/**
	 * A <code>HttpPut</code> object for making HTTP Put requests
	 */
	private HttpPut httpPut;

	/**
	 * A <code>HttpPost</code> object for making HTTP Post requests
	 */
	private HttpPost httpPost;

	/**
	 * A <code>HttpClient</code> object
	 */
	private HttpClient httpClient;

	/**
	 * Constructs a <code>Sender</code>
	 */
	public Sender() {
		this.httpPut = new HttpPut();
		this.httpPost = new HttpPost();

		this.httpClient = new DefaultHttpClient();
		this.httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		HttpConnectionParams.setConnectionTimeout(this.httpClient.getParams(), 20000);

	}

	/**
	 * Check if an entry for app by the package name passed as the parameter
	 * already exists in the database.
	 * 
	 * @param packageName
	 *            the existence of packageName to check
	 * @return <code>ExistResponse</code> containing the id of app in the
	 *         database if it exists.
	 * 
	 * @throws ConnectivityException
	 *             thrown if there was a problem connecting with the database
	 */
	public ExistResponse appExists(String packageName) throws ConnectivityException {
		ExistResponse existResponse = null;

		AppQuery query = new AppQuery();
		query.packageName = packageName;

		byte[] response = doBasicHttpPost(query.toString(), Constants.appExistUrl);

		InputStream is = new ByteArrayInputStream(response);
		Reader reader = new InputStreamReader(is);

		try {
			existResponse = new Gson().fromJson(reader, ExistResponse.class);
		} catch (JsonSyntaxException jse) {
			jse.printStackTrace();
		} catch (JsonIOException jio) {
			jio.printStackTrace();
		}

		return existResponse;
	}

	/**
	 * Check if an entry exists in the database for an app with the passed
	 * target parameter
	 * 
	 * @param appId
	 *            the id of app in the database
	 * @param apiLevel
	 *            the android sdk
	 * @return <code>AppTargetExistResponse</code> containing a boolean flag.
	 *         The flag is set to true if target exists, false otherwise.
	 * @throws ConnectivityException
	 *             thrown if there was a problem connecting with the database
	 */
	public AppTargetExistResponse appTargetExists(int appId, int apiLevel) throws ConnectivityException {
		AppTargetExistResponse existResponse = null;
		Target target = new Target();
		target.app_target.app_id = appId;
		target.app_target.target_id = apiLevel;

		byte[] response = doBasicHttpPost(target.toString(), Constants.appTargetExistUrl);

		InputStream is = new ByteArrayInputStream(response);
		Reader reader = new InputStreamReader(is);

		try {
			existResponse = new Gson().fromJson(reader, AppTargetExistResponse.class);
		} catch (JsonSyntaxException jse) {
			jse.printStackTrace();
		} catch (JsonIOException jio) {
			jio.printStackTrace();
		}

		return existResponse;
	}

	/**
	 * Check if a comment exists by the same author.
	 * 
	 * @param appId
	 *            the id of app in the database
	 * @param authorId
	 *            the id of author
	 * @return <code>ExistResponse</code> containing the id of comment in the
	 *         database if it exists.
	 * @throws ConnectivityException
	 *             thrown if there was a problem connecting with the database
	 */
	public ExistResponse appCommentExists(int appId, String authorId) throws ConnectivityException {
		ExistResponse existResponse = null;

		CommentQuery commentQuery = new CommentQuery();
		commentQuery.app_id = appId;
		commentQuery.authorId = authorId;

		byte[] response = doBasicHttpPost(commentQuery.toString(), Constants.appCommentExistUrl);

		InputStream is = new ByteArrayInputStream(response);
		Reader reader = new InputStreamReader(is);

		try {
			existResponse = new Gson().fromJson(reader, ExistResponse.class);
		} catch (JsonSyntaxException jse) {
			jse.printStackTrace();
		} catch (JsonIOException jio) {
			jio.printStackTrace();
		}

		return existResponse;
	}

	/**
	 * Check if a visual by the filename (passed as a parameter) already exist
	 * in the database.
	 * 
	 * @param appId
	 *            the id of app in the database
	 * @param filename
	 *            the name of file to check the existence of
	 * @return <code>ExistResponse</code> containing the id of visual in the
	 *         database if it exists.
	 * @throws ConnectivityException
	 *             thrown if there was a problem connecting with the database
	 */
	public ExistResponse appVisualExists(int appId, String filename) throws ConnectivityException {
		ExistResponse existResponse = null;

		VisualQuery visualQuery = new VisualQuery();
		visualQuery.app_id = appId;
		visualQuery.filename = filename;

		byte[] response = doBasicHttpPost(visualQuery.toString(), Constants.appsUrl + "/" + appId + Constants.partVisualExistUrl);

		InputStream is = new ByteArrayInputStream(response);
		Reader reader = new InputStreamReader(is);

		try {
			existResponse = new Gson().fromJson(reader, ExistResponse.class);
		} catch (JsonSyntaxException jse) {
			jse.printStackTrace();
		} catch (JsonIOException jio) {
			jio.printStackTrace();
		}

		return existResponse;
	}

	/**
	 * Save android app details to a Rails app.
	 * 
	 * @param data
	 *            the app data in jSon formate
	 * @return an internal id assigned by Rails to the app.
	 * @throws ConnectivityException
	 *             thrown if there was a problem connecting with the database
	 */
	public int saveApp(String data) throws ConnectivityException {
		AppSaveResponse appResponse = null;
		byte[] response = doBasicHttpPost(data, Constants.appsUrlJson);

		InputStream is = new ByteArrayInputStream(response);
		Reader reader = new InputStreamReader(is);

		try {
			appResponse = new Gson().fromJson(reader, AppSaveResponse.class);
		} catch (JsonSyntaxException jse) {
			jse.printStackTrace();
		} catch (JsonIOException jio) {
			jio.printStackTrace();
		}

		return appResponse.app.id;
	}

	/**
	 * Saves app details, the android sdk it targets and the required permission
	 * 
	 * @param appsResponse
	 *            the <code>AppResponse</code> object to save
	 * @param sdkVersion
	 *            the target sdk by app
	 * @throws ConnectivityException
	 *             thrown if there was a problem connecting with the database
	 */
	public void addAppToCollection(AppsResponse appsResponse, int sdkVersion) throws ConnectivityException {
		for (int appIndex = 0; appIndex < appsResponse.getAppCount(); appIndex++) {

			int appId;

			// Check if an entry already exists
			ExistResponse existResponse = appExists(appsResponse.getApp(appIndex).getPackageName());
			String data = convertAppToJsonString(appsResponse.getApp(appIndex));

			if (existResponse.exists) {
				appId = existResponse.id;
				doBasicHttpPut(data, Constants.appsUrl + "/" + appId + Constants.jsonExtension);

			} else {
				appId = saveApp(data);
			}

			System.out.println("Saving app data");

			AppTargetExistResponse appTarget = appTargetExists(appId, sdkVersion);

			if (!appTarget.exists) {
				Target target = new Target();
				target.app_target.app_id = appId;
				target.app_target.target_id = sdkVersion;
				doBasicHttpPost(target.toString(), Constants.appTargetUrl);
			}

			Permissions permissions = new Permissions();
			permissions.app_id = appId;
			permissions.permissions = Utils.permissionToInt(appsResponse.getApp(appIndex).getExtendedInfo().getPermissionIdList());

			doBasicHttpPost(permissions.toString(), Constants.appPermissionUrl);
		}
	}

	/**
	 * Saves app comments to the database
	 * 
	 * @param commentsResponse
	 *            <code>CommentResponse</code> object containing the comments to
	 *            save
	 * @param appId
	 *            the id of app in database
	 * @throws ConnectivityException
	 *             thrown if there was a problem connecting with the database
	 */
	public void addCommentToCollection(CommentsResponse commentsResponse, int appId) throws ConnectivityException {
		for (int commentIndex = 0; commentIndex < commentsResponse.getCommentsCount(); commentIndex++) {
			ExistResponse existResponse = appCommentExists(appId, commentsResponse.getComments(commentIndex).getAuthorId());
			String data = convertCommentToJsonString(commentsResponse.getComments(commentIndex), appId);

			if (existResponse.exists) {
				doBasicHttpPut(data, Constants.appCommentUrl + "/" + existResponse.id + Constants.jsonExtension);

			} else {
				doBasicHttpPost(data, Constants.appCommentUrlJson);
			}
		}
	}

	/**
	 * aves app visuals to the database.
	 * 
	 * @param data
	 *            app image
	 * @param db_app_id
	 *            the id of app in database
	 * @param filename
	 *            the name by which to save the image
	 * @throws ConnectivityException
	 *             thrown if there was a problem connecting with the database
	 */
	public void addVisualToCollection(byte[] data, int db_app_id, String filename) throws ConnectivityException {
		ExistResponse existResponse = appVisualExists(db_app_id, filename);

		System.out.println("Visual exist? - " + existResponse.exists);

		if (existResponse.exists) {
			doComplexHttpPut(data, "image/jpeg", filename, Constants.appsUrl + "/" + db_app_id + Constants.visuals + "/" + existResponse.id);
		} else {
			doComplexHttpPost(data, "image/jpeg", filename, Constants.appsUrl + "/" + db_app_id + Constants.visuals);
		}
	}

	/**
	 * Perform a simple HTTP Put request
	 * 
	 * @param data
	 *            information that needs to be sent
	 * @param url
	 *            the location to send the information to
	 * @throws ConnectivityException
	 *             thrown if there was a problem connecting with the database
	 */
	public void doBasicHttpPut(String data, String url) throws ConnectivityException {
		HttpResponse httpResponse = null;
		StringEntity stringEntity = null;

		try {
			httpPut = new HttpPut();
			httpPut.setURI(new URI(url));
			httpPut.setHeader("Content-Type", "application/json");
		} catch (URISyntaxException e) {
			throw new ConnectivityException("Error occured while setting URL");
		}

		try {
			stringEntity = new StringEntity(data, "UTF-8");

			if (stringEntity != null) {
				httpPut.setEntity(stringEntity);
				httpResponse = httpClient.execute(httpPut);
				HttpEntity entity = httpResponse.getEntity();

				if (entity != null) {
					entity.consumeContent();
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			throw new ConnectivityException("Error occured in Client Protocol");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Perform a simple HTTP Post request
	 * 
	 * @param data
	 *            information that needs to be sent
	 * @param url
	 *            the location to send the information to
	 * @return the response returned by Rails app
	 * @throws ConnectivityException
	 *             thrown if there was a problem connecting with the database
	 */
	public byte[] doBasicHttpPost(String data, String url) throws ConnectivityException {
		byte[] response = null;
		HttpResponse httpResponse = null;
		StringEntity stringEntity = null;

		try {
			httpPost = new HttpPost();
			httpPost.setURI(new URI(url));
			httpPost.addHeader("Content-Type", "application/json");
		} catch (URISyntaxException e) {
			throw new ConnectivityException("Error occured while setting URL");
		}

		try {
			stringEntity = new StringEntity(data, "UTF-8");

			if (stringEntity != null) {
				httpPost.setEntity(stringEntity);
				httpResponse = httpClient.execute(httpPost);

				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					response = IOUtils.toByteArray(entity.getContent());
					entity.consumeContent();
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			throw new ConnectivityException("Error occured in Client Protocol");
		} catch (IOException e) {
			e.printStackTrace();
		}

		return response;
	}

	/**
	 * Perform a complex HTTP Put (send files over the network)
	 * 
	 * @param data
	 *            file that needs to be sent
	 * @param mimeType
	 *            the content type of file
	 * @param filename
	 *            the name of file
	 * @param url
	 *            the location to send the file to
	 * @throws ConnectivityException
	 *             thrown if there was a problem connecting with the database
	 */
	public void doComplexHttpPut(byte[] data, String mimeType, String filename, String url) throws ConnectivityException {
		HttpResponse httpResponse = null;

		try {
			httpPut = new HttpPut();
			httpPut.setURI(new URI(url));
			httpPut.addHeader("content_type", "image/jpeg");
		} catch (URISyntaxException e) {
			throw new ConnectivityException("Error occured while setting URL");
		}

		ContentBody contentBody = new ByteArrayBody(data, mimeType, filename);

		MultipartEntity multipartEntity = new MultipartEntity();
		multipartEntity.addPart("image", contentBody);

		httpPut.setEntity(multipartEntity);

		try {
			httpResponse = httpClient.execute(httpPut);
			HttpEntity entity = httpResponse.getEntity();

			if (entity != null) {
				entity.consumeContent();
			}

		} catch (ClientProtocolException e) {
			throw new ConnectivityException("Error occured in Client Protocol");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Perform a complex HTTP Post (send files over the network)
	 * 
	 * @param data
	 *            file that needs to be sent
	 * @param mimeType
	 *            the content type of file
	 * @param filename
	 *            the name of file
	 * @param url
	 *            the location to send the file to
	 * @throws ConnectivityException
	 *             thrown if there was a problem connecting with the database
	 */
	public void doComplexHttpPost(byte[] data, String mimeType, String filename, String url) throws ConnectivityException {
		System.out.println("Performing Post");
		System.out.println("URL => " + url);

		HttpResponse httpResponse = null;

		try {
			httpPost = new HttpPost();
			httpPost.setURI(new URI(url));
			httpPost.addHeader("content_type", "image/jpeg");
		} catch (URISyntaxException e) {
			throw new ConnectivityException("Error occured while setting URL");
		}

		ContentBody contentBody = new ByteArrayBody(data, mimeType, filename);

		MultipartEntity multipartEntity = new MultipartEntity();
		multipartEntity.addPart("image", contentBody);

		httpPost.setEntity(multipartEntity);

		try {
			httpResponse = httpClient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();

			if (entity != null) {
				entity.consumeContent();
			}

		} catch (ClientProtocolException e) {
			throw new ConnectivityException("Error occured in Client Protocol");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Converts a Market.App object to json string
	 * 
	 * @param app
	 *            the <code>Market.App</code> to convert
	 * @return jSon representation of <code>Market.App</code>
	 */
	public String convertAppToJsonString(Market.App app) {
		App appJsonString = new App();
		appJsonString.app.appId = app.getId();
		appJsonString.app.appType = app.getAppType().toString();
		appJsonString.app.category = app.getExtendedInfo().getCategory();
		appJsonString.app.creator = app.getCreator();
		appJsonString.app.description = app.getExtendedInfo().getDescription();
		appJsonString.app.email = app.getExtendedInfo().getContactEmail();
		appJsonString.app.installSize = app.getExtendedInfo().getInstallSize();
		appJsonString.app.packageName = app.getPackageName();
		appJsonString.app.phone = app.getExtendedInfo().getContactPhone();
		appJsonString.app.price = app.getPrice();
		appJsonString.app.priceCurrency = app.getPriceCurrency();
		appJsonString.app.promoText = app.getExtendedInfo().getPromoText();
		appJsonString.app.promoVideo = app.getExtendedInfo().getPromotionalVideo();
		appJsonString.app.recentChanges = app.getExtendedInfo().getRecentChanges();
		appJsonString.app.screenshotCount = app.getExtendedInfo().getScreenshotsCount();
		appJsonString.app.title = app.getTitle();
		appJsonString.app.version = app.getVersion();
		appJsonString.app.website = app.getExtendedInfo().getContactWebsite();

		appJsonString.rating.rating = app.getRating();
		appJsonString.rating.ratingCount = app.getRatingsCount();
		appJsonString.rating.downloadCount = app.getExtendedInfo().getDownloadsCount();
		appJsonString.rating.downloadCountText = app.getExtendedInfo().getDownloadsCountText();

		return appJsonString.toString();
	}

	/**
	 * Converts a Market.Comment object to json string
	 * 
	 * @param comment
	 *            the <code>Market.Comment</code> to convert
	 * @param app_id
	 *            the id of app in database
	 * @return jSon representation of <code>Market.Comment</code>
	 */
	public String convertCommentToJsonString(Market.Comment comment, int app_id) {
		Comment commentJsonString = new Comment();
		commentJsonString.app_id = app_id;

		commentJsonString.comment.text = comment.getText();
		commentJsonString.comment.rating = comment.getRating();
		commentJsonString.comment.authorId = comment.getAuthorId();
		commentJsonString.comment.authorName = comment.getAuthorName();
		commentJsonString.comment.creationTime = comment.getCreationTime();

		return commentJsonString.toString();
	}

	/**
	 * <code>Permissions</code> is used for converting a list of permissions for
	 * an Android app into jSon String.
	 * 
	 * @author raunak
	 * @version 1.0
	 */
	class Permissions {

		/**
		 * the app id in database
		 */
		public int app_id;

		/**
		 * the permissions for an app
		 */
		public List<Integer> permissions;

		@Override
		public String toString() {
			return Utils.gson.toJson(this);
		}
	}

	/**
	 * <code>Target</code> is used for converting a target sdk for an Android
	 * app into jSon String.
	 * 
	 * @author raunak
	 * @version 1.0
	 */
	class Target {

		public TargetContainer app_target;

		public Target() {
			this.app_target = new TargetContainer();
		}

		public class TargetContainer {

			/**
			 * The app id in database
			 */
			public int app_id;

			/**
			 * the target sdk
			 */
			public int target_id;
		}

		@Override
		public String toString() {
			return Utils.gson.toJson(this);
		}
	}

	/**
	 * <code>App</code> is used for creating an jSon representation of
	 * <code>Market.App</code.
	 * 
	 * @author raunak
	 * @version 1.0
	 */
	public class App {

		public AppContainer app;
		public Rating rating;

		public App() {
			app = new AppContainer();
			rating = new Rating();
		}

		/**
		 * Contains basic information on an App.
		 * 
		 * @author raunak
		 * @version 1.0
		 */
		public class AppContainer {

			public String title;
			public String promoText;
			public String priceCurrency;
			public String email;
			public String appId;
			public String website;
			public String version;
			public String recentChanges;
			public String promoVideo;
			public int versionCode;
			public String category;
			public String description;
			public String packageName;
			public int screenshotCount;
			public String price;
			public String phone;
			public String appType;
			public String creator;
			public int installSize;
		}

		/**
		 * Contains rating information pertaining to an app.
		 * 
		 * @author raunak
		 * @version 1.0
		 */
		public class Rating {

			public String rating;
			public int ratingCount;
			public int downloadCount;
			public String downloadCountText;
		}

		@Override
		public String toString() {
			return Utils.gson.toJson(this);
		}
	}

	/**
	 * <code>Comment</code> is used for creating a jSon representation of
	 * <code>Market.Comment</code>.
	 * 
	 * @author raunak
	 * @version 1.0
	 */
	public class Comment {

		/**
		 * The id of app in the database
		 */
		public int app_id;

		public CommentContainer comment;

		public Comment() {
			this.comment = new CommentContainer();
		}

		/**
		 * Holds basic info of comment
		 * 
		 * @author raunak
		 * @version 1.0
		 */
		public class CommentContainer {

			public int rating;
			public String text;
			public String authorId;
			public String authorName;
			public long creationTime;
		}

		@Override
		public String toString() {
			return Utils.gson.toJson(this);
		}
	}

	/**
	 * <code>AppQuery</code> is used to make packageName query against the
	 * database
	 * 
	 * @author raunak
	 * @version 1.0
	 */
	class AppQuery {

		/**
		 * The name of package (for app) to package into a query.
		 */
		public String packageName;

		@Override
		public String toString() {
			return Utils.gson.toJson(this);
		}
	}

	/**
	 * <code>CommentQuery</code> is used to create comment queries. The comment
	 * query is used to check if an author has left a comment before. If so,
	 * instead of inserting a new comment, update their comment in the database.
	 * 
	 * @author raunak
	 * @version 1.0
	 */
	class CommentQuery {

		/**
		 * Id of app in the database
		 */
		public int app_id;

		/**
		 * Id of author
		 */
		public String authorId;

		@Override
		public String toString() {
			return Utils.gson.toJson(this);
		}
	}

	/**
	 * <code>VisualQuery</code> is used to create queries for visual. The query
	 * is to check if an image by the filename already exists.
	 * 
	 * @author raunak
	 * @version 1.0
	 */
	class VisualQuery {

		/**
		 * Id of app in the database
		 */
		public int app_id;

		/**
		 * Name of file to query
		 */
		public String filename;

		@Override
		public String toString() {
			return Utils.gson.toJson(this);
		}
	}

	/**
	 * The response returned from a rails app after saving an app. "{app:{id:1}"
	 * <code>AppSaveResponse</code> is used to convert the reponse from json to
	 * java object.
	 * 
	 * @author raunak
	 * @version 1.0
	 */
	static class AppSaveResponse {
		public App app;

		public AppSaveResponse() {
			app = new App();
		}

		public static class App {
			public int id;
		}
	}

	/**
	 * <code>ExistResponse</code> essentially holds the id of app in the
	 * database if the app exists.
	 * 
	 * @author raunak
	 * @version 1.0
	 */
	static class ExistResponse {

		/**
		 * The app id in the database. </br> <Strong>Note:</Strong> Only gets
		 * assigned if the apps exists in database.
		 */
		public int id;

		/**
		 * a boolean flag. true if app exists in database; false otherwise
		 */
		public boolean exists;
	}

	/**
	 * <code>AppTargetExistResponse</code> holds a boolean flag. The flag is set
	 * to true if the target exists for an app, false otherwise.
	 * 
	 * @author raunak
	 * @version 1.0
	 */
	static class AppTargetExistResponse {

		/**
		 * a boolean flag; true if target exists, false otherwise.
		 */
		public boolean exists;
	}
}
