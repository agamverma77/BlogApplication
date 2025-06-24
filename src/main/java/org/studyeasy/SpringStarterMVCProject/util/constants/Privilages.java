package org.studyeasy.SpringStarterMVCProject.util.constants;

public enum Privilages {
    RESET_ANY_USER_PASSWORD(1l,"RESET_ANY_USER_PASSWORD"),
    ACCESS_ADMIN_PANEL(2l,"ACCESS_ADMIN_PANEL");
    private Long authorityId;
    private String authorityString;
    private Privilages(Long authorityId,String authorityString)
    {
        this.authorityId=authorityId;
        this.authorityString=authorityString;
    }
    public Long getAuthorityId()
    {
        return authorityId;
    }
    public String getAuthorityString()
    {
        return authorityString;
    }
}

