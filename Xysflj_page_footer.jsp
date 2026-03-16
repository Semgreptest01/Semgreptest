<%
}
catch(Exception e){
	XysfljParameters.redirectErrorPage(request, response, e);
}
finally{
	if(null != pageParameters){
		pageParameters.exit();
	}
}
%>
