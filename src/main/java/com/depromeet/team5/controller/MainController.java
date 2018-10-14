package com.depromeet.team5.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;


@Controller
public class MainController {

	@GetMapping(path="/")
	public String index(Model model, HttpServletRequest req, HttpServletResponse res) {
		model.addAttribute("msg", "message");
		return "index";
	}
	
	@GetMapping(path="/post")
	public String goPostPage(String url, Model model) throws IOException{
		model.addAttribute("url", url);
		return "post/post";
	}
}
