## How to crawl apps via a list of Publisher Names? ##

  * Build java project using ant
  * move the file _permission_ (located hg/crawler/permission) to _hg/crawler/src/dist/lib/_
  * Set up rails server
  * Start rails server

## Details ##
  * cd /path/to/crawler.jar
  * java -jar crawler.jar -pub /path/to/file\_containing\_list\_of\_publishers

I have provided sample list of publishers in a file _publishers_ found _hg/crawler/data/publishers_