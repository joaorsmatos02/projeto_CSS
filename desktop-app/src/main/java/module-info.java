module com.example.desktopapp {
    requires javafx.fxml;
    requires javafx.controls;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;

    opens com.example.desktopapp to javafx.base;

    exports com.example.desktopapp.main to javafx.graphics;

    exports com.example.desktopapp.login to javafx.fxml;
    opens com.example.desktopapp.login to javafx.fxml;

    exports com.example.desktopapp.menu to javafx.fxml;
    opens com.example.desktopapp.menu to javafx.fxml;

    exports com.example.desktopapp.consult_projects to javafx.fxml;
    opens com.example.desktopapp.consult_projects to javafx.fxml;

    exports com.example.desktopapp.list_polls to javafx.fxml;
    opens com.example.desktopapp.list_polls to javafx.fxml;

    exports com.example.desktopapp.support_projects to javafx.fxml;
    opens com.example.desktopapp.support_projects to javafx.fxml;

    exports com.example.desktopapp.vote_poll to javafx.fxml;
    opens com.example.desktopapp.vote_poll to javafx.fxml;

    opens com.example.desktopapp.dtos to com.fasterxml.jackson.databind;
    exports com.example.desktopapp to javafx.graphics;

    exports com.example.desktopapp.mocks to javafx.fxml;
    opens com.example.desktopapp.mocks to javafx.fxml;
}
