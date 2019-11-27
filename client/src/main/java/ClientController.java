import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ClientController implements Initializable {

    @FXML
    ListView<String> filesList;

    @FXML
    ListView<String> filesServerList;

    @FXML
    VBox rootNode;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Thread t = new Thread(() -> {
            try {
                while (true) {
                    AbstractMessage am = Network.readObject();
                    if (am instanceof FileMessage) {
                        FileMessage fm = (FileMessage) am;
                        Files.write(Paths.get("client_storage/" + fm.getFilename()), fm.getData(), StandardOpenOption.CREATE);
                        refreshLocalFilesList();
                    }
                    if (am instanceof FileListRequest){
                        FileListRequest flr = (FileListRequest) am;
                        List<String> filesName = flr.getFilenameList();
                        refreshRemoteFilesList(filesName);
                    }
                }
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            } finally {
                Network.stop();

            }
        });
        t.setDaemon(true);
        t.start();
        refreshLocalFilesList();
        loadFiles();
    }


    public void loadFiles() {
        Network.sendMsg(new FileListRequest(new ArrayList<>(), "", null, "show"));
    }

    public void refreshLocalFilesList() {
        updateUI(() -> {
            try {
                filesList.getItems().clear();
                Files.list(Paths.get("client_storage")).map(p -> p.getFileName().toString()).forEach(o -> filesList.getItems().add(o));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void refreshRemoteFilesList(List<String> remotFileList) {
        updateUI(() -> {
            filesServerList.getItems().clear();
            remotFileList.forEach(o -> filesServerList.getItems().add(o));

        });
    }

    public static void updateUI(Runnable r) {
        if (Platform.isFxApplicationThread()) {
            r.run();
        } else {
            Platform.runLater(r);
        }
    }

    @FXML
    public void sendToServer(ActionEvent actionEvent) throws IOException {
        if (filesList.getItems().size() > 0) {
            FileMessage fm = new FileMessage(Paths.get("client_storage/" + filesList.getSelectionModel().getSelectedItem()));
            Network.sendMsg(new FileListRequest(new ArrayList<>(), filesList.getSelectionModel().getSelectedItem(), fm, "send"));
        }
    }

    @FXML
    public void sendToClient() {
        Network.sendMsg(new FileRequest(filesServerList.getSelectionModel().getSelectedItem()));
    }

    @FXML
    public void delOnServer () {
        if (filesList.getItems().size() > 0) {
            Network.sendMsg(new FileListRequest(new ArrayList<>(), filesServerList.getSelectionModel().getSelectedItem(), null, "del"));
        }
    }

    @FXML
    public void delOnClient () {
        try {
            Files.delete(Paths.get("client_storage/" + filesList.getSelectionModel().getSelectedItem()));
            refreshLocalFilesList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
