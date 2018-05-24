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
public class ScanInfo {
    private DataSource[] dataSource;

    private String engineVersion;

    public DataSource[] getDataSource ()
    {
        return dataSource;
    }

    public void setDataSource (DataSource[] dataSource)
    {
        this.dataSource = dataSource;
    }

    public String getEngineVersion ()
    {
        return engineVersion;
    }

    public void setEngineVersion (String engineVersion)
    {
        this.engineVersion = engineVersion;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [dataSource = "+dataSource+", engineVersion = "+engineVersion+"]";
    }
}
