package ru.novikov;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public class AlgorithmsTestsController {
    //IDEA
    @FXML
    private TextField ideaKey;
    @FXML
    private TextField ideaTextToEncrypt;
    @FXML
    private TextField ideaEncryptedText;
    @FXML
    private TextField ideaTextToDecrypt;
    @FXML
    private TextField ideaDecryptedText;

    //RSA
    @FXML
    private TextField rsaTextToEncrypt;
    @FXML
    private TextField rsaEncryptedBytes;
    @FXML
    private TextField rsaDecryptedText;
    @FXML
    private TextField rsaE;
    @FXML
    private TextField rsaN;
    @FXML
    private TextField rsaFilename;

    //MD5
    @FXML
    private TextField md5Text;
    @FXML
    private TextField md5Hash;

    //EDSRSA
    @FXML
    private TextField edsrsaTextToSign;
    @FXML
    private TextField edsrsaSignedBytes;
    @FXML
    private TextField edsrsaPrototypeText;
    @FXML
    private TextField edsrsaE;
    @FXML
    private TextField edsrsaN;
    @FXML
    private TextField edsrsaFilename;

    private CipherAlg idea = null;
    private RSA rsa;
    private MD5 md5 = null;
    private EDSRSA edsrsa = null;
    // Ссылка на главное приложение.
    private MainApp mainApp;

    /**
     * Конструктор.
     * Конструктор вызывается раньше метода initialize().
     */
    public AlgorithmsTestsController() {
    }

    /**
     * Инициализация класса-контроллера. Этот метод вызывается автоматически
     * после того, как fxml-файл будет загружен.
     */
    @FXML
    private void initialize() {

    }
    /**
     * Вызывается главным приложением, которое даёт на себя ссылку.
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleIdeaSetKeyButton() throws IOException {
        if (ideaKey.getText() != null) {
            ByteArrayInputStream key = new ByteArrayInputStream(ideaKey.getText().getBytes());
            idea = new Idea(key);
        } else {
            ManyUtils.callMessagBox(mainApp, "No key entered", "Key field is empty", "Please, enter a non-empty key.");
        }
    }

    @FXML
    private void handleIdeaEncryptButton() throws IOException {
        if (idea != null) {
            InputStream data = IOUtils.toInputStream(ideaTextToEncrypt.getText(), StandardCharsets.UTF_8);
            ByteArrayOutputStream newData = new ByteArrayOutputStream();
            idea.encrypt(data, newData);

            ideaEncryptedText.setText(ManyUtils.byteArrToStr(newData.toByteArray()));
        } else {
            ManyUtils.callMessagBox(mainApp, "No key set", "The key for encryption has not been set yet", "Please, set the key first.");
        }
    }

    @FXML
    private void handleIdeaDecryptButton() throws IOException {
        if (idea != null) {
            ByteArrayInputStream data = new ByteArrayInputStream(ManyUtils.strToByteArr(ideaTextToDecrypt.getText()));
            ByteArrayOutputStream newData = new ByteArrayOutputStream();
            idea.decrypt(data, newData);

            ideaDecryptedText.setText(newData.toString());
        } else {
            ManyUtils.callMessagBox(mainApp, "No key set", "The key for encryption has not been set yet", "Please, set the key first.");
        }
    }

    @FXML
    private void handleRsaEncryptButton() {
        try {
        if (rsaN.getText().length() == 0 || rsaE.getText().length() == 0 || rsaFilename.getText().length() == 0) {
            rsa = new RSA();
            rsaE.setText(rsa.getE().toString());
            rsaN.setText(rsa.getN().toString());
            rsaFilename.setText("private.dat");
        } else {
            rsa = new RSA(new BigInteger(rsaE.getText()), new BigInteger(rsaN.getText()), rsaFilename.getText());
        }
        ByteArrayInputStream data = new ByteArrayInputStream(rsaTextToEncrypt.getText().getBytes());
        ByteArrayOutputStream newData = new ByteArrayOutputStream();
        rsa.encrypt(data, newData);

        rsaEncryptedBytes.setText(ManyUtils.byteArrToStr(newData.toByteArray()));
        } catch (IOException e) {
            ManyUtils.callMessagBox(mainApp, "Error", "Encrypting error", "An error has occurred during encryption");
        } catch (Exception e) {
            ManyUtils.callMessagBox(mainApp, "Error", "Getting keys error", "An error has occurred during getting keys");
        }
    }

    @FXML
    private void handleRsaDecryptButton() {
        try {
            if (rsaN.getText().length() != 0 && rsaE.getText().length() != 0 && rsaFilename.getText().length() != 0) {
                rsa = new RSA(new BigInteger(rsaE.getText()), new BigInteger(rsaN.getText()), rsaFilename.getText());
            }
            ByteArrayInputStream data = new ByteArrayInputStream(ManyUtils.strToByteArr(rsaEncryptedBytes.getText()));
            ByteArrayOutputStream newData = new ByteArrayOutputStream();

            rsa.decrypt(data, newData);
            rsaDecryptedText.setText(newData.toString());
        } catch (IOException e) {
            ManyUtils.callMessagBox(mainApp, "Error", "Decrypting error", "An error has occurred during decryption");
        } catch (Exception e) {
            ManyUtils.callMessagBox(mainApp, "Error", "Getting keys error", "An error has occurred during getting keys");
        }
    }

    @FXML
    private void handleMd5HashButton() {
        try {
            if (md5Text.getText().length() == 0) {
                throw new Exception("No text entered");
            }
            md5 = new MD5();
            ByteArrayInputStream data = new ByteArrayInputStream(md5Text.getText().getBytes());
            ByteArrayOutputStream newData = new ByteArrayOutputStream();
            md5.computeMD5(data, newData);

            byte[] buffer = newData.toByteArray();
            md5Hash.setText(md5.toHexString(buffer));
        } catch (IOException e) {
            ManyUtils.callMessagBox(mainApp, "Error", "Hashing error", "An error has occurred during getting hash");
        } catch (Exception e) {
            ManyUtils.callMessagBox(mainApp, "Error", "No text entered", "An error has occurred during inputing text");
        }
    }

    @FXML
    private void handleEdsrsaSignButton() {
        try {
            if (edsrsaN.getText().length() == 0 || edsrsaE.getText().length() == 0 || edsrsaFilename.getText().length() == 0) {
                edsrsa = new EDSRSA();
                edsrsaE.setText(edsrsa.getE().toString());
                edsrsaN.setText(edsrsa.getN().toString());
                edsrsaFilename.setText("private.dat");
            } else {
                edsrsa = new EDSRSA(new BigInteger(edsrsaE.getText()), new BigInteger(edsrsaN.getText()), edsrsaFilename.getText());
            }
            ByteArrayInputStream data = new ByteArrayInputStream(edsrsaTextToSign.getText().getBytes());
            ByteArrayOutputStream newData = new ByteArrayOutputStream();
            edsrsa.sign(data, newData);

            edsrsaSignedBytes.setText(ManyUtils.byteArrToStr(newData.toByteArray()));
        } catch (IOException e) {
            ManyUtils.callMessagBox(mainApp, "Error", "Encrypting error", "An error has occurred during encryption");
        } catch (Exception e) {
            ManyUtils.callMessagBox(mainApp, "Error", "Getting keys error", "An error has occurred during getting keys");
        }
    }

    @FXML
    private void handleEdsrsaGetPrototypeButton() {
        try {
            if (edsrsaN.getText().length() != 0 && edsrsaE.getText().length() != 0 && edsrsaFilename.getText().length() != 0) {
                edsrsa = new EDSRSA(new BigInteger(edsrsaE.getText()), new BigInteger(edsrsaN.getText()), edsrsaFilename.getText());
            }
            ByteArrayInputStream data = new ByteArrayInputStream(ManyUtils.strToByteArr(edsrsaSignedBytes.getText()));
            ByteArrayOutputStream newData = new ByteArrayOutputStream();

            edsrsa.getPrototype(data, newData);
            edsrsaPrototypeText.setText(newData.toString());
        } catch (IOException e) {
            ManyUtils.callMessagBox(mainApp, "Error", "Decrypting error", "An error has occurred during decryption");
        } catch (Exception e) {
            ManyUtils.callMessagBox(mainApp, "Error", "Getting keys error", "An error has occurred during getting keys");
        }
    }
}
