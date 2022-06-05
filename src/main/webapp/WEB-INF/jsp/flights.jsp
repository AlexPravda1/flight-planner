<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<style>
    <%@include file='/resources/css/table_dark.css' %>
</style>
<html>
<head>
    <title>Flight list</title>
    <%@ include file="/WEB-INF/parts/header.jsp" %>
</head>

<body>

<h4 class="table_dark">
    Here you have flight list:
</h4>

<table class="table_dark">
    <thead>
    <tr>
        <th>FLIGHT</th>
        <th>DATE</th>
        <th>ADEP</th>
        <th>ADES</th>
        <th>ACFT</th>
        <th>FILES</th>
        <th>NOTES</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${leonData}" var="leonFlights">
        <tr>
            <td>${leonFlights.flightNo}</td>
            <td><fmt:formatDate pattern = "dd-MMM HH:ss" value = "${leonFlights.startTimeUtc}"/>Z</td>
            <td>${leonFlights.startAirport.code.icao}</td>
            <td>${leonFlights.endAirport.code.icao}</td>
            <td>${leonFlights.acft.registration}</td>

            <td>
            <c:forEach items="${leonFlights.checklist.allItems}" var="services">
                <c:if test="${not empty services.files}" >
                        <c:forEach items="${services.files}" var="file">
                            <c:if test="${not empty file.fileName}" >
                                <a href="${file.signedUrl}" target="_blank"><b>${services.definition.label}</b></a>
                            </c:if>
                        </c:forEach>
                        <br><br>
                </c:if>
            </c:forEach>
            </td>

            <td>
                <c:if test="${not empty leonFlights.notes.ops}" >
                    <b>OPS: </b>${leonFlights.notes.ops} <br>
                </c:if>

                <c:if test="${not empty leonFlights.notes.sales}" >
                    <b>SALES: </b>${leonFlights.notes.sales} <br>
                </c:if>

                <c:if test="${not empty leonFlights.trip.notes}" >
                    <b>TRIP: </b>${leonFlights.trip.notes} <br>
                </c:if>

                <c:if test="${not empty leonFlights.trip.tripNotes.tripSupplementaryInfo}" >
                    <b>SUPPLEMENTARY: </b>${leonFlights.trip.tripNotes.tripSupplementaryInfo} <br>
                </c:if>
      </td>
    </c:forEach>
    </tbody>
    </table>
    </body>
    </html>

<%-- <c:out value="${empty leonFlights.notes.ops ?  'OPS XXX' : 'OPS: '.concat(leonFlights.notes.ops)}" /> --%>
<%-- <c:out value="${empty leonFlights.notes.sales ?  'SALES XXX' : 'SALES: '.concat(leonFlights.notes.sales)}" />--%>
<%-- <c:out value="${empty leonFlights.trip.notes ?  '' : 'TRIP: '.concat(leonFlights.trip.notes)}" /><br> --%>
<%-- <c:out value="${empty leonFlights.trip.tripNotes.tripSupplementaryInfo ?  '' : 'TRIP SUPP: '.concat(leonFlights.trip.tripNotes.tripSupplementaryInfo)}" />--%>
