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
public class Credits {
    private String NSP;

    private String NVD;

    public String getNSP ()
    {
        return NSP;
    }

    public void setNSP (String NSP)
    {
        this.NSP = NSP;
    }

    public String getNVD ()
    {
        return NVD;
    }

    public void setNVD (String NVD)
    {
        this.NVD = NVD;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [NSP = "+NSP+", NVD = "+NVD+"]";
    }
}
