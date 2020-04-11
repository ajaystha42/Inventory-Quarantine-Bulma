package com.home.quarantine.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.home.quarantine.model.CurrentStock;
import com.home.quarantine.model.Sales;
import com.home.quarantine.repository.CurrentStockRepository;
import com.home.quarantine.repository.SalesRepository;

@Controller
public class DashboardController {

	@Autowired
	private SalesRepository salesRepo;

	@Autowired
	private CurrentStockRepository currentRepo;

	@GetMapping({ "/", "/dashboard" })
	public String getDashboard(Model model) {
		LocalDate date = LocalDate.now();
		String dateOnly = date.toString();
		List<Sales> allSales = salesRepo.findByDate(dateOnly);
//		ArrayList updatedSales = new ArrayList();
		List<CurrentStock> currentStocks = currentRepo.findAll();
		for (CurrentStock item : currentStocks) {
			if (Integer.parseInt(item.getQuantity()) == 0) {
				currentRepo.delete(item);
			}
		}
		List<CurrentStock> updatedCurrentStocks = currentRepo.findAll();

		model.addAttribute("currentStocks", updatedCurrentStocks);
		model.addAttribute("todaySales", allSales);
		return ("dashboard");
	}

	@GetMapping("/charts")
	public String getCharts(Model model) {
		int quantity = 0;
		List<Sales> sales = salesRepo.findAll();
		ArrayList salesList = new ArrayList();
		ArrayList<String> head = new ArrayList<String>();
		head.add("Date");
		head.add("Quantity");
		salesList.add(head);
		Set<String> check = new HashSet<String>();
		for (Sales s : sales) {
			ArrayList salesL = new ArrayList();
			boolean flag = true;
			if (!check.contains(s.getDate())) {
				salesL.add(s.getDate());
				for (Sales sa : sales) {
					if (salesL.contains(sa.getDate())) {
						quantity = quantity + Integer.parseInt(sa.getQuantity());
						System.out.println("quantity " + quantity);
					}
				}
				salesL.add(quantity);
				flag = false;
			}
			salesList.add(salesL);
			check.add(s.getDate());
		}
		salesList.remove((salesList.size() - 1));
		System.out.println("00000000000000000000000" + salesList);
//		System.out.println("00000000000000000000000000000000000000000000");
//		System.out.println(sales.size());
//		System.out.println(salesList);
		List<CurrentStock> currentStocks = currentRepo.findAll();

		for (CurrentStock cs : currentStocks) {
			if (Integer.parseInt(cs.getQuantity()) == 0) {
				currentRepo.delete(cs);
			}
		}
		List<CurrentStock> currentStocksUpdated = currentRepo.findAll();
		model.addAttribute("currentStocks", currentStocksUpdated);
		LocalDate date = LocalDate.now();
		String lDate = date.toString();
		List<Sales> salesByDate = salesRepo.findByDate(lDate);
		model.addAttribute("salesByDate", salesByDate);
		model.addAttribute("saleList", salesList);
		return "charts";
	}
}
