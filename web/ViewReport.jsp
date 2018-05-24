<%@page import="Controllers.FileController"%>
<%@page import="Models.Libraries"%>
<%@page import="Controllers.SScannerController"%>
<%@page import="Models.RequirementsInfo"%>
<%@page import="java.util.Comparator"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Arrays"%>
<%@page import="ReportModels.Vulnerabilities"%>
<%@page import="ReportModels.Dependencies"%>
<%@page import="ReportModels.ReportDependency"%>
<%@page import="Models.CveObject"%>
<%@page import="Models.DependencyObject"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Models.ReportObject"%>
<%@page import="Controllers.ReportController"%>
<!DOCTYPE html>
<!--
To change this license header, choose License Headers in Project Properties.
To change this template file, choose Tools | Templates
and open the template in the editor.
-->
<html>
    <head>
        <title>Dependency Scanner Report</title>
        <link href="CSS/mystyle.css" rel="stylesheet" type="text/css">
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
        <script src="JavaScript/myscript.js"></script>
    </head>
    <body>
        <%
            String projectName = "";
            Dependencies[] deps = null;
            String md5 = null;
            String sha1 = null;
            ArrayList<String> scannedInformation = new ArrayList<String>();
            ArrayList<Libraries> missingLibraries = new ArrayList<Libraries>();
            ArrayList<Libraries> unaffectedLibraries = new ArrayList<Libraries>();
            ArrayList<String> hashValue = new ArrayList<String>();
            ArrayList<String> fileName = new ArrayList<String>();
            DependencyObject[] vulDependencyObject = null;
            DependencyObject[] notVulDependencyObject = null;
            Comparator cveCountComp = new DependencyObject.cveCountComparator();
            Comparator eviCountComp = new DependencyObject.evidenceCountComparator();
            Comparator cpeConfidenceComp = new DependencyObject.cpeConfidenceComparator();
            Comparator sevComparator = new DependencyObject.severityComparator();

            String cveorder = "asc";
            String evidenceorder = "asc";
            String cpeorder = "asc";
            String severityorder = "asc";

            try {
                ReportDependency reportSummary = ReportController.getReportObject(request.getParameter("filehash"));
                projectName = reportSummary.getProjectInfo().getName();
                scannedInformation = reportSummary.getScanInformation();
                vulDependencyObject = reportSummary.getDependencyDetail();
                notVulDependencyObject = reportSummary.getNotVulDependencyDetail();
                missingLibraries = reportSummary.getMissingLibraries();
                unaffectedLibraries = reportSummary.getUnaffectedLibraries();
                hashValue = FileController.getDirHashValue();
                fileName = FileController.getFileNameForScanner();

                Comparator selected = sevComparator;
                String option = request.getParameter("option");
                if (option != null) {
                    if (option.equals("evidence")) {
                        selected = eviCountComp;
                        evidenceorder = (request.getParameter("order").equals("asc")) ? "desc" : "asc";
                    } else if (option.equals("cve")) {
                        selected = cveCountComp;
                        cveorder = (request.getParameter("order").equals("asc")) ? "desc" : "asc";
                    } else if (option.equals("cpeconfidence")) {
                        selected = cpeConfidenceComp;
                        cpeorder = (request.getParameter("order").equals("asc")) ? "desc" : "asc";
                    } else {
                        severityorder = (request.getParameter("order").equals("asc")) ? "desc" : "asc";
                    }
                }

                String order = request.getParameter("order");
                if (order == null || order.equals("asc")) {
                    Arrays.sort(vulDependencyObject, selected);
                } else {
                    Arrays.sort(vulDependencyObject, selected.reversed());
                }

                deps = reportSummary.getDependencies();
            } catch (Exception e) {
                System.out.println("Error in ViewReport.jsp: " + e);
            }
        %>
    <center><div id="head-title">REPORT RESULT</div></center>
    <div class="download">
            <label id="date_of_report"><%=scannedInformation.get(1).replace("Report Generated", "Report Last Generated")%></label>
            <p><div class="tip"><a href="RequirementsContent.jsp?filehash=<%=request.getParameter("filehash")%>" download><img src="ImageIcon/arrow-download-icon.png">Download Requirements Report</a><span class="tip_text">Download report regarding requirement.txt</span></div></p>
            <p><div class="tip"><a href="Completion/<%=request.getParameter("filehash")%>/dependency-check-report.html" download><img src="ImageIcon/arrow-download-icon.png">Download OWASP Check Report</a><span class="tip_text">Download report from Open Web Application Security Project</span></div></p>
                    <%
                        if (hashValue.size() > 1) {
                            if (!request.getParameter("filename").equals(fileName.get(0))) {
                                Collections.reverse(hashValue);
                                Collections.reverse(fileName);
                    }%>
            <div>
                <p>Total project scanned: <%=" " + hashValue.size()%></p>
                <select name="forma" onchange="window.location.href = this.value">
                    <%for (int i = 0; i < hashValue.size(); i++) {%>
                    <option value="ViewReport.jsp?filehash=<%=hashValue.get(i)%>&filename=<%=fileName.get(i)%>&option=severity&order=asc"><%=fileName.get(i)%></option>
                    <%}
                    }%>
                </select>
            </div>
        </div>
    <div class="return"><a href="http://localhost:8080/DependencyCheck/"><img src="ImageIcon/return_icon.png"></a><span class="return_text">Return for more scanning.</span></div>

    <div style="padding-left: 50px;">

        <p><b>Project Name:</b> <%=projectName%></p>
        <p><%=scannedInformation.get(0)%></p><br/>
        <p><b>Scanned Information:</b></p>
        <p><%=scannedInformation.get(2)%></p>
        <p><%=scannedInformation.get(3)%></p>
        <p><%=scannedInformation.get(4)%></p>
        <p><%=scannedInformation.get(5)%></p>

        <%if (vulDependencyObject.length != 0) {%>
        <table id="table1" class="fixed" style="width: 95%">
            <col width="80px" />
            <col width="60px" />
            <col width="60px" />
            <col width="20px" />
            <col width="20px" />
            <col width="20px" />
            <col width="20px" />
            <tr>
                <th colspan="7" style="text-align: center; background-color:#f2f2f2;">Vulnerable Dependencies</th>
            </tr>
            <tr>
                <th style="text-align: center"><div class="tip">Dependency<span class="tip_text">The file name of the dependency scanned</span></div></th>
                <th style="text-align: center"><div class="tip">CPE<span class="tip_text">Common Platform Enumeration identifiers found.</span></th>
                <th style="text-align: center"><div class="tip">Coordinates<span class="tip_text">c</span></th>
                <th style="text-align: center"><div class="tip"><a class="order" href="ViewReport.jsp?filehash=<%=request.getParameter("filehash")%>&filename=<%=request.getParameter("filename")%>&option=severity&order=<%=severityorder%>">Highest Severity</a><span class="tip_text">The highest severity of any associated CVEs.</span></th>
                <th style="text-align: center"><div class="tip"><a class="order" href="ViewReport.jsp?filehash=<%=request.getParameter("filehash")%>&filename=<%=request.getParameter("filename")%>&option=cve&order=<%=cveorder%>">CVE Count</a><span class="tip_text">The number of associated CVEs for identified Dependency</span></th>
                <th style="text-align: center"><div class="tip"><a class="order" href="ViewReport.jsp?filehash=<%=request.getParameter("filehash")%>&filename=<%=request.getParameter("filename")%>&option=cpeconfidence&order=<%=cpeorder%>">CPE Confidence</a><span class="tip_text">The confidence rating dependency-check has for the identified CPE</span></th>
                <th style="text-align: center"><div class="tip"><a class="order" href="ViewReport.jsp?filehash=<%=request.getParameter("filehash")%>&filename=<%=request.getParameter("filename")%>&option=evidence&order=<%=evidenceorder%>">Evidence Count</a><span class="tip_text">The quantity of data extracted from the dependency that was used to identify the CPE</span></th>
            </tr>
            <%
                String name = null;
                String cvssScore = null;
                String severity = null;
                String description = null;
                String severityColor = null;
                for (int i = 0; i < vulDependencyObject.length; i++) {
            %>
            <tr>
                <td>
                    <a id="dependencyName" data-target="#myModal<%=i%>" data-toggle="modal" class="popping_modal" href=""><%=vulDependencyObject[i].getDependencyName()%></a>
                    <div class="modal fade" id="myModal<%=i%>" role="dialog">
                        <div class="modal-dialog">

                            <!-- Modal content-->
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                                    <h4 class="modal-title">Published Vulnerabilities</h4>
                                </div>
                                <div class="modal-body">
                                    <p><b>Dependency Name: </b><%=vulDependencyObject[i].getDependencyName()%></p>
                                    <p><b>Severity Level: </b><%=vulDependencyObject[i].getSeverityLevel()%></p>
                                    <p><b>CPE Confidence Level: </b><%=vulDependencyObject[i].getCpeConfidence()%></p>
                                    <p><b>Number of CVE: </b><%=vulDependencyObject[i].getCveCount()%></p>
                                    <%for (Dependencies dep : deps) {
                                            if (dep.getFileName().equals(vulDependencyObject[i].getDependencyName())) {
                                                md5 = dep.getMd5();
                                                sha1 = dep.getSha1();
                                    %>
                                    <p><b>MD5: </b><%=md5%></p>
                                    <p><b>SHA1: </b><%=sha1%></p>
                                    <%}
                                        }%>
                                    <hr>
                                    <%
                                        for (Dependencies dep : deps) {
                                            if (dep.getFileName().equals(vulDependencyObject[i].getDependencyName())) {
                                                Vulnerabilities[] vuls = dep.getVulnerabilities();
                                                if (vuls != null) {
                                                    //                                                    SortingController.severitySort(vuls, "desc");
                                                    Arrays.sort(vuls, Collections.reverseOrder());
                                                    for (Vulnerabilities vul : vuls) {

                                                        name = vul.getName();
                                                        cvssScore = vul.getCvssScore();
                                                        severity = vul.getSeverity();
                                                        if (severity.equals("High")) {
                                                            severityColor = "#FF0000";
                                                        } else if (severity.equals("Medium")) {
                                                            severityColor = "#CCCC00";
                                                        } else {
                                                            severityColor = "#808080";
                                                        }
                                                        description = vul.getDescription();
                                    %>
                                    <div class="modal-box">
                                        <p>Name: <a href="https://nvd.nist.gov/vuln/detail/<%=name%>" target="_blank"><%=name%></a></p>
                                        <p>CVSS Score: <%=cvssScore%></p>
                                        <p>Severity: <label style="color: <%=severityColor%>"><%=severity%></label></p>
                                        <p>Description: <%=description%></p>
                                    </div>

                                    <%
                                                    }
                                                }
                                            }
                                        }
                                    %>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                </div>
                            </div>

                        </div>
                    </div>
                </td>
                <!--<td>asdasd</td>-->
                <td><%=vulDependencyObject[i].getCpe()%></td>
                <td><%=vulDependencyObject[i].getCoordinates()%></td>
                <td><%=vulDependencyObject[i].getSeverityLevel()%></td>
                <td><%=vulDependencyObject[i].getCveCount()%></td>
                <td><%=vulDependencyObject[i].getCpeConfidence()%></td>
                <td><%=vulDependencyObject[i].getEvidenceCount()%></td>
            </tr>
            <%}%>
            <%}%>

        </table>
        <%-- @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ --%>
        <%-- @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ --%>
        <%-- @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ --%>
        <%-- @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ --%>
        <%-- @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ --%>

        <%if (notVulDependencyObject.length != 0) {%>
        <table id="table2" class="fixed" style="width:95%">
            <col width="80px" />
            <col width="60px" />
            <col width="60px" />
            <col width="20px" />
            <col width="20px" />
            <col width="20px" />
            <col width="20px" />
            <tr>
                <th colspan="7" style="text-align: center; background-color:#f2f2f2;">Not Vulnerable Dependencies</th>
            </tr>
            <tr>
                <th style="text-align: center"><div class="tip">Dependency<span class="tip_text">The file name of the dependency scanned</span></div></th>
                <th style="text-align: center"><div class="tip">CPE<span class="tip_text">Common Platform Enumeration identifiers found.</span></th>
                <th style="text-align: center"><div class="tip">Coordinates<span class="tip_text">c</span></th>
                <th style="text-align: center"><div class="tip"><a class="order" href="ViewReport.jsp?filehash=<%=request.getParameter("filehash")%>&option=severity&order=<%=severityorder%>">Highest Severity</a><span class="tip_text">The highest severity of any associated CVEs.</span></th>
                <th style="text-align: center"><div class="tip"><a class="order" href="ViewReport.jsp?filehash=<%=request.getParameter("filehash")%>&option=cve&order=<%=cveorder%>">CVE Count</a><span class="tip_text">The number of associated CVEs for identified Dependency</span></th>
                <th style="text-align: center"><div class="tip"><a class="order" href="ViewReport.jsp?filehash=<%=request.getParameter("filehash")%>&option=cpeconfidence&order=<%=cpeorder%>">CPE Confidence</a><span class="tip_text">The confidence rating dependency-check has for the identified CPE</span></th>
                <th style="text-align: center"><div class="tip"><a class="order" href="ViewReport.jsp?filehash=<%=request.getParameter("filehash")%>&option=evidence&order=<%=evidenceorder%>">Evidence Count</a><span class="tip_text">The quantity of data extracted from the dependency that was used to identify the CPE</span></th>
            </tr>
            </tr>
            <%
                String name = null;
                String cvssScore = null;
                String severity = null;
                String description = null;
                String severityColor = null;
                for (int i = 0; i < notVulDependencyObject.length; i++) {
            %>
            <tr>
            <tr>
                <td>
                    <a data-target="#myNotVulModal<%=i%>" data-toggle="modal" class="popping_modal" href=""><%=notVulDependencyObject[i].getDependencyName()%></a>
                    <div class="modal fade" id="myNotVulModal<%=i%>" role="dialog">
                        <div class="modal-dialog">

                            <!-- Modal content-->
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                                    <h4 class="modal-title">Published Vulnerabilities</h4>
                                </div>
                                <div class="modal-body">
                                    <p><b>Dependency Name: </b><%=notVulDependencyObject[i].getDependencyName()%></p>
                                    <p><b>Severity Level: </b><%=notVulDependencyObject[i].getSeverityLevel()%></p>
                                    <p><b>CPE Confidence Level: </b><%=notVulDependencyObject[i].getCpeConfidence()%></p>
                                    <p><b>Number of CVE: </b><%=notVulDependencyObject[i].getCveCount()%></p>
                                    <%for (Dependencies dep : deps) {
                                            if (dep.getFileName().equals(notVulDependencyObject[i].getDependencyName())) {
                                                md5 = dep.getMd5();
                                                sha1 = dep.getSha1();
                                    %>
                                    <p><b>MD5: </b><%=md5%></p>
                                    <p><b>SHA1: </b><%=sha1%></p>
                                    <%}
                                        }%>
                                    <hr>
                                    <%
                                        for (Dependencies dep : deps) {
                                            if (dep.getFileName().equals(notVulDependencyObject[i].getDependencyName())) {
                                                Vulnerabilities[] vuls = dep.getVulnerabilities();
                                                if (vuls != null) {
                                                    //                                                    SortingController.severitySort(vuls, "desc");
                                                    Arrays.sort(vuls, Collections.reverseOrder());
                                                    for (Vulnerabilities vul : vuls) {

                                                        name = vul.getName();
                                                        cvssScore = vul.getCvssScore();
                                                        severity = vul.getSeverity();
                                                        if (severity.equals("High")) {
                                                            severityColor = "#FF0000";
                                                        } else if (severity.equals("Medium")) {
                                                            severityColor = "#CCCC00";
                                                        } else {
                                                            severityColor = "#808080";
                                                        }
                                                        description = vul.getDescription();
                                    %>
                                    <div class="modal-box">
                                        <p>Name: <a href="https://nvd.nist.gov/vuln/detail/<%=name%>" target="_blank"><%=name%></a></p>
                                        <p>CVSS Score: <%=cvssScore%></p>
                                        <p>Severity: <label style="color: <%=severityColor%>"><%=severity%></label></p>
                                        <p>Description: <%=description%></p>
                                    </div>

                                    <%
                                                    }
                                                }
                                            }
                                        }
                                    %>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                </div>
                            </div>

                        </div>
                    </div>
                </td>
                <!--<td>asdasd</td>-->
                <td><%=notVulDependencyObject[i].getCpe()%></td>
                <td><%=notVulDependencyObject[i].getCoordinates()%></td>
                <td><%=notVulDependencyObject[i].getSeverityLevel()%></td>
                <td><%=notVulDependencyObject[i].getCveCount()%></td>
                <td><%=notVulDependencyObject[i].getCpeConfidence()%></td>
                <td><%=notVulDependencyObject[i].getEvidenceCount()%></td>
            </tr>
            <%}%>
            <%}%>
        </table>        
    </div>
    <jsp:include page="RequirementsContent.jsp">
        <jsp:param name="filehash" value="${param.filehash}"></jsp:param>
    </jsp:include>
</body>
</html>
