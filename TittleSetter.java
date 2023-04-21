package application;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class TitleSetter extends Application {

    @Override
    public void start(Stage primaryStage) {

        // Create the HBox and set its properties
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(10);

        // Create the Label and TextField
        Label label = new Label("Set title to be:");
        TextField textField = new TextField();

        // Create the Button and set its action
        Button button = new Button("Set Title");
        button.setOnAction(event -> {
            primaryStage.setTitle(textField.getText());
        });

        // Add GUI nodes to hbox
        hbox.getChildren().addAll(label, textField, button);

        // Create the Scene and set it on the Stage
        Scene scene = new Scene(hbox, 400, 200);
        primaryStage.setScene(scene);

        // Set the title of the Stage
        primaryStage.setTitle("Title Setter");

        // Show the Stage
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
