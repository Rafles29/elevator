package controllers;

import controler.RealTimeController;
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
import listeners.IListener;

public class Controller implements IListener {
    private final String IMAGES_PATH = "/resources/images/";
    private final int FLOOR_CAP = 4;
    private final int BASE_MILIS = 1000;
    
    
    private List<Integer> peopleCount;
    private RealTimeController rtc;
    private int milisPerTick = 1000;
    
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
        resetButton.setDisable(true);
        List<HBox> floors = Arrays.asList(firstFloorBox, secondFloorBox, thirdFloorBox, 
                                    fourthFloorBox, fifthFloorBox, sixthFloorBox);

        floors.stream().forEach((floor) -> { // for (HBox floor : floors) {
            VBox buttonsBox = new VBox();
            buttonsBox.getChildren().addAll(createImageForUrl("button_up_off.png"), 
                                            createImageForUrl("button_down_off.png"));
            
            StackPane elevatorPane = new StackPane();
            elevatorPane.getChildren().addAll(createImageForUrl("ele_closed.png"),
                                              createInsidePeopleCountLabel());
            
            floor.getChildren().setAll(createImageForUrl("light_off.png"),
                                       elevatorPane,
                                       buttonsBox);
        });
        
        peopleCount = Arrays.asList(0, 0, 0, 0, 0, 0);
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
        resetButton.setDisable(true);
        //animationSpeedSlider.setDisable(true);
        algorithmComboBox.setDisable(true);
        generatorComboBox.setDisable(true);
        rtc.setMilis(this.milisPerTick);
        if(startButton.getText().equals("START"))
            rtc.start();
        else
            rtc.resume();
        startButton.setText("RESUME");
    }
    
    public void handleStopClicked() {
        startButton.setDisable(false);
        stopButton.setDisable(true);
        resetButton.setDisable(false);
        //animationSpeedSlider.setDisable(false);
        algorithmComboBox.setDisable(false);
        generatorComboBox.setDisable(false);
        rtc.suspend();
    }
    
    public void handleResetClicked() {
        rtc.restart();
        startButton.setText("START");
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
        int sliderValue = (int)Math.round(animationSpeedSlider.valueProperty().getValue());
        animationSpeedLabel.setText("x" + sliderValue);
        this.milisPerTick = (int)(BASE_MILIS / sliderValue);
        rtc.setMilis(this.milisPerTick);
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
    
    private void setButtonsOff(int floor) {
        VBox box = (VBox) getFloorByInt(floor).getChildren().get(2);
        box.getChildren().setAll(createImageForUrl("button_up_off.png"),
                                 createImageForUrl("button_down_off.png"));
    }
    
    private void setUpButtonOn(int floor) {
        VBox box = (VBox) getFloorByInt(floor).getChildren().get(2);
        box.getChildren().set(0, createImageForUrl("button_up_on.png"));
    }
    
    private void setDownButtonOn(int floor) {
        VBox box = (VBox) getFloorByInt(floor).getChildren().get(2);
        box.getChildren().set(1, createImageForUrl("button_down_on.png"));
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

    @Override
    public void newPersonOnFloor(int floor) {
        changeNumberOfPeople(floor, 1);
    }

    @Override
    public void personLeftFloor(int floor) {
        changeNumberOfPeople(floor, -1);
    }

    @Override
    public void peopleExited(int floor, int newCount) {
        setInsideCountLabel(floor, newCount);
    }
    
    @Override
    public void peopleEntered(int floor, int newCount) {
        setInsideCountLabel(floor, newCount);
    }

    @Override
    public void elevatorToFrom(int to, int from, int passengersCount) {
        removeInsideCountLabel(from);
        setLightOn(from, false);
        setInsideCountLabel(to, passengersCount);
        setLightOn(to, true);
    }

    @Override
    public void openDoor(int floor) {
        setElevatorOpen(floor, true);
    }

    @Override
    public void closeDoor(int floor) {
        setElevatorOpen(floor, false);
    }

    @Override
    public void pressUpButton(int floor) {
        setUpButtonOn(floor);
    }

    @Override
    public void pressDownButton(int floor) {
        setDownButtonOn(floor);
    }

    @Override
    public void turnOffButtons(int floor) {
        setButtonsOff(floor);
    }
    
    @Override
    public void initElevator(int floor, int passengersCount) {
        setInsideCountLabel(floor, passengersCount);
        setLightOn(floor, true);
    }
    
    public void setRTC(RealTimeController rtc) {
        this.rtc = rtc;
    }
    
    public void cleanup() {
        initialize();
    }
}
