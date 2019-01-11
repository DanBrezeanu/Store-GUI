import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Notification {

    public enum NotificationType{
        ADD, REMOVE, MODIFY
    }

    Date currentDate;
    private NotificationType type;
    private Integer departmentID;
    private Integer productID;

    public Notification(Integer departmentID, Integer productID, NotificationType type) {
        this.departmentID = departmentID;
        this.productID = productID;
        this.type = type;

        currentDate = new Date();
    }

    @Override
    public String toString() {
        return type.toString() + ";" + productID + ";" + departmentID;
    }
}
