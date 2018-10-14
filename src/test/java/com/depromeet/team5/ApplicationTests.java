package com.depromeet.team5;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest @Slf4j
public class ApplicationTests {

	@Test
	public void contextLoads() throws IOException {
		String url = "http://news.chosun.com/site/data/html_dir/2018/10/13/2018101300225.html";
		Document doc = Jsoup.connect(url).get();
		log.info(doc.toString().replaceAll("(\r\n|\r|\n|\n\r)", "").trim());
	}

}
