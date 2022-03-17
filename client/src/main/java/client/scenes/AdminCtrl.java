package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class AdminCtrl {

    private final ServerUtils server;

    @FXML
    private Button addButton;

    @FXML
    private TextField titleText;

    @FXML
    private TextField consumptionText;

    @FXML
    private TextField sourceText;

    //Connecting the page with the server
    @Inject
    public AdminCtrl(ServerUtils server) {
        this.server = server;

        //TO BE COMPLETED BY THE END OF WEEK 6
        addButton.setOnAction(event -> {
            String title = titleText.getText();
            String consumption = consumptionText.getText();
            String source = sourceText.getText();
        });

    }










}
