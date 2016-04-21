## How to crawl apps via category? ##

  * Build java project using ant
  * move the file _permission_ (located hg/crawler/permission) to _hg/crawler/src/dist/lib/_
  * Set up rails server
  * Start rails server

## Details ##
  * cd /path/to/crawler.jar

_To crawl the top apps per category_
  * java -jar crawler.jar -c

_To crawl the latest apps_
  * java -jar crawler.jar -c -l true