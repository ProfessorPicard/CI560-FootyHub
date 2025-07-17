package uk.phsh.footyhub.models;

import androidx.fragment.app.Fragment;

import uk.phsh.footyhub.fragments.BaseFragment;

public class NavItem {
    private String _title;
    private int _icon;
    private Fragment _fragment;

    public NavItem(String title, int icon, Fragment fragment) {
        this._title = title;
        this._icon = icon;
        this._fragment = fragment;
    }

    public int getIcon() { return _icon; }
    public String getTitle() { return _title; }
    public Fragment getFragment() { return _fragment; }

    public void setTitle(String value) { this._title = value; }
    public void setIcon(int value) { this._icon = value; }
    public void setFragment(BaseFragment value) { this._fragment = value; }
}
