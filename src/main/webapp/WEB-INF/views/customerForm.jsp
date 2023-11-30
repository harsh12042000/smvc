<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<title>New/Edit Customer</title>
<style>
	.no-wrap {
		white-space: nowrap;
	}
</style>
<script>


/*function disable(link){
    var a=document.getElementById("link");
    a.setAttribute("class","disabled");
    a.setAttribute("style","color:black;pointer-events:none;");
    
    link.onclick = function(event) {
        event.preventDefault();
    }
}

function enable() {
	var a = document.getElementById("link");
	a.setAttribute("href", "https://www.google.com/");
}*/

function dobValidation() {

	var dob = $('#dob').val();
    var dobDate = new Date(dob);
    var now = new Date();
    
    var age = now.getFullYear() - dobDate.getFullYear();

    if(age < 18) {
    	alert("Age should be greater than 18");
    	$('#dob').focus();
    	return false;
    } 
}

function onChangeGetCities()
{
    var name = $('#state').val();
    
    $.ajax({
        type: "GET",
        url : 'getCities',
        data: ({
            state: name
        }),
        success : function(data) {
        	//console.log(data);
			$('#city').empty();
        	$('#city').append('<option value="">Select City</option>');
 
        	$.each(data, function(index, value) {
        		$('#city').append('<option value="' + value.id + '">' + value.city + '</option>');
        	});
        }
    });
}

function getStateFromPincode() {
	var pin = $('#pincode').val();
	
	/*
	var a=document.getElementById("link");
    a.setAttribute("class","disabled");
    a.setAttribute("style","color:black;pointer-events:none;"); */
    
    /*var link = document.getElementById('link');
    link.onclick = function(event) {
        event.preventDefault();
        alert("something changed!");
    }*/
	
	$.getJSON("https://api.postalpincode.in/pincode/" + pin, function(data) {
		if (data[0].PostOffice && data[0].PostOffice.length) {
			$('#stateFromPincode').empty();
			$('#stateFromPincode').append('<p>State : ' + data[0].PostOffice[0].State + '</p>');
			$('#stateFromPincode').append('<p>City : ' + data[0].PostOffice[0].Block + '</p>');
		} else {
			alert('Enter Valid Pincode');
		}
	})
}

</script>
</head>
<body>
    <div class="container">
    
	    <!--<a href="javascript:void(0);" onclick="alert('hello')" id="link">www.google.com</a>
	    <button onclick="disable()" >Disable</button> -->
    
        <h1 style="text-align: center;" class="mb-3">Enter Your Details</h1>
        <form:form action="saveCustomer" method="post" modelAttribute="customer" onsubmit="return dobValidation()">
			<div class="row">
				<form:hidden path="id"/>
				<div class="col-md-6">
		        	<div class="form-group row col-12">
            			<label class="col-4 col-form-label" >First Name:</label>
            			<div class="col-8 mb-3">
            				<form:input path="firstName" class="form-control" required="required" />
            			</div>
        			</div>
        			
        			<div class="form-group row col-12" style="display: flex;">
            			<label class="col-4 col-form-label">Last Name:</label>
            			<div class="col-8 mb-3">
            				<form:input path="lastName" class="form-control" required="required"/>
            			</div>
        			</div>
        			
        			<div class="form-group row col-12" style="display: flex; ">
					    <label class="col-4 col-form-label" >Gender:</label>
					    <div class="col-8 mb-3">
						    <form:select path="gender" class="form-control" required="required">
						        <form:option value="" label="--Select--" />
						        <form:option value="M" label="Male" />
						        <form:option value="F" label="Female" />
						        <form:option value="O" label="Other" />
						    </form:select>
						 </div>
					</div>
					
					<div class="form-group row col-12" style="display: flex;">
            			<label class="col-4 col-form-label" >Email:</label>
            			<div class="col-8 mb-3">
            				<form:input path="email" type="email" class="form-control"  required="required" pattern="[a-z0-9._%+\-]+@[a-z0-9.\-]+\.[a-z]{2,}$"/>
        				</div>
        			</div>
					
					<div class="form-group row col-12" style="display: flex;">
            			<label class="col-4 col-form-label" >Mobile No:</label>
            			<div class="col-8 mb-3">
            				<form:input path="mobileNo" type="tel" class="form-control" required="required" pattern="[7-9]{1}[0-9]{9}"/>
        				</div>
        			</div>
        			
        			<div class="form-group row col-12" style="display: flex;">
            			<label class="col-4 col-form-label" >Date of Birth:</label>
            			<div class="col-8 mb-3">
            				<form:input type="date" id="dob" name="dob" path="dob" class="dob form-control"  required="required"/>
        				</div>
        			</div>
        			
        			<div class="form-group row col-12" style="display: flex;">
            			<label class="col-4 col-form-label" >Website URL :</label>
            			<div class="col-8 mb-3">
            				<form:input path="url" type="url" class="form-control" required="required" pattern="https?://.+"/>
        				</div>
        			</div>
		    	</div>
		
		    	<div class="col-md-6">
					<div class="form-group row col-12">
					    <label class="col-4 col-form-label" >Address 1:</label>
					    <div class="col-8 mb-3">
					    	<form:input path="address1" class="form-control" />
						</div>
					</div>
					
					<div class="form-group row col-12">
					    <label class="col-4 col-form-label">Address 2:</label>
					    <div class="col-8 mb-3">
					    	<form:input path="address2" class="form-control" />
						</div>
					</div>
					
					<div class="form-group row col-12">
					    <label class="col-4 col-form-label">Address 3:</label>
					    <div class="col-8 mb-3">
					    	<form:input path="address3" class="form-control" />
						</div>
					</div>
					
					<div class="form-group row col-12">
					    <label class="col-4 col-form-label">State: </label>
						<div class="col-8 mb-3">
							<form:select class="form-control" path="state.id" id="state" onchange="onChangeGetCities()"
								aria-label="Default select example" >
								<form:option value="">Select State</form:option>
								<form:options items="${states}" itemLabel="name" itemValue="id" />
							</form:select>
						</div>
					</div>

					<div class="form-group row col-12">
					    <label class="col-4 col-form-label">City:</label>
					    <div class="col-8 mb-3">
						    <form:select class="form-control"   id="city" path="city.id" onchange="onChangeGetPincode">
	        					<form:option value="">Select City</form:option>
	        					<form:options items="${cities}" itemLabel="city" itemValue="id"/>
	    					</form:select>
	    				</div>
					</div>
					
					<div class="form-group row col-12">
					    <label class="col-4 col-form-label">Pincode: </label>
					    <div class="col-8 mb-3">
					    	<form:input type="text" path="pincode" class="form-control" id="pincode" onblur="getStateFromPincode()"/>
						</div>
					</div>
					
					<div class="form-group row col-12">
					    <label class="col-4"></label>
					    <div class="col-8 mb-3">
					    	<p id="stateFromPincode"></p>
						</div>
					</div>
		    	</div>
			</div>
		
			<div style="display: flex; justify-content: center; margin-top: 20px; ">
		    	<button type="submit" class="btn btn-primary">Submit</button>
		    	<a class="btn btn-secondary" href="../crudmvc" style="margin-left: 1%">Cancel</a>
			</div>
			
        </form:form>
        
    </div>
</body>
</html>

