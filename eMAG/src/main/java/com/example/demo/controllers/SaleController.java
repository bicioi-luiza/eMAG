package com.example.demo.controllers;

import com.example.demo.dtos.SaleDTO;
import com.example.demo.services.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/sales")
public class SaleController {

    private final SaleService saleService;

    @Autowired
    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @GetMapping
    public ModelAndView getAllSales() {
        ModelAndView mav=new ModelAndView("sales");
        List<SaleDTO> dtos = saleService.getAllSales();
        mav.addObject("sales",dtos);
        return mav;
    }

    @GetMapping("/byId")
    public ModelAndView getUserById(@RequestParam("id") Long id) {
        return getSaleById(id, "sales");
    }

    @GetMapping("/saleUpdateDetails")
    public ModelAndView getUserByIdForUpdate(@RequestParam("id") Long id) {
        return getSaleById(id, "saleUpdate");
    }
    public ModelAndView getSaleById(@RequestParam("id") Long id,String viewName) {
        ModelAndView mav=new ModelAndView(viewName);
        try {
            SaleDTO saleDTO = saleService.getSaleById(id);
            mav.addObject("sales",saleDTO);
            return mav;
        } catch (Exception e) {
            return mav;
        }
    }

    /**
     *
     * @return
     */
    @GetMapping("/insertSale")
    public ModelAndView showCreateProductForm() {
        return new ModelAndView("saleCreate");
    }

    /**
     *
     * @param saleDTO
     * @return
     */
    @PostMapping("/insertSale")
    public ModelAndView createSale( @ModelAttribute SaleDTO saleDTO) {
        ModelAndView mav=new ModelAndView("saleCreate");
        try {
            SaleDTO createdSale = saleService.createSale(saleDTO);
            mav.addObject("sales",createdSale);
            return mav;
        } catch (Exception e) {
            mav.addObject("error","Eroare");
            return mav;
        }
    }
    @GetMapping("/updateSale")
    public ModelAndView showUpdateProductForm() {
        return new ModelAndView("saleUpdate");
    }

    /**
     *
     * @param id
     * @param saleDTO
     * @return
     */
    @PostMapping("/updateSale")
    public ModelAndView updateSale(@RequestParam("id") Long id, @Valid @ModelAttribute  SaleDTO saleDTO) {
        ModelAndView mav=new ModelAndView("saleUpdate");
        try {
            SaleDTO updatedSale = saleService.updateSale(id, saleDTO);
            mav.addObject("sales",updatedSale);
            return mav;
        } catch (Exception e) {
            mav.addObject("error","Sale cu id-ul respectiv nu exista");
            return mav;
        }
    }

    /**
     *
     * @return
     */
    @GetMapping("/deleteSale")
    public ModelAndView showDeleteSaleForm() {
        return new ModelAndView("saleDelete");
    }
    @PostMapping("/deleteSale")
    public ModelAndView deleteSale(@RequestParam("id") Long id) {
        ModelAndView mav=new ModelAndView("saleDelete");
        try {
            saleService.deleteSale(id);
            return  mav.addObject("ok","Reducerea cu id-ul respectiv s-a sters");
        } catch (Exception e) {
            return  mav.addObject("error","Reducerea  cu id-ul respectiv nu exista");
        }
    }
}
