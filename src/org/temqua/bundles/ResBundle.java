package org.temqua.bundles;

import java.util.ResourceBundle;

/**
 * Created by Артем on 29.09.2017.
 */
public class ResBundle {

    private ResourceBundle bundle;

    public ResBundle() {
        this.bundle = ResourceBundle.getBundle("org.temqua.bundles.Strings");
    }

    public String getValue(String key){
        return bundle.getString(key);
    }
}
