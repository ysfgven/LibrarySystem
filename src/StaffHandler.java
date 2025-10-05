import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class StaffHandler {

    Path path = Paths.get("data", "staff.csv");

    public StaffHandler() {
    }
    public void createCSVFile() {
        if (!Files.exists(path)) {
            try{
                Files.createFile(path);
            }catch(IOException e){
                ErrorHandler.showErrorAlert("Warning","Create Error","There was an error while creating the file",false);
                LogHelper.logException(e);
            }
        }

    }
    public void readCSVFile() {
        List<String> readedList = new ArrayList<>();
        List<Staff> tempList = new ArrayList<>();

        try{
          readedList = Files.readAllLines(path);
           }
        catch(IOException e){
            ErrorHandler.showErrorAlert("Error","Read Error","There was an error while reading the file ",false);
            LogHelper.logException(e);
        }
        for (String line : readedList) {
            String[] fields = line.split(",");
            tempList.add(new Staff(fields[0], fields[1]));
        }
        Staff.setStaffList(tempList);
    }

}
