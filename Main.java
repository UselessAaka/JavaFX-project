import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    private List<Patient> patientList; // Shared patient list
    private List<Appointment> appointmentList; // Shared appointment list
    private List<Doctor> doctorList; // Shared doctor list

    @Override
    public void init() {
        // Load data from files
        patientList = FileUtil.loadPatients();
        doctorList = FileUtil.loadDoctors();
        appointmentList = FileUtil.loadAppointments();
        
        // Initialize lists if loading fails
        if (patientList == null) patientList = new ArrayList<>();
        if (doctorList == null) doctorList = new ArrayList<>();
        if (appointmentList == null) appointmentList = new ArrayList<>();
    }

    @Override
    public void start(Stage primaryStage) {
        Button registerButton = new Button("Patient Registration");
        registerButton.setOnAction(e -> {
            PatientRegistration patientRegistration = new PatientRegistration(patientList);
            patientRegistration.start(new Stage());
        });

        Button appointmentButton = new Button("Appointment Scheduling");
        appointmentButton.setOnAction(e -> {
            AppointmentScheduling appointmentScheduling = new AppointmentScheduling(patientList, doctorList, appointmentList);
            appointmentScheduling.start(new Stage());
        });

        Button doctorButton = new Button("Doctor Management");
        doctorButton.setOnAction(e -> {
            DoctorManagement doctorManagement = new DoctorManagement(doctorList);
            doctorManagement.start(new Stage());
        });

        VBox vbox = new VBox(registerButton, appointmentButton, doctorButton);
        Scene scene = new Scene(vbox, 300, 200);
        primaryStage.setTitle("Smart Healthcare Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {
        // Save data to files when application stops
        FileUtil.savePatients(patientList);
        FileUtil.saveDoctors(doctorList);
        FileUtil.saveAppointments(appointmentList);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
