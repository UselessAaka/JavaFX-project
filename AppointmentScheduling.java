import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

public class AppointmentScheduling {
    private List<Patient> patientList;
    private List<Doctor> doctorList;
    private List<Appointment> appointmentList;
    private TextArea outputArea = new TextArea();

    public AppointmentScheduling(List<Patient> patientList, List<Doctor> doctorList, List<Appointment> appointmentList) {
        this.patientList = patientList;
        this.doctorList = doctorList;
        this.appointmentList = appointmentList;
    }

    public void start(Stage stage) {
        Label label = new Label("Appointment Scheduling");
        TextField patientNameField = new TextField();
        patientNameField.setPromptText("Patient Name");

        TextField specialtyField = new TextField();
        specialtyField.setPromptText("Doctor Specialty");

        ComboBox<String> doctorComboBox = new ComboBox<>();
        doctorComboBox.setPromptText("Select Doctor");

        Button filterDoctorsButton = new Button("Find Doctors");
        filterDoctorsButton.setOnAction(e -> {
            String specialty = specialtyField.getText();
            List<Doctor> filteredDoctors = doctorList.stream()
                    .filter(doctor -> doctor.getSpecialty().equalsIgnoreCase(specialty))
                    .collect(Collectors.toList());

            doctorComboBox.getItems().clear();
            for (Doctor doctor : filteredDoctors) {
                doctorComboBox.getItems().add(doctor.getName());
            }
        });

        Button bookButton = new Button("Book Appointment");
        bookButton.setOnAction(e -> {
            String patientName = patientNameField.getText();
            String doctorName = doctorComboBox.getValue();

            if (doctorName == null || patientName.isEmpty()) {
                outputArea.appendText("Please enter both patient name and doctor selection.\n");
                return;
            }

            Patient patient = patientList.stream().filter(p -> p.getName().equalsIgnoreCase(patientName)).findFirst().orElse(null);
            Doctor doctor = doctorList.stream().filter(d -> d.getName().equalsIgnoreCase(doctorName)).findFirst().orElse(null);

            if (patient != null && doctor != null) {
                boolean isDoctorAvailable = appointmentList.stream().noneMatch(app -> app.getDoctor().equals(doctor));
                if (isDoctorAvailable) {
                    Appointment appointment = new Appointment(patient, doctor);
                    appointmentList.add(appointment);
                    FileUtil.saveAppointments(appointmentList); // Save appointments after booking
                    String message = "Appointment booked for " + patientName + " with Dr. " + doctorName;
                    outputArea.appendText(message + "\n");
                } else {
                    outputArea.appendText("Selected doctor is not available. Choose a different doctor or time.\n");
                }
            } else {
                outputArea.appendText("Error: Patient or Doctor not found.\n");
            }

            patientNameField.clear();
            specialtyField.clear();
            doctorComboBox.getItems().clear();
        });

        VBox vbox = new VBox(label, patientNameField, specialtyField, filterDoctorsButton, doctorComboBox, bookButton, outputArea);
        Scene scene = new Scene(vbox, 400, 300);
        stage.setTitle("Appointment Scheduling");
        stage.setScene(scene);
        stage.show();
    }
}
