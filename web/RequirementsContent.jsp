<%-- 
    Document   : tableContent
    Created on : 7 Mar, 2018, 5:31:42 PM
    Author     : Eugene Tan
--%>

<%@page import="Models.RequirementsInfo"%>
<%@page import="Models.DependencyObject"%>
<%@page import="Models.Libraries"%>
<%@page import="java.util.ArrayList"%>
<%@page import="ReportModels.Dependencies"%>
<%@page import="Controllers.SScannerController"%>
<%@page import="Controllers.ReportController"%>
<%@page import="ReportModels.ReportDependency"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<head>
    <title>Dependency Scanner Report</title>
    <link href="CSS/mystyle.css" rel="stylesheet" type="text/css">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="JavaScript/myscript.js"></script>
    <style type="text/css">
        /*        table th, td {
                    *margin-top: 10px;
                    padding: 0 20px;
                    text-align: center;
                    border: 1px solid black;
                    border-collapse: collapse;
                }*/
        #table3 th, td{
            margin-top: 15px;
            padding: 0 20px;
            text-align: center;
            border: 1px solid black;
            border-collapse: collapse;

        }
        #table4{
            margin-top: 15px;
            padding: 0 20px;
            text-align: center;
            border: 1px solid black;
            border-collapse: collapse;
        }
        #table5{
            margin-top: 15px;
            padding: 0 20px;
            text-align: center;
            border: 1px solid black;
            border-collapse: collapse;
        }
        #result-scanning{
            margin-top: 30px;
        }
        .req-table{
            text-align: center;
        }
        .submain-container{
            margin-left: 50px;
            margin-right: 20px;
        }
    </style>
</head>

<%
    String projectName = "";
    Dependencies[] deps = null;
    ArrayList<String> scannedInformation = new ArrayList<String>();
    ArrayList<Libraries> missingLibraries = new ArrayList<Libraries>();
    ArrayList<Libraries> unaffectedLibraries = new ArrayList<Libraries>();
    RequirementsInfo[] requirementsInfo = null;
    int affectedPackageCount = 0;
    int missingPackageCount = 0;
    int unaffectedPackageCount = 0;
    String packageNumber = null;
    String packageCount = "0";
    try {
        ReportDependency reportSummary = ReportController.getReportObject(request.getParameter("filehash"));
        projectName = reportSummary.getProjectInfo().getName();
        scannedInformation = reportSummary.getScanInformation();
        requirementsInfo = reportSummary.getRequirementsInfo();
        missingLibraries = reportSummary.getMissingLibraries();
        unaffectedLibraries = reportSummary.getUnaffectedLibraries();
        packageNumber = reportSummary.getPackageNumber();

        deps = reportSummary.getDependencies();

        reportSummary = ReportController.getReportObject(request.getParameter("filehash"));
        affectedPackageCount = 0;
        if (reportSummary.getRequirementsInfo() != null) {
            affectedPackageCount = reportSummary.getRequirementsInfo().length;
        }
        if (packageNumber != null) {
            packageCount = packageNumber;
        }
        if (missingLibraries != null) {
            missingPackageCount = missingLibraries.size();
        }
        if (unaffectedLibraries != null) {
            unaffectedPackageCount = unaffectedLibraries.size();
        }

    } catch (Exception e) {
        response.getWriter().print("Error");
    }
%>
<div class="submain-container">
    <p id="result-scanning">
        <b>Result scanned on requirements file:<%=" " + packageCount%></b><br>
        Packages affected: <%=" " + affectedPackageCount%><br>
        Packages not affected: <%=" " + unaffectedPackageCount%><br>
        Packages not found in database: <%=" " + missingPackageCount%><br><br>
    </p>
    <div>
        <%if (requirementsInfo != null) {%>
        <table id="table3" class="req-table" style="width:80%; border: 1px solid black; ">
            <tr>
                <th colspan="4" style="text-align: center; background-color:#f2f2f2;">Affected Package </th>

            </tr>
            <tr style="border: 1px solid black;">
                <th>Name</th>
                <th>Current Version</th>
                <th>Affected Version</th>
                <th>Description</th>
            </tr>
            <%for (int j = 0; j < affectedPackageCount; j++) {%>
            <tr>
                <td><%=requirementsInfo[j].getLibName()%></td>
                <td><%=requirementsInfo[j].getVersion()%></td>
                <td><%=requirementsInfo[j].getAffectedVersion()%></td>
                <td><%=requirementsInfo[j].getAdversary()%></td>
            </tr>
            <%}%>
        </table>
    </div>
    <div>
        <table id="table4" class="req-table" style="width:80%; margin-top: 15px;">
            <tr>
                <th colspan="2" style="text-align: center; background-color:#f2f2f2;">Unaffected Package</th>

            </tr>
            <tr>
                <th>Name</th>
                <th>Current Version</th>
            </tr>
            <%for (int i = 0; i < unaffectedPackageCount; i++) {%>
            <tr>
                <td><%=unaffectedLibraries.get(i).getLibrariesName()%></td>
                <td><%=unaffectedLibraries.get(i).getLibrariesVersion()%></td>
            </tr>
            <%}%>
        </table>
    </div>
    <div>
        <table id="table5" class="req-table" style="width:80%; margin-top: 15px;">
            <tr>
                <th colspan="2" style="text-align: center; background-color:#f2f2f2;">Missing Package</th>

            </tr>
            <tr>
                <th>Name</th>
                <th>Current Version</th>
            </tr>
            <%for (int i = 0; i < missingPackageCount; i++) {%>
            <tr>
                <td><%=missingLibraries.get(i).getLibrariesName()%></td>
                <td><%=missingLibraries.get(i).getLibrariesVersion()%></td>
            </tr>
            <%}%>
        </table>
        <%}%>
    </div>
</div>