package xyz.sakret.ui.dialogs;

import com.sun.javafx.scene.control.skin.resources.ControlResources;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class ChoiceDialogListView<T> extends Dialog<T> {

    private final GridPane grid;
    private final Label label;
    private final ListView<T> listView;

    //----------------------------------------------------------------------------
    public ChoiceDialogListView() {
        this((T[])null);
    }

    //----------------------------------------------------------------------------
    @SafeVarargs
    public ChoiceDialogListView(@SuppressWarnings("unchecked") T... choices) {
        this(choices == null ? Collections.emptyList() : Arrays.asList(choices));
    }


    //----------------------------------------------------------------------------
    public ChoiceDialogListView(Collection<T> choices) {
        final DialogPane dialogPane = getDialogPane();
        // -- grid
        this.grid = new GridPane();
        this.grid.setHgap(10);
        this.grid.setMaxWidth(Double.MAX_VALUE);
        this.grid.setAlignment(Pos.CENTER);

        // -- label

        label = createContentLabel(dialogPane.getContentText());
        label.setPrefWidth(Region.USE_COMPUTED_SIZE);
        label.textProperty().bind(dialogPane.contentTextProperty());

        dialogPane.contentTextProperty().addListener(o -> updateGrid());

        setTitle(ControlResources.getString("Dialog.confirm.title"));
        dialogPane.setHeaderText(ControlResources.getString("Dialog.confirm.header"));
        dialogPane.getStyleClass().add("choice-dialog");
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        final double MIN_WIDTH = 350;

        listView = new ListView<>();
        listView.setMinWidth(MIN_WIDTH);
        if (choices != null) {
            listView.getItems().addAll(choices);
        }

        listView.setMaxWidth(Double.MAX_VALUE);
//        listView.setMaxHeight(180);
        GridPane.setHgrow(listView, Priority.ALWAYS);
        GridPane.setFillWidth(listView, true);


        updateGrid();

        setResultConverter((dialogButton) -> {
            ButtonBar.ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
            return data == ButtonBar.ButtonData.OK_DONE ? getSelectedItem() : null;
        });
    }

    //----------------------------------------------------------------------------
    public final T getSelectedItem() {
        return listView.getSelectionModel().getSelectedItem();
    }

    //----------------------------------------------------------------------------
    public final ReadOnlyObjectProperty<T> selectedItemProperty() {
        return listView.getSelectionModel().selectedItemProperty();
    }

    //----------------------------------------------------------------------------
    public final void setSelectedItem(T item) {
        listView.getSelectionModel().select(item);
    }

    //----------------------------------------------------------------------------
    public final ObservableList<T> getItems() {
        return listView.getItems();
    }

    //----------------------------------------------------------------------------
    private void updateGrid() {
        grid.getChildren().clear();

        grid.add(label, 0, 0);
        grid.add(listView, 1, 0);
        getDialogPane().setContent(grid);

        Platform.runLater(() -> listView.requestFocus());
    }

    //----------------------------------------------------------------------------
    private Label createContentLabel(String text) {
        Label label = new Label(text);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setMaxHeight(Double.MAX_VALUE);
        label.getStyleClass().add("content");
        label.setWrapText(true);
        label.setPrefWidth(360);
        return label;
    }
}

