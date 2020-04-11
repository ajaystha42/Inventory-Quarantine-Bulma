package com.home.quarantine.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.home.quarantine.model.Category;
import com.home.quarantine.model.CurrentStock;
import com.home.quarantine.repository.CategoryRepository;
import com.home.quarantine.repository.CurrentStockRepository;

@Controller
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class CategoryController extends GlobalMethodSecurityConfiguration {

	@Autowired
	private CategoryRepository cateRepo;

	@Autowired
	private CurrentStockRepository currentStockRepo;

	@Secured("ajay")
	@GetMapping("/allCategory")
	public String getAllCategories(Model model) {
		List<Category> categories = cateRepo.findByIfDeleted("No");
		model.addAttribute("categoriesTable", categories);
		return "allCategory";
	}

	@GetMapping("/addCategory")
	public String addCat(Model model) {
		model.addAttribute("categories", new Category());
		return "addCategory";
	}

	@PostMapping("/addCategory")
	public String postAddCat(Model model, @ModelAttribute Category category) {
		Boolean flag = false;
		String catName = category.getCategoryName();
		List<Category> categories = cateRepo.findByIfDeleted("No");
		for (Category item : categories) {
			if (catName.equals(item.getCategoryName())) {
				flag = true;
			}
		}
		if (flag == false) {
			category.setIfDeleted("No");
			cateRepo.save(category);
			return "redirect:/allCategory";
		} else {
			String message = "This Category already exists!!";
			model.addAttribute("message", message);
			model.addAttribute("categories", new Category());
			return "addCategory";
		}
	}

	@GetMapping("/editCategory")
	public String editCat(@RequestParam(required = false) Integer id, Model model) {
		model.addAttribute("categories", cateRepo.findById(id).get());
		return "editCategory";
	}

	@PostMapping("/editCategory")
	public String postEditCat(@ModelAttribute Category cat) {
		cat.setIfDeleted("No");
		cateRepo.save(cat);
		return "redirect:/allCategory";
	}

	@GetMapping("/deleteCategory")
	public String deleteCat(Model model, @RequestParam(required = false) Integer id) {
		Category category = cateRepo.findById(id).get();
		Boolean flag = false;
		List<CurrentStock> currentStocks = currentStockRepo.findAll();
		List<Category> categories = cateRepo.findByIfDeleted("No");
		for (CurrentStock item : currentStocks) {
			if (category.getCategoryName().equals(item.getCategory().getCategoryName())) {
				String message = "Category still exists in current stock";
				flag = true;
				model.addAttribute("categoriesTable", categories);
				model.addAttribute("message", message);
				return "allCategory";
			}

		}

		if (flag == false) {

			category.setIfDeleted("Yes");
			cateRepo.save(category);

		}
		return "redirect:/allCategory";
	}

}
