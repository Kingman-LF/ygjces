package com.xwkj.ygjces.controller.vo;

import java.util.List;

public class PermissionVo {
    private Long id;
    private String label;
    private Boolean checked;
    private List<PermissionVo> children;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public List<PermissionVo> getChildren() {
        return children;
    }

    public void setChildren(List<PermissionVo> children) {
        this.children = children;
    }
}
