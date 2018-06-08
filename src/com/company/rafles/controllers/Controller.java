package controllers;

import java.util.Arrays;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class Controller {
    private final String IMAGES_PATH = "/resources/images/";
    private final int FLOOR_CAP = 4;
    
    List<Integer> peopleCount;
    
    @FXML
    Slider animationSpeedSlider;
    @FXML
    Label animationSpeedLabel;
    @FXML
    ComboBox algorithmComboBox;
    @FXML
    ComboBox generatorComboBox;
    @FXML
    Button startButton;
    @FXML
    Button stopButton;
    @FXML
    Button resetButton;
    @FXML
    HBox sixthFloorBox;
    @FXML
    HBox fifthFloorBox;
    @FXML
    HBox fourthFloorBox;
    @FXML
    HBox thirdFloorBox;
    @FXML
    HBox secondFloorBox;
    @FXML
    HBox firstFloorBox;
    
    @FXML
    TextArea floorToAddPerson;
    
    @FXML
    public void initialize() {
        stopButton.setDisable(true);
        List<HBox> floors = Arrays.asList(firstFloorBox, secondFloorBox, thirdFloorBox, 
                                    fourthFloorBox, fifthFloorBox, sixthFloorBox);

        floors.stream().forEach((floor) -> { // for (HBox floor : floors) {
            VBox buttonsBox = new VBox();
            buttonsBox.getChildren().addAll(createImageForUrl("button_up_off.png"), 
                                            createImageForUrl("button_down_off.png"));
            
            StackPane elevatorPane = new StackPane();
            elevatorPane.getChildren().addAll(createImageForUrl("ele_closed.png"),
                                              createInsidePeopleCountLabel());
            
            floor.getChildren().addAll(createImageForUrl("light_off.png"),
                                       elevatorPane,
                                       buttonsBox,
                                       createImageForUrl("stickman.png"));
        });
        
        firstFloorBox.getChildren().set(0, createImageForUrl("light_on.png"));
        StackPane elevatorPane = (StackPane) firstFloorBox.getChildren().get(1);
        elevatorPane.getChildren().set(0, createImageForUrl("ele_open.png"));
        elevatorPane.getChildren().set(1, createInsidePeopleCountLabel(1));
        
        peopleCount = Arrays.asList(1, 1, 1, 1, 1, 1);
    }
    
    private ImageView createImageForUrl(String url) {
        ImageView image = new ImageView();
        image.setPreserveRatio(true);
        image.setImage(new Image(IMAGES_PATH + url));
        return image;
    }
    
    public void handleStartClicked() {
        startButton.setDisable(true);
        stopButton.setDisable(false);
        algorithmComboBox.setDisable(true);
        generatorComboBox.setDisable(true);
    }
    
    public void handleStopClicked() {
        startButton.setDisable(false);
        stopButton.setDisable(true);
        algorithmComboBox.setDisable(false);
        generatorComboBox.setDisable(false);
    }
    
    public void handleResetClicked() {
        ImageView newPerson = new ImageView();
        newPerson.setImage(new Image(IMAGES_PATH + "stickman.png"));
        sixthFloorBox.getChildren().add(newPerson);
    }
    
    public void setAlgorithms(List algorithms) {
        algorithmComboBox.getItems().setAll(algorithms);
        algorithmComboBox.setValue(algorithms.get(0));
    }
    
    public void setGenerators(List generators) {
        generatorComboBox.getItems().setAll(generators);
        generatorComboBox.setValue(generators.get(0));
    }
    
    public void setAnimationSpeedLabel() {
        animationSpeedLabel.setText("x" + Math.round(animationSpeedSlider.valueProperty().getValue()));
    }
    
    public void handleAddPerson() {
        int floor = Integer.parseInt(floorToAddPerson.getText());
        changeNumberOfPeople(floor, 1);
    }
    
    public void handleRemovePerson() {
        int floor = Integer.parseInt(floorToAddPerson.getText());
        changeNumberOfPeople(floor, -1);
    }
    
    public void handleOpenElevator() {
        int floor = Integer.parseInt(floorToAddPerson.getText());
        setElevatorOpen(floor, true);
        setLightOn(floor, true);
    }
    
    public void handleCloseElevator() {
        int floor = Integer.parseInt(floorToAddPerson.getText());
        setElevatorOpen(floor, false);
        setLightOn(floor, false);
    }
    
    public void handleMoveElevator() {
        int floor = Integer.parseInt(floorToAddPerson.getText());
        setInsideCountLabel(floor, 1); // TODO take count from args
        setLightOn(floor, true);
    }
    
    public void handleRemoveCount() {
        int floor = Integer.parseInt(floorToAddPerson.getText());
        removeInsideCountLabel(floor);
        setLightOn(floor, false);
    }
    
    public void handleNoButtons() {
        int floor = Integer.parseInt(floorToAddPerson.getText());
        setButtonsState(floor, false, false);
    }
    
    public void handleOnlyUp() {
        int floor = Integer.parseInt(floorToAddPerson.getText());
        setButtonsState(floor, true, false);
    }
    
    public void handleOnlyDown() {
        int floor = Integer.parseInt(floorToAddPerson.getText());
        setButtonsState(floor, false, true);
    }
    
    public void handleBothButtons() {
        int floor = Integer.parseInt(floorToAddPerson.getText());
        setButtonsState(floor, true, true);
    }
 
    private void setElevatorOpen(int floor, boolean isOpen) {
        StackPane elevatorPane = (StackPane) getFloorByInt(floor).getChildren().get(1);
        elevatorPane.getChildren().set(0, createImageForUrl(isOpen ? "ele_open.png" : "ele_closed.png"));
    }
    
    private void setLightOn(int floor, boolean isOn) { 
        getFloorByInt(floor).getChildren().set(0, createImageForUrl(isOn ? "light_on.png" : "light_off.png"));
    }
    
    private void setInsideCountLabel(int floor, int count) { 
        StackPane elevatorPane = (StackPane) getFloorByInt(floor).getChildren().get(1);
        elevatorPane.getChildren().set(1, createInsidePeopleCountLabel(count));
    }
    
    private void removeInsideCountLabel(int floor) { 
        StackPane elevatorPane = (StackPane) getFloorByInt(floor).getChildren().get(1);
        elevatorPane.getChildren().set(1, createInsidePeopleCountLabel());
    }
    
    private void setButtonsState(int floor, boolean isUpOn, boolean isDownOn) {
        VBox box = (VBox) getFloorByInt(floor).getChildren().get(2);
        box.getChildren().setAll(createImageForUrl(isUpOn ? "button_up_on.png" : "button_up_off.png"),
                                 createImageForUrl(isDownOn ? "button_down_on.png" : "button_down_off.png"));
    }
    
    private Label createPeopleCountLabel(int count) {
        Label label = new Label("+" + count);
        label.getStyleClass().add("people-count");
        return label;
    }
    
    private Label createInsidePeopleCountLabel(int count) {
        Label label = new Label(Integer.toString(count));
        label.getStyleClass().add("inside-people-count");
        return label;
    }
    
    private Label createInsidePeopleCountLabel() {
        Label label = new Label();
        label.getStyleClass().add("inside-people-count");
        return label;
    }
    
    private HBox getFloorByInt(int floor) {
        switch(floor) {
            case 0: return firstFloorBox;
            case 1: return secondFloorBox;
            case 2: return thirdFloorBox;
            case 3: return fourthFloorBox;
            case 4: return fifthFloorBox;
            default: return sixthFloorBox;
        }
    }
    
    private void changeNumberOfPeople(int floor, int change) {
        int tempPeopleCount = peopleCount.get(floor);
        int newPeopleCount = tempPeopleCount + change;
        
        HBox floorBox = getFloorByInt(floor);
        
        if(change > 0) {
            while(tempPeopleCount < FLOOR_CAP && change > 0) {
                floorBox.getChildren().add(createImageForUrl("stickman.png"));
                tempPeopleCount++;
                change--;
            }      
            if(change > 0) {
                if(tempPeopleCount == FLOOR_CAP) {
                    floorBox.getChildren().add(createPeopleCountLabel(newPeopleCount - FLOOR_CAP));
                } else {
                    floorBox.getChildren().set(floorBox.getChildren().size() - 1,
                                               createPeopleCountLabel(newPeopleCount - FLOOR_CAP));
                } 
            }
        }
         else {
            while(tempPeopleCount <= FLOOR_CAP && change < 0) {
                if(tempPeopleCount == 0) {
                    peopleCount.set(floor, 0);
                    return;
                }
                floorBox.getChildren().remove(floorBox.getChildren().size() - 1);
                tempPeopleCount--;
                change++;
            }      
            if(change < 0) {
                if(tempPeopleCount == FLOOR_CAP + 1) {
                    floorBox.getChildren().remove(floorBox.getChildren().size() - 1);
                } else {
                    floorBox.getChildren().set(floorBox.getChildren().size() - 1,
                                               createPeopleCountLabel(newPeopleCount - FLOOR_CAP));
                } 
            }
        }
        
        peopleCount.set(floor, newPeopleCount);
    }
}
