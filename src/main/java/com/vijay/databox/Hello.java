package com.vijay.databox;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hello {
	@GetMapping("/movies/{id}")
	public String index(@PathVariable String id) {
		return "Leo" + id;
	}
	// @GetMapping("/")
	// public String index() {
	// 	return "Leo Das";
	// }
	
//	@GetMapping("/new/movies")
//	public HelloBean index() {
//		return new HelloBean("boruto");
//	}
}
