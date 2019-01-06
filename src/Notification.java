public class Notification {

    public enum NotificationType{
        ADD, REMOVE, MODIFY
    }

    private NotificationType type;
    private Integer departmentID;
    private Integer productID;

    public Notification(Integer departmentID, Integer productID, NotificationType type) {
        this.departmentID = departmentID;
        this.productID = productID;
        this.type = type;
    }
}
