
package Restaurant.Controller.Event;

//Event khi chọn menu (menu index + subMenu index)
public interface EventMenuSelected {
    public void menuSelected(int menuIndex,int subMenuIndex);
}
