package com.xwkj.ygjces.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class Permission {
    @Setter
    @Getter
    private Long id;
    @Setter
    @Getter
    private String permName;
    @Setter
    @Getter
    private String permssion;
    @Setter
    @Getter
    private Long fid;
    @Setter
    @Getter
    private Boolean checked;

    private Boolean isParent;

    public Boolean getIsParent() {
        return isParent;
    }

    public void setIsParent(Boolean parent) {
        isParent = parent;
    }
}
