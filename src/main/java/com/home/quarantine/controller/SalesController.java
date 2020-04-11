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
import com.home.quarantine.model.Product;
import com.home.quarantine.model.Sales;
import com.home.quarantine.model.Unit;
import com.home.quarantine.repository.CategoryRepository;
import com.home.quarantine.repository.CurrentStockRepository;
import com.home.quarantine.repository.ProductRepository;
import com.home.quarantine.repository.SalesRepository;
import com.home.quarantine.repository.UnitRepository;

@Controller
public class SalesController {

	@Autowired
	private SalesRepository salesRepo;

	@Autowired
	private CategoryRepository catRepo;

	@Autowired
	private ProductRepository productRepo;

	@Autowired
	private UnitRepository unitRepo;

	@Autowired
	private CurrentStockRepository currentStockRepo;

	@GetMapping("/sales")
	public String sales(Model model) {
		List<Sales> sales = salesRepo.findAll();
		model.addAttribute("sales", sales);
		return "sales";
	}

	@GetMapping("/addSales")
	public String addSales(Model model, @RequestParam(required = false) Integer id) {
		Sales sale = new Sales();
		List<Category> categories = catRepo.findByIfDeleted("No");
		List<Unit> units = unitRepo.findByIfDeleted("No");

		if (id == null) {
			List<Product> products = productRepo.findByIfDeleted("No");
			model.addAttribute("products", products);
			model.addAttribute("categories", categories);
			model.addAttribute("units", units);
			model.addAttribute("sales", sale);

		} else {
			List<Product> products = productRepo.findByCategoryId(id);
			model.addAttribute("categories", categories);
			model.addAttribute("sales", sale);
			model.addAttribute("products", products);

			model.addAttribute("units", units);
			sale.setCategory(categories.stream().filter(c -> c.getId().equals(id)).findFirst().get());

		}
		return "addSales";
	}

	@PostMapping("/addSales")
	public String postAddSales(@ModelAttribute Sales sales, Model model) {
		String message = "";
		int a = 0;
		int b = 0;
		Boolean flag = false;
		LocalDate date = LocalDate.now();
		String onlyDate = date.toString();
		sales.setDate(onlyDate);
		List<Category> categories = catRepo.findByIfDeleted("No");
		List<Product> products = productRepo.findByIfDeleted("No");
		List<Unit> units = unitRepo.findByIfDeleted("No");
		List<Sales> mySales = salesRepo.findAll();

		List<CurrentStock> currentStocks = currentStockRepo.findAll();

		if (currentStocks.isEmpty()) {
			message = "Stock is empty";
			model.addAttribute("message", message);
			model.addAttribute("categories", categories);
			model.addAttribute("products", products);
			model.addAttribute("units", units);
			return "addSales";

		}

		for (CurrentStock item : currentStocks) {
			if (sales.getProduct().equals(item.getProduct()) && sales.getCategory().equals(item.getCategory())
					&& sales.getUnit().equals(item.getUnit())) {
				int quantity = 0;
				quantity = Integer.parseInt(item.getQuantity()) - Integer.parseInt(sales.getQuantity());
				if (quantity >= 0) {
					item.setQuantity(Integer.toString(quantity));
					currentStockRepo.save(item);

				} else {
					message = "Available Quantity : " + item.getQuantity() + " only";
					model.addAttribute("message", message);
					model.addAttribute("categories", categories);
					model.addAttribute("products", products);
					model.addAttribute("units", units);
					return "addSales";
				}

				b = 2;
			}

			a = 2;

		}
		if (b != 2) {
			message = "This doesnot belong in the stocks!!";
			model.addAttribute("message", message);
			model.addAttribute("categories", categories);
			model.addAttribute("products", products);
			model.addAttribute("units", units);
			return "addSales";
		}
		if (mySales.isEmpty()) {
			salesRepo.save(sales);
			return "redirect:/sales";
		}
		if (a != 2) {
			message = "This item doesnot belong in the stocks!!";
			model.addAttribute("message", message);
			model.addAttribute("categories", categories);
			model.addAttribute("products", products);
			model.addAttribute("units", units);
			return "addSales";

		} else {
			for (Sales aj : mySales) {
				if (sales.getProduct().equals(aj.getProduct()) && sales.getCategory().equals(aj.getCategory())
						&& sales.getUnit().equals(aj.getUnit()) && sales.getDate().equals(aj.getDate())) {
					System.out.println("222222222222 If vitra");
					int salesQuantity = 0;
					salesQuantity = Integer.parseInt(aj.getQuantity()) + Integer.parseInt(sales.getQuantity());
					aj.setQuantity(Integer.toString(salesQuantity));
					salesRepo.save(aj);
					flag = true;
					return "redirect:/sales";

				}
			}
			if (!flag) {
				salesRepo.save(sales);
				return "redirect:/sales";
			}
		}

		return "redirect:/sales";
	}

}
