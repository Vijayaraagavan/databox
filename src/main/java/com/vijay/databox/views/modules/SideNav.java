package com.vijay.databox.views.modules;

import com.vijay.databox.core.model.SideTab;

public class SideNav {
    public  SideTab[] getNav() {
        SideTab[] tabs = new SideTab[] {
            new SideTab("images", "Images", "image", true, "/gallery"),
            new SideTab("videos", "Videos", "schedule", true, "/videos"),
            new SideTab("shared", "Shared with me", "folder_shared", true, "")
    };
    return tabs;
    }
}
