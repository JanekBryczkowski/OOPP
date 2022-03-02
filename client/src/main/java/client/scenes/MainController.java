package client.scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainController {

    //TODO: injection

    private Stage primaryStage1;

    private LeaderBoardScreenController leaderBoardScreenController;
    private Scene overview;

    private AddQuoteCtrl addCtrl;
    private Scene add;

    public void initialize(Stage primaryStage, Pair<QuoteOverviewCtrl, Parent> overview,
                           Pair<AddQuoteCtrl, Parent> add) {
        this.primaryStage1 = primaryStage;
        //this.overviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());

        this.addCtrl = add.getKey();
        this.add = new Scene(add.getValue());

        showOverview();
        primaryStage.show();
    }

    public void showOverview() {
        primaryStage1.setTitle("Quotes: Overview");
        primaryStage1.setScene(overview);
        //overviewCtrl.refresh();
    }

    public void showAdd() {
        primaryStage1.setTitle("Quotes: Adding Quote");
        primaryStage1.setScene(add);
        add.setOnKeyPressed(e -> addCtrl.keyPressed(e));
    }
}

