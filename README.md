# Amazon_Product_Crawler

## Introduction
This is a application to crawler product data from Amazon with different "Query" in file rawQuery.txt
THen, generate Ads info in Json format.

## Installation

### Dependency
1. Jsoup
2. Jackson
3. lucene
4. slf4j

### File
1. rawQuery
2. proxylist_bittiger.csv ( The proxy ip address, just in case of blocking.)

### Start Application
```
git clone https://github.com/mrjzhu/Amazon_Product_Crawler.git
```
```
cd Ad_crawler/out/artifacts/Ad_crawler_jar
```

```
java -jar Ad_crawler.jar
```

### Result

Generate a AdsDate.txt file to store the Ads infomation.

#### result example
```
{"detail_url":"https://www.amazon.com/Rainbow-Light-Prenatal-Multivitamin-120-Count/dp/B06Y1PF11F","keyWords":["rainbow","light","prenatal","one","multivitamin","120","count","bottle"],"thumbnail":"https://images-na.ssl-images-amazon.com/images/I/41swTxW2+DL._AC_US218_.jpg","costPerClick":0,"campaignId":8040,"query":"Prenatal DHA","rankScore":0,"title":"Rainbow Light Prenatal One Multivitamin, 120-Count Bottle","query_group_id":10,"bidPrice":3.4,"adId":2,"relevanceScore":0,"price":36.47,"qualityScore":0,"pClick":0,"position":0,"category":"Health & Household","brand":"Rainbow Light"}

```

#### note
THere are logs for exception, some data may not be parse or crawl.
