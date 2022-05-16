package planner.model.json.checklist;

import java.util.ArrayList;
import lombok.Data;

@Data
public class AllItem {
    private Definition definition;
    private Status status;
    private String comment;
    private ArrayList<File> files;
}
