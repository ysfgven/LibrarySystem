import java.util.ArrayList;
import java.util.List;

public class Staff {
    private final String staffUserName;
    private final String staffPassword;
    private static List<Staff> staffList = new ArrayList<>();

    Staff(String staffUserName, String staffPassword) {
        this.staffUserName = staffUserName;
        this.staffPassword = staffPassword;
        staffList.add(this);
    }

    public static List<Staff> getList() {
        return staffList;
    }

    public String getStaffPassword() {
        return staffPassword;
    }

    public String getStaffUserName() {
        return staffUserName;
    }

    public static void setStaffList(List<Staff> list) {
        staffList = list;
    }
}
