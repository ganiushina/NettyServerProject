import java.util.List;

public class FileListRequest extends AbstractMessage {

    private List<String> filenameList;

    private String fileName;

    private String action;

    public String getAction() {
        return action;
    }

    FileMessage fileMessage;

    public FileMessage getFileMessage() {
        return fileMessage;
    }

    public String getFileName() {

        return fileName;
    }

    public List<String> getFilenameList() {
        return filenameList;
    }

    public void setFilenameList(List<String> filenameList) {
        this.filenameList = filenameList;
    }

    public FileListRequest(List<String> filenameList, String fileName, FileMessage fileMessage, String action) {
        this.filenameList = filenameList;
        this.fileName = fileName;
        this.fileMessage = fileMessage;
        this.action = action;
    }

}
