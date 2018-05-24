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
public class VulnerableSoftware {
    private String software;

    private String allPreviousVersion;

    public String getSoftware ()
    {
        return software;
    }

    public void setSoftware (String software)
    {
        this.software = software;
    }

    public String getAllPreviousVersion ()
    {
        return allPreviousVersion;
    }

    public void setAllPreviousVersion (String allPreviousVersion)
    {
        this.allPreviousVersion = allPreviousVersion;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [software = "+software+", allPreviousVersion = "+allPreviousVersion+"]";
    }
}
