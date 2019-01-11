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

    public NotificationType getType() {
        return type;
    }

    public Integer getDepartmentID() {
        return departmentID;
    }

    public Integer getProductID() {
        return productID;
    }


    public boolean equals(Notification n) {
        if(n.getDepartmentID().equals(this.departmentID) && n.getProductID().equals(this.productID) &&
        n.getType().equals(this.type))
            return true;

        return false;
    }

    @Override
    public String toString() {
        return type.toString() + ";" + productID + ";" + departmentID;
    }
}
