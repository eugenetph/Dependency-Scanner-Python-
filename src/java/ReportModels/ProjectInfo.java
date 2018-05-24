/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ReportModels;

/**
 *
 * @author Eugene Tan
 */
public class ProjectInfo {
    private String name;

    private Credits credits;

    private String reportDate;

    private String version;

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public Credits getCredits ()
    {
        return credits;
    }

    public void setCredits (Credits credits)
    {
        this.credits = credits;
    }

    public String getReportDate ()
    {
        return reportDate;
    }

    public void setReportDate (String reportDate)
    {
        this.reportDate = reportDate;
    }

    public String getVersion ()
    {
        return version;
    }

    public void setVersion (String version)
    {
        this.version = version;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [name = "+name+", credits = "+credits+", reportDate = "+reportDate+", version = "+version+"]";
    }
}
