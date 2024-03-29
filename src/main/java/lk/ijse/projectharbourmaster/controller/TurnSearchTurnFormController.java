package lk.ijse.projectharbourmaster.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lk.ijse.projectharbourmaster.bo.BOFactory;
import lk.ijse.projectharbourmaster.bo.custom.TurnBO;
import lk.ijse.projectharbourmaster.dto.*;
import lk.ijse.projectharbourmaster.dto.tm.CrewTM;
import lk.ijse.projectharbourmaster.dto.tm.TurnFishSearchTM;
import lk.ijse.projectharbourmaster.dto.tm.TurnFishTM;
//import lk.ijse.projectharbourmaster.model.*;
import lk.ijse.projectharbourmaster.util.Validations;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class TurnSearchTurnFormController {

    @FXML
    public JFXButton endTurnBtn;

    @FXML
    public JFXTextField fishIdTxt;

    @FXML
    public JFXTextField fishweight;

    @FXML
    public Label boatIdLbl;

    @FXML
    public Label capNIClbl;

    @FXML
    public TableView fishTable;

    @FXML
    public TableColumn fishIdCol;

    @FXML
    public TableColumn fishNameCol;

    @FXML
    public TableColumn unitPrice;

    @FXML
    public TableColumn qtyCol;

    @FXML
    public TableColumn actionCol;

    @FXML
    public Label fishNameLbl;

    @FXML
    public Label unitPricelbl;

    @FXML
    private JFXTextField crewCounttxt;

    @FXML
    private JFXTextField outDateTxt;

    @FXML
    private JFXTextField outTimeTxt;

    @FXML
    private JFXButton backBtn;

    @FXML
    private JFXButton mainBtn;

    @FXML
    private JFXTextField inDatetxt;

    @FXML
    private JFXTextField inTimetxt;

    @FXML
    private Label turnIdLbl;

    @FXML
    private ImageView capImageView;

    @FXML
    private Label capNameLbl;

    @FXML
    private Label capDOBLbl;

    @FXML
    private Label capContactLbl;

    @FXML
    private Label emailLbl;

    @FXML
    private Label boatNameLbl;

    @FXML
    private Label boatTypeLbl;

    @FXML
    private Label noCrewLbl;

    @FXML
    private JFXTextField turnSearchIdTxt;

    @FXML
    private JFXButton searchBtn;

    @FXML
    private JFXButton deleteTurnBtn;

    @FXML
    private JFXButton registerBtn;

    @FXML
    private JFXButton saveBtn;

    @FXML
    private TableView<CrewTM> crewTbl;

    @FXML
    private TableColumn<?, ?> crewNicCol;

    @FXML
    private TableColumn<?, ?> crewNameCol;

    @FXML
    private TableColumn<?, ?> crewAddressCol;

    @FXML
    private TableColumn<?, ?> crewContactCol;

    @FXML
    private TableColumn<?, ?> crewAgeCol;

    @FXML
    private JFXButton addBtn;

    TurnDTO turnDTO;
    BoatDTO boatDTO;
    CrewDTO cap;

    private ObservableList<TurnFishTM> turnFishTMObList = FXCollections.observableArrayList();


    TurnBO turnBO = (TurnBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.TURN);

    @FXML
    void initialize() {
        setCellValueFactory();
        crewCounttxt.setEditable(false);
        outDateTxt.setEditable(false);
        outTimeTxt.setEditable(false);

        setUpdatable(false);

        setTextFieldValidations();
    }

    private void setTextFieldValidations() {
        Validations.setFocus(turnSearchIdTxt, Validations.turnPattern);
        Validations.setFocus(inDatetxt, Validations.datePattern);
        Validations.setFocus(inTimetxt, Validations.timePattern);
        Validations.setFocus(fishIdTxt, Validations.fishPattern);
        Validations.setFocus(fishweight, Validations.doublePattern22);

    }

    private boolean isAllDataValidated() {
        if (inDatetxt.getFocusColor().equals(javafx.scene.paint.Paint.valueOf("red")) || inTimetxt.getFocusColor().equals(javafx.scene.paint.Paint.valueOf("red"))) {

            return false;
        }
        return true;

    }

    void setUpdatable(boolean updatable) {
        if (updatable) {
            inDatetxt.setEditable(true);
            inTimetxt.setEditable(true);
            fishIdTxt.setEditable(true);
            fishweight.setEditable(true);

            endTurnBtn.setVisible(true);
            addBtn.setVisible(true);
        } else {
            inDatetxt.setEditable(false);
            inTimetxt.setEditable(false);
            fishIdTxt.setEditable(false);
            fishweight.setEditable(false);

            endTurnBtn.setVisible(false);
            addBtn.setVisible(false);
        }

    }

    private void setRemoveBtnOnAction(Button btn) {
        btn.setOnAction((e) -> {
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

            if (result.orElse(no) == yes) {

                int index = fishTable.getSelectionModel().getSelectedIndex();
                turnFishTMObList.remove(index);

                fishTable.refresh();
            }

        });
    }

    void setCellValueFactory() {
        crewNicCol.setCellValueFactory(new PropertyValueFactory<>("nic"));
        crewNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        crewAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        crewContactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
        crewAgeCol.setCellValueFactory(new PropertyValueFactory<>("age"));

        fishIdCol.setCellValueFactory(new PropertyValueFactory<>("fishId"));
        fishNameCol.setCellValueFactory(new PropertyValueFactory<>("fishName"));
        unitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("qty"));
        actionCol.setCellValueFactory(new PropertyValueFactory<>("btn"));


    }

    @FXML
    void addBtnOnAction(ActionEvent event) {

        if (fishNameLbl.getText() == null || fishNameLbl.getText().equals("") || fishweight.getText().length() == 0) {
            new Alert(Alert.AlertType.ERROR,
                    "Fill Fish Id And Fish Weight",
                    ButtonType.OK
            ).show();
            return;
        }

        double weight = Double.valueOf(fishweight.getText());

        if (!turnFishTMObList.isEmpty()) {
            for (int i = 0; i < fishTable.getItems().size(); i++) {
                if (fishIdCol.getCellData(i).equals(fishIdTxt.getText())) {
                    weight += (double) qtyCol.getCellData(i);

                    turnFishTMObList.get(i).setQty(weight);

                    fishTable.refresh();
                    return;
                }
            }
        }

        try {
            CustomDTO customDTO = turnBO.searchFishTM(fishIdTxt.getText(), Double.valueOf(fishweight.getText()));
            TurnFishTM turnFishTM = new TurnFishTM(customDTO.getFishId(), customDTO.getFishName(), customDTO.getFishunitPrice(), customDTO.getWeight(), null);

            Button removeBtn = new Button("Remove");
            setRemoveBtnOnAction(removeBtn);
            turnFishTM.setBtn(removeBtn);

            if (turnFishTM == null) {
                return;
            }

            turnFishTMObList.add(turnFishTM);
            fishTable.setItems(turnFishTMObList);

            fishIdTxt.clear();
            fishweight.clear();

        } catch (SQLException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }

    }

    @FXML
    void backBtnOnAction(ActionEvent event) {
        DashboardFormController.fullScreen.getChildren().clear();
        DashboardFormController.menuScreen.getChildren().clear();
        try {
            DashboardFormController.fullScreen.getChildren().add(FXMLLoader.load(getClass().getResource("/view/dashboard_form.fxml")));
            DashboardFormController.menuScreen.getChildren().add(FXMLLoader.load(getClass().getResource("/view/dashboard_turn_menu_form.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void inDatetxtOnAction(ActionEvent event) {

    }

    @FXML
    void inTimetxtOnAction(ActionEvent event) {

    }

    @FXML
    void mainBtnOnAction(ActionEvent event) {
        DashboardFormController.fullScreen.getChildren().clear();
        try {
            DashboardFormController.fullScreen.getChildren().add(FXMLLoader.load(getClass().getResource("/view/dashboard_form.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void outDateTxtOnAction(ActionEvent event) {

    }

    @FXML
    void outTimeTxtOnAction(ActionEvent event) {

    }

    @FXML
    void searchBtnOnAction(ActionEvent event) {

        try {

            turnDTO = turnBO.searchTurn(turnSearchIdTxt.getText());

            if (turnDTO == null) {
                new Alert(Alert.AlertType.ERROR,
                        "Invalid TurnId",
                        ButtonType.OK
                ).show();
                turnSearchIdTxt.clear();
                return;
            }

            boatDTO = turnBO.searchBoat(turnDTO.getBoatId());
            cap = turnBO.searchCrew(turnDTO.getCapNIC());

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //turn
        turnIdLbl.setText(turnDTO.getTurnId());
        crewCounttxt.setText(turnDTO.getCrewCount() + "");
        outDateTxt.setText(turnDTO.getOutDate());
        outTimeTxt.setText(turnDTO.getOutTime());

        if (turnDTO.getInDate() == null || turnDTO.getInDate().equals("")) {
            inDatetxt.setText("");
            inTimetxt.setText("");

            setUpdatable(true);
        } else {
            inDatetxt.setText(turnDTO.getInDate());
            inTimetxt.setText(turnDTO.getInTime());
        }

        //boat
        boatIdLbl.setText(boatDTO.getBoatId());
        boatNameLbl.setText(boatDTO.getBoatName());
        boatTypeLbl.setText(boatDTO.getBoatType());
        noCrewLbl.setText(boatDTO.getNoCrew() + "");

        //cap
        capNIClbl.setText(cap.getNic());
        capNameLbl.setText(cap.getName());
        capDOBLbl.setText(cap.getDob());
        capContactLbl.setText(cap.getContact());
        emailLbl.setText(cap.getEmail());

        if (cap.getPhoto() != null) {
            capImageView.setImage(cap.getPhoto());
        }else {
            capImageView.setImage(new Image("E:\\Final Project Sem 1\\New folder\\project-harbourmaster\\src\\main\\resources\\view\\img\\396915-200.png"));
        }

        List<CrewTM> crewTM = turnDTO.getCrewTM();
        ObservableList<CrewTM> crewTMObservableList = FXCollections.observableArrayList();
        for (CrewTM crew : crewTM) {
            crewTMObservableList.add(crew);
        }

        crewTbl.setItems(crewTMObservableList);

        //fish
        try {
            ObservableList<TurnFishSearchTM> turnFishOblist = FXCollections.observableArrayList();

            for (TurnFishDTO turnFishDTO : turnBO.getFishForTurnFishTBL(turnDTO.getTurnId()) ) {
                turnFishOblist.add(
                        new TurnFishSearchTM(
                                turnFishDTO.getFishId(),
                                turnFishDTO.getFishName(),
                                turnFishDTO.getQty()
                        )
                );
            }


            fishTable.setItems(turnFishOblist);

        } catch (SQLException e) {
//            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }


    }

    @FXML
    void turnSearchIdTxtOnAction(ActionEvent event) {
        searchBtnOnAction(event);
    }

    @FXML
    public void crewCounttxtOnAction(ActionEvent actionEvent) {

    }

    private String dateFormateChanger() {
        String date = inDatetxt.getText();

        String[] dateAr = date.split("-");

        return dateAr[2] + "-" + dateAr[1] + "-" + dateAr[0];

    }

    @FXML
    public void endTurnBtnOnAction(ActionEvent actionEvent) {
        if (!isAllDataValidated()) {
            new Alert(Alert.AlertType.WARNING,
                    "Fill All Data Correctly",
                    ButtonType.OK
            ).show();
            return;
        }

        if (inDatetxt.getText().equals("") || inTimetxt.getText().equals("")) {
            new Alert(Alert.AlertType.WARNING,
                    "Fill All Data Correctly",
                    ButtonType.OK
            ).show();
            return;
        }

        turnDTO.setInDate(dateFormateChanger());
        turnDTO.setInTime(inTimetxt.getText());
        try {
            boolean isEnded = turnBO.endTurn(turnDTO, turnFishTMObList);
            if (isEnded) {
                new Alert(Alert.AlertType.INFORMATION,
                        "Turn Ended and Details Recorded",
                        ButtonType.OK
                ).show();
                backBtnOnAction(actionEvent);
            } else {
                new Alert(Alert.AlertType.ERROR,
                        "Turn Not Ended",
                        ButtonType.OK
                ).show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void fishIdTxtOnSction(ActionEvent actionEvent) {
        if (fishIdTxt.getText() == null || fishIdTxt.getText().equals("")) {
            return;
        }

        try {
            FishDTO fishDTO = turnBO.searchFish(fishIdTxt.getText());
            if (fishDTO != null) {
                fishNameLbl.setText(fishDTO.getFishName());
                unitPricelbl.setText(fishDTO.getUnitPrice() + "");

            } else {
                new Alert(Alert.AlertType.INFORMATION,
                        "Invalid FishId",
                        ButtonType.OK
                ).show();
                fishIdTxt.clear();
                fishweight.clear();
                fishNameLbl.setText("");
                unitPricelbl.setText("");

            }

        } catch (SQLException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }


    }

}
