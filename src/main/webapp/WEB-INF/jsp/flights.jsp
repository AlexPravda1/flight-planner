<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<style>
    <%@include file='/resources/css/table_flights.css' %>
</style>
<html>
<head>
    <title>Flight list</title>
    <%@ include file="/WEB-INF/parts/header.jsp" %>
</head>

<body>

<div class="table_flights">
    <div class="header">Flight list</div>
<table>
    <thead>
    <tr>
        <th>FLIGHT</th>
        <th>STD</th>
        <th>ADEP</th>
        <th>ADES</th>
        <th>STA</th>
        <th>CHECKLIST</th>
        <th>FILES</th>
        <th>NOTES</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${leonData}" var="leonFlights">
        <tr>
            <td><B>${leonFlights.flightNo} <B><BR><BR>
                <B>${leonFlights.acft.registration}<B><BR><BR>
                <c:choose>
                    <c:when test="${leonFlights.passengerList.countExcludingAnimals == 0}">
                        <B>FERRY</B>
                    </c:when>
                    <c:otherwise>
                        (${leonFlights.passengerList.countExcludingAnimals} PAX)
                    </c:otherwise>
                </c:choose>
            </td>

            <td><fmt:formatDate pattern = "dd-MMM" value = "${leonFlights.startTimeUtc}"/> <BR><BR>
                <fmt:formatDate pattern = "HH:ss" value = "${leonFlights.startTimeUtc}"/>Z</td>

            <td>${leonFlights.startAirport.code.icao}</td>

            <td>${leonFlights.endAirport.code.icao}</td>

            <td><fmt:formatDate pattern = "HH:ss" value = "${leonFlights.endTimeUtc}"/>Z <BR><BR>
                <fmt:formatDate pattern = "dd-MMM" value = "${leonFlights.endTimeUtc}"/></td>

            <td>
            <c:forEach items="${leonFlights.checklist.allItems}" var="checklist">
                <c:choose>

                    <c:when test="${checklist.definition.label == 'Handling (ADEP)'}">
                        <B>${checklist.definition.label}</B>:
                                                        <c:choose>
                                                            <c:when test="${(checklist.status.caption == 'Untouched')
                                                                            || (checklist.status.caption == '?')}">
                                                                <B><p2>MISSING</p2></B>
                                                            </c:when>

                                                            <c:when test="${(checklist.status.caption == 'Requested')}">
                                                                <B><p3>UNDER PROCESS</p3></B>
                                                            </c:when>

                                                            <c:otherwise>
                                                                ${checklist.status.caption}
                                                            </c:otherwise>
                                                        </c:choose>
                        <BR> <B>${leonFlights.legHandling.adepHandler.name}</B> <BR><BR>
                    </c:when>

                    <c:when test="${checklist.definition.label == 'Handling (ADES)'}">
                        <B>${checklist.definition.label}</B>:
                                                        <c:choose>
                                                            <c:when test="${(checklist.status.caption == 'Untouched')
                                                                            || (checklist.status.caption == '?')}">
                                                                <B><p2>MISSING</p2></B>
                                                            </c:when>

                                                            <c:when test="${(checklist.status.caption == 'Requested')}">
                                                                <B><p3>UNDER PROCESS</p3></B>
                                                            </c:when>

                                                            <c:otherwise>
                                                                ${checklist.status.caption}
                                                            </c:otherwise>
                                                        </c:choose>
                        <BR> <B>${leonFlights.legHandling.adesHandler.name}</B> <BR><BR>
                    </c:when>

                    <c:otherwise>
                        <B>${checklist.definition.label}</B>:
                                    <c:choose>
                                        <c:when test="${(checklist.status.caption == 'Untouched')
                                            || (checklist.status.caption == '?')}">
                                            <B><p2>MISSING</p2></B> <BR>
                                        </c:when>

                                        <c:when test="${(checklist.status.caption == 'Requested')}">
                                            <B><p3>UNDER PROCESS</p3></B> <BR>
                                        </c:when>

                                        <c:otherwise>
                                            ${checklist.status.caption} <BR>
                                        </c:otherwise>
                                     </c:choose>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
            </td>

            <td>
                <c:forEach items="${leonFlights.checklist.allItems}" var="services">
                    <c:if test="${(services.definition.label == 'Fuel Release')
                        && (services.status.caption != 'Not Applicable')
                        && (empty services.files)}" >
                        <B><p1>NO FUEL RELEASE</p1></B> <BR><BR>
                    </c:if>
                    <c:if test="${(services.definition.label == 'GAR sent (ADEP)')
                        && ((services.status.caption == 'Untouched') || (services.status.caption == '?'))}" >
                        <p1>${leonFlights.startAirport.code.icao} GAR!</p1><BR><BR>
                    </c:if>
                    <c:if test="${(services.definition.label == 'GAR sent (ADES)')
                        && ((services.status.caption == 'Untouched') || (services.status.caption == '?'))}" >
                        <p1>${leonFlights.endAirport.code.icao} GAR!</p1><BR><BR>
                    </c:if>

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
                    <b>OPS: </b>${leonFlights.notes.ops} <br><br>
                </c:if>

                <c:if test="${not empty leonFlights.notes.sales}" >
                    <b>SALES: </b>${leonFlights.notes.sales} <br><br>
                </c:if>

                <c:if test="${not empty leonFlights.trip.notes}" >
                    <b>TRIP: </b>${leonFlights.trip.notes} <br><br>
                </c:if>

                <c:if test="${not empty leonFlights.trip.tripNotes.tripSupplementaryInfo}" >
                    <b>SUPPLEMENTARY: </b>${leonFlights.trip.tripNotes.tripSupplementaryInfo} <br><br>
                </c:if>
            </td>
        </c:forEach>
        </tr>
    </tbody>
    </table>
    </div>
    </body>
    </html>

<%-- <c:out value="${empty leonFlights.notes.ops ?  'OPS XXX' : 'OPS: '.concat(leonFlights.notes.ops)}" /> --%>
<%-- <c:out value="${empty leonFlights.notes.sales ?  'SALES XXX' : 'SALES: '.concat(leonFlights.notes.sales)}" />--%>
<%-- <c:out value="${empty leonFlights.trip.notes ?  '' : 'TRIP: '.concat(leonFlights.trip.notes)}" /><br> --%>
