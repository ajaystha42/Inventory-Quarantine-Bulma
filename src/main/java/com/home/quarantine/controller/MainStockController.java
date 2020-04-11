package com.home.quarantine.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.home.quarantine.model.Category;
import com.home.quarantine.model.CurrentStock;
import com.home.quarantine.model.MainStock;
import com.home.quarantine.model.Product;
import com.home.quarantine.model.Unit;
import com.home.quarantine.repository.CategoryRepository;
import com.home.quarantine.repository.CurrentStockRepository;
import com.home.quarantine.repository.MainStockRepository;
import com.home.quarantine.repository.ProductRepository;
import com.home.quarantine.repository.UnitRepository;

@Controller
public class MainStockController {

	@Autowired
	private MainStockRepository mainStockRepo;

	@Autowired
	private CategoryRepository catRepo;

	@Autowired
	private ProductRepository prodRepo;

	@Autowired
	private UnitRepository unitRepo;

	@Autowired
	private CurrentStockRepository currentStockRepo;

	@GetMapping("/mainStock")
	public String getMainStocks(Model model) {
		List<MainStock> mainStocks = mainStockRepo.findByIfDeleted("No");
		model.addAttribute("mainStocks", mainStocks);
		return "mainStock";
	}

	@GetMapping("/addStock")
	public String getAddStock(Model model, @RequestParam(required = false) Integer id) {
		MainStock mainStock = new MainStock();
		model.addAttribute("mainStocks", mainStock);
			if (id == null) {
			List<Product> products = prodRepo.findByIfDeleted("No");
			model.addAttribute("products", products);
			List<Category> categories = catRepo.findByIfDeleted("No");
			model.addAttribute("categories", categories);
		

		} else {
			List<Category> categories = catRepo.findByIfDeleted("No");
			model.addAttribute("categories", categories);
			List<Product> products = prodRepo.findByCategoryId(id);
			model.addAttribute("products", products);
			model.addAttribute("selectedCategoryId", id);
			mainStock.setCategory(categories.stream().filter(c -> c.getId().equals(id)).findFirst().get());
		}
//			System.out.println("2222222222222222222222222222222222"+id);
		List<Unit> units = unitRepo.findByIfDeleted("No");
		model.addAttribute("units", units);
		model.addAttribute("idd", id);
		return "addStock";
	}

	@PostMapping("/addStock")
	public String postAddStock(@ModelAttribute MainStock mainStock) {
		LocalDate date = LocalDate.now();
		String onlyDate = date.toString();
		mainStock.setDate(onlyDate);
		mainStock.setIfDeleted("No");
		mainStockRepo.save(mainStock);
		List<CurrentStock> currentStocks = currentStockRepo.findAll();
		Boolean flag = false;
		CurrentStock currentStock = new CurrentStock();
		for (CurrentStock item : currentStocks) {
			if (item.getProduct().equals(mainStock.getProduct())) {
				int quantity = 0;
				quantity = Integer.parseInt(mainStock.getQuantity()) + Integer.parseInt(item.getQuantity());
				item.setQuantity(Integer.toString(quantity));
				currentStockRepo.save(item);
				flag = true;

			}
		}

		if (!flag) {
			currentStock.setCategory(mainStock.getCategory());
			currentStock.setQuantity(mainStock.getQuantity());
			currentStock.setProduct(mainStock.getProduct());
			currentStock.setUnit(mainStock.getUnit());
			currentStock.setMainStock(mainStock);
			currentStockRepo.save(currentStock);

		}

		return "redirect:/mainStock";
	}

	@GetMapping("/editStock")
	public String getEdit(@RequestParam(required = false) Integer id, Model model) {
		MainStock mainStock = mainStockRepo.findById(id).get();
		model.addAttribute("mainStocks", mainStock);
		List<Category> categories = catRepo.findByIfDeleted("No");
		model.addAttribute("categories", categories);
		List<Product> products = prodRepo.findByIfDeleted("No");
		model.addAttribute("products", products);
		List<Unit> units = unitRepo.findByIfDeleted("No");
		model.addAttribute("units", units);

		return "editStock";
	}

	@PostMapping("/editStock")
	public String postEdit(@ModelAttribute MainStock mainStock) {
		mainStock.setIfDeleted("No");
		LocalDate date = LocalDate.now();
		String onlyDate = date.toString();
		mainStock.setDate(onlyDate);
		mainStockRepo.save(mainStock);
		return "redirect:/mainStock";
	}

	@GetMapping("/deleteStock")
	public String deleteStock(@RequestParam(required = false) Integer id) {
		MainStock mainStock = mainStockRepo.findById(id).get();
		mainStock.setIfDeleted("Yes");
		mainStockRepo.save(mainStock);
		return "redirect:/mainStock";
	}

}
