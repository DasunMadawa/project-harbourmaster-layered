package lk.ijse.projectharbourmaster.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import lk.ijse.projectharbourmaster.bo.BOFactory;
import lk.ijse.projectharbourmaster.bo.custom.UserBO;
import lk.ijse.projectharbourmaster.dto.UserDTO;
//import lk.ijse.projectharbourmaster.model.UserModel;
import lk.ijse.projectharbourmaster.util.Validations;

import java.io.IOException;
import java.sql.SQLException;

public class DashboardUsersMenuFormController {

    @FXML
    public JFXTextField user1EmpIdTxt;

    @FXML
    public JFXTextField user1UserNameTxt;

    @FXML
    public JFXTextField user1Passwordxt;

    @FXML
    public JFXTextField user1UserIdTxt;

    @FXML
    public JFXTextField user2EmpIdTxt;

    @FXML
    public JFXTextField user2UserNameTxt;

    @FXML
    public JFXTextField user2Passwordxt;

    @FXML
    public JFXTextField user2UserIdTxt;

    @FXML
    public JFXTextField user3EmpIdTxt;

    @FXML
    public JFXTextField user3UserNameTxt;

    @FXML
    public JFXTextField user3Passwordxt;

    @FXML
    public JFXTextField user3UserIdTxt;

    @FXML
    private Pane usersMenuPane;

    @FXML
    private JFXButton userMenuUpdateBtn;

    @FXML
    private JFXButton userMenuSaveBtn;


    UserBO userBO = (UserBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.USER);

    @FXML
    void initialize(){
        setUpdatable(false);
        setUserData();
        setTextFieldValidations();

    }

    private void setTextFieldValidations() {
        Validations.setFocus(user1UserIdTxt, Validations.userPattern);
        Validations.setFocus(user1EmpIdTxt, Validations.idPattern);
        Validations.setFocus(user1UserNameTxt, Validations.namePattern);
        Validations.setFocus(user1Passwordxt, Validations.passwordPattern);

        Validations.setFocus(user2UserIdTxt, Validations.userPattern);
        Validations.setFocus(user2EmpIdTxt, Validations.idPattern);
        Validations.setFocus(user2UserNameTxt, Validations.namePattern);
        Validations.setFocus(user2Passwordxt, Validations.passwordPattern);

        Validations.setFocus(user3UserIdTxt, Validations.userPattern);
        Validations.setFocus(user3EmpIdTxt, Validations.idPattern);
        Validations.setFocus(user3UserNameTxt, Validations.namePattern);
        Validations.setFocus(user3Passwordxt, Validations.passwordPattern);

    }

    private boolean isAllDataValidated() {
        if (user1UserIdTxt.getFocusColor().equals(Paint.valueOf("red")) ||
                user1EmpIdTxt.getFocusColor().equals(Paint.valueOf("red")) ||
                user1UserNameTxt.getFocusColor().equals(Paint.valueOf("red")) ||
                user1Passwordxt.getFocusColor().equals(Paint.valueOf("red")) ||

                user2UserIdTxt.getFocusColor().equals(Paint.valueOf("red")) ||
                user2EmpIdTxt.getFocusColor().equals(Paint.valueOf("red")) ||
                user2UserNameTxt.getFocusColor().equals(Paint.valueOf("red")) ||
                user2Passwordxt.getFocusColor().equals(Paint.valueOf("red")) ||

                user3UserIdTxt.getFocusColor().equals(Paint.valueOf("red")) ||
                user3EmpIdTxt.getFocusColor().equals(Paint.valueOf("red")) ||
                user3UserNameTxt.getFocusColor().equals(Paint.valueOf("red")) ||
                user3Passwordxt.getFocusColor().equals(Paint.valueOf("red"))
        ) {
            return false;
        }
        return true;

    }

    private void setUserData() {
        try {
            UserDTO u001 = userBO.searchUserByUserId("U001");
            UserDTO u002 = userBO.searchUserByUserId("U002");
            UserDTO u003 = userBO.searchUserByUserId("U003");

            user1UserIdTxt.setText(u001.getUserId());
            user1EmpIdTxt.setText(u001.getNic());
            user1UserNameTxt.setText(u001.getUserName());
            user1Passwordxt.setText(u001.getPassword());

            user2UserIdTxt.setText(u002.getUserId());
            user2EmpIdTxt.setText(u002.getNic());
            user2UserNameTxt.setText(u002.getUserName());
            user2Passwordxt.setText(u002.getPassword());

            user3UserIdTxt.setText(u003.getUserId());
            user3EmpIdTxt.setText(u003.getNic());
            user3UserNameTxt.setText(u003.getUserName());
            user3Passwordxt.setText(u003.getPassword());

        } catch (SQLException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private void setUpdatable(boolean updatable) {
        if (updatable){
            userMenuSaveBtn.setVisible(true);
            userMenuUpdateBtn.setVisible(false);

            user1EmpIdTxt.setEditable(true);
            user1UserNameTxt.setEditable(true);
            user1Passwordxt.setEditable(true);

            user2EmpIdTxt.setEditable(true);
            user2UserNameTxt.setEditable(true);
            user2Passwordxt.setEditable(true);

            user3EmpIdTxt.setEditable(true);
            user3UserNameTxt.setEditable(true);
            user3Passwordxt.setEditable(true);



        }else {
            userMenuSaveBtn.setVisible(false);
            userMenuUpdateBtn.setVisible(true);

            user1UserIdTxt.setEditable(false);
            user1EmpIdTxt.setEditable(false);
            user1UserNameTxt.setEditable(false);
            user1Passwordxt.setEditable(false);

            user2UserIdTxt.setEditable(false);
            user2EmpIdTxt.setEditable(false);
            user2UserNameTxt.setEditable(false);
            user2Passwordxt.setEditable(false);

            user3UserIdTxt.setEditable(false);
            user3EmpIdTxt.setEditable(false);
            user3UserNameTxt.setEditable(false);
            user3Passwordxt.setEditable(false);

        }


    }

    @FXML
    void userMenuSaveBtnOnAction(ActionEvent event) {
        if (!isAllDataValidated()){
            new Alert(Alert.AlertType.WARNING,
                    "Fill All Data Correctly",
                    ButtonType.OK
            ).show();
            return;

        }


        try {
            UserDTO userDTO1 = new UserDTO(
                    user1UserIdTxt.getText(),
                    user1EmpIdTxt.getText(),
                    user1UserNameTxt.getText(),
                    user1Passwordxt.getText() );

            UserDTO userDTO2 = new UserDTO(
                    user2UserIdTxt.getText(),
                    user2EmpIdTxt.getText(),
                    user2UserNameTxt.getText(),
                    user2Passwordxt.getText()
            );

            UserDTO userDTO3 = new UserDTO(
                    user3UserIdTxt.getText(),
                    user3EmpIdTxt.getText(),
                    user3UserNameTxt.getText(),
                    user3Passwordxt.getText()
            );

            boolean isUpdated = userBO.updateUserAll(userDTO1, userDTO2, userDTO3);

            if (isUpdated){
                new Alert(Alert.AlertType.CONFIRMATION ,
                        "Updated" ,
                        ButtonType.OK
                ).show();

            }else {
                new Alert(Alert.AlertType.CONFIRMATION ,
                        "Not Updated Check Duplicate UserName " ,
                        ButtonType.OK
                ).show();
                initialize();
            }

        }catch (SQLException e) {
            e.printStackTrace();

        }

        setUpdatable(false);
    }

    @FXML
    void userMenuUpdateBtnOnAction(ActionEvent event) {
        setUpdatable(true);
    }

}
