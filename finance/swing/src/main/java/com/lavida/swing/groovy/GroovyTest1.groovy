package com.lavida.swing.groovy

import com.lavida.swing.groovy.http.HttpRequest
import com.lavida.swing.groovy.http.RequestMethod
import com.lavida.swing.groovy.http.browser.BrowserChrome
import com.lavida.swing.groovy.http.client.ApacheHttpClient
import com.lavida.swing.groovy.model.Device
import groovy.util.slurpersupport.GPathResult
import groovy.xml.StreamingMarkupBuilder
import org.springframework.context.support.ClassPathXmlApplicationContext

class GroovyTest1 {
	
	static String getPage(url) {
		def httpClient = new ApacheHttpClient();
		def browser = new BrowserChrome();
		def request = new HttpRequest(RequestMethod.GET, url, null);
		def response = httpClient.sendRequest(request, browser);
		return response.getContent();
	}
	
	static String getFromFile(String filePath) {
		def fileReader = new FileReader(filePath);
		def str = new StringBuilder();
		def ch;
		while ((ch = fileReader.read()) != -1) {
			str.append((char)ch);
		}
		return str.toString();
	}
	
	static void saveToFile(String filePath, String content) {
		def fileWriter = new FileWriter(filePath);
		fileWriter.write(content);
		fileWriter.close();
	}
	
	static void main1(args) {
		def content = getPage("http://internet.yandex.ru/");
//		def content = getPage("http://hard.rozetka.com.ua/ru/products/procategory/271/index.html");
		saveToFile("C:/viderCards.htm", content);
	}

	static main44(args) {
		testRozetkaSerfing('http://hard.rozetka.com.ua/ru/products/procategory/271/sor/p1096r164/index.html');
	}
	static main33(args) {
		def filePath = "C:/viderCards.htm";
//		def content = getPage("http://hard.rozetka.com.ua/ru/products/procategory/271/index.html");
//		saveToFile(filePath, content);
		def content = getFromFile(filePath);
//		System.out.println(content);
		
		def tagsoupParser = new org.ccil.cowan.tagsoup.Parser()
		def slurper = new XmlSlurper(tagsoupParser)
		
		def parser = slurper.parseText(content);

		// Firms static analyze:
//		def firms = parser.'**'.find { tag -> tag.@id == 'sort_producer' };
//		firms.'**'.findAll { it.name() == 'li' }.each { li ->
//			def name = li.label.a.span.text().trim();
//			def link = li.label.a.@href.text().trim();
//			def cnt = li.i.text().trim();
//			println "${name}) ${cnt} ${link}";
//		}
		
		// Video cards get data:
		def goodsList = parser.'**'.find { tag -> tag.@class == 'goods list' };
		goodsList.table.each { good ->
			def rating = good.tr.td.table.find { table -> table.@class == 'rating' };
			def feedbacks = rating.tr.td.a.text().trim();
			def feedbacksUrl = rating.tr.td.a.@href;
			def stars = rating.tr.td.div.find { it.@class == 'value' }.div.@style;
			
			def detail = good.tr.td.find { td -> td.@class == 'detail' };
			def title = detail.div.a.text().trim();
			def titleLink = detail.div.a.@href.text().trim();
			def articul = detail.div.span.text().trim();
			def promotions = detail.div.find { div -> div.@name == 'promotions_catalog' };
			def status = detail.'**'.find { tag -> tag.@class == 'status' }.span.@class == 'available';
			def price = detail.'**'.find { tag -> tag.@class == 'price' };
			def priceUAH = price.div.find { it.@class == 'uah' }.text().trim();
			def priceUSD = price.div.find { it.@class == 'usd' }.text().trim();
			
			println(title);
			println titleLink;
//			println promotions;
			println articul;
			println '-----------------------';
//			System.exit(0);
		}

		// next page (may be absent):
		def nextPage = parser.'**'.find { tag -> tag.@class == 'nextt' };
		if (nextPage != null) {
			def nextPageUrl = nextPage.a.@href;
			println nextPageUrl;
		}
	}

	static testRozetkaSerfing(url) {
		def counter = 0;
		while (url != null) {
			println '============== Go to: ' + url;
			def content = getPage(url);
			
			def tagsoupParser = new org.ccil.cowan.tagsoup.Parser()
			def slurper = new XmlSlurper(tagsoupParser)
			
			def parser = slurper.parseText(content);
	
			// Video cards get data:
			def goodsList = parser.'**'.find { tag -> tag.@class == 'goods list' };
			goodsList.table.each { good ->
				println '---------- ' + ++counter + ') data:';
				
				def device = new Device();
				
				def rating = good.tr.td.table.find { table -> table.@class == 'rating' };
				def feedbacks = rating.tr.td.a.text().trim();
				def feedbacksUrl = rating.tr.td.a.@href;
				def stars = rating.tr.td.div.find { it.@class == 'value' }.div.@style;
				
				def detail = good.tr.td.find { td -> td.@class == 'detail' };
				device.title = detail.div.a.text().trim();
				device.detailsLink = detail.div.a.@href.text().trim();
				device.articul = detail.div.span.text().trim();
				device.promotion = detail.div.find { div -> div.@name == 'promotions_catalog' }.text().trim();
				device.available = detail.'**'.find { tag -> tag.@class == 'status' }.span.@class == 'available';
				
				def price = detail.'**'.find { tag -> tag.@class == 'price' };
				if (price != null) {
					device.priceUAH = price.div.find { it.@class == 'uah' }.text().trim();
					println 'price: ' + priceUAH;
					def priceUSD = price.div.find { it.@class == 'usd' }.text().trim();
				}
				
//				println titleLink;
//				println promotions;
			}
	
			// next page (may be absent):
			def nextPage = parser.'**'.find { tag -> tag.@class == 'next' };
			url = (nextPage != null) ? nextPage.a.@href.toString() : null;
		}
	}

	static main(args) {
		def context = new ClassPathXmlApplicationContext("spring-security.xml");
		def deviceService = context.getBean("deviceServiceImpl");

		def baseUrl = 'http://hard.rozetka.com.ua/asus_rampage_4_gene/p200373/';

		def tagsoupParser = new org.ccil.cowan.tagsoup.Parser()
		def slurper = new XmlSlurper(tagsoupParser)
		
		def device = new Device();
		device.type = 'Rozetka';

		// All details data:
		println '============== Go to: ' + baseUrl;
		def content = getPage(baseUrl);
		def parser = slurper.parseText(content);

//			def titleDiv = parser.'**'.find { tag -> tag.name() == 'div' && tag.@class == 'title-page-with-code' };
//			device.title = titleDiv.h1.text().trim();
//			def articulDiv = titleDiv.div.find { div -> div.@class == 'code' };
//			device.articul = articulDiv.span.text().trim() + articulDiv.text().trim();
		def tabContentAll = parser.'**'.find { it.@class == 'tab-content all' };
		def container = tabContentAll.div.find { it.@class == 'item' }.div;
		device.title = container.h2.text().trim();
		device.description = container.div.text().trim();

		// Characteristics details data:
		def url = parser.'**'.find { it.@name == 'characteristics' }.a.@href.text().trim();
		println('============== Go to: ' + url);
		content = getPage(url);
		parser = slurper.parseText(content);

		def tabContentCh = parser.'**'.find { it.@class == 'tab-content characteristics' };
		tabContentCh.div.table.tbody.tr.each { tr ->
			device.characteristics.put(
				tr.td.find { it.@class == 'title' }.text().trim(),
				tr.td.find { it.@class == 'field' }.text().trim()
			);
		}
		println device.characteristics;
		
//		println 'descr: ' + device.description.size();
		device.description = device.description.substring(0, 255);
//		device.description += "aaaaaa";
		println device.description;
		
		device.articul = '15';
		deviceService.saveOrUpdateByArticul(device);
	}
	
	static Double getMoney(String str) {
		StringBuilder parsedMoney = new StringBuilder();
		for (int i = 0; i<str.length(); ++i) {
			if (Character.isDigit(str.charAt(i))) {
				parsedMoney.append(str.charAt(i));
			} else if (str.charAt(i) == '.' || str.charAt(i) == ',') {
				parsedMoney.append('.');
			} else if (Character.isWhitespace(str.charAt(i))) {
				continue;
			} else {
				break;
			}
		}
		return (parsedMoney.length() > 0) ? Double.parseDouble(parsedMoney.toString()) : null;
	}

	static String getModel(String title) {
		if (title.lastIndexOf("(") < 0 || title.lastIndexOf(")") < 0) return null;
		def model = title.substring(title.lastIndexOf("(")+1, title.lastIndexOf(")"));
		for (int i = 0; i<model.length(); ++i) {
			if (Character.isWhitespace(model.charAt(i)) || model.charAt(i) == ',') {
				return null;
			}
		}
		return model;
	}

	public static String getXmlString(GPathResult root){
		return new StreamingMarkupBuilder().bind{
			out << root
		}
	}

	static main3(args) {
		def counter = 0;
		def url = 'http://hard.rozetka.com.ua/ru/products/procategory/271/sor/p1096r164/index.html';
		while (url != null) {
			println '============== Go to: ' + url;
			def content = getPage(url);
			
			def tagsoupParser = new org.ccil.cowan.tagsoup.Parser()
			def slurper = new XmlSlurper(tagsoupParser)
			
			def parser = slurper.parseText(content);
	
			// Video cards get data:
			def goodsList = parser.'**'.find { tag -> tag.@class == 'goods list' };
			goodsList.table.each { parentTag ->
				println '---------- ' + ++counter + ') data:';
				if (counter < 3) return;
				
				def device = new Device();
				
		//		def rating = parentTag.tr.td.table.find { table -> table.@class == 'rating' };
		//		def feedbacks = rating.tr.td.a.text().trim();
		//		def feedbacksUrl = rating.tr.td.a.@href;
		//		def stars = rating.tr.td.div.find { it.@class == 'value' }.div.@style;
				
				def detail = parentTag.tr.td.find { td -> td.@class == 'detail' };
				device.type = parser.'**'.find { it.@class == 'title-page-with-filters' }.h1.text().trim();
				device.detailsLink = detail.div.a.@href.text().trim();
				
				assert detail.div.span.'**'.size() == 2;
				device.articul = '01-' + detail.div.span.text().trim().substring(4);
		
//				device.promotion = detail.div.find { it.@name == 'promotions_catalog' }.text().trim();
				device.available = detail.'**'.find { tag -> tag.@class == 'status' }.span.@class == 'available';
				
				def price = detail.'**'.find { tag -> tag.@class == 'price' };
				if (price != null) {
					device.priceUAH = getMoney(price.div.find { it.@class == 'uah' }.text().trim());
				}
				
				println device;
				System.exit(0);
			}
	
			// next page (may be absent):
			def nextPage = parser.'**'.find { tag -> tag.@class == 'next' };
			url = (nextPage != null) ? nextPage.a.@href.toString() : null;
		}
	}
}
