package nl.northcreek.twitazia.drawer;

public class NavDrawerItem {
    private boolean showNotify;
    private String title;
    private int imageIcon;
 
 
    public NavDrawerItem() {
 
    }
 
    public NavDrawerItem(boolean showNotify, String title) {
        this.showNotify = showNotify;
        this.title = title;
        this.setImageIcon(imageIcon);
    }
 
    public boolean isShowNotify() {
        return showNotify;
    }
 
    public void setShowNotify(boolean showNotify) {
        this.showNotify = showNotify;
    }
 
    public String getTitle() {
        return title;
    }
 
    public void setTitle(String title) {
        this.title = title;
    }
    public int getImageIcon() {
		return imageIcon;
	}

	public void setImageIcon(int imageIcon) {
		this.imageIcon = imageIcon;
	}
}