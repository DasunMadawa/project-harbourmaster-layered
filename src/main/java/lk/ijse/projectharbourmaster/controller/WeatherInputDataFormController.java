package lk.ijse.projectharbourmaster.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.paint.Paint;
import lk.ijse.projectharbourmaster.bo.BOFactory;
import lk.ijse.projectharbourmaster.bo.custom.WeatherBO;
import lk.ijse.projectharbourmaster.dto.WeatherDTO;
//import lk.ijse.projectharbourmaster.model.BoatModel;
//import lk.ijse.projectharbourmaster.model.CrewModel;
//import lk.ijse.projectharbourmaster.model.EmailModel;
//import lk.ijse.projectharbourmaster.model.WeatherModel;
import lk.ijse.projectharbourmaster.util.EmailUtil;
import lk.ijse.projectharbourmaster.util.Validations;

import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;

public class WeatherInputDataFormController {

    @FXML
    private JFXTextField specialCasestxt;

    @FXML
    private JFXTextField datetxt;

    @FXML
    private JFXButton updateBtn;

    @FXML
    private JFXButton backkBtn;

    @FXML
    private JFXButton mainBtn;


    WeatherBO weatherBO = (WeatherBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.WEATHER);

    @FXML
    void initialize(){
        setTextFieldValidations();

    }

    private void setTextFieldValidations() {
        Validations.setFocus(specialCasestxt, Validations.namePattern);
        Validations.setFocus(datetxt, Validations.datePattern);

    }

    private boolean isAllDataValidated() {
        if (specialCasestxt.getFocusColor().equals(Paint.valueOf("red")) || datetxt.getFocusColor().equals(Paint.valueOf("red")) ) {

            return false;
        }
        return true;

    }

    @FXML
    void backkBtnOnAction(ActionEvent event) {
        DashboardFormController.fullScreen.getChildren().clear();
        DashboardFormController.menuScreen.getChildren().clear();
        try {
            DashboardFormController.fullScreen.getChildren().add(FXMLLoader.load(getClass().getResource("/view/dashboard_form.fxml")));
            DashboardFormController.menuScreen.getChildren().add(FXMLLoader.load(getClass().getResource("/view/dashboard_weather_menu_form.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void datetxtOnAction(ActionEvent event) {
        updateBtnOnAction(new ActionEvent());
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
    void specialCasestxtOnAction(ActionEvent event) {
        updateBtnOnAction(new ActionEvent());
    }

    private String dateFormateChanger() {
        String date = datetxt.getText();

        String[] dateAr = date.split("-");

        return dateAr[2]+"-" + dateAr[1] + "-" + dateAr[0] ;

    }

    @FXML
    void updateBtnOnAction(ActionEvent event) {
        if (!isAllDataValidated()){
            new Alert(Alert.AlertType.WARNING,
                    "Fill All Data Correctly",
                    ButtonType.OK
            ).show();
            return;

        }

        String specialCauseMessage = specialCasestxt.getText();

        try {
            boolean isinserted = weatherBO.addWeatherRecords(new WeatherDTO(LoginFormController.userId , 00 ,  specialCasestxt.getText() , dateFormateChanger() , LocalTime.now()+"" , null));

            if (isinserted){
                new Alert(Alert.AlertType.INFORMATION,
                        "Data Inserted",
                        ButtonType.OK
                ).show();

                datetxt.setText("");
                specialCasestxt.setText("");
            }else {
                new Alert(Alert.AlertType.INFORMATION,
                        "Data Not Inserted",
                        ButtonType.OK
                ).show();
            }

        } catch (SQLException e) {
            System.out.println(e);

            new Alert(Alert.AlertType.ERROR ,
                    "Database Crash" ,
                    ButtonType.OK
            ).show();
        } catch (IOException e) {
            System.out.println(e);
        }


        new Thread() {
            @Override
            public void run() {
                informSpecialCausesByEmail(specialCauseMessage);
                this.stop();
            }
        }.start();

    }

    private void informSpecialCausesByEmail(String specialCause) {
        try {
            List<String> allEmailsCrew = weatherBO.getAllEmailsCrew();
            List<String> allEmailsBoatOwners = weatherBO.getAllEmailsBoat();

            try {
                sendSpecialCausesMails(allEmailsCrew, specialCause);
            } finally {
                sendSpecialCausesMails(allEmailsBoatOwners, specialCause);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {

        }

    }

    private void sendSpecialCausesMails(List<String> allEmailsCrew, String specialCause) {
        for (String email:allEmailsCrew) {
            try {
                EmailUtil.sendMail("projecthm083@gmail.com" , "fpvbezdnxctmjzpr" , email , specialCause);
            } catch (MessagingException e) {
                e.printStackTrace();
            }

        }

    }

}
