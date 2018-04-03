package com.example.jmfs1.ebec.scoresfragment;

import android.view.View;

/**
 * Created by miguel on 21-12-2016.
 */

public class NLevelItem implements NLevelListItem {

    private Object wrappedObject;
    private NLevelItem parent;
    private NLevelView nLevelView;
    private boolean isExpanded = false;

    public NLevelItem(Object wrappedObject, NLevelItem parent, NLevelView nLevelView){
        this.wrappedObject = wrappedObject;
        this.parent = parent;
        this.nLevelView = nLevelView;
    }

    public Object getWrappedObject(){
        return wrappedObject;
    }

    @Override
    public boolean isExpanded() {
        return isExpanded;
    }

    @Override
    public void toggle() {
        isExpanded = !isExpanded;
    }

    @Override
    public NLevelListItem getParent() {
        return parent;
    }

    @Override
    public View getView() {
        return nLevelView.getView(this);
    }
}
