## Requirements ##
  * ANT
  * Java
  * Ruby on Rails 3

## Crawler ##
_The crawler uses Open Source Android Market API developed by Thiel Alexandre; latest version can be found here: http://code.google.com/p/android-market-api/_

Mohsin was kind enough to write a guide on how to use the crawler, which can be found here: http://mohsin-junaid.blogspot.co.uk/2012/12/how-to-install-android-marketplace.html

## APKs ##
The crawler does not download apks from the marketplace. Tim Strazzere wrote a blog post on how to do this - http://www.strazzere.com/blog/2009/09/downloading-market-applications-without-the-vending-app/

### Authentication ###
You need a valid Google Account to run the crawler. Open and **modify** the constructor, getters and getUsers() method in the file Secure.java found in _hg/crawler/src/com/marketplace/io/Secure.java_

## Building ##
Simply move directory _hg/crawler/_, which contains a build file. Run **ant** to build the program. It creates
  * a runnable jar file located in  _hg/crawler/dist/lib_
  * java doc

### Fetching ###
You need a crawl Market ID to fetch hidden apps. If you already know what your real Market ID is, you can modify the constructor of Device.java (line 54-58) located at _hg/crawler/src/com/marketplace/util/Device.java_.
```
        public Device(int deviceVersion, String deviceName) {
                this.deviceName = deviceName;
                this.deviceVersion = deviceVersion;
                this.marketId = "308174787AB"; //Your real market id
        }

```

Check out how to:

[CrawlAppsViaCategory](https://code.google.com/p/android-marketplace-crawler/wiki/CrawlAppsViaCategory)

[CrawlAppsViaPublisherNames](https://code.google.com/p/android-marketplace-crawler/wiki/CrawlAppsViaPublisherNames)

[CrawlAppViaPackageName](https://code.google.com/p/android-marketplace-crawler/wiki/CrawlAppViaPackageName)

## Command Line Usage ##
**Ensure that you move the permission file in the same directory as _crawler.jar_.** If the rails server is not running on your local machine, modify the static variable '_url_' in _hg/crawler/src/com/marketplace/Constants.java_ to reflect the changes.

usage: android-marketplace-crawler
> -c,--category            fetch app(s) for all categories

> -com,--comment           fetch comment(s) for all app(s)

> -i,--image               fetch image for all app(s)

> -l,--latest `<arg>`        latest app(s), where `<arg>` is true or false

> -pname,--package `<arg>`   fetch app(s) by package(s), where `<arg>` is location to file

> -pub,--publisher `<arg>`   fetch app(s) by publisher(s), where `<arg>` is location to file


## Server ##

### Prerequisites ###
  * gems defined in gemfile
  * run _rake:db:seed_ to initialise the database.