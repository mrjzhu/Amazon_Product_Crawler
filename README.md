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
{
    "adId": 2,
    "bidPrice": 3.4,
    "brand": "Rainbow Light",
    "campaignId": 8040,
    "category": "Health & Household",
    "costPerClick": 0,
    "detail_url": "https://www.amazon.com/Rainbow-Light-Prenatal-Multivitamin-120-Count/dp/B06Y1PF11F",
    "keyWords": [
        "rainbow",
        "light",
        "prenatal",
        "one",
        "multivitamin",
        "120",
        "count",
        "bottle"
    ],
    "pClick": 0,
    "position": 0,
    "price": 36.47,
    "qualityScore": 0,
    "query": "Prenatal DHA",
    "query_group_id": 10,
    "rankScore": 0,
    "relevanceScore": 0,
    "thumbnail": "https://images-na.ssl-images-amazon.com/images/I/41swTxW2+DL._AC_US218_.jpg",
    "title": "Rainbow Light Prenatal One Multivitamin, 120-Count Bottle"
}

```

#### note
THere are logs for exception, some data may not be parse or crawl.
