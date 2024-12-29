package com.example.ispcourseproject;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;

public class Controller {
    @FXML
    TextField KeyValue1;

    @FXML Button KeyButton;
    @FXML
    PasswordField pswWordField;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private static String secretKeyGen;

    private static File decryptFile;


    @FXML
    protected void displayKey(MouseEvent event) throws IOException {
        KeyValue1.setEditable(false);
        //System.out.print(file.getAbsolutePath());
        //System.out.println(file.getParent());
        if(!KeyValue1.isVisible()) {
            KeyValue1.setText(secretKeyGen);
            KeyValue1.setVisible(true);
            KeyButton.setText(" Hide ");
        }
        else if(KeyValue1.isVisible()){
            KeyValue1.setText(secretKeyGen);
            KeyValue1.setVisible(false);
            KeyButton.setText("Reveal");
        }
    }
@FXML
protected void onEncryptButtonClick(MouseEvent event) throws IOException {
    root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("FileEncrypt.fxml")));
    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
}

    @FXML
    protected void onDecryptButtonClick(MouseEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("FileDecrypt.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void onHomeButtonClick(MouseEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Home.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void onOpenFileButtonClick(MouseEvent event) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Encrypt File");
        File file = fileChooser.showOpenDialog(stage);
        String skey = EncryptFile(file);
        System.out.println(skey);
        secretKeyGen = skey;
        //System.out.println("123 " + secretKeyGen);

        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("KeyDisplay.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public static String EncryptFile(File file)throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException {
        //declaring ciphers and key gens
        //lets ciphers know what key to use
        Cipher encipher = Cipher.getInstance("AES");
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(256);
        SecretKey skey = kgen.generateKey();
        byte[] rawData = skey.getEncoded();
        String encodedKey = Base64.getEncoder().encodeToString(rawData);

        //System.out.print(skey);
        //System.out.print(skey.toString());
        encipher.init(Cipher.ENCRYPT_MODE, skey);


        //get file information
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[fis.available()];
        byte[] encryptedData;

        // Read file info and store in data[]
        fis.read(data);
        fis.close();

        //create encrpyted data file and decrypted data file
        //these end up in the EncryptTest file, if you want them in a different directory change the path
        File outfile = new File(file.getParent() + "/EncryptedFile");
        if (!outfile.exists())
            outfile.createNewFile();
        encryptedData = encipher.doFinal(data);

        //write encrypted data to encrypted file, then decipher the data and write to the deciphered file
        FileOutputStream writeEncrypt = new FileOutputStream(outfile);
        //these end up in the EncryptTest file, if you want them in a different directory change the path
        writeEncrypt.write(encryptedData);
        writeEncrypt.close();
        return encodedKey;
    }

    @FXML
    protected void onOpenFileButtonClickDecrypt(MouseEvent event) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Decrypt File");
        File file = fileChooser.showOpenDialog(stage);
        decryptFile = file;

        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("KeyEnter.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void DecryptFile(MouseEvent event) throws IOException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException {
        //declaring ciphers and key gens
        //lets ciphers know what key to use
        Cipher encipher = Cipher.getInstance("AES");
        Cipher decipher = Cipher.getInstance("AES");
        //KeyGenerator kgen = KeyGenerator.getInstance("AES");
        byte[] decodedKey = Base64.getDecoder().decode(pswWordField.getText());
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
        //encipher.init(Cipher.ENCRYPT_MODE, originalKey);
        decipher.init(Cipher.DECRYPT_MODE, originalKey);

        //get file information
        FileInputStream fis = new FileInputStream(decryptFile);
        byte data[] = new byte[fis.available()];
        byte encryptedData[];

        // Read file info and store in data[]
        fis.read(data);
        fis.close();

        //create encrpyted data file and decrypted data file
        //these end up in the EncryptTest file, if you want them in a different directory change the path
//        File outfile = new File("/Users/patrickbismack/Desktop/TestEnv/EnCryptMessage.txt");
//        if (!outfile.exists())
//            outfile.createNewFile();
        File decfile = new File(decryptFile.getParent() + "/DecryptedFile");
        if (!decfile.exists())
            decfile.createNewFile();

        //encrypts data
        //encryptedData = encipher.doFinal(data);

        //write encrypted data to encrypted file, then decipher the data and write to the deciphered file
        //FileOutputStream writeEncrypt = new FileOutputStream(outfile);
        FileOutputStream writeDecrypt = new FileOutputStream(decfile);

        //these end up in the EncryptTest file, if you want them in a different directory change the path
        //writeEncrypt.write(encryptedData);
        encryptedData = decipher.doFinal(data);
        writeDecrypt.write(encryptedData);
        writeDecrypt.close();

        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Home.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
