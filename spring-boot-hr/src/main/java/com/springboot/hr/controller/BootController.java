package com.springboot.hr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author huangran <huangran@kuaishou.com>
 * created on 2022-04-25
 */
@Controller
public class BootController {

	@ResponseBody
	@RequestMapping("/boot")
	public String bootString() {
		System.out.println("bootController run....");
		return "hello SpringBoot!";
	}
}
