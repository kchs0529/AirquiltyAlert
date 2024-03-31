<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String spotCode = request.getParameter("staionCode");
	String spotNm = request.getParameter("stationName");
	String msg = request.getParameter("msg");

	out.println(msg);
%>   
