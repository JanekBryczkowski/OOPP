/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.scenes;

import client.utils.ServerUtils;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class AdminRunCtrl {

    private Stage primaryStage;

    private AdminCtrl adminCtrl;
    private Scene adminScene;

    private ServerUtils server;

    /**
     * This initializes the adminScene and the primaryStage.
     * @param primaryStage
     * @param admin
     */
    public void initialize(Stage primaryStage, Pair<AdminCtrl, Parent> admin) {
        this.primaryStage = primaryStage;

        this.adminCtrl = admin.getKey();
        this.adminScene = new Scene(admin.getValue());

        showAdminScreen();
        primaryStage.show();
    }

    /**
     * This function sets the adminScene as the primaryScene, so it shows up to the
     * administrator who runs AdminMain.
     */
    public void showAdminScreen() {
        primaryStage.setTitle("Admin Screen");
        adminScene.getStylesheets().add("client.styles/AdminScreenStyle.css");
        primaryStage.setScene(adminScene);
    }
}