## How to crawl apps via a list of Package Names? ##

  * Build java project using ant
  * move the file _permission_ (located hg/crawler/permission) to _hg/crawler/src/dist/lib/_
  * Set up rails server
  * Start rails server

## Details ##
  * cd /path/to/crawler.jar
  * java -jar crawler.jar -pname /path/to/file\_containing\_list\_of\_packages

I have provided sample list of packages in a file _packages_ found _hg/crawler/data/packages_