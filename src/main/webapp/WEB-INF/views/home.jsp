<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
 
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Customer Manager</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
        <style>
        	.nowrap {
    			white-space: nowrap;
			}
        </style>
    </head>
    <body>
    	<c:if test="${not empty param['pageno']}">
			<c:set var="pageno" value="${param['pageno']}"/>        
        </c:if>
        <c:if test="${empty param['pageno']}">
			<c:set var="pageno" value="1"/>        
        </c:if>
        <c:set var="sortField" value="id"/>
        <c:set var="orderField" value="asc"/>
        
        <c:if test="${not empty param['col']}">
		<c:set var="sortField" value="${param['col']}"/>        
        </c:if>
        <c:if test="${not empty param['orderby']}">
        <c:set var="orderField" value="${param['orderby']}"/>
        </c:if>
        
        <c:choose>
		    <c:when test="${not empty param['colName'] && not empty param['searchValue']}">
		        <c:set var="url" value="searchCustomer?colName=${colName}&searchValue=${searchValue}"/>
		    </c:when>
		    <c:otherwise>
		        <c:set var="url" value="?colName=${colName}&searchValue=${searchValue}"/>
		    </c:otherwise>
		</c:choose>
        
        <div align="center" class="container">
            <h1>Customer List</h1>
            <h3><a href="newCustomer">New Customer</a></h3>
            <div class="row mt-5">
			    <div class="col-md-6">
			        <form action="searchCustomer" method="get">
			            <div class="form-group row">
			                <div class="col-md-3">
			                    <select name="colName" id="colName" class="form-control" required="required">
								    <option value="">Select Column</option>
								    <option value="name" <c:if test="${colName eq 'name'}">selected="selected"</c:if>>Name</option>
								    <option value="email" <c:if test="${colName eq 'email'}">selected="selected"</c:if>>Email</option>
								    <option value="sex" <c:if test="${colName eq 'sex'}">selected="selected"</c:if>>Gender</option>
								</select>
			                </div>
			                <div class="col-md-4">
			                    <input type="text" value="${searchValue}" name="searchValue" class="form-control" required="required"/>
			                </div>
			                <div class="col-md-3">
			                	<input type="submit" value="Search" class="btn btn-primary"/>
			                	<a class="btn btn-secondary" href="../crudmvc">Reset</a>
			                </div>
			                <div class="col-md-2">
			                	<a class="btn btn-secondary" href="exportToExcel?colName=${colName}&searchValue=${searchValue}&col=${sortField}&orderby=${orderField}">Export</a>
			                </div>
			            </div>
			        </form>
			    </div>
			</div>
            
            <div class="table-responsive">
            <table border="1" class="table mt-5  table-striped">
            	<th>ID
            		<a href="${url}&col=id&orderby=asc&pageno=${pageno}">↑</a> 
                	<a href="${url}&col=id&orderby=desc&pageno=${pageno}">↓</a> 
            	</th>
                <th>Name 
                	<a href="${url}&col=first_name&orderby=asc&pageno=${pageno}">↑</a> 
                	<a href="${url}&?col=first_name&orderby=desc&pageno=${pageno}">↓</a> 
                </th>
                <th>Email
                	<a href="${url}&?col=email&orderby=asc&pageno=${pageno}">↑</a> 
                	<a href="${url}&?col=email&orderby=desc&pageno=${pageno}">↓</a> 
                </th>
                <th>Gender</th>
                <th>Mobile Number</th>
                <th>DOB</th>
                <th>Age</th>
                <th>URL</th>
                <th>State</th>
                <th>City</th>
                <th>Pincode</th>
                <th>Action</th>
                <c:forEach var="customer" items="${listCustomer}" varStatus="status">
                <tr id="customer-row">
                	<td>${customer.id}</td>
                    <td class="nowrap">${customer.firstName} ${customer.lastName}</td>
                    <td>${customer.email}</td> 
                    <c:choose>
						<c:when test="${customer.gender eq 'M'}">
							<c:set var="gender" value="Male"/>
						</c:when>
						<c:when test="${customer.gender eq 'F'}">
							<c:set var="gender" value="Female"/>
						</c:when>
						<c:otherwise>
							<c:set var="gender" value="Other"/>
						</c:otherwise>
					</c:choose>
                    <td>${gender}</td> 
                    <td>${customer.mobileNo}</td> 
                    <td class="nowrap">${customer.dob}</td> 
                    
                    <jsp:useBean id="currentDate" class="java.util.Date" />
     
            		<fmt:parseDate value="${customer.dob}" var="dob" pattern="yyyy-MM-dd"/>
		            <fmt:formatDate var="birthYear" value="${dob}" pattern="yyyy" />
		            <fmt:formatDate var="currentYear" value="${currentDate}" pattern="yyyy" />
		            <c:set var="age" value="${currentYear - birthYear}"/>
            		
                    <td>${age}</td>
                    <td><a href="${customer.url}" target="_blank">${customer.url}</a></td> 
                    <td>${customer.state.name}</td>
                    <td>${customer.city.city}</td> 
                    <td>${customer.pincode}</td>  
                    <td class="nowrap">
                        <a href="editCustomer?id=${customer.id}"><img 
                        width="16px" src='<c:url value="/images/edit_icon.png"></c:url>' /></a>
                        &nbsp;&nbsp;
                        <a href="deleteCustomer?id=${customer.id}" 
                        onclick="return confirm('Are you sure to delete?')"><img 
                        width="16px" src='<c:url value="/images/trash.png"></c:url>' /></a>
                    </td>      
                </tr>
                </c:forEach>             
            </table>
            </div>
        <div>
        
		<ul class="pagination" style="float: right">
   			<c:forEach var="i" begin="1" end="${totalPages}">
       			<c:choose>
       				<c:when test="${pageno == i}">
               			<li class="page-item disabled"><a class="page-link" href="${url}&col=${sortField}&orderby=${orderField}&pageno=${i}">${i}</a></li>
           			</c:when>
           			<c:otherwise>
               			<li class="page-item"><a class="page-link" href="${url}&col=${sortField}&orderby=${orderField}&pageno=${i}">${i}</a></li>
           			</c:otherwise>

       			</c:choose>
   			</c:forEach>
		</ul>
		</div>
        </div>
    </body>
</html>