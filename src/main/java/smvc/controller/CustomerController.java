package smvc.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import smvc.dao.CustomerDao;
import smvc.model.City;
import smvc.model.Customer;
import smvc.model.State;
import smvc.utils.ExcelGenerator;

@Controller
public class CustomerController {

	@Autowired
	private CustomerDao customerDao;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView listCustomer(HttpServletRequest request, ModelAndView model) throws IOException {

		//test1
		//test2
		String colname = request.getParameter("col");
		String orderby = request.getParameter("orderby");

		if (colname == null || orderby.trim().equals("") || orderby == null || orderby.trim().equals("")) {
			colname = "id";
			orderby = "asc";
		}

		int pageid = 1;
		int total = 4;

		int totalRecords = customerDao.getCount();
		int totalPages = totalRecords / total;
		if (totalRecords % total > 0) {
			totalPages++;
		}

		if (request.getParameter("pageno") != null)
			pageid = Integer.parseInt(request.getParameter("pageno"));

		if (pageid != 1)
			pageid = (pageid - 1) * total + 1;

		List<Customer> listCustomer = null;

		listCustomer = customerDao.list(colname, orderby, pageid, total);

		model.addObject("listCustomer", listCustomer);
		model.addObject("pageno", pageid);
		model.addObject("totalPages", totalPages);
		model.setViewName("home");

		return model;
	}

	@RequestMapping(value = "/newCustomer", method = RequestMethod.GET)
	public ModelAndView newCustomer(ModelAndView model) {
		Customer newCustomer = new Customer();
		List<State> listofStates = customerDao.getStates();
		model.addObject("states", listofStates);
		model.addObject("customer", newCustomer);
		model.setViewName("customerForm");
		return model;
	}

	@RequestMapping(value = "/saveCustomer", method = RequestMethod.POST)
	public ModelAndView saveCustomer(@ModelAttribute Customer customer) {
		if (customer.getPincode().isEmpty())
			customer.setPincode(null);
		customerDao.saveOrUpdate(customer);
		return new ModelAndView("redirect:/");
	}

	@RequestMapping(value = "/editCustomer", method = RequestMethod.GET)
	public ModelAndView editCustomer(HttpServletRequest request) {
		int customerId = Integer.parseInt(request.getParameter("id"));
		Customer customer = customerDao.get(customerId);
		ModelAndView model = new ModelAndView("customerForm");
		List<State> listofStates = customerDao.getStates();
		List<City> listOfCities = customerDao.getCities(customer.getState().getId());
		model.addObject("states", listofStates);
		model.addObject("customer", customer);
		model.addObject("cities", listOfCities);
		return model;
	}

	@RequestMapping(value = "/deleteCustomer", method = RequestMethod.GET)
	public ModelAndView deleteContact(HttpServletRequest request) {
		int customerId = Integer.parseInt(request.getParameter("id"));
		customerDao.delete(customerId);
		return new ModelAndView("redirect:/");
	}

	@RequestMapping(value = "/getCities", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<City> getCities(@RequestParam("state") String state) {

		List<City> cities = customerDao.getCities(state);

		return cities;
	}

	@RequestMapping(value = "/searchCustomer", method = RequestMethod.GET)
	public ModelAndView searchCustomer(HttpServletRequest request, ModelAndView model) {

		String colName = request.getParameter("colName");
		String searchValue = request.getParameter("searchValue");
		String col = request.getParameter("col");
		String orderby = request.getParameter("orderby");

		if (col == null || orderby.trim().equals("") || orderby == null || orderby.trim().equals("")) {
			col = "id";
			orderby = "asc";
		}

		int pageid = 1;
		int total = 4;

		int totalRecords = customerDao.getTotalSearchRecords(colName, searchValue);
		int totalPages = totalRecords / total;
		if (totalRecords % total > 0) {
			totalPages++;
		}

		if (request.getParameter("pageno") != null)
			pageid = Integer.parseInt(request.getParameter("pageno"));

		if (pageid != 1)
			pageid = (pageid - 1) * total + 1;

		List<Customer> listCustomer = null;

		listCustomer = customerDao.getSearchRecords(colName, searchValue, pageid, total, col, orderby);

		model.addObject("listCustomer", listCustomer);

		model.addObject("pageno", pageid);
		model.addObject("totalPages", totalPages);
		model.addObject("colName", colName);
		model.addObject("searchValue", searchValue);
		model.setViewName("home");

		return model;
	}

	@RequestMapping(value = "/exportToExcel", method = RequestMethod.GET)
    public void exportIntoExcelFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=student" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
        
        String colName = request.getParameter("colName");
		String searchValue = request.getParameter("searchValue");
		String col = request.getParameter("col");
		String orderby = request.getParameter("orderby");

		if (col == null || orderby.trim().equals("") || orderby == null || orderby.trim().equals("")) {
			col = "id";
			orderby = "asc";
		}

		int pageid = 1;
		int totalRecords = customerDao.getTotalSearchRecords(colName, searchValue);

		List<Customer> listCustomer = null;

		listCustomer = customerDao.getSearchRecords(colName, searchValue, pageid, totalRecords, col, orderby);
		
        ExcelGenerator generator = new ExcelGenerator(listCustomer);
        generator.generateExcelFile(response);
    }

}
