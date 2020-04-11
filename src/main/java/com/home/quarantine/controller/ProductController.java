package com.home.quarantine.controller;

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
import com.home.quarantine.repository.CategoryRepository;
import com.home.quarantine.repository.CurrentStockRepository;
import com.home.quarantine.repository.ProductRepository;

@Controller
public class ProductController {

	@Autowired
	private ProductRepository productRepo;

	@Autowired
	private CategoryRepository cateRepo;

	@Autowired
	private CurrentStockRepository currentStockRepo;

	@GetMapping("/allProducts")
	public String getAllProducts(Model model) {
		List<Product> products = productRepo.findByIfDeleted("No");
		model.addAttribute("productsTable", products);
		return "allProducts";
	}

	@GetMapping("/addProduct")
	public String getAddProduct(Model model) {
		model.addAttribute("products", new Product());
		List<Category> myCategories = cateRepo.findByIfDeleted("No");
		model.addAttribute("categories", myCategories);
		return "addProduct";
	}

	@PostMapping("/addProduct")
	public String postAddProduct(Model model, @ModelAttribute Product product) {
		Boolean flag = false;
		String productName = product.getProductName();
		List<Product> products = productRepo.findByIfDeleted("No");
		for (Product item : products) {
			if (productName.equals(item.getProductName())) {
				flag = true;
			}
		}
		if (!flag) {

			product.setIfDeleted("No");
			productRepo.save(product);
			return "redirect:/allProducts";
		} else {
			String message = "This Product already exists!!";
			model.addAttribute("message", message);
			model.addAttribute("products", new Product());
			List<Category> myCategories = cateRepo.findByIfDeleted("No");
			model.addAttribute("categories", myCategories);
			return "addProduct";
		}
	}

	@GetMapping("/editProduct")
	public String getEditProduct(Model model, @RequestParam(required = false) Integer id) {
		model.addAttribute("products", productRepo.findById(id).get());
		List<Category> myCategories = cateRepo.findByIfDeleted("No");
		model.addAttribute("categories", myCategories);
		return "editProduct";
	}

	@PostMapping("/editProduct")
	public String postEditProduct(@ModelAttribute Product product) {
		product.setIfDeleted("No");
		productRepo.save(product);
		return "redirect:/allProducts";
	}

	@GetMapping("/deleteProduct")
	public String deleteProduct(Model model, @RequestParam(required = false) Integer id) {
		Product product = productRepo.findById(id).get();
		Boolean flag = false;
		List<CurrentStock> currentStocks = currentStockRepo.findAll();
		for (CurrentStock item : currentStocks) {
			if (product.getProductName().equals(item.getProduct().getProductName())) {
				String message = "Product still exists in Current Stock";
				flag = true;
				List<Product> productsNew = productRepo.findByIfDeleted("No");
				model.addAttribute("productsTable", productsNew);
				model.addAttribute("message", message);
				return "allProducts";
			}
		}
		if (!flag) {

			product.setIfDeleted("Yes");
			productRepo.save(product);
		}
		return "redirect:/allProducts";
	}
}
