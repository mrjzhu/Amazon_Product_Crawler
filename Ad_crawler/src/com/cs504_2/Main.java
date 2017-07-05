package com.cs504_2;

import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.en.KStemFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;
import org.apache.lucene.analysis.util.CharArraySet;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Main {
    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.95 Safari/537.36";
    private final String authUser = "bittiger";
    private final String authPassword = "cs504";
    private static final Version LUCENE_VERSION = Version.LUCENE_40;

    private static List<String> proxys;
    private static int index_Proxy = 0;

    private Logger logger = LoggerFactory.getLogger(Main.class);
    public void initProxy() {
        System.setProperty("socksProxyHost", "199.101.97.161"); // set socks proxy server
        System.setProperty("socksProxyPort", "61336"); // set socks proxy port

//        System.setProperty("http.proxyHost", "173.208.78.34"); // set proxy server
//        System.setProperty("http.proxyPort", "60099"); // set proxy port
        System.setProperty("http.proxyUser", authUser);
        System.setProperty("http.proxyPassword", authPassword);
        Authenticator.setDefault(
                new Authenticator() {
                    @Override
                    public PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                authUser, authPassword.toCharArray());
                    }
                }
        );
    }
    private void initProxyList() {
        proxys = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(" proxylist_bittiger.csv"))) {
            String line;
            while((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                String ip = fields[0].trim();
                proxys.add(ip);
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        Authenticator.setDefault(
                new Authenticator() {
                    @Override
                    public PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                authUser, authPassword.toCharArray());
                    }
                }
        );

        System.setProperty("http.proxyUser", authUser);
        System.setProperty("http.proxyPassword", authPassword);
        System.setProperty("socksProxyPort", "61336");
    }

    private void changeProxy() {
        if (index_Proxy == proxys.size()) index_Proxy = 0;
        String proxy = proxys.get(index_Proxy++);
        System.setProperty("socksProxyHost", proxy);
        System.out.println(proxy);
    }

    public String getTitle(Document doc, int i){
        String titleSelector1 =" > div > div > div > div.a-fixed-left-grid-col.a-col-right > div.a-row.a-spacing-small > div:nth-child(1) > a > h2";
        String titleSelector2 =" > div > div > div > div.a-fixed-left-grid-col.a-col-right > div.a-row.a-spacing-small > a > h2";
        String select1 = "#result_" +Integer.toString(i) + titleSelector1;
        String select2 = "#result_" +Integer.toString(i) + titleSelector2;

        Element title = doc.select(select1).first();
        if(title != null){
//            System.out.println("Title: " + title.text());
            return title.text();
        }
        else{
            title = doc.select(select2).first();
            if(title == null) {
                logger.error("FAIL TO GET TITLE! RETURN UNKNOWN");
                return "UNKNOWN";
            }
//            System.out.println("Title: " + title.text());
            return title.text();
        }
    }
    public String getProdUrl(Document doc, int i){
        String selector = "#result_"+ Integer.toString(i)+ " > div > div > div > div.a-fixed-left-grid-col.a-col-left > div > div > a";
        Element element = doc.select(selector).first();
        if(element != null) {
            String url = element.attr("href");
            int index = url.indexOf("ref");
            if(url.substring(0,1).equals("/")){
                int index2 = url.indexOf("www");
                url = url.substring(index2);
            }
            else{
                url = url.substring(0, index - 1);

            }
//            System.out.println("url: "+ detail_url);
            return url;
        }
        logger.error("FAIL TO GET DETAIL URL! RETURN UNKNOWN");
        return "UNKNOWN";
    }
    public double getPrice(Document doc, int i){
        String price_path = "#result_"+ Integer.toString(i)+" > div > div > div > div.a-fixed-left-grid-col.a-col-right > div:nth-child(2) > div.a-column.a-span7 > div:nth-child(1) > div:nth-child(3) > a > span.a-color-base.sx-zero-spacing";
        Element priceElement = doc.select(price_path).first();
        if(priceElement != null){
            String[] priceStr = priceElement.text().split(" ");
            String[] IntParts = priceStr[1].split(",");
            String IntPart = "";
            for(String str: IntParts){
                IntPart = IntPart + str.trim();
            }
            double price = Double.parseDouble(IntPart+"."+priceStr[2]);

//            System.out.println("Price: "+ price);
            return price;
        }
        logger.error("FAIL TO GET PRICE! RETURN 0");

        return 0.00;


    }
    public String getImageUrl(Document doc, int i){
        String imageUrl = "#result_"+Integer.toString(i)+" > div > div > div > div.a-fixed-left-grid-col.a-col-left > div > div > a > img";
        Element element = doc.select(imageUrl).first();
        if(element != null) {
            String image_url = element.attr("src");
//            System.out.println("image: "+ image_url);
            return image_url;
        }
        return "UNKNOWN";
    }
    public String getCatogory(Document doc, int i){

        Element category = doc.select("#leftNavContainer > ul:nth-child(2) > div > li:nth-child(1) > span > a > h4").first();
//        System.out.println("catogory:" + category.text());
        if(category == null) {
            logger.error("FAIL TO GET CATEGORY! RETURN UNKNOWN");
            return "UNKNOWN";
        }
        return category.text();
    }

    public String getBrand(Document doc, int i){
        String brand_path = "#result_"+Integer.toString(i)+" > div > div > div > div.a-fixed-left-grid-col.a-col-right > div.a-row.a-spacing-small > div > span:nth-child(2)";
        Element brand = doc.select(brand_path).first();
        if(brand != null) {
//            System.out.println("brand:" + brand.text());
            return brand.text();
        } else {
            logger.error("FAIL TO GET BRAND! RETURN UNKNOWN");
            return "";
        }
    }

    private static String stopWords = "a,able,about,across,after,all,almost,also,am,among,an,and,any,are,as,at,be,because,been,but,by,can,cannot,could,dear,did,do,does,either,else,ever,every,for,from,get,got,had,has,have,he,her,hers,him,his,how,however,i,if,in,into,is,it,its,just,least,let,like,likely,may,me,might,most,must,my,neither,no,nor,not,of,off,often,on,only,or,other,our,own,rather,said,say,says,she,should,since,so,some,than,that,the,their,them,then,there,these,they,this,tis,to,too,twas,us,wants,was,we,were,what,when,where,which,while,who,whom,why,will,with,would,yet,you,your";
    private static CharArraySet getStopwords(String stopwords) {
        List<String> stopwordsList = new ArrayList<String>();
        for (String stop : stopwords.split(",")) {
            stopwordsList.add(stop.trim());
        }
        return new CharArraySet(LUCENE_VERSION, stopwordsList, true);
    }

    public static List<String> cleanedTokenize(String input) {
        List<String> tokens = new ArrayList<String>();
        StringReader reader = new StringReader(input.toLowerCase());
        Tokenizer tokenizer = new StandardTokenizer(LUCENE_VERSION, reader);
        TokenStream tokenStream = new StandardFilter(LUCENE_VERSION, tokenizer);
        tokenStream = new StopFilter(LUCENE_VERSION, tokenStream, getStopwords(stopWords));
        tokenStream = new KStemFilter(tokenStream);
        StringBuilder sb = new StringBuilder();
        CharTermAttribute charTermAttribute = tokenizer.addAttribute(CharTermAttribute.class);
        try {
            tokenStream.reset();
            while (tokenStream.incrementToken()) {
                String term = charTermAttribute.toString();

                tokens.add(term);
                sb.append(term + " ");
            }
            tokenStream.end();
            tokenStream.close();

            tokenizer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return tokens;
    }

    public List<Ad> getResultFromQuery(String query,double bid, int campaignID, int queryGroupID, int total_page){
        List<Ad> adList = new ArrayList<>();
        for(int page = 1; page <= total_page; page++ ){
            String url = "http://www.amazon.com/s/ref=nb_sb_noss?field-keywords=" + query + "&page="+Integer.toString(page);
            logger.info("Crawler data from...."+url);
            try{
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
                headers.put("Accept-Encoding", "gzip, deflate, sdch, br");
                headers.put("Accept-Language", "en-US,en;q=0.8");

                Document doc = Jsoup.connect(url).userAgent(USER_AGENT).timeout(100000).get();
                if(doc == null) {
                    logger.error("Cannot connect url, ");
                    return null;
                }
//            System.out.println("page size: " + docSize);

                Elements prods = doc.select("li[data-asin]");

                int startId = Integer.parseInt(prods.first().id().split("_")[1]);//            System.out.println("product size: "+ prods.size());

                for (int i = startId; i < startId + prods.size(); i++) {
                    Ad ad = new Ad();
                    ad.title = getTitle(doc,i);
                    ad.detail_url = getProdUrl(doc,i);
                    ad.price = getPrice(doc,i);
                    ad.thumbnail = getImageUrl(doc,i);
                    ad.category = getCatogory(doc,i);
                    ad.brand = getBrand(doc,i);
                    ad.query = query;
                    ad.bidPrice = bid;
                    ad.campaignId = campaignID;
                    ad.query_group_id = queryGroupID;
                    ad.keyWords = cleanedTokenize(ad.title);
                    adList.add(ad);
                    i++;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return adList;



    }

    public List<String> Json(List<Ad> adList){
        List<String> JsonStr = new ArrayList<>();
        for(Ad ad: adList){
            JSONObject obj = new JSONObject();
            obj.put("adId", ad.adId);
            obj.put("bidPrice", ad.bidPrice);
            obj.put("brand", ad.brand);
            obj.put("campaignId", ad.campaignId);
            obj.put("category", ad.category);
            obj.put("costPerClick", ad.costPerClick);
            obj.put("description", ad.description);
            obj.put("detail_url", ad.detail_url);
            obj.put("pClick", ad.pClick);
            obj.put("position", ad.position);
            obj.put("price", ad.price);
            obj.put("qualityScore", ad.qualityScore);
            obj.put("query", ad.query);
            obj.put("query_group_id", ad.query_group_id);
            obj.put("rankScore", ad.rankScore);
            obj.put("relevanceScore", ad.relevanceScore);
            obj.put("thumbnail", ad.thumbnail);
            obj.put("title", ad.title);
            JSONArray keyWords = new JSONArray();
            for(String keyword: ad.keyWords)
                keyWords.put(keyword);
            obj.put("keyWords", keyWords);
            JsonStr.add(obj.toString());
        }
        return JsonStr;


    }
    public static void main(String[] args) throws IOException {
        // write your code here
        new Main().initProxyList();

        int totalPage = 3; //total pages to crawler
        String line = null;

        BufferedReader br = new BufferedReader(new FileReader("rawQuery.txt"));
        File adsDataFile = new File("AdsData.txt");
        if (!adsDataFile.exists()) {
            adsDataFile.createNewFile();
        }

        FileWriter fileWriterForAds = new FileWriter(adsDataFile.getAbsoluteFile());
        BufferedWriter bufferedWriterForAds = new BufferedWriter(fileWriterForAds);

        int total = 0;
        int loop = 1;
        while ((line = br.readLine()) != null) {
            new Main().changeProxy();
            if(line.length() == 0) continue;
            String[] mesg = line.split(",");

            List<Ad> adList = new Main().getResultFromQuery(mesg[0].trim(),Double.parseDouble(mesg[1].trim()),Integer.parseInt(mesg[2].trim()),Integer.parseInt(mesg[3].trim()),totalPage);
            if (adList == null) continue;
            List<String> JsonStrs = new Main().Json(adList);
//            int i = 0;
            for(String str: JsonStrs) {

                bufferedWriterForAds.write(str);
                bufferedWriterForAds.newLine();
//                i++;
            }
//            total += i;
//            System.out.println(i);
//            System.out.println(loop++ + "/233");
        }
//        System.out.println("total: " + total);




        bufferedWriterForAds.close();
    }
}
