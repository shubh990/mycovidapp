package com.consumingapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.consumingapi.model.CountryData;
import com.consumingapi.model.Covid19Stat;
import com.consumingapi.model.Coviddata;
import com.consumingapi.model.TotalCountryData;
import com.consumingapi.service.ServiceClass;

import java.util.*;

@Controller
public class Controllers {

	@Autowired
	ServiceClass serviceClass;
	
	Coviddata coviddata;

	TotalCountryData totalCountryData;

	List<Covid19Stat> stats;
	
	CountryData countryData;
	
	String countryName;

	@GetMapping("/")
	public String home() {
//		coviddata = serviceClass.Totals();
//		List<Covid19Stat> stats = coviddata.getData().getCovid19Stats();
//		stats.get(0);
//		System.out.println(stats.get(12).getProvince());
		return "covid19";
	}

	@RequestMapping("/country")
	public String country(@RequestParam("country") String country, Model model) {

		String countryName = firstLetterUppercase(country);
		model.addAttribute("title", "Covid19");
		serviceClass.setUrlname(countryName);
		this.countryName = countryName;
		
		try {
			coviddata = serviceClass.Totals();
			totalCountryData = serviceClass.totalCountryData();
			stats = coviddata.getData().getCovid19Stats();
			countryData = totalCountryData.getData();
		} catch (Exception e) {
			model.addAttribute("invalid", "Something went Wrong! Please Enter Valid Country Name! ");
			return "covid19";
		}

		if (stats.size() > 3000) {
			model.addAttribute("invalid", "Please Enter Valid Country Name!!");
			return "covid19";
		} else {
			model.addAttribute("stats", stats);
			model.addAttribute("total", countryData);
			model.addAttribute("country", country);
			return "covid19";
		}
	}

	@GetMapping("/confirmed")
	public String confirmed(Model model) {
		stats.sort((Covid19Stat o2, Covid19Stat o1) -> o1.getConfirmed() - o2.getConfirmed());

		model.addAttribute("stats", stats);
		model.addAttribute("total", countryData);
		model.addAttribute("country", countryName);
		return "covid19";
	}
	
	@GetMapping("/active")
	public String active(Model model) {
		stats.sort((Covid19Stat o2, Covid19Stat o1) -> (o1.getConfirmed() - o1.getRecovered()) - (o2.getConfirmed() - o2.getRecovered()));

		model.addAttribute("stats", stats);
		model.addAttribute("total", countryData);
		model.addAttribute("country", countryName);
		return "covid19";
	}

	@GetMapping("/recovered")
	public String recovered(Model model) {

		stats.sort((Covid19Stat o2, Covid19Stat o1) -> o1.getRecovered() - o2.getRecovered());

		model.addAttribute("stats", stats);
		model.addAttribute("total", countryData);
		model.addAttribute("country", countryName);
		return "covid19";
	}

	@GetMapping("/deaths")
	public String deaths(Model model) {

		stats.sort((Covid19Stat o2, Covid19Stat o1) -> o1.getDeaths() - o2.getDeaths());

		model.addAttribute("stats", stats);
		model.addAttribute("total", countryData);
		model.addAttribute("country", countryName);
		return "covid19";
	}
	//lol
	@GetMapping("/state")
	public String state(Model model) {
		stats.sort((Covid19Stat o2, Covid19Stat o1) -> o2.getProvince().compareToIgnoreCase(o1.getProvince()));

		model.addAttribute("stats", stats);
		model.addAttribute("total", countryData);
		model.addAttribute("country", countryName);
		return "covid19";
	}
	
	@RequestMapping("/about")
	public String about(Model model){
		model.addAttribute("title", "About");
		return "about";
	}
	
	public String firstLetterUppercase(String country) {
		String firstLetter = country.substring(0, 1);
		String remaingLetter = country.substring(1);
		firstLetter = firstLetter.toUpperCase();
		remaingLetter = remaingLetter.toLowerCase();
		return firstLetter + remaingLetter;
	}

}
