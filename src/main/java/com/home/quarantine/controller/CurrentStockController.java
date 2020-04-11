package com.home.quarantine.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.home.quarantine.model.CurrentStock;
import com.home.quarantine.repository.CurrentStockRepository;

@Controller
public class CurrentStockController {

	@Autowired
	private CurrentStockRepository currentStockRepo;
//
//	@GetMapping("/currentStock")
//	public String currentStock(Model model) {
//		List<CurrentStock> currentStocks = currentStockRepo.findAll();
//		for (CurrentStock item : currentStocks) {
//			if (Integer.parseInt(item.getQuantity()) == 0) {
//				currentStockRepo.delete(item);
//			}
//
//		}
//		List<CurrentStock> updatedCurrentStock = currentStockRepo.findAll();
//		model.addAttribute("currentStocks", updatedCurrentStock);
//
//		return "currentStock";
//	}

}
