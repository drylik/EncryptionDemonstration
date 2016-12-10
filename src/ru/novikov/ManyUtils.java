package ru.novikov;

import javafx.scene.control.Alert;

class ManyUtils {
    static void callMessagBox(MainApp mainApp, String title, String header, String text) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(mainApp.getPrimaryStage());
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(text);

        alert.showAndWait();
    }

    static String byteArrToStr(byte[] bytes) {
        String result = "";
        for (byte b :
                bytes) {
            result += b + " ";
        }
        return result;
    }

    static byte[] strToByteArr(String str) {
        String[] strs = str.split(" ");
        byte[] bytes = new byte[strs.length];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = Byte.parseByte(strs[i]);
        }
        return bytes;
    }
}
