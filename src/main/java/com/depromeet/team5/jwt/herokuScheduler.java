package com.depromeet.team5.jwt;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class herokuScheduler {
	
	@Scheduled(cron="0 0/20 * * * ?")
	public void requestToMe() throws IOException {
		Jsoup.connect("https://minu-jus.herokuapp.com/").get();
		System.out.println("req to index");
	}
}
