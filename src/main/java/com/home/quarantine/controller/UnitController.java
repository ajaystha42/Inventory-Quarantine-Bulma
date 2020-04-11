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
import com.home.quarantine.model.Unit;
import com.home.quarantine.repository.CurrentStockRepository;
import com.home.quarantine.repository.UnitRepository;

@Controller
public class UnitController {

	@Autowired
	private UnitRepository unitRepo;

	@Autowired
	private CurrentStockRepository currentStockRepo;

	@GetMapping("/allUnits")
	public String allUnits(Model model) {
		List<Unit> units = unitRepo.findByIfDeleted("No");
		model.addAttribute("unitTable", units);
		return "allUnits";
	}

	@GetMapping("/addUnit")
	public String addUnits(Model model) {
		model.addAttribute("units", new Unit());
		return "addUnit";
	}

	@PostMapping("/addUnit")
	public String postUnits(Model model, @ModelAttribute Unit unit) {
		Boolean flag = false;
		String unitName = unit.getUnitName();
		List<Unit> units = unitRepo.findByIfDeleted("No");
		for (Unit item : units) {
			if (unitName.equals(item.getUnitName())) {
				flag = true;
			}
		}
		if (!flag) {
			unit.setIfDeleted("No");
			unitRepo.save(unit);
			return "redirect:/allUnits";
		} else {
			String message = "This Unit already exists!!";
			model.addAttribute("message", message);
			model.addAttribute("units", new Unit());

			return "addUnit";
		}
	}

	@GetMapping("/deleteUnit")
	public String deleteUnit(Model model, @RequestParam(required = false) Integer id) {
		Unit unit = unitRepo.findById(id).get();
		Boolean flag = false;
		List<CurrentStock> currentStocks = currentStockRepo.findAll();
		List<Unit> units = unitRepo.findByIfDeleted("No");
		for (CurrentStock item : currentStocks) {
			if (unit.getUnitName().equals(item.getUnit().getUnitName())) {
				String message = "Unit still exists in Current Stocks";
				model.addAttribute("unitTable", units);
				model.addAttribute("message", message);
				flag = true;
				return "allUnits";
			}

		}
		if (!flag) {
			unit.setIfDeleted("Yes");
			unitRepo.save(unit);
		}

		return "redirect:/allUnits";
	}

	@GetMapping("/editUnit")
	public String getEditUnit(@RequestParam(required = false) Integer id, Model model) {
		model.addAttribute("units", unitRepo.findById(id).get());
		return "editUnit";
	}

	@PostMapping("/editUnit")
	public String postEditUnit(@ModelAttribute Unit unit) {
		unit.setIfDeleted("No");
		unitRepo.save(unit);
		return "redirect:/allUnits";
	}
}
