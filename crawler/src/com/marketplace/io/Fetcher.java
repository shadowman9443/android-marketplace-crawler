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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gc.android.market.api.MarketSession;
import com.gc.android.market.api.MarketSession.Callback;
import com.gc.android.market.api.model.Market;
import com.gc.android.market.api.model.Market.AppsRequest;
import com.gc.android.market.api.model.Market.AppsRequest.Builder;
import com.gc.android.market.api.model.Market.AppsRequest.OrderType;
import com.gc.android.market.api.model.Market.AppsResponse;
import com.gc.android.market.api.model.Market.CommentsRequest;
import com.gc.android.market.api.model.Market.CommentsResponse;
import com.gc.android.market.api.model.Market.GetImageRequest;
import com.gc.android.market.api.model.Market.GetImageRequest.AppImageUsage;
import com.gc.android.market.api.model.Market.GetImageResponse;
import com.gc.android.market.api.model.Market.ResponseContext;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.marketplace.Constants;
import com.marketplace.exceptions.ConnectivityException;

/**
 * <code>Fetcher</code> fetches the app info (app details, comments, and images
 * ) from the Marketplace.
 * 
 * @author raunak
 * @version 1.0
 */
public class Fetcher {

	/**
	 * <code>Log</code> object used for logging
	 */
	private Log log = LogFactory.getLog(Fetcher.class);

	private volatile byte[] imageResponse;
	private volatile AppsResponse appResponse;
	private volatile CommentsResponse commentResponse;

	/**
	 * Gets the internal app id and google app id of all apps whose id is
	 * greater than the passed parameter
	 * 
	 * @param id
	 *            the
	 * @return an array of <code>NextAppResponse</code>
	 * @throws ConnectivityException
	 *             thrown if there was a problem connecting with the database
	 */
	public NextAppResponse[] getNextAppIds(int id) throws ConnectivityException {
		NextAppResponse[] appResponse = null;

		NextAppQuery nextAppQuery = new NextAppQuery();
		nextAppQuery.id = id;

		byte[] response = new Sender().doBasicHttpPost(nextAppQuery.toString(), Constants.appNextIdUrl);

		InputStream is = new ByteArrayInputStream(response);
		Reader reader = new InputStreamReader(is);

		try {
			appResponse = new Gson().fromJson(reader, NextAppResponse[].class);
		} catch (JsonSyntaxException jse) {
			jse.printStackTrace();
		} catch (JsonIOException jio) {
			jio.printStackTrace();
		}

		return appResponse;
	}

	/**
	 * <code>NextAppQuery</code> is used to create jSon string like this
	 * {"id":42}
	 * 
	 * @author raunak
	 * @version 1.0
	 */
	public static class NextAppQuery {
		public int id;

		@Override
		public String toString() {
			return new Gson().toJson(this);
		}
	}

	/**
	 * <code>NextAppResponse</code> is used to convert jSon string like this
	 * {"app":{"appId":"-8021680695223296191","id":791}} into java objects
	 * 
	 * @author raunak
	 * @version 1.0
	 */
	public static class NextAppResponse {
		public AppResponseContainer app;

		public NextAppResponse() {
			this.app = new AppResponseContainer();
		}

		public static class AppResponseContainer {
			public int id;
			public String appId;
		}
	}

	/**
	 * Reads the content of a file and stores each line of the file in a list.
	 * 
	 * @param fileName
	 *            the file name to read
	 * @return contents of a file in a list.
	 */
	public List<String> readFile(String fileName) {
		LinkedList<String> list = new LinkedList<String>();

		if (fileName.isEmpty() || fileName.equals(" ")) {
			System.out.println("Please specify file name.");
			System.exit(1);
		} else {
			try {
				BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));

				String line = null;
				while ((line = bufferedReader.readLine()) != null) {
					list.add(line);
				}
				bufferedReader.close();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		log.info("Read " + list.size() + " lines");
		return list;
	}

	/**
	 * Get App(s) on Android Marketplace by Category
	 * 
	 * @param marketSession
	 *            the <code>MarketSession</code> object to use to fetch the
	 *            app(s) for the app
	 * @param categoryName
	 *            the name of category to fetch app(s) via
	 * @param startIndex
	 *            the index from where to begin fetching app(s) from
	 * 
	 * @return AppsResponse object containing information on App(s).
	 */
	synchronized public AppsResponse getAppByCategory(MarketSession marketSession, String categoryName, int startIndex, boolean newest) {
		log.info(new Date() + " Getting App(s) for Category " + categoryName + "\t --> On Index Id:" + startIndex);
		this.appResponse = null;

		Builder builder = AppsRequest.newBuilder().setCategoryId(categoryName).setStartIndex(startIndex).setEntriesCount(10)
				.setWithExtendedInfo(true);

		AppsRequest request;

		if (newest) {
			request = builder.setOrderType(OrderType.NEWEST).build();
		} else {
			request = builder.build();
		}

		marketSession.append(request, new Callback<Market.AppsResponse>() {
			@Override
			public void onResult(ResponseContext context, AppsResponse response) {
				setAppsResponse(response);
			}
		});

		marketSession.flush();
		return getAppsResponse();
	}

	/**
	 * Get App(s) from Android Marketplace by publisher
	 * 
	 * @param marketSession
	 *            the <code>MarketSession</code> object to use to fetch the
	 *            app(s) for the app
	 * @param publisherName
	 *            the name of publisher to fetch app(s) via
	 * @param startIndex
	 *            the index from where to begin fetching app(s) from
	 * @return AppsResponse object containing information on App(s).
	 */
	synchronized public AppsResponse getAppByPublisher(MarketSession marketSession, String publisherName, int startIndex) {
		log.info("Getting apps published by " + publisherName + "\t --> On Index Id:" + startIndex);
		this.appResponse = null;

		AppsRequest request = AppsRequest.newBuilder().setQuery("pub:" + "\"" + publisherName + "\"").setStartIndex(startIndex).setEntriesCount(10)
				.setWithExtendedInfo(true).build();

		marketSession.append(request, new Callback<Market.AppsResponse>() {

			@Override
			public void onResult(ResponseContext context, AppsResponse response) {
				setAppsResponse(response);
			}
		});

		marketSession.flush();
		return getAppsResponse();
	}

	/**
	 * Get comment(s) for an app from the Marketplace
	 * 
	 * @param marketSession
	 *            the <code>MarketSession</code> object to use to fetch the
	 *            comment for the app
	 * @param startIndex
	 *            the index from where to begin fetching comments from
	 * @param appId
	 *            the unique identifier given to an app by Google
	 * @return CommentsResponse object containing information on Comments(s).
	 */
	synchronized public CommentsResponse getAppComments(MarketSession marketSession, int startIndex, String appId) {
		log.info("Getting comments for appId " + appId + "\t --> On Index Id:" + startIndex);
		CommentsRequest commentsRequest = CommentsRequest.newBuilder().setAppId(appId).setStartIndex(startIndex).setEntriesCount(10).build();

		marketSession.append(commentsRequest, new Callback<Market.CommentsResponse>() {
			@Override
			public void onResult(ResponseContext context, CommentsResponse response) {
				setCommentResponse(response);
			}
		});

		marketSession.flush();
		return getCommentResponse();
	}

	/**
	 * Get app from the Marketplace via package name.
	 * 
	 * @param marketSession
	 *            the <code>MarketSession</code> object to use to fetch the app
	 * @param packageName
	 *            the package to fetch from the marketplace
	 * @return AppResponse object containing information on App(s).
	 */
	synchronized public AppsResponse getAppsByPackageName(MarketSession marketSession, String packageName) {
		log.info("Getting package: " + packageName);

		this.appResponse = null;
		AppsRequest request = AppsRequest.newBuilder().setQuery("pname:" + "\"" + packageName + "\"").setStartIndex(0).setEntriesCount(10)
				.setWithExtendedInfo(true).build();

		marketSession.append(request, new Callback<Market.AppsResponse>() {

			@Override
			public void onResult(ResponseContext context, AppsResponse response) {
				setAppsResponse(response);
			}
		});

		marketSession.flush();
		return getAppsResponse();
	}

	/**
	 * Get visuals for an app from the Marketplace
	 * 
	 * @param marketSession
	 *            the <code>MarketSession</code> object to use to fetch the
	 *            visuals
	 * @param appId
	 *            the unique identifier given to an app by Google
	 * @param imageType
	 *            the type of image; ICON, SCREENSHOT, ETC.
	 * @param imageId
	 *            the index of image. '0' for 1st image; '1' for 2nd image
	 * 
	 * @return the visual
	 */
	synchronized public byte[] getAppVisuals(MarketSession marketSession, String appId, AppImageUsage imageType, String imageId) {
		GetImageRequest imageRequest = GetImageRequest.newBuilder().setAppId(appId).setImageUsage(imageType).setImageId(imageId).build();

		marketSession.append(imageRequest, new Callback<Market.GetImageResponse>() {

			@Override
			public void onResult(ResponseContext context, GetImageResponse response) {
				setImageResponse(response.getImageData().toByteArray());
			}
		});

		marketSession.flush();
		return getImageResponse();
	}

	private void setAppsResponse(AppsResponse response) {
		this.appResponse = response;
	}

	private AppsResponse getAppsResponse() {
		return appResponse;
	}

	private void setCommentResponse(CommentsResponse response) {
		this.commentResponse = response;
	}

	private CommentsResponse getCommentResponse() {
		return commentResponse;
	}

	private void setImageResponse(byte[] imageResponse) {
		this.imageResponse = imageResponse;
	}

	private byte[] getImageResponse() {
		return imageResponse;
	}
}
