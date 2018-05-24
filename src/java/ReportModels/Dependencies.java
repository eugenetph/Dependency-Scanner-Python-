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
public class Dependencies {
    private Identifiers[] identifiers;

    private String description;

    private String filePath;

    private EvidenceCollected evidenceCollected;

    private String md5;

    private String fileName;

    private String sha1;

    private String isVirtual;

    private Vulnerabilities[] vulnerabilities;

    public Identifiers[] getIdentifiers ()
    {
        return identifiers;
    }

    public void setIdentifiers (Identifiers[] identifiers)
    {
        this.identifiers = identifiers;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public String getFilePath ()
    {
        return filePath;
    }

    public void setFilePath (String filePath)
    {
        this.filePath = filePath;
    }

    public EvidenceCollected getEvidenceCollected ()
    {
        return evidenceCollected;
    }

    public void setEvidenceCollected (EvidenceCollected evidenceCollected)
    {
        this.evidenceCollected = evidenceCollected;
    }

    public String getMd5 ()
    {
        return md5;
    }

    public void setMd5 (String md5)
    {
        this.md5 = md5;
    }

    public String getFileName ()
    {
        return fileName;
    }

    public void setFileName (String fileName)
    {
        this.fileName = fileName;
    }

    public String getSha1 ()
    {
        return sha1;
    }

    public void setSha1 (String sha1)
    {
        this.sha1 = sha1;
    }

    public String getIsVirtual ()
    {
        return isVirtual;
    }

    public void setIsVirtual (String isVirtual)
    {
        this.isVirtual = isVirtual;
    }

    public Vulnerabilities[] getVulnerabilities ()
    {
        return vulnerabilities;
    }

    public void setVulnerabilities (Vulnerabilities[] vulnerabilities)
    {
        this.vulnerabilities = vulnerabilities;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [identifiers = "+identifiers+", description = "+description+", filePath = "+filePath+", evidenceCollected = "+evidenceCollected+", md5 = "+md5+", fileName = "+fileName+", sha1 = "+sha1+", isVirtual = "+isVirtual+", vulnerabilities = "+vulnerabilities+"]";
    }
}
